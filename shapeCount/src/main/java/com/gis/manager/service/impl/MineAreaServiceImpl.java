package com.gis.manager.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gis.manager.dao.MineAreaDao;
import com.gis.manager.dao.PointCloudDao;
import com.gis.manager.dao.RadarInfoDao;
import com.gis.manager.model.FileIdPartition;
import com.gis.manager.model.MineArea;
import com.gis.manager.model.RadarInfo;
import com.gis.manager.model.reqentity.ReqMinearea;
import com.gis.manager.model.reqentity.ReqRadarInfo;
import com.gis.manager.service.IFileIdPartitionService;
import com.gis.manager.service.IMineAreaService;
import com.gis.manager.service.IRadarInfoService;
import com.gis.manager.utils.DateUtil;
import com.gis.manager.utils.NUllToStrUtils;
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

import java.io.File;
import java.io.IOException;
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
public class MineAreaServiceImpl extends BaseServiceImpl<MineArea, Long> implements IMineAreaService {
    @Value("${filePath}")
    private String filePah;

    @Value("${DM.service.address}")
    private String dmaddress;

    public static final String PlotParseQueue = "PlotParseFile.queue";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MineAreaDao mineAreaDao;

    @Override
    public JpaRepository<MineArea, Long> getDao() {
        return mineAreaDao;
    }

    @Override
    public ModelMap mimnareaAdd(ReqMinearea reqMinearea) {

        ModelMap mm=new ModelMap();
        mm.put("success",false);

        MineArea mineArea = reqMinearea.getMineArea();
        Long parentId = reqMinearea.getParentid();
        String mineAreaName = mineArea.getMineName();

        //2、创建数管目录
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Accept","application/json");
        String urlStr = dmaddress+"/manager/rest/filecatalog/addFolder";
        MultiValueMap<String, Object> multipartRequest = new LinkedMultiValueMap<>();
        multipartRequest.add("parentId", parentId);
        multipartRequest.add("folderName", mineAreaName);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity(multipartRequest, requestHeaders);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(urlStr, requestEntity, String.class);
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            mm.put("msg","创建雷达目录失败");
            return mm;
        }
        String data = responseEntity.getBody();
        Long fileId = JSONObject.parseObject(data).getLong("id");
        mineArea.setMineId(fileId);
        mineAreaDao.save(mineArea);
        return null;
    }

    @Override
    public ModelMap mineAreaSearch(String mineareaname) {
        String value = mineareaname.replace(" ","");
        List<MineArea> reasultList = mineAreaDao.findByMineNameLike("%"+value+"%");
        ModelMap mm=new ModelMap();
        mm.put("success",true);
        mm.put("data",reasultList);
        return mm;
    }

    @Override
    public ModelMap mineareaedit(MineArea mineArea) {
        ModelMap mm=new ModelMap();
        mm.put("success",false);
        String name = mineArea.getMineName();
        Long id = mineArea.getId();
        if(id != null) {
            mineAreaDao.save(mineArea);
        }
        else{
            mm.put("msg","更新矿区信息失败："+name);
            return mm;
        }
        mm.put("success",true);
        mm.put("data",mineArea);
        return mm;
    }
}
