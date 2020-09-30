package com.gis.manager.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gis.manager.dao.PointCloudDao;
import com.gis.manager.dao.RadarInfoDao;
import com.gis.manager.model.FileIdPartition;
import com.gis.manager.model.PointCloud;
import com.gis.manager.model.RadarInfo;
import com.gis.manager.model.reqentity.ReqRadarInfo;
import com.gis.manager.service.IFileIdPartitionService;
import com.gis.manager.service.IRadarInfoService;
import com.gis.manager.utils.DateUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author:wangmeng
 * @Date:创建于2018/3/21 17:03
 * @Description:
 */
@Service
public class RadarInfoServiceImpl extends BaseServiceImpl<RadarInfo, Long> implements IRadarInfoService {
    @Value("${filePath}")
    private String filePah;

    @Value("${DM.service.address}")
    private String dmaddress;

    public static final String PlotParseQueue = "PlotParseFile.queue";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RadarInfoDao radarInfoDao;

    @Autowired
    private PointCloudDao pointCloudDao;

    @Autowired
    private IFileIdPartitionService iFileIdPartitionService;

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public JpaRepository<RadarInfo, Long> getDao() {
        return radarInfoDao;
    }

    @Override
    public RadarInfo findOne(String radarName) {
        return radarInfoDao.findByRadarName(radarName);
    }

    @Override
    public List<RadarInfo> findAll() {
        return radarInfoDao.findAll();
    }

    @Override
    public ModelMap radarFile(MultipartFile data, String radar) {
        ModelMap mm=new ModelMap();
        mm.put("success",false);

        String radarName = "";
        String fileName = data.getOriginalFilename();
        int i = fileName.lastIndexOf(".");
        String substring = fileName.substring(0, i);
        if (radar == null || radar.isEmpty())
            radarName = "null";
        else
            radarName = radar;
        String dataPath = filePah + File.separator + radarName + File.separator + substring + File.separator + fileName;
        File targetFile = new File(dataPath);
        try {
            FileUtils.copyInputStreamToFile(data.getInputStream(), targetFile);
            FileIdPartition fileIdPartition = new FileIdPartition();
            fileIdPartition.setTime(new Date());
            fileIdPartition.setFilePath(dataPath);
            fileIdPartition.setRadar(radar);
            iFileIdPartitionService.save(fileIdPartition);
            mm.put("success",true);

            //发送diffimage解析的消息
            Map<String, Object> param = new HashMap<>();
            JSONArray files = new JSONArray();
            files.add(dataPath);
            param.put("filePaths", files);
            param.put("radarName", radarName);
            jmsMessagingTemplate.convertAndSend(PlotParseQueue, JSONObject.toJSONString(param));
        } catch (IOException e) {
            e.printStackTrace();
            mm.put("success",false);
        }
        return mm;
    }

    @Override
    public ModelMap radarStatus(String radar, Double scaninterval, Double scanlen, Double observemin, Double observemax, Double diskfree, Long radarstatus, Long trackstatus) {
        ModelMap mm=new ModelMap();
        mm.put("success",false);
        RadarInfo radarInfo = radarInfoDao.findByRadarName(radar);
        if (radarInfo == null){
            mm.put("msg","雷达名称不存在");
            return mm;
        }
        radarInfo.setScanInterval(scaninterval);
        radarInfo.setScanLen(scanlen);
        radarInfo.setObserveMin(observemin);
        radarInfo.setObserveMax(observemax);
        radarInfo.setDiskFree(diskfree);
        radarInfo.setRadarStatus(radarstatus);
        radarInfo.setTrackStatus(trackstatus);
        radarInfoDao.save(radarInfo);
        mm.put("success",true);
        return mm;
    }

