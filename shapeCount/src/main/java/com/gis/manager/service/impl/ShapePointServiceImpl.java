package com.gis.manager.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gis.manager.annotation.mutijms.MultiJmsListener;
import com.gis.manager.common.ApplicationContextProvider;
import com.gis.manager.dao.ShapePointDao;
import com.gis.manager.model.RadarInfo;
import com.gis.manager.model.ShapePoint;
import com.gis.manager.model.extra.PointInfo;
import com.gis.manager.model.extra.RangeCount;
import com.gis.manager.service.IRadarInfoService;
import com.gis.manager.service.IShapePointService;

import com.gis.manager.utils.DateUtil;
import com.gis.manager.utils.GeometryUtil;
import com.vividsolutions.jts.geom.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ShapePointServiceImpl extends BaseServiceImpl<ShapePoint, String> implements IShapePointService {

    private static final Logger logger = LoggerFactory.getLogger(ShapePointServiceImpl.class);

    @Autowired
    private ShapePointDao shapePointDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;
    @Autowired
    private IRadarInfoService radarInfoService;

    private static final String SHAPE_POINT_QUEUE = "PlotAdd.queue";

    private static final String table_name = "shape_point";

    @Value("${insert_num}")
    private int INSERT_NUM;

    @Override
    public JpaRepository<ShapePoint, String> getDao() {
        return shapePointDao;
    }

    /**
     * 对消息队列进行监听，获取已经上传的shape文件路径，对shape文件进行解析，要素信息入库
     * @param msg
     */
    @MultiJmsListener(destination = SHAPE_POINT_QUEUE)
    public void addShapePoints(String msg) {

        if (jdbcTemplate == null){
            jdbcTemplate = (JdbcTemplate) ApplicationContextProvider.getBean("jdbcTemplate");
        }
        if (jmsMessagingTemplate == null){
            jmsMessagingTemplate = (JmsMessagingTemplate) ApplicationContextProvider.getBean("jmsMessagingTemplate");
        }
        if (radarInfoService == null){
            radarInfoService = (IRadarInfoService) ApplicationContextProvider.getBean(RadarInfoServiceImpl.class);
        }

        JSONObject json = JSONObject.parseObject(msg);
        List<PointInfo> pList = json.getJSONArray("list").toJavaList(PointInfo.class);
        String time = json.getString("time");
        String day = DateUtil.stringToString(time,"yyyyMMdd");
        String radar = json.getString("radar");
        logger.info((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date()) + "," + Thread.currentThread().getId()+" : " + time + " , " + day +"start");
        RadarInfo radarInfo = radarInfoService.findOne(radar);
        if (radarInfo == null){
            logger.info("雷达"+radar+"信息不存在");
            return;
        }
        String radarKey = radarInfo.getRadarKey();
        int index = 1;
        StringBuilder sql = new StringBuilder();
        sql.append("insert into shape_point_");
        sql.append(radarKey);
        sql.append("_");
        sql.append(day);
        sql.append("(file_id,m,n,change,time,geo,speed,accspeed,radar,id,range,a_axis,r_axis) values (?,?,?,?,'" + time + "',ST_GeomFromText(?,3857),?,?,'"+radarKey+"',?,?,?,?)");

        int size = INSERT_NUM;
        int[][] insertCounts = jdbcTemplate.batchUpdate(
            sql.toString(),
            pList,
            10000,
            new ParameterizedPreparedStatementSetter<PointInfo>() {
                @Override
                public void setValues(PreparedStatement preparedStatement, PointInfo sp) throws SQLException {
                    String id = UUID.randomUUID().toString().replace("-", "");
                    Long fileId = 8888L;
                    Long m = sp.getM();
                    Long n = sp.getN();
                    Double strain = sp.getStrain();
                    String range = range(strain);
                    Double x = sp.getX();
                    Double y = sp.getY();
                    Point point = GeometryUtil.createPoint(x, y);
                    Double aAxis = sp.getaAxis();
                    Double rAxis = sp.getrAxis();
                    preparedStatement.setLong(1, fileId);
                    preparedStatement.setLong(2, m);
                    preparedStatement.setLong(3, n);
                    preparedStatement.setDouble(4, strain);
                    preparedStatement.setString(5, point.toText());
                    preparedStatement.setDouble(6, 0.00);
                    preparedStatement.setDouble(7, 0.00);
                    preparedStatement.setString(8, id);
                    preparedStatement.setString(9, range);
                    preparedStatement.setDouble(10, aAxis);
                    preparedStatement.setDouble(11, rAxis);
                }
            });
        logger.info((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date()) + "," + Thread.currentThread().getName()+" : over");
    }

    private String range(Double strain){
        if (strain >= -1 && strain <= -0.5){
            return "[-1,-0.5]";
        }
        else if (strain > -0.5 && strain <= -0.2){
            return "(-0.5,-0.2]";
        }
        else if (strain > -0.2 && strain <= 0.2){
            return "(-0.2,0.2]";
        }
        else if (strain > 0.2 && strain <= 0.5){
            return "(0.2,0.5]";
        }
        else if (strain > 0.5 && strain <= 1){
            return "(0.5,1]";
        }
        return "other";
    }

    private static Point get3857Point(double lon, double lat) {
        lon = lon * 20037508.34 / 180;
        lat = Math.log(Math.tan((90 + lat) * Math.PI / 360)) / (Math.PI / 180);
        lat = lat * 20037508.34 / 180;
        return GeometryUtil.createPoint(lon, lat);
    }

    private static Point getLonLatPoint(double x, double y) {
        Double lon = x / 20037508.34 * 180;
        Double lat = y / 20037508.34 * 180;
        lat = 180 / Math.PI * (2 * Math.atan(Math.exp(lat * Math.PI / 180)) - Math.PI/2);
        return GeometryUtil.createPoint(lon, lat);
    }

//    private synchronized void add(String msg){
//        if (fileIdPartitionService == null){
//            fileIdPartitionService = (IFileIdPartitionService) ApplicationContextProvider.getBean(FileIdPartitionImpl.class);
//        }
//        JSONObject json = JSONObject.parseObject(msg);
//        String time = json.getString("time");
//        RadarInfo radarInfo = radarInfoService.findOne(json.getString("radar"));
//        if (radarInfo == null){
//            return;
//        }
//        String radar = radarInfo.getRadarKey();
//        Long fileId = json.getLong("fileId");
//        FileIdPartition fileIdPartition = fileIdPartitionService.findByFileId(fileId);
//        if ( fileIdPartition == null){
//            fileIdPartition = new FileIdPartition();
//            fileIdPartition.setTime(DateUtil.stringToDate(time));
//            fileIdPartition.setFilePath(fileId);
//            fileIdPartition.setRadar(radar);
//            fileIdPartitionService.save(fileIdPartition);
//        }
//    }

//    @Override
//    public ModelMap MaxMin(Long fileId) {
//        ModelMap modelMap = new ModelMap("success","false");
//        FileIdPartition fileIdPartition = fileIdPartitionService.findByFileId(fileId);
//        if (fileIdPartition == null){
//            modelMap.put("describe","文件"+fileId+"不存在");
//            return modelMap;
//        }
//        Date time = fileIdPartition.getTime();
//        String radar = fileIdPartition.getRadar();
//        MaxMin map = shapePointDao.findMaxMinChange(radar,time,fileId);
//        modelMap.put("success",true);
//        modelMap.put("data",map);
//        return modelMap;
//    }

//    @Override
//    public ModelMap closedPoint(Long fileId, Double x, Double y) {
//        ModelMap modelMap = new ModelMap("success","false");
//        FileIdPartition fileIdPartition = fileIdPartitionService.findByFileId(fileId);
//        if (fileIdPartition == null){
//            modelMap.put("describe","文件"+fileId+"不存在");
//            return modelMap;
//        }
//        Date time = fileIdPartition.getTime();
//        DecimalFormat df = new DecimalFormat("#.###########");
//        StringBuilder point = new StringBuilder("POINT");
//        point.append("("+df.format(x)+" "+df.format(y)+")");
//        String res = shapePointDao.findClosedPoint(time,fileId,point.toString());
//        modelMap.put("success",true);
//        modelMap.put("data",res);
//        return modelMap;
//    }

    @Override
    public ModelMap statitcsSpeed(String radar, String startTime, String endTime) {
        ModelMap modelMap = new ModelMap("success","false");
        JSONArray ponits = new JSONArray();
        RadarInfo radarInfo = radarInfoService.findOne(radar);
        if (radarInfo == null){
            return modelMap;
        }
        String radarKey = radarInfo.getRadarKey();
        try {
            List<ShapePoint> list = shapePointDao.findStatitcsSpeed(radarKey, DateUtil.stringToDate(startTime), DateUtil.stringToDate(endTime));

            int i = 10;
            while (i <= 30) {
                JSONObject point = new JSONObject();
                List<Double> speed = new ArrayList<>();
                List<Date> time = new ArrayList<>();
                List<Double> accspeed = new ArrayList<>();
                List<Double> strain = new ArrayList<>();
                for (ShapePoint sp : list) {
                    Long m = sp.getM();
                    if (m == i) {
                        speed.add(sp.getSpeed());
                        accspeed.add(sp.getAccSpeed());
                        time.add(sp.getTime());
                        strain.add(sp.getChange());
                    }
                }
                if (speed.size() == 0 || accspeed.size() == 0 ||time.size() == 0 ||strain.size() == 0 )
                {
                    i++;
                    continue;
                }
                point.put("speed", speed);
                point.put("accspeed", accspeed);
                point.put("time", time);
                point.put("strain", strain);
                ponits.add(point);
                i++;
            }
            modelMap.put("success", true);
            modelMap.put("data", ponits);
            return modelMap;
        }catch(NumberFormatException e){
            modelMap.put("describe","日期参数格式错误");
            return modelMap;
        }
    }

    @Override
    public ModelMap statitcsRange(String radar, String startTime, String endTime) {
        ModelMap modelMap = new ModelMap("success","false");
        if (radar == null || radar.isEmpty()){
            modelMap.put("describ","radar信息无效");
            return modelMap;
        }
        if (startTime == null || endTime == null){
            modelMap.put("describ","时间参数信息为null");
            return modelMap;
        }
        if (startTime.isEmpty() || endTime.isEmpty()){
            modelMap.put("describ","时间参数信息为空");
            return modelMap;
        }
        Map<String,Long> res = new HashMap<>();
        res.put("[-1,-0.5]",0L);
        res.put("(-0.5,-0.2]",0L);
        res.put("(-0.2,0.2]",0L);
        res.put("(0.2,0.5]",0L);
        res.put("(0.5,1]",0L);
        res.put("other",0L);
        RadarInfo radarInfo = radarInfoService.findOne(radar);
        if (radarInfo == null){
            modelMap.put("describ","radar信息无效");
            return modelMap;
        }
        String radarKey = radarInfo.getRadarKey();
        try {
            List<RangeCount> list = shapePointDao.findStatitcsRange(radarKey,DateUtil.stringToDate(startTime), DateUtil.stringToDate(endTime));

            for (RangeCount r : list){
                res.put(r.getRange(),r.getCount());
            }
            modelMap.put("success",true);
            modelMap.put("data",res);
            return modelMap;
        }catch(NumberFormatException e){
            modelMap.put("describe","日期参数格式错误");
            return modelMap;
        }
    }

//    @Override
//    public ModelMap sifteShape(String polygon, Long fileId) {
//        ModelMap modelMap = new ModelMap("success","false");
//        try{
//            JSONArray jsonList = JSONArray.parseArray(polygon);
//            StringBuilder poly = new StringBuilder();
//            for (int i = 0;i<jsonList.size();i++){
//                JSONObject json = jsonList.getJSONObject(i);
//                Double lon = json.getDouble("lon");
//                Double lat = json.getDouble("lat");
//                poly.append(lon);
//                poly.append(" ");
//                poly.append(lat);
//                poly.append(",");
//            }
//            JSONObject json = jsonList.getJSONObject(0);
//            Double lon = json.getDouble("lon");
//            Double lat = json.getDouble("lat");
//            poly.append(lon);
//            poly.append(" ");
//            poly.append(lat);
//            FileIdPartition fileIdPartition = fileIdPartitionService.findByFileId(fileId);
//            if (fileIdPartition == null){
//                modelMap.put("describ","文件记录不存在");
//                return modelMap;
//            }
//            String radar = fileIdPartition.getRadar();
//            String time = DateUtil.dateToString(fileIdPartition.getTime()).substring(0,10).replace("-","");
//
//            String sql = "select max(change) as max, avg(change) as avg, st_area(ST_Transform(st_geometryfromtext('POLYGON(("+poly.toString()+"))',4326),4527))as area from shape_point_"+radar+"_"+time+" where file_id = "+fileId+" and st_contains(ST_GeomFromText('POLYGON(("+poly.toString()+"))',3857),geo) and change <> -1000";
//            List<SifteShape> list = jdbcTemplate.query(sql,new RowMapper<SifteShape>(){
//                @Override
//                public SifteShape mapRow(ResultSet resultSet, int i) throws SQLException {
//                    SifteShape sifteShape = new SifteShape();
//                    sifteShape.setMaxChange(resultSet.getDouble("max"));
//                    sifteShape.setAvgChange(resultSet.getDouble("avg"));
//                    sifteShape.setArea(resultSet.getDouble("area"));
//                    return sifteShape;
//                }
//            });
//            modelMap.put("data",list.get(0));
//            modelMap.put("success",true);
//            return modelMap;
//        }catch(com.alibaba.fastjson.JSONException e){
//            modelMap.put("describ","polygon json格式错误");
//            return modelMap;
//        }
//    }

    @Override
    public ModelMap test(String radar, Long fileId, Long m, Long n) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("radar",radar);
        jsonObject.put("fileId",fileId);
        jsonObject.put("time",DateUtil.dateToString(new Date()));
        JSONArray list = new JSONArray();
        int index = 0;
        for (int i = 1; i <= m;i++){
            for (int j = 1; j <= n;j++){
                index++;
                JSONObject o = new JSONObject();
                o.put("m",i);
                o.put("n",j);
                Random r = new Random();
                o.put("strain",r.nextDouble());
                o.put("x",12936111.406585108);
                o.put("y",4887073.9158);
                list.add(o);
                if (index == INSERT_NUM){
                    jsonObject.put("list",list);
                    jmsMessagingTemplate.convertAndSend(SHAPE_POINT_QUEUE,jsonObject.toJSONString());
                    list = new JSONArray();
                    index = 0;
                }
            }
        }
        if (index > 0){
            jsonObject.put("list",list);
            jmsMessagingTemplate.convertAndSend(SHAPE_POINT_QUEUE,jsonObject.toJSONString());
        }
        return null;
    }

    @Override
    public ModelMap ct() {
        List<RadarInfo> list = radarInfoService.findAll();
        for (RadarInfo radarInfo : list){
            String tableName = "shape_point_"+radarInfo.getRadarKey();
            String next = DateUtil.nextDayDate(new Date(),"yyyy-MM-dd",1);
            String nextt = DateUtil.nextDayDate(new Date(),"yyyy-MM-dd",2);
            String dateformat = next.replaceAll("-","");
            String sql = "CREATE TABLE IF NOT EXISTS "+tableName+"_"+dateformat+" (check (time > Date '"+next+"' and time < Date '"+nextt+"')) inherits ("+tableName+");";
            jdbcTemplate.execute(sql);
            sql = "CREATE INDEX IF NOT EXISTS "+tableName+"_"+dateformat+"_time_index ON "+tableName+"_"+dateformat+"(time)";
            jdbcTemplate.execute(sql);
            sql = "CREATE INDEX IF NOT EXISTS "+tableName+"_"+dateformat+"_radar_index ON "+tableName+"_"+dateformat+"(radar)";
            jdbcTemplate.execute(sql);
        }
        return null;
    }
}
