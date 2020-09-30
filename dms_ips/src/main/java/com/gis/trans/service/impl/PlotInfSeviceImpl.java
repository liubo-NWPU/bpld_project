package com.gis.trans.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gis.trans.config.FileConfig;
import com.gis.trans.controller.PlotInfController;
import com.gis.trans.dao.DiffimageLogDao;
import com.gis.trans.dao.PlotInfDao;
import com.gis.trans.dao.PointCloudDao;
import com.gis.trans.dao.SqliteFileDao;
import com.gis.trans.model.*;
import com.gis.trans.mq.Producer;
import com.gis.trans.service.EarlyWarnHistService;
import com.gis.trans.service.PlotInfSevice;
import com.gis.trans.service.PointFileService;
import com.gis.trans.service.RadarService;
import com.gis.trans.sqlites.SqliteUtil;
import com.gis.trans.utils.*;
import com.mathworks.toolbox.javabuilder.MWArray;
import com.mathworks.toolbox.javabuilder.MWCellArray;
import com.mathworks.toolbox.javabuilder.MWException;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Polygon;
import com.gis.trans.tiles3d.database.sqlite.SqliteDBManagerFactory;
import com.gis.trans.tiles3d.generator.PntcConfig;
import com.gis.trans.tiles3d.generator.PntcGenerationException;
import com.gis.trans.tiles3d.generator.PntcGenerator;
import com.gis.trans.tiles3d.model.PointObject;
import org.gdal.osr.CoordinateTransformation;
import org.gdal.osr.SpatialReference;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.geo.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.*;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import plotCon.PlotCon;