    @Override
    public ModelMap radarTime(String radar, String day) {
        ModelMap mm=new ModelMap();
        mm.put("success",false);
        RadarInfo radarInfo = radarInfoDao.findByRadarName(radar);
        if (radarInfo == null){
            mm.put("msg","雷达名称不存在");
            return mm;
        }
        String radarKey = radarInfo.getRadarKey();
        String nextDay = DateUtil.nextDayString(day,"yyyyMMdd",1);
        String start = DateUtil.stringToString(day,"yyyyMMdd","yyyy-MM-dd HH:mm:ss");
        String end = DateUtil.stringToString(nextDay,"yyyyMMdd","yyyy-MM-dd HH:mm:ss");
        String parentTable = "shape_point_"+radarKey;
        String tableName = "shape_point_"+radarKey+"_"+day;
        String sql = "CREATE TABLE IF NOT EXISTS shape_point_"+radarKey+" (check (radar = '"+radarKey+"')) inherits (shape_point);";
        System.out.println(sql);
        jdbcTemplate.execute(sql);
        sql = "CREATE TABLE IF NOT EXISTS "+tableName+" (check (time > Date '"+start+"' and time < Date '"+end+"')) inherits ("+parentTable+");";
        System.out.println(sql);
        jdbcTemplate.execute(sql);
//            sql = "CREATE INDEX IF NOT EXISTS "+tableName+"_"+dateformat+"_file_id_index ON "+tableName+"_"+dateformat+"(file_id)";
//            jdbcTemplate.execute(sql);
        sql = "CREATE INDEX IF NOT EXISTS "+tableName+"_time_index ON "+tableName+"(time)";
        System.out.println(sql);
        jdbcTemplate.execute(sql);
        sql = "CREATE INDEX IF NOT EXISTS "+tableName+"_radar_index ON "+tableName+"(radar)";
        System.out.println(sql);
        jdbcTemplate.execute(sql);
        mm.put("msg","表"+tableName+"创建成功");
        return mm;
    }

    @Override
    public ModelMap drift(String radar, double driftX, double driftY, double driftZ) {
        ModelMap mm=new ModelMap();
        mm.put("success",false);
        RadarInfo radarInfo = radarInfoDao.findByRadarName(radar);
        if (radarInfo == null){
            mm.put("msg","雷达名称不存在");
            return mm;
        }
        radarInfo.setDriftX(driftX);
        radarInfo.setDriftY(driftY);
        radarInfo.setDriftZ(driftZ);
        radarInfoDao.save(radarInfo);
        mm.put("msg","雷达'"+radar+"'点云偏移保存成功");
        mm.put("success",true);
        return mm;
    }

    @Override
    public ModelMap radarAdd(ReqRadarInfo reqRadarInfo) {
        ModelMap mm=new ModelMap();
        mm.put("success",false);
        //1、雷达key计算
        List<RadarInfo> list = radarInfoDao.findAll();
        int num = list.size();
        String radarKey = "r" + (num + 1);

        RadarInfo r = radarInfoDao.findByRadarKey(radarKey);
        while(r!=null){
            num++;
            radarKey = "r"+num;
            r = radarInfoDao.findByRadarKey(radarKey);
        }
        RadarInfo radarInfo = reqRadarInfo.getRadarInfo();
        radarInfo.setRadarKey(radarKey);
        radarInfo.setRadarStatus(1L);
        Long parentId = reqRadarInfo.getParentid();
        String radarName = radarInfo.getRadarName();


        //2、创建数管目录
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Accept","application/json");
        String urlStr = dmaddress+"/manager/rest/filecatalog/addFolder";
        MultiValueMap<String, Object> multipartRequest = new LinkedMultiValueMap<>();
        multipartRequest.add("parentId", parentId);
        multipartRequest.add("folderName", radarName);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity(multipartRequest, requestHeaders);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(urlStr, requestEntity, String.class);
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            mm.put("msg","创建雷达目录失败");
            return mm;
        }
        String data = responseEntity.getBody();
        Long fileId = JSONObject.parseObject(data).getLong("id");
        radarInfo.setRadarId(fileId);
        radarInfoDao.save(radarInfo);
        mm.put("success",true);
        return mm;
    }

    @Override
    public ModelMap radarsearch(String radarname) {
        String value = radarname.replace(" ","");
        List<RadarInfo> reasultList = radarInfoDao.findByRadarNameLike("%"+value+"%");
        ModelMap mm=new ModelMap();
        mm.put("success",true);
        mm.put("data",reasultList);
        return mm;
    }

    @Override
    public ModelMap radaredit(RadarInfo radarInfo) {
        Long id = radarInfo.getId();
        RadarInfo radar = radarInfoDao.findById(id).orElse(null);
        String radarKey = radar.getRadarKey();
        radarInfo.setRadarKey(radarKey);
        radarInfoDao.save(radarInfo);

        ModelMap mm=new ModelMap();
        mm.put("success",true);
        mm.put("data",radarInfo);
        return mm;
    }
}