import javax.persistence.*;
import java.io.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class PlotInfSeviceImpl extends BaseServiceImpl<ShapePoint, String> implements PlotInfSevice {

    private static final Logger logger = LoggerFactory.getLogger(PlotInfSeviceImpl.class);

    private static final double value = 0.9;

    @Autowired
    private FileConfig fileConfig;

    @Value("${system.file.parentid}")
    private Long UPLOAD_PARENTID;

    @Value("${system.file.uploadfolder}")
    private String UPLOAD_FOLDER;

    @Autowired
    private PlotInfController plotInfController;

    @Value("${system.file.outfolder}")
    private String OUT_FOLDER;

    @Value("${system.file.manager}")
    private String managerUrl;

    @Value("${system.file.gisserver}")
    private String gisserverUrl;

    @Value("${DM.service.address}")
    private String dmaddress;

    public static final String DeleteFileQueue = "PlotDeleteFile.queue";

    public static final String PlotAddQueue = "PlotAdd.queue";

    public static final String PlotParseQueue = "PlotParseFile.queue";

    @Autowired
    private PlotInfDao plotInfDao;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private Producer producer;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private RadarService radarService;

    @Autowired
    private PointFileService pointFileService;

    @Autowired
    private EarlyWarnHistService earlyWarnHistService;

    @Autowired
    private DiffimageLogDao diffimageLogDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private SqliteFileDao sqliteFileDao;

    @Autowired
    private PointCloudDao pointCloudDao;

    @Override
    public JpaRepository<ShapePoint, String> getDao() {
        return plotInfDao;
    }

    @Override
    public List<ShapePoint> searchContain(Long fileId, String polygons) {
        Polygon polygon;
        List<ShapePoint> resultList = new ArrayList<ShapePoint>();
        String baseSQL = "select p from ShapePoint p where p.fileId = " + fileId;
        if (!StringUtils.isEmpty(polygons)) {
            List list = JSONObject.parseObject(polygons, List.class);
            if (list.size() > 0) {
                List<String> arrayList = new ArrayList<>();
                for (Object o : list) {
                    JSONArray polygonArrays = JSONObject.parseArray(o.toString());
                    int count = polygonArrays.size();
                    Coordinate[] coords = new Coordinate[count + 1];
                    JSONObject coordObj;
                    int i;
                    for (i = 0; i < count; i++) {
                        coordObj = polygonArrays.getJSONObject(i);
                        Point point = new Point(coordObj.getDouble("lon"), coordObj.getDouble("lat"));
                        //Point point = PlotLocationUtil.get3857Point(coordObj.getDouble("lon"), coordObj.getDouble("lat"));
                        coords[i] = new Coordinate(point.getX(), point.getY());
                    }
                    coords[i] = coords[0];
                    polygon = GeometryUtil.createPolygon(coords);
                    arrayList.add(String.format(" ( (ST_Contains(st_geomfromtext('%s',%d),st_geomfromtext (st_astext(p.geo),%d)))=true ) ", polygon.toText(), 4326, 4326));
                }
                if (arrayList.size() > 0) {
                    for (int j = 0; j < arrayList.size(); j++) {
                        if (j == 0) {
                            baseSQL += " And " + " ( " + arrayList.get(j);
                        } else {
                            baseSQL += " OR " + arrayList.get(j);
                        }
                        if (j == arrayList.size() - 1) {
                            baseSQL += " ) " + "";
                        }
                    }
                }
            }
            System.out.println(baseSQL);
            TypedQuery<ShapePoint> queryResult = entityManager.createQuery(baseSQL, ShapePoint.class);
            resultList = queryResult.getResultList();
            Double lon = 0.0;
            Double lat = 0.0;
            for (ShapePoint shapePoint : resultList) {
                com.vividsolutions.jts.geom.Point geo = shapePoint.getGeo();
                lon = geo.getX();
                lat = geo.getY();
                lon = lon * 20037508.34 / 180;
                lat = Math.log(Math.tan((90 + lat) * Math.PI / 360)) / (Math.PI / 180);
                lat = lat * 20037508.34 / 180;
                shapePoint.setX(lon);
                shapePoint.setY(lat);
            }
        }
        return resultList;
    }
    @Override
    public List<ShapePoint> searchExclude(Long fileId, String polygons) {
        Polygon polygon;
        List<ShapePoint> resultList = new ArrayList<ShapePoint>();
        String baseSQL = "select p from ShapePoint p where p.fileId = " + fileId;
        if (!StringUtils.isEmpty(polygons)) {
            List list = JSONObject.parseObject(polygons, List.class);
            if (list.size() > 0) {
                List<String> arrayList = new ArrayList<>();
                for (Object o : list) {
                    JSONArray polygonArrays = JSONObject.parseArray(o.toString());
                    int count = polygonArrays.size();
                    Coordinate[] coords = new Coordinate[count + 1];
                    JSONObject coordObj;
                    int i;
                    for (i = 0; i < count; i++) {
                        coordObj = polygonArrays.getJSONObject(i);
                        Point point = new Point(coordObj.getDouble("lon"), coordObj.getDouble("lat"));
                        //Point point = PlotLocationUtil.get3857Point(coordObj.getDouble("lon"), coordObj.getDouble("lat"));
                        coords[i] = new Coordinate(point.getX(), point.getY());
                    }
                    coords[i] = coords[0];
                    polygon = GeometryUtil.createPolygon(coords);
                    arrayList.add(String.format(" ( (ST_Contains(st_geomfromtext('%s',%d),st_geomfromtext (st_astext(p.geo),%d)))=false ) ", polygon.toText(), 4326, 4326));
                }
                if (arrayList.size() > 0) {
                    for (int j = 0; j < arrayList.size(); j++) {
                        if (j == 0) {
                            baseSQL += " And " + " ( " + arrayList.get(j);
                        } else {
                            baseSQL += " And " + arrayList.get(j);
                        }
                        if (j == arrayList.size() - 1) {
                            baseSQL += " ) " + "";
                        }
                    }
                }
            }
            TypedQuery<ShapePoint> queryResult = entityManager.createQuery(baseSQL, ShapePoint.class);
            resultList = queryResult.getResultList();
            Double lon = 0.0;
            Double lat = 0.0;
            for (ShapePoint shapePoint : resultList) {
                com.vividsolutions.jts.geom.Point geo = shapePoint.getGeo();
                lon = geo.getX();
                lat = geo.getY();
                lon = lon * 20037508.34 / 180;
                lat = Math.log(Math.tan((90 + lat) * Math.PI / 360)) / (Math.PI / 180);
                lat = lat * 20037508.34 / 180;
                shapePoint.setX(lon);
                shapePoint.setY(lat);
            }
        }
        return resultList;
    }

    //创建shp
    @Override
    public Result makeShape(List<ShapePoint> list, String fileName, Long parentId) {
        String uploadFileName = fileName;
        // 拼接字符串
        String jsonStr = "";
        try {
            if (list == null && list.size() == 0) {
                return new Result(false, "文件不存在!");
            }
            logger.info("===============创建shp文件==============");
            //首先得创建存储目录
            String outFloderStr = UPLOAD_FOLDER + File.separator + uploadFileName;
            String outFileStr = outFloderStr + File.separator + uploadFileName + ".shp";
            File targetFile = new File(outFileStr);
            File parentFile = targetFile.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            GeoToolsUtils.writeSHPbyPoint(outFileStr, list);
            File file = new File(outFloderStr);
            if (!file.exists()) {
                return new Result(false, "shp及相关文件创建");
            } else {
                File[] files = file.listFiles();
                for (File file1 : files) {
                    if (file1.getName().endsWith(".prj")) {
                        file1.delete();
                        try {
                            file1.createNewFile();
                            FileWriter fileWriter = new FileWriter(file1);
                            fileWriter.write("PROJCS[\"WGS_1984_Web_Mercator_Auxiliary_Sphere\",GEOGCS[\"GCS_WGS_1984\",DATUM[\"D_WGS_1984\",SPHEROID[\"WGS_1984\",6378137.0,298.257223563]],PRIMEM[\"Greenwich\",0.0],UNIT[\"Degree\",0.0174532925199433]],PROJECTION[\"Mercator_Auxiliary_Sphere\"],PARAMETER[\"False_Easting\",0.0],PARAMETER[\"False_Northing\",0.0],PARAMETER[\"Central_Meridian\",0.0],PARAMETER[\"Standard_Parallel_1\",0.0],PARAMETER[\"Auxiliary_Sphere_Type\",0.0],UNIT[\"Meter\",1.0]]");
                            fileWriter.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                            return new Result(false, "写入prj文件失败");
                        }
                        break;
                    }
                }
            }
            // 压缩文件夹下的所有文件为zip
            logger.info("===============压缩shp文件，开始==============");
            Result result = ZipUtils.fileToZip(outFloderStr, OUT_FOLDER, uploadFileName);
            if (result.getFlag()) {
                logger.info("===============压缩shp文件，结束==============");
                //上传sdc
                //     uploadFilePath(result.getData().toString());
                Result result1 = uploadFile(result.getData().toString(), parentId);
                if (!result1.getFlag()) {
                    return new Result(false, "上传shp文件失败");
                }
                return new Result(true, "压缩上传shp文件成功", result1.getData());
            } else {
                logger.info("===============压缩shp文件异常");
                return new Result(false, "压缩shp文件异常");
            }

        } catch (Exception e) {
            logger.info("===============创建shp文件，异常==============");
            e.printStackTrace();
            return new Result(false, "压缩shp文件异常");
        }
    }

    //上传到数管
    @Override
    public Result uploadFile(String filePath, Long parentId) {

        FileSystemResource resource = new FileSystemResource(filePath);
        File file = resource.getFile();

        MultiValueMap map = new LinkedMultiValueMap();

        map.add("file", resource);
        //map.add("name", file.getName());
        map.add("fileType", 2);
        map.add("parentId",parentId);
        map.add("lastModified", file.lastModified());
        map.add("param", file.getName());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Accept", MediaType.MULTIPART_FORM_DATA_VALUE);
        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap> multiValueMapHttpEntity = new HttpEntity<>(map, httpHeaders);

        try {
            logger.info("=================上传shp文件，开始================");
            //ResponseEntity<ModelMap> stringResponseEntity = restTemplate.postForEntity(managerUrl + "/manager/rest/file/uploadFile", multiValueMapHttpEntity, ModelMap.class);
            ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(managerUrl + "/manager/rest/file/uploadShapeFile", multiValueMapHttpEntity, String.class);
            String body = stringResponseEntity.getBody();
            JSONObject jsonObject3 = new JSONObject();
            if (stringResponseEntity.getStatusCode() == HttpStatus.OK) {
                Map map1 = new HashMap<>();
                if(org.apache.commons.lang3.StringUtils.isNotEmpty(body)) {
                    map1.put("fileId", Long.valueOf(body.split("-")[0]));
                    map1.put("fileName", body.split("-")[1]);
                }
                String post1 = HttpClientUtil.get(managerUrl + "/manager/rest/file/fileMeta", map1);
                JSONObject jsonObject = JSONObject.parseObject(post1);
                JSONArray rows = jsonObject.getJSONArray("rows");
                if (rows.size() != 1) {
                    return new Result(false, "上传-解析失败");
                } else {
                    JSONObject jsonObject1 = JSONObject.parseObject(rows.get(0).toString());
                    Long id = jsonObject1.getLong("id");
                    jsonObject3.put("id", id);
                    Map map2 = new HashMap<>();
                    map2.put("id", id);
                    map2.put("wfs", false);
                    logger.info("===============发布shp文件，开始==============");
                    String post2 = HttpClientUtil.post(gisserverUrl + "/gisserver/rest/shapeservice/publish", map2);
                    JSONObject jsonObject2 = JSONObject.parseObject(post2);
                    jsonObject3.put("catalogId",  Long.valueOf(body.split("-")[0]));
                    if (jsonObject2.getBoolean("success") == true) {
                        jsonObject3.put("serviceId", jsonObject2.get("serviceId"));
                        jsonObject3.put("fileId", Long.valueOf(body.split("-")[0]));
                        jsonObject3.put("fileName", body.split("-")[1]);
                        logger.info("===============发布shp文件，成功==============");
                        return new Result(true,  jsonObject3); //jsonObject3
                    } else {
                        logger.info("===============发布shp文件，失败==============");
                        return new Result(false, "发布服务失败");
                    }
                }

            } else {
                logger.info("===============上传shp文件，失败==============");
                return new Result(false, "上传失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "上传失败");
        }
    }

    @Override
    public ResponseModel updateShapeInf(Double x, Double y, Double angle, Long fileId) {
        ResponseModel responseModel = new ResponseModel();
        try {
            Map map = new HashMap();
            map.put("id", fileId.toString());
            String s = HttpUtil.sendGet(managerUrl + "/manager/rest/filecatalog/getById", map);
            if (s == null) {
                responseModel.setSuccess(false);
                return responseModel;
            }
            JSONObject jsonObject = JSONObject.parseObject(s);
            String fileName = jsonObject.getString("fileName");
            int i = 0;
            i = fileName.lastIndexOf(".");
            fileName = fileName.substring(0, i);
            Map map1 = new HashMap<>();
            map1.put("fileIds[]", jsonObject.getString("id"));
            HttpUtil.sendPost(managerUrl + "/manager/rest/file/deleteFiles", map1);
            List<ShapePoint> list1 = new ArrayList<ShapePoint>();
            List<ShapePoint> list = new ArrayList<ShapePoint>();
            list1 = plotInfDao.findByFileId(fileId);
            Double x1 = 0.0;
            Double y1 = 0.0;
            if (list1.size() > 0) {
                for (ShapePoint plotInf : list1) {
                    com.vividsolutions.jts.geom.Point geo = plotInf.getGeo();
                    ShapePoint shapePoint = new ShapePoint();
                    x1 = geo.getX() * Math.cos(angle) - geo.getY() * Math.sin(angle) + x;
                    y1 = geo.getX() * Math.sin(angle) + geo.getY() * Math.cos(angle) + y;
                    shapePoint.setrAxis(plotInf.getrAxis());
                    shapePoint.setaAxis(plotInf.getaAxis());
                    Point point = PlotLocationUtil.get3857Point(x1, y1);
                    shapePoint.setX(point.getX());
                    shapePoint.setY(point.getY());
                    shapePoint.setM(plotInf.getM());
                    shapePoint.setN(plotInf.getN());
                    shapePoint.setStrain(plotInf.getStrain());
                    list.add(shapePoint);
                }
                String radar = list1.get(0).getRadar();
                Radar radarInfo = radarService.findByRadarKey(radar);
                Long parentId = -1L;
                if (radarInfo != null) {
                    radar = radarInfo.getRadarName();
                    parentId = radarInfo.getRadarId();
                } else {
                    responseModel.setSuccess(false);
                    responseModel.setMessage("雷达表中无相关雷达信息");
                    return responseModel;
                }
                String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(list1.get(0).getTime());
                Result result = makeShape(list, fileName, parentId);
                if (result.getFlag() != null) {
                    if (result.getFlag() == true) {
                        JSONObject jsonObject1 = JSONObject.parseObject(result.getData().toString());
                        Long catalogId = jsonObject1.getLong("catalogId");
                        List<List<ShapePoint>> list12 = new ArrayList<>();
                        int toIndex = 20000;
                        int listsize = list.size();
                        for (int i1 = 0; i1 < list.size(); i1 += toIndex) {
                            if (i1 + toIndex > listsize - 1) {
                                toIndex = listsize - i1;
                            }
                            List<ShapePoint> plotInfs = list.subList(i1, i1 + toIndex);
                            if (i1 + toIndex > listsize) {
                                list12.add(plotInfs);
                                break;
                            } else {
                                list12.add(plotInfs);
                            }
                        }
                        JSONObject jsonObject2 = new JSONObject();
                        jsonObject2.put("fileId", fileId);
                        producer.sendMessage(DeleteFileQueue, jsonObject2.toJSONString());
                        handleList(list12, list12.size(), catalogId, radar, time);
                    }
                    responseModel.setSuccess(result.getFlag());
                    responseModel.setData(result.getData());
                } else {
                    responseModel.setSuccess(false);
                }
            } else {
                responseModel.setSuccess(false);
                responseModel.setMessage("矢量信息获取错误");
                responseModel.setException(NullPointerException.class.getName());
            }
        } catch (Exception e) {
            responseModel.setSuccess(false);
            responseModel.setMessage("数管文件查询有误");
            responseModel.setException(FileNotFoundException.class.getName());
        }
        return responseModel;
    }
    //多线程发送mq
    /*public void handleList1(List<List<ShapePoint>> data, int threadNum,Long fileId) {
        for (int i = 0; i < threadNum; i++) {
            List<ShapePoint> plotInfs = data.get(i);
            InsertListThreadL insertListThread = new InsertListThreadL(plotInfs) {
                @Override
                public void method(List<ShapePoint> plotInfs) {
                    for (ShapePoint plotInf : plotInfs) {
                        plotInf.setFileId(fileId);
                       *//* String sql = "update ShapePoint set fileId = "+plotInf.getFileId()+" ,geo = "+plotInf.getGeo()+"where id = "+plotInf.getId()+" and radar = "+plotInf.getRadar();
                        jdbcTemplate.batchUpdate(sql);*//*
                        plotInfDao.saveAndFlush(plotInf);
                   //        plotInfDao.updateShapePoint(plotInf.getFileId(),plotInf.getGeo(),plotInf.getId(),plotInf.getRadar());
                    }
                }
            };
            insertListThread.start();
        }
    }*/

    @Override
    public List<ShapePoint> findByFileId(Long fileId, String radar) {
        long start2 = System.currentTimeMillis();
        logger.info(String.valueOf(start2));
        Date start21 = new Date();
        logger.info(start21.toString());
        List<ShapePoint> list2 = plotInfDao.findByFileIdAndRadar(fileId, radar);
        long end2 = System.currentTimeMillis();
        logger.info(String.valueOf(end2));
        Date end21 = new Date();
        logger.info(end21.toString());
        System.out.println("time:------" + (end2 - start2) / 1000);
        return null;
    }

    @Override
    public ResponseModel getShape(String parentId,String radar, String startTime, String endTime) {
        logger.info("---------getShape 方法开始执行-----------");
        ResponseModel responseModel = new ResponseModel();
        try {
            logger.info(new Date().toString());

            if(org.apache.commons.lang.StringUtils.isBlank(startTime)){
                startTime=DateUtil.getlastYearBeginDate() ;
            }
            if(org.apache.commons.lang.StringUtils.isBlank(endTime)){
                endTime=DateUtil.getDate("yyyy.MM.dd HH:mm:ss") ;
            }

            // sql 第二行为假列(类属性)，目的是可以直接转成ShapePoint对象 and b.a_axis is not null and b.r_axis is not NULL

              String sql = "SELECT C.cm as m, C.cn as n, W.br as rAxis, W.ba as aAxis,C.sumChange as strain  " +
                                 ",random() as id,W.ba as a_axis,W.br as r_axis,1000 as file_id,C.sumChange as change,1.20 as speed,1.20 as accspeed,W.geo as geo,null as time,'qq'as radar,'qq'as range  " +
                                 " FROM ( SELECT SUM(A.change) AS sumChange,A.M AS cm, A.n AS cn FROM shape_point A WHERE A.radar ='"+radar+"' and A.change > - 1000 " +
                                 " and (A.time > '"+startTime+"' and A.time < '"+endTime+"') "+
                                 " GROUP BY A.M, A.n ) C " +
                                    " LEFT JOIN ( SELECT DISTINCT b.a_axis AS ba, b.r_axis AS br, b.m AS bm, b.n AS bn ,b.geo as geo " +
                                 " FROM shape_point b WHERE b.radar ='"+radar+"' and  b .change > - 1000  " +
                                 " and (b.time > '"+startTime+"' and b.time < '"+endTime+"') "+
                                 ") W ON C .cm = W.bm AND C .cn = W.bn";
            /**
             *             String sql = "SELECT W.bm AS M, W.bn AS n, W.br as rAxis, W.ba as aAxis,C.sumChange as strain  " +
             *                     ",random() as id,W.ba as a_axis,W.br as r_axis,1000 as file_id,C.sumChange as change,1.20 as speed,1.20 as accspeed,W.geo as geo,null as time,'qq'as radar,'qq'as range  " +
             *                     " FROM ( SELECT SUM(A.change) AS sumChange, A.r_axis AS ra FROM shape_point A WHERE A.radar ='"+radar+"' and A.change > - 1000 " +
             *                     " and (A.time > '"+startTime+"' and A.time < '"+endTime+"') "+
             *                     " GROUP BY A.r_axis ) C " +
             *                     " LEFT JOIN ( SELECT DISTINCT b.a_axis AS ba, b.r_axis AS br, b.m AS bm, b.n AS bn ,b.geo as geo " +
             *                     " FROM shape_point b WHERE b.radar ='"+radar+"' and  b .change > - 1000  " +
             *                     " and (b.time > '"+startTime+"' and b.time < '"+endTime+"') "+
             *                     ") W ON C .ra = W.br";
             */

            logger.info("=====sql====="+sql);
            Query query = entityManager.createNativeQuery(sql,ShapePoint.class);
            List<ShapePoint>  point = (List<ShapePoint>)query.getResultList();
          /*  Query query = entityManager.createNativeQuery(sql);
            List  points = query.getResultList();*/
            entityManager.clear(); //释放持久化bean

            Double lon = 0.0;
            Double lat = 0.0;

            List<ShapePoint> plotList = new ArrayList<>();
          for(ShapePoint obj:point){
                Object geo = obj.getGeo();
                if(geo!=null){
                    String s = geo.toString();
                    //logger.info(s);
                    String replace = s.replace("POINT (", "").replace(")", "");
                    String[] split = replace.split(" ");
                    lon = Double.parseDouble(split[0]);
                    lat = Double.parseDouble(split[1]);
                    Point point1 = PlotLocationUtil.get3857Point(lon, lat);
                    obj.setX(point1.getX());
                    obj.setY(point1.getY());
                }
                obj.setTime(new Date());
                plotList.add(obj);
            }
            logger.info(new Date().toString());
           /* for (ShapePointVo shapePoint : plotList) {
                if (shapePoint==null){
                    shapePoint.setStrain(-1000.0);
                }
                com.vividsolutions.jts.geom.Point geo = shapePoint.getGeo();
                lon = geo.getX();
                lat = geo.getY();
                lon = lon * 20037508.34 / 180;
                lat = Math.log(Math.tan((90 + lat) * Math.PI / 360)) / (Math.PI / 180);
                lat = lat * 20037508.34 / 180;
                shapePoint.setX(lon);
                shapePoint.setY(lat);
                shapePoint.setTime(new Date());
            }*/
            String start_date = "";
            String end_date = "";
            try {
                start_date =startTime.replace("-","").replace(":","").replace(" ","");
                end_date = endTime.replace("-","").replace(":","").replace(" ","");;
            } catch (Exception e) {
                e.printStackTrace();
            }
            Radar radarInfo = radarService.findByRadarKey(radar);
            //判断雷达是否存在
            if (radarInfo == null){
                responseModel.setSuccess(false);
                responseModel.setMessage("雷达不存在");
                return responseModel;
            }
            String fileName=start_date+"_"+end_date+"_"+radar;
            Long  shape_parentId  =Long.parseLong(parentId) ; //list1.get(0).getRadarId()

            Result s = this.makeShape(plotList, fileName,shape_parentId);

            if (s.getFlag()) {
                responseModel.setSuccess(true);
                responseModel.setData(s.getData());
                responseModel.setMessage("解析-生成shp-上传-执行成功");
            } else {
                responseModel.setSuccess(false);
                responseModel.setMessage("解析-生成shp-上传-执行失败");
            }
            responseModel.setSuccess(true);
        }catch (Exception e){
            e.printStackTrace();
            responseModel.setSuccess(false);
        }
        logger.info("---------getShape 方法执行完毕-----------");
        return responseModel;
    }

    @Override
    public ResponseModel getShapepath(String path) {
        ResponseModel responseModel = new ResponseModel();
        //54
        double rx = 471466.974;
        double ry = 2717978.331;
        double rz = 731.610;
        double lx = 471467.256;
        double ly = 2717977.481;
        double lz = 731.611;
        double cx = (lx + rx) / 2;  //雷达中心坐标
        double cy = (ly + ry) / 2;
        double cz = (lz + rz) / 2;
        double xc = rx - lx;   //差值
        double yc = ry - ly;
        double ewAngle = Math.atan(yc / xc);  //雷达倾斜角
        //84
        double[] doublesL = get84(lx, ly);
        double[] doublesR = get84(rx, ry);
        Point l = PlotLocationUtil.get3857Point(doublesL[0], doublesL[1]);//左轨道
        Point r = PlotLocationUtil.get3857Point(doublesR[0], doublesR[1]);//右轨道
        File file = new File("C:\\Users\\BYY\\Desktop\\gd\\【批量下载】2020_03_23_19_46_07等\\aky\\2019-2020广东大宝山矿业\\大宝山变形图zip\\diff");
        File[] files = file.listFiles();
        List<ShapePoint> plotList = PlotLocationUtil.getPlotList(l, r, files[0].getPath(),"strain");
        logger.info("第1个文件：  " + new Date().toString());
        for (int i = 1; i < files.length; i++) {
            logger.info("第" + (i + 1) + "个文件：    " + new Date().toString());
            List<ShapePoint> plotList2 = PlotLocationUtil.getPlotList(l, r, files[i].getPath(),"strain");
            logger.info("第" + (i + 1) + "个文件：   000结束 " + new Date().toString() +"  "+plotList2.size());
            for (ShapePoint shapePoint : plotList) {
                System.out.println(plotList2.size());
                ShapePoint point = new ShapePoint();
                for (int i1 = 0; i1 < plotList2.size(); i1++) {
                    point = plotList2.get(i1);
                    if (point.getStrain()==-1000.0)
                    {
                        plotList2.remove(point);
                        i1--;
                        continue;
                    }
                    if (shapePoint.getM() == point.getM() && shapePoint.getN() == shapePoint.getN()) {
                        if (shapePoint.getStrain() == -1000.0) {
                            shapePoint.setStrain(point.getStrain());
                        } else {
                            shapePoint.setStrain(shapePoint.getStrain() + point.getStrain());
                        }
                        plotList2.remove(point);
                        i1--;
                        break;
                    }
                }

            }
        }
        String uploadFileName = "test123";
        // 拼接字符串
        String jsonStr = "";
        try {
            if (plotList == null && plotList.size() == 0) {
                responseModel.setMessage("数据为空");
                return responseModel;
            }
            logger.info("===============创建shp文件==============");
            //首先得创建存储目录
            String outFloderStr = UPLOAD_FOLDER + File.separator + uploadFileName;
            String outFileStr = outFloderStr + File.separator + uploadFileName + ".shp";
            File targetFile = new File(outFileStr);
            File parentFile = targetFile.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            GeoToolsUtils.writeSHPbyPoint(outFileStr, plotList);
            File file1 = new File(outFloderStr);
            if (!file1.exists()) {
                responseModel.setMessage("shp及相关文件创建");
                return responseModel;
            } else {
                File[] files1 = file1.listFiles();
                for (File file12 : files1) {
                    if (file12.getName().endsWith(".prj")) {
                        file12.delete();
                        try {
                            file12.createNewFile();
                            FileWriter fileWriter = new FileWriter(file12);
                            fileWriter.write("PROJCS[\"WGS_1984_Web_Mercator_Auxiliary_Sphere\",GEOGCS[\"GCS_WGS_1984\",DATUM[\"D_WGS_1984\",SPHEROID[\"WGS_1984\",6378137.0,298.257223563]],PRIMEM[\"Greenwich\",0.0],UNIT[\"Degree\",0.0174532925199433]],PROJECTION[\"Mercator_Auxiliary_Sphere\"],PARAMETER[\"False_Easting\",0.0],PARAMETER[\"False_Northing\",0.0],PARAMETER[\"Central_Meridian\",0.0],PARAMETER[\"Standard_Parallel_1\",0.0],PARAMETER[\"Auxiliary_Sphere_Type\",0.0],UNIT[\"Meter\",1.0]]");
                            fileWriter.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                            responseModel.setMessage("写入prj文件失败");
                            return responseModel;
                        }
                        break;
                    }
                }
            }
            // 压缩文件夹下的所有文件为zip
            logger.info("===============压缩shp文件，开始==============");
            Result result = ZipUtils.fileToZip(outFloderStr, OUT_FOLDER, uploadFileName);
            if (result.getFlag()) {
                logger.info("===============压缩shp文件，结束==============");
                return responseModel;
            } else {
                logger.info("===============压缩shp文件异常");
                return responseModel;
            }

        } catch (Exception e) {
            logger.info("===============创建shp文件，异常==============");
            e.printStackTrace();
        }
        return responseModel;
    }

    @Override
    public ResponseModel getCount(String radar, String startTime, String endTime) {
        ResponseModel responseModel = new ResponseModel();

        try {
            logger.info(new Date().toString());
            if(org.apache.commons.lang.StringUtils.isBlank(startTime)){
                startTime=DateUtil.getlastYearBeginDate();
            }
            if(org.apache.commons.lang.StringUtils.isBlank(endTime)){
                endTime=DateUtil.getDate("yyyy-MM-dd HH:mm:ss") ;
            }
            // 雷达名称 大写
            String whereCon=" where s.radar_name = '"+radar+"' and s.this_time > '"+startTime+"' and s.this_time < '"+endTime+"' ";
            String sql = " select s.id,s.radar_name,s.flag,s.before_time,s.this_time FROM  diffimage_log  s "+ whereCon+ "ORDER BY s.this_time ";
            Query query = entityManager.createNativeQuery(sql);
            List  rows = query.getResultList();
            entityManager.clear();

            List<CountVo> result=new ArrayList<>();
            String startTime1="";
            String endTime1="";
            for(int i=0;i<rows.size();i++){
                Object[] cells= (Object[])rows.get(i);
                String   flag=cells[2].toString();
                endTime1 = cells[4].toString();
                if(cells[3]==null){
                    startTime1=DateUtil.getlastYearBeginDate();
                    CountVo cvo = new CountVo();
                    cvo.setStartDate(startTime1);
                    cvo.setEndDate(endTime1);
                    cvo.setType("1");
                    result.add(cvo);
                }else {
                    startTime1 = cells[3].toString();
                    CountVo cvo = new CountVo();
                    cvo.setStartDate(startTime1);
                    cvo.setEndDate(endTime1);
                    cvo.setType(flag);
                    result.add(cvo);
                }

            }

//連續1 不合并 連續0 合并
            for (int k = 1; k <=result.size()-1 ; k++) {
                for (int j = 1; j <result.size()-1; j++) {
                    CountVo v1 = result.get(j);
                    CountVo v2= result.get(j+1);
                    if(v2.getType().equals("0")&&v1.getType().equals("0")){
                        v1.setEndDate(v2.getEndDate());
                        result.remove(v2);

                    }
                }
            }
//最后的一條
            CountVo cvo = new CountVo();
            cvo.setStartDate(endTime1);
            cvo.setEndDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            cvo.setType("1");
            result.add(cvo);

            logger.info("===result.size()==="+result.size());
            responseModel.setSuccess(true);
            responseModel.setData(result);
        }catch (Exception e){
            e.printStackTrace();
            responseModel.setSuccess(false);
        }
        return responseModel;
    }

    @Override
    public ResponseModel getStrain(String radar, String startTime, String endTime) {
        ResponseModel responseModel=new ResponseModel();
        Double max = 0.0;
        Double min = 100000.0;
        List<ShapePoint> list = getPointByTime(radar,startTime,endTime);

        for (ShapePoint shapePoint : list){
            Double strain = shapePoint.getStrain();
            if (strain == 0.0){
                continue;
            }
            if (strain > max){
                max = strain;
            }
            if (strain < min){
                min = strain;
            }
        }
        JSONObject json = new JSONObject();
        json.put("max",max);
        json.put("min",min);
        responseModel.setSuccess(true);
        responseModel.setData(json);
        return responseModel;

    }

    @JmsListener(destination = PlotParseQueue)
    public void parseDiffImage(String msg){
        System.out.println("收到消息，准备开始解析diffimage文件");
        JSONObject json = JSONObject.parseObject(msg);
        List<String> pList = json.getJSONArray("filePaths").toJavaList(String.class);
        String[] filePaths = pList.toArray(new String[pList.size()]);
        String filePath = json.getString("filePath");
        String radarName = json.getString("radarName");
        ResponseModel responseModel = parseDiffImage(filePath, filePaths, radarName);
        System.out.println(responseModel.getMessage());
    }

    public List<ShapePoint> getPointByTime(String radarkey,String startTime,String endTime){
        try {
            logger.info("##################################1.1、search ");
            List<ShapePoint> startList = new ArrayList<>();
            Date start = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startTime);
            SqliteFile startSqliteFile = sqliteFileDao.findLastFile(radarkey,start);
            if (startSqliteFile != null) {
                String startFilePath = startSqliteFile.getSqlitePath();
                startList = SqliteUtil.readPointList(startFilePath);
            }
            logger.info("##################################1.1、search start over");

            List<ShapePoint> endList = new ArrayList<>();
            Date end = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endTime);
            SqliteFile endSqliteFile = sqliteFileDao.findLastFile(radarkey,end);
            if (endSqliteFile != null) {
                String endFilePath = endSqliteFile.getSqlitePath();
                endList = SqliteUtil.readPointList(endFilePath);
            }
            logger.info("##################################1.2、search end over");
            if (startList.size() == 0){
                return endList;
            }
            for (int i = 0; i<endList.size(); i++){
                ShapePoint endPoint = endList.get(i);
                ShapePoint startPoint = startList.get(i);
                Double change = endPoint.getStrain() - startPoint.getStrain();
                endPoint.setStrain(change);
            }
            logger.info("##################################1.3、search final over");
            return endList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public ResponseModel parseDiffImage(String filePath, String[] filePaths, String radarName) {
        System.out.println("解析diffimage文件开始...");
        ResponseModel responseModel = new ResponseModel();
        //1、判断雷达是否存在
        Radar radar = radarService.checkByName(radarName);
        if (radar == null) {
            responseModel.setSuccess(false);
            responseModel.setMessage("雷达不存在!");
            return responseModel;
        }
        String radarKey = radar.getRadarKey();
        Double timeInterval = radar.getSampInterval();
        if (timeInterval == null){
            responseModel.setSuccess(false);
            responseModel.setMessage("数据库表radar中接收间隔时间samp_Interval为空");
            return responseModel;
        }
        Long parentId = radar.getRadarId();
        Point l = new Point(radar.getLeftlon(), radar.getLeftlat());//左轨道
        Point r = new Point(radar.getRightlon(), radar.getRightlat());//右轨道

        //2.获取文件列表
        List filePathList=new ArrayList();
        if(filePath != null) {
            File filedir = new File(filePath);
            if (filedir.isDirectory()) {
                File[] files = filedir.listFiles();
                for (File f : files) {
                    String path = f.getAbsolutePath();
                    filePathList.add(path);
                }
            }
        }else if(filePaths!=null){
            for (String s : filePaths) {
                filePathList.add(s);
            }
        }

        //3、遍历列表，开始文件解析
        try {
            for (int k = 0; k < filePathList.size(); k++) {
                String path = filePathList.get(k).toString();
                System.out.println("开始解析文件：" + path);

                List<ShapePoint> plotList = PlotLocationUtil.getPlotList(l, r, path,"all");
                List<ShapePoint> usedList = new ArrayList<>();
                if (plotList != null && plotList.size() > 0) {
                    logger.info("----parseDiffImage----plotList.size()--" + plotList.size());
                } else {
                    logger.info("-----parseDiffImage----plotList is null-----");
                    responseModel.setSuccess(false);
                    return responseModel;
                }
                //4、解析文件名中的时间戳
                File file = new File(path);
                int index = file.getName().lastIndexOf(".");
                String substring = file.getName().substring(0, index);
                Date date = new Date();
                String replace = "";
                try {
                    replace = substring.replace("_", "");
                    date = new SimpleDateFormat("yyyyMMddHHmmss").parse(replace);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                //保存文件到sqlite，与上一个sqlite文件中的数据进行对比生成change累计值、速度值、加速值报错到sqlite数据库
                String sqlitesFile = fileConfig.getSqlitefile();
                String sqlFilePath = sqlitesFile + File.separator + radarKey + File.separator + replace;
                SqliteFile lastSqliteFile = sqliteFileDao.findLastFile(radarKey,date);
                List<ShapePoint> lastPlotList = new ArrayList<>();
                if (lastSqliteFile != null) {
                    System.out.println("有历史文件");
                    String lastFilePath = lastSqliteFile.getSqlitePath();
                    lastPlotList = SqliteUtil.readPointList(lastFilePath);
                }
                if (lastPlotList == null){
                    System.out.println("没有读取到数据");
                }
                int j = 0;
                List<ShapePoint> sqlitePlotList = new ArrayList<>();
                for(int i = 0; i<plotList.size();i++){
                    ShapePoint shapePoint = plotList.get(i);
                    Double finalChange = 0.0;
                    Double speed = 0.0;
                    Double accspeed = 0.0;
                    Double change = shapePoint.getStrain();
                    if(change != -1000){
                        usedList.add(shapePoint);
                    }
                    if (lastPlotList.size() != 0){
                        ShapePoint lastShapePoint = lastPlotList.get(i);
                        Double lastChange = lastShapePoint.getStrain();
                        Double lastSpeed = lastShapePoint.getSpeed();
                        if (change == -1000){
                            change = 0.0;
                        }
                        finalChange = lastChange + change;
                        speed = change / timeInterval;
                        accspeed = (speed - lastSpeed) / timeInterval;
                    }
                    else {
                        if (change == -1000){
                            finalChange = 0.0;
                        }
                        else
                            finalChange = change;
                    }
                    ShapePoint sqlShapePoint = new ShapePoint();
                    sqlShapePoint.setM(shapePoint.getM());
                    sqlShapePoint.setN(shapePoint.getN());
                    sqlShapePoint.setStrain(finalChange);
                    sqlShapePoint.setSpeed(speed);
                    sqlShapePoint.setAccspeed(accspeed);

                    sqlitePlotList.add(sqlShapePoint);
                }
                logger.info("----parseDiffImage----usedList.size()--" + usedList.size());

                SqliteUtil.createSqliteDatabase(sqlitePlotList,sqlFilePath,radarKey);
                SqliteFile sqliteFile = new SqliteFile();
                sqliteFile.setDiffimageFile(file.getName());
                sqliteFile.setRadarKey(radarKey);
                sqliteFile.setSqlitePath(sqlFilePath);
                sqliteFile.setTime(date);

                sqliteFileDao.deleteByRadarKeyAndTime(radarKey,date);
                sqliteFileDao.save(sqliteFile);

                Thread.sleep(4000);

                earlyWarnHistService.addEarlyInf(plotList, null, parentId);
                List<List<ShapePoint>> list = new ArrayList<>();
                int toIndex = 20000;//20000

                int listsize = usedList.size();
                for (int i1 = 0; i1 < listsize; i1 += toIndex) {
                    if (i1 + toIndex > listsize - 1) {
                        toIndex = listsize - i1;
                    }
                    List<ShapePoint> plotInfs = usedList.subList(i1, i1 + toIndex);
                    if (i1 + toIndex > listsize) {
                        list.add(plotInfs);
                        break;
                    } else {
                        list.add(plotInfs);
                    }
                }
                // 记录同步日志（雷达名称、当前时间！）
                // 业务逻辑：取出上次同步时间和这次时间对比：大于20分钟算异常，否则算是正常
                String flag = "1"; //正常标识
                Date beforeTime = null; //上批次时间
                String sql = "select * FROM  diffimage_log where id=(select max(id) from diffimage_log)";
                Query query = entityManager.createNativeQuery(sql, DiffimageLog.class);
                List<DiffimageLog> listDl = (List<DiffimageLog>) query.getResultList();
                entityManager.clear(); //释放持久化bean
                if (listDl != null && listDl.size() > 0) {
                    //进行比较时间
                    beforeTime = listDl.get(0).getThisTime();
                    double min = DateUtil.getMinuteOfTwoDate(beforeTime, date);
                    if (min > 15 && min < 25) {
                        flag = "0";
                    }
                } else {
                    long time = 20 * 60 * 1000; //20分
                    beforeTime = null;
                }

                // 记录批次日志
                DiffimageLog dl = new DiffimageLog();
                dl.setRadarName(radarName);
                dl.setFlag(Long.valueOf(flag));
                dl.setBeforeTime(beforeTime);
                dl.setThisTime(date);
                diffimageLogDao.save(dl);
                //------------------------------------------------------------
                String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
                this.handleList(list, list.size(), Long.valueOf("8888"), radarName, time);
                Thread.sleep(1000);
            }
            responseModel.setSuccess(true);
            responseModel.setMessage("解析diffimage文件完成");
            return responseModel;

        }catch (Exception e){
            e.printStackTrace();
            responseModel.setMessage(e.getMessage());
            return responseModel;
        }
    }

    @Override
    public ResponseModel insertPlotList(String filePath, String radarName) {
        ResponseModel responseModel = new ResponseModel();
        Radar radar = radarService.checkByName(radarName);
        //判断雷达是否存在
        if (radar == null) {
            responseModel.setSuccess(false);
            responseModel.setMessage("雷达不存在");
            return responseModel;
        }
        Long parentId = radar.getRadarId();
        Point l = new Point(radar.getLeftlon(), radar.getLeftlat());//左轨道
        Point r = new Point(radar.getRightlon(), radar.getRy());//右轨道
//        Point l = PlotLocationUtil.get3857Point(lx, ly);//左轨道
//        Point r = PlotLocationUtil.get3857Point(rx, ry);//右轨道
        List<ShapePoint> plotList = PlotLocationUtil.getPlotList(l, r, filePath,"strain");

        File file = new File(filePath);
        int i = file.getName().lastIndexOf(".");
        String substring = file.getName().substring(0, i);
        Date date1 = new Date();
        try {
            String replace = substring.replace("_", "");
            date1 = new SimpleDateFormat("yyyyMMddHHmmss").parse(replace);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Result s = makeShape(plotList, substring, parentId);
        if (s.getFlag()) {
            JSONObject jsonObject1 = JSONObject.parseObject(s.getData().toString());
            earlyWarnHistService.addEarlyInf(plotList, jsonObject1.getLong("catalogId"), parentId);
            List<List<ShapePoint>> list = new ArrayList<>();
            int toIndex = 20000;//20000
            int listsize = plotList.size();
            for (int i1 = 0; i1 < plotList.size(); i1 += toIndex) {
                if (i1 + toIndex > listsize - 1) {
                    toIndex = listsize - i1;
                }
                List<ShapePoint> plotInfs = plotList.subList(i1, i1 + toIndex);
                if (i1 + toIndex > listsize) {
                    list.add(plotInfs);
                    break;
                } else {
                    list.add(plotInfs);
                }
            }
            // 记录同步日志（雷达名称、当前时间）
            // 业务逻辑：取出上次同步时间和这次时间对比：大于20分钟算异常，否则算是正常，并把
            // 正常和异常标志入库，入库字段（雷达名称、上次同步时间、这次同步时间、是否正常）
            //
            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date1);
            handleList(list, list.size(), jsonObject1.getLong("catalogId"), radarName, date);
            responseModel.setSuccess(true);
            responseModel.setData(s.getData());
            responseModel.setMessage("解析-生成shp-上传-执行成功");

        } else {
            responseModel.setSuccess(false);
            responseModel.setMessage("解析-生成shp-上传-执行失败");
        }
        return responseModel;
    }

    @Override
    public ResponseModel dtop(String diffimagePath, String pointCloudPath, String radarName) {
        logger.info(new Date().toString());

        //1、判断雷达是否存在
        ResponseModel responseModel = new ResponseModel();
        Radar radarInfo = radarService.findByRadarName(radarName);
        if ( radarInfo == null ){
            responseModel.setSuccess(false);
            responseModel.setMessage("雷达表中无相关雷达信息");
            return responseModel;
        }
        String radar = radarInfo.getRadarKey();

        //2、根据雷达左右轨坐标计算雷达中心坐标（54坐标）
        double lx = radarInfo.getLx();
        double ly = radarInfo.getLy();
        double lz = radarInfo.getLz();

        double rx = radarInfo.getRx();
        double ry = radarInfo.getRy();
        double rz = radarInfo.getRz();

        double cx = (lx + rx) / 2;  //雷达中心坐标
        double cy = (ly + ry) / 2;
        double cz = (lz + rz) / 2;

        //3、解析diffimage文件
        double LX = radarInfo.getLeftlon();
        double LY = radarInfo.getLeftlat();
        double RX = radarInfo.getRightlon();
        double RY = radarInfo.getRightlat();

        Point l = new Point(LX, LY);//左轨道
        Point r = new Point(RX, RY);//右轨道

        List<ShapePoint> plotList = PlotLocationUtil.getPlotList(l, r, diffimagePath,"all");

        //4、开始构建扫描点的 偏移距离数组rInfos 和 偏移角数组aInfos，用于后续的二分查找
        int M = 0;
        int N = 0;
        PlotCon plotCon = null;
        try {
            plotCon = new PlotCon();
            Object[] objects = plotCon.plotCon(1, diffimagePath);
            MWCellArray object = (MWCellArray) objects[0];
            List<MWArray> mwArrays = object.asList();
            M = Integer.valueOf(mwArrays.get(0).toString()); //像素行数
            N = Integer.valueOf(mwArrays.get(1).toString()); //像素列数
        } catch (MWException e) {
            e.printStackTrace();
        }
        double[] rInfos = new double[M];
        double[] aInfos = new double[N];
        int i = 0;
        for (ShapePoint shapePoint : plotList) {
            rInfos[i / N] = shapePoint.getrAxis();
            if (i < N) {
                aInfos[i] = shapePoint.getaAxis();
            }
            i++;
        }

        //5、读取点云文件中的每个点,与diffimage中的点进行匹配
        double pointX = 0.0;
        double pointY = 0.0;
        double pointZ = 0.0;
        double rAxis = 0.0;
        double aAxis = 0.0;
        double plotAngle = 0.0; //点偏移角度
        List<PointCloud> listPoint = new ArrayList<>();

        //读取矿场点云文件
        try (
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(pointCloudPath), "GBK"));
        ) {
            String line = null;

            //遍历点云文件，通过每个点的坐标，计算出于雷达偏转距离 和 偏转角
            while ((line = br.readLine()) != null) {
                int mPosition = -1;
                int nPosition = -1;
                String[] xyz = new String[1024];
                if (line.contains(",")){
                    xyz = line.split(",");
                }else {
                    xyz = line.split(" ");
                }
                if (xyz.length<3){
                    continue;
                }
                pointX = Double.parseDouble(xyz[0]);
                pointY = Double.parseDouble(xyz[1]);
                pointZ = Double.parseDouble(xyz[2]);

                //计算偏移距离
                rAxis = Math.sqrt(Math.pow(pointX - cx, 2) + Math.pow(pointY - cy, 2)+Math.pow(pointZ-cz,2));
                //计算偏移角
                //左右轨道的斜率 方程1
                double k1 = (ry-ly) / (rx- lx);
                double m1 = cy - k1*cx;
                //中垂线直线方程2
                double k2 = -(1.0/k1);
                double m2 = cy - k2*cx;
                //点云与轨道平行的直线方程3
                double k3 = k1;
                double m3 = pointY - pointX * k3;

                //点云方程 与 中垂线方程的交点
                double xj = (m3 - m2)/(k2 - k3);
                double yj = k3 * xj + m3;

                double x = Math.sqrt(Math.pow(pointX - xj, 2) + Math.pow(pointY - yj, 2));
                double y = Math.sqrt(Math.pow(cx - xj, 2) + Math.pow(cy - yj, 2));
                aAxis = Math.atan(x / y);
                if (rx > lx){
                    double tmpy1 = k1 * pointX + m1;
                    if (tmpy1 > pointY){
                        //不用匹配
                        mPosition = -1;
                        nPosition = -1;
                    }
                    else {
                        double tmpy2 = k2 * pointX + m2;
                        if (tmpy2 > pointY) {
                            aAxis = -aAxis;
                        }
                        mPosition = binSearch_1(rAxis,rInfos,rAxis/100);
                        nPosition = binSearch_1(aAxis,aInfos, 0.01);
                    }
                }else {
                    double tmpy1 = k1 * pointX + m1;
                    if (tmpy1 < pointY){
                        //不用匹配
                        mPosition = -1;
                        nPosition = -1;
                    }
                    else {
                        double tmpy2 = k2 * pointX + m2;
                        if (tmpy2 < pointY) {
                            aAxis = -aAxis;
                        }
                        mPosition = binSearch_1(rAxis,rInfos,rAxis/100);
                        nPosition = binSearch_1(aAxis,aInfos, 0.01);
                    }
                }

                ShapePoint shapePoint = null;
//                for (ShapePoint point : plotList){
//                    if (point.getM() == mPosition && point.getN()==nPosition){
//                        if (point.getStrain() != -1000) {
//                            shapePoint = point;
//                        }else{
//                            mPosition = -1;
//                            nPosition = -1;
//                        }
//                    }
//                }
                if (mPosition != -1 && nPosition != -1) {
                    int index = mPosition * nPosition + nPosition;
                    shapePoint = plotList.get(index);
                    if(shapePoint.getStrain() == -1000){
                        mPosition = -1;
                        nPosition = -1;
                        shapePoint = null;
                    }
                }

                PointCloud pointCloud = new PointCloud();
                pointCloud.setX(pointX);
                pointCloud.setY(pointY);
                pointCloud.setZ(pointZ);
                pointCloud.setaAxis(aAxis);
                pointCloud.setrAxis(rAxis);
                pointCloud.setRadar(radar);
                pointCloud.setM(mPosition);
                pointCloud.setN(nPosition);
                if(shapePoint != null) {
                    pointCloud.setLot(shapePoint.getX());
                    pointCloud.setLat(shapePoint.getY());
                }
                listPoint.add(pointCloud);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        StringBuilder sql = new StringBuilder();
        sql.append("insert into point_cloud");
        sql.append("(x,y,z,r_axis,a_axis,m,n,radar,geo) values (?,?,?,?,?,?,?,?,ST_GeomFromText(?,3857))");

        int[][] insertCounts = jdbcTemplate.batchUpdate(
            sql.toString(),
            listPoint,
            20000,
            new ParameterizedPreparedStatementSetter<PointCloud>() {
                @Override
                public void setValues(PreparedStatement preparedStatement, PointCloud sp) throws SQLException {
                    double x = sp.getX();
                    double y = sp.getY();
                    double z = sp.getZ();
                    int m = sp.getM();
                    int n = sp.getN();
                    double lot = sp.getLot();
                    double lat = sp.getLat();
                    com.vividsolutions.jts.geom.Point geo = null;
                    if(lot != 0.0 && lat != 0.0){
                        geo = GeometryUtil.createPoint(sp.getLot(),sp.getLat());
                    }
                    String radar = sp.getRadar();
                    Double aAxis = sp.getaAxis();
                    Double rAxis = sp.getrAxis();
                    preparedStatement.setDouble(1, x);
                    preparedStatement.setDouble(2, y);
                    preparedStatement.setDouble(3, z);
                    preparedStatement.setDouble(4, rAxis);
                    preparedStatement.setDouble(5, aAxis);
                    preparedStatement.setInt(6, m);
                    preparedStatement.setInt(7, n);
                    preparedStatement.setString(8,radar);
                    if(geo != null)
                        preparedStatement.setString(9,geo.toString());
                    else
                        preparedStatement.setString(9,null);
                }
            }
        );
        responseModel.setSuccess(true);
        return responseModel;
    }

    private void writePlyFile(String resultPath,List<PointObject> pointObjectList){
        File file = new File(resultPath);
        if(!file.exists()){
            file.mkdir();
        }
        String filePath = resultPath + File.separator + "test.ply";
        try (
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath));
        ) {
            bufferedWriter.write("ply\n" +
                    "format ascii 1.0\n" +
                    "comment Created by CloudCompare v2.11 alpha (Anoia)\n" +
                    "comment Created 20/08/09 18:51\n" +
                    "obj_info Generated by CloudCompare!\n" +
                    "element vertex " + pointObjectList.size() + "\n" +
                    "property double x\n" +
                    "property double y\n" +
                    "property double z\n" +
                    "property double Intensity\n" +
                    "end_header\n");
            for (PointObject pointObject : pointObjectList){
                double x = pointObject.getX();
                double y = pointObject.getY();
                double z = pointObject.getZ();
                double strain = pointObject.getAddProperty();
                bufferedWriter.write(x + " " +y + " " + z + " " + strain + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized ResponseModel ptostrain(String radarName, String startTime, String endTime, Long parentId) {
        logger.info("##################################1、start produce pointcloud strain");
        ResponseModel responseModel = new ResponseModel();
        try{
            //1、检查雷达是否存在
            Radar radarInfo = radarService.findByRadarName(radarName);
            if ( radarInfo == null ){
                responseModel.setSuccess(false);
                responseModel.setMessage("雷达表中无相关雷达信息");
                return responseModel;
            }
            String wkt = radarInfo.getWkt();
            String radarKey = radarInfo.getRadarKey();

            //2、组合生成文件的名称
            String startStr = DateUtil.stringToString(startTime,"yyyyMMddHHmmss");
            String endStr = DateUtil.stringToString(endTime,"yyyyMMddHHmmss");
            String tilesFileName = "point_"+radarKey+"_"+startStr+"_to_"+endStr;

            //3、检查这个文件以前与没有生成过，有的话直接返回结果
            ResponseModel responseModelTmp = pointFileService.pointFileByName(tilesFileName+".zip");
            if(responseModelTmp.getData() != null){
                return responseModelTmp;
            }

            //3、中间文件ply的的名称
            String fileName = radarKey+"_"+startStr+"_to_"+endStr;
            String plyFilePath = fileConfig.getPlyfile() + File.separator + fileName;

            //4、1）通过sqlite文件获取雷达扫描点的形变量累加值 2）获取已经匹配好的矿场点云的所有点
            List<ShapePoint> plotList = getPointByTime(radarKey,startTime,endTime);
            List<PointCloud> pointClouds = pointCloudDao.findValidPointByRadar(radarKey);

            //5、通过两个列表中m,n的值进行关联，并统计色带区间
            List<Qujian> qujian = new ArrayList<>();
            int plusLen = 0;
            int minusLen = 0;
            List<Double> strainList = new ArrayList<>();
            List<PointObject> pointObjectList = new ArrayList<>();
            int k = 0;
            for (ShapePoint shapePoint : plotList) {
                Long m = shapePoint.getM();
                Long n = shapePoint.getN();
                double strain = shapePoint.getStrain();
                if (strain != 0.0) {
                    for(int i=k; i<pointClouds.size();i++){
                        PointCloud pointCloud = pointClouds.get(i);
                        double x = pointCloud.getX();
                        double y = pointCloud.getY();
                        double z = pointCloud.getZ();
                        int mp = pointCloud.getM();
                        int np = pointCloud.getN();
                        if(mp == m && np == n){
                            strainList.add(strain);
                            if (strain < 0.0)
                                minusLen++;
                            else if (strain > 0.0) {
                                plusLen++;
                            }

                            strain = strain + 1000.0;
                            PointObject pointObject = new PointObject(x, y, z, strain);
                            pointObjectList.add(pointObject);
                            k = i;
                            break;
                        }
                        if(m < mp){
                            break;
                        }
                    }
                }
            }
            strainList.sort((x, y) -> Double.compare(x, y));
            //6、是否需要生成中间文件ply
            if(fileConfig.isPlyexists()) {
                writePlyFile(plyFilePath, pointObjectList);
            }
            logger.info("##################################3、total:" + plotList.size()+", useful:"+pointObjectList.size());
            //7、组合拼接色带区间
            if (minusLen > 3) {
                Qujian qujian1 = new Qujian(strainList.get(0),strainList.get(minusLen / 2),"rgba(235,96,3,0.6)",0.2);
                Qujian qujian2 = new Qujian(strainList.get(minusLen / 2 ),strainList.get(minusLen-1),"rgba(230,230,0,0.6)",0.2);
//                Qujian qujian3 = new Qujian(strainList.get(minusLen * 2 / 3 ),strainList.get(minusLen-1));
                qujian.add(qujian1);
                qujian.add(qujian2);
//                qujian.add(qujian3);
            }
            else if (minusLen > 0){
                Qujian qujian1 = new Qujian(strainList.get(0),strainList.get(minusLen - 1),"rgba(235,96,3,0.6)",0.2);
                qujian.add(qujian1);
            }
            if (plusLen > 3) {
                Qujian qujian4 = new Qujian(strainList.get(minusLen),strainList.get(minusLen + plusLen / 3),"rgba(30,255,255,0.6)",0.2);
                Qujian qujian5 = new Qujian(strainList.get(minusLen + plusLen / 3),strainList.get(minusLen + plusLen * 2 / 3),"rgba(53,159,230,0.6)",0.2);
                Qujian qujian6 = new Qujian(strainList.get(minusLen + plusLen * 2 / 3),strainList.get(minusLen + plusLen - 1),"rgba(16,0,213,0.6)",0.2);
                qujian.add(qujian4);
                qujian.add(qujian5);
                qujian.add(qujian6);
            }
            else if (plusLen > 0) {
                Qujian qujian2 = new Qujian(plotList.get(minusLen).getStrain(),plotList.get(minusLen + plusLen - 1).getStrain(),"rgba(16,0,213,0.6)",0.2);
                qujian.add(qujian2);
            }
            logger.info("##################################4、compute qujian finish");

            //8、生成3dtiles文件，并上传数管系统制定目录下。
            String tilesFilePath = fileConfig.getTilesfile() + File.separator + tilesFileName;
            boolean success = doProcess(pointObjectList,tilesFilePath,wkt);
            logger.info("##################################5、convert 3dtiles finish");

            FileOutputStream fos1 = null;
            Long fileId = -2L;
            if (success) {
                File zipFile = new File(tilesFilePath + ".zip");
                fos1 = new FileOutputStream(zipFile);
                ZipUtils.toZip(tilesFilePath, fos1, true);

                //上传文件1
                HttpHeaders requestHeaders = new HttpHeaders();
                requestHeaders.add("Accept","application/json");

                String urlStr1 = dmaddress+"/manager/rest/file/uploadCheck";
                MultiValueMap<String, Object> multipartRequest1 = new LinkedMultiValueMap<>();
                multipartRequest1.add("name", zipFile.getName());
                multipartRequest1.add("fileSize", parentId);
                multipartRequest1.add("lastModified", zipFile.lastModified());
                multipartRequest1.add("fileType",8);
                multipartRequest1.add("parentId",parentId);
                HttpEntity<MultiValueMap<String, Object>> requestEntity1 = new HttpEntity(multipartRequest1, requestHeaders);
                ResponseEntity<String> responseEntity1 = restTemplate.postForEntity(urlStr1, requestEntity1, String.class);
                if (responseEntity1.getStatusCode() != HttpStatus.OK) {
                    throw new Exception();
                }
                logger.info("##################################6、uploadcheck finish");

                String data = responseEntity1.getBody();
                fileId = JSONObject.parseObject(data).getLong("fid");
                //上传文件2
                String urlStr2 = dmaddress+"/manager/rest/file/upload";
                requestHeaders.add("Accept","application/json");
                requestHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
                FileSystemResource resourceFile = new FileSystemResource(tilesFilePath + ".zip");

                MultiValueMap<String, Object> multipartRequest2 = new LinkedMultiValueMap<>();
                multipartRequest2.add("file", resourceFile);
                multipartRequest2.add("fid", fileId);
                multipartRequest2.add("name", fileName);
                multipartRequest2.add("chunk",null);
                HttpEntity<MultiValueMap<String, Object>> requestEntity2 = new HttpEntity(multipartRequest2, requestHeaders);
                ResponseEntity<String> responseEntity2 = restTemplate.postForEntity(urlStr2, requestEntity2, String.class);
                if (responseEntity2.getStatusCode() != HttpStatus.OK) {
                    throw new Exception();
                }
                logger.info("##################################7、upload finish");

                String urlStr3 = dmaddress+"/manager/rest/file/uploadMerge";
                MultiValueMap<String, Object> multipartRequest3 = new LinkedMultiValueMap<>();
                multipartRequest3.add("fid", fileId);
                multipartRequest3.add("param", "{\"note\":\"\"}");
                HttpEntity<MultiValueMap<String, Object>> requestEntity3 = new HttpEntity(multipartRequest3, requestHeaders);
                ResponseEntity<String> responseEntity3 = restTemplate.postForEntity(urlStr3, requestEntity3, String.class);
                if (responseEntity3.getStatusCode() != HttpStatus.OK) {
                    throw new Exception();
                }
                logger.info("##################################8、uploadmerge finish");
            }
            else{
                responseModel.setSuccess(false);
                responseModel.setMessage("变形图生成失败");
                return responseModel;
            }
            JSONObject data = new JSONObject();
            data.put("qujian",qujian);
            data.put("fid",fileId);

            PointQujian pointQujian = new PointQujian();
            pointQujian.setPointFile(tilesFileName + ".zip");
            pointQujian.setPointFileId(fileId);
            pointQujian.setQujian(JSONArray.toJSONString(qujian));
            pointFileService.save(pointQujian);

            responseModel.setSuccess(true);
            responseModel.setMessage("变形图生成完成");
            responseModel.setData(data);
            logger.info("##################################9、all finish");
            return responseModel;
        } catch (Exception e) {
            responseModel.setSuccess(false);
            responseModel.setMessage("变形图生成失败");
            e.printStackTrace();
            return responseModel;
        }
    }

    /**
     * 点云转化Tiles文件
     */
    private static boolean doProcess(List<PointObject> pointObjectList,String outputPath, String wkt) {
        String srid = wkt;
        int colorBitSize = Integer.valueOf(8).intValue();

        double zScaleFactor = 1.0D;
        double zOffset = 0D;
        double tileSize = 1000.0D;

        int maxNumOfPointsPerTile = 200000;

        File file = new File(outputPath);
        if (!file.exists()) {
            file.mkdirs();
        }

        PntcConfig config = new PntcConfig();
        config.setInputPath("");
        config.setSrid(srid);
        config.setColorBitSize(colorBitSize);
        config.setzScaleFactor(zScaleFactor);
        config.setzOffset(zOffset);
        config.setTileSize(tileSize);
        config.setMaxNumOfPointsPerTile(maxNumOfPointsPerTile);
        config.setOutputFolderPath(outputPath);
        config.setSeparatorCharacter(" ");

        SqliteDBManagerFactory dbManagerFactory = new SqliteDBManagerFactory(config);
        PntcGenerator generator = new PntcGenerator(config, dbManagerFactory);

        boolean success = false;
        try {
            success = generator.doProcess(pointObjectList,false);
        } catch (PntcGenerationException e) {
            com.gis.trans.tiles3d.util.Logger.error(e.getMessage());

            Throwable cause = e.getCause();
            while (cause != null) {
                com.gis.trans.tiles3d.util.Logger.error("Cause: " + cause.getMessage());
                cause = cause.getCause();
                generator.setShouldRun(false);
            }
        }
        return success;
    }

    private String getReadlineCount(String path){
        FileReader fr = null; //这里定义一个字符流的输入流的节点流，用于读取文件（一个字符一个字符的读取）
        try {
            fr = new FileReader(path);
            BufferedReader br = new BufferedReader(fr); // 在定义好的流基础上套接一个处理流，用于更加效率的读取文件（一行一行的读取）
            int x = 0; // 用于统计行数，从0开始
            while(br.readLine() != null) { // readLine()方法是按行读的，返回值是这行的内容
                x++; // 每读一行，则变量x累加1
            }
            return String.valueOf(x);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private int binSearch_1(double key, double[] array, double cha) {
        int low = 0; //第一个下标
        int high = array.length - 1;//最后一个下标
        int middle = 0;
        //防越界
        if (key < array[low] || key > array[high]) {
            if (Math.abs(key - array[low]) < cha ){
                return low;
            }
            else if (Math.abs(key - array[high]) < cha ){
                return high;
            }
            else
                return -1;
        }
        while (low + 1 != high) {
            middle = (low + high) / 2;
            if (array[middle] == key) {
                return middle;
            } else if (array[middle] < key) {
                low = middle;
            } else {
                high = middle;
            }
        }
        if (Math.abs(key - array[low]) < cha ){
            return low;
        }
        else if (Math.abs(key - array[high]) < cha ){
            return high;
        }
        else
            return -1;
    }

     private Double getPlot(Double n) {
        if (Math.abs(n)<Math.PI/2){
            return -n;
        }else if (n<-Math.PI/2&&n>-Math.PI){
            return -(n+Math.PI);
        }else if (n>Math.PI/2&&n<Math.PI){
            return -(n-Math.PI);
        }else if (n>Math.PI){
            return n-Math.PI*1.5;
        }else {
            return n+Math.PI*1.5;
        }
    }

    public void handleList(List<List<ShapePoint>> data, int threadNum, Long fileId, String radar, String date) {
        for (int i = 0; i < threadNum; i++) {//10-->threadNum
            List<ShapePoint> plotInfs = data.get(i);
            InsertListThreadL insertListThread = new InsertListThreadL(plotInfs) {
                @Override
                public void method(List<ShapePoint> plotInfs) {
                    JSONArray jsonArray = JSONArray.parseArray(JSON.toJSONString(plotInfs));
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("radar", radar);
                    jsonObject.put("fileId", fileId);
                    jsonObject.put("time", date);
                    jsonObject.put("list", jsonArray);
                    producer.sendMessage(PlotAddQueue, jsonObject.toJSONString());
                }
            };
            insertListThread.start();
        }
    }

    public static double[] get84(double x, double y) {
        //   wgsbj54.SetWellKnownGeogCS("GCS_Beijing_1954");
        SpatialReference wgsbj54 = new SpatialReference("PROJCS[\"Beijing 1954 / 3-degree Gauss-Kruger CM 120E\", \n" +  "  GEOGCS[\"Beijing 1954\", \n" + "    DATUM[\"Beijing 1954\", \n" + "      SPHEROID[\"Krassowsky 1940\", 6378245.0, 298.3, AUTHORITY[\"EPSG\",\"7024\"]], \n" + "      TOWGS84[15.8, -154.4, -82.3, 0.0, 0.0, 0.0, 0.0], \n" + "      AUTHORITY[\"EPSG\",\"6214\"]], \n" + "    PRIMEM[\"Greenwich\", 0.0, AUTHORITY[\"EPSG\",\"8901\"]], \n" + "    UNIT[\"degree\", 0.017453292519943295], \n" + "    AXIS[\"Geodetic longitude\", EAST], \n" + "    AXIS[\"Geodetic latitude\", NORTH], \n" + "    AUTHORITY[\"EPSG\",\"4214\"]], \n" + "  PROJECTION[\"Transverse_Mercator\"], \n" + "  PARAMETER[\"central_meridian\", 114.0], \n" + "  PARAMETER[\"latitude_of_origin\", 0.0], \n" + "  PARAMETER[\"scale_factor\", 1.0], \n" + "  PARAMETER[\"false_easting\", 500000.0], \n" + "  PARAMETER[\"false_northing\", 0.0], \n" + "  UNIT[\"m\", 1.0], \n" + "  AXIS[\"Easting\", EAST], \n" + "  AXIS[\"Northing\", NORTH], \n" + "  AUTHORITY[\"EPSG\",\"2437\"]]");
//        SpatialReference wgsbj54 = new SpatialReference();
//        wgsbj54.ImportFromEPSG(2435);
        System.out.println(wgsbj54.ExportToWkt());
        SpatialReference wgs84 = new SpatialReference();
        //  wgs84.SetWellKnownGeogCS("WGS84");
        wgs84.ImportFromEPSG(4326);
        CoordinateTransformation coordinateTransformation = CoordinateTransformation.CreateCoordinateTransformation(wgsbj54, wgs84);
        double[] srsStdOut = new double[3];
        coordinateTransformation.TransformPoint(srsStdOut, x, y); // 转化到WGS84或GCJ02大地坐标
        return srsStdOut;
    }
}
