package com.gis.trans.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gis.trans.dao.PointQujianDao;
import com.gis.trans.model.PointQujian;
import com.gis.trans.model.Qujian;
import com.gis.trans.model.ResponseModel;
import com.gis.trans.service.PointFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PointFileServiceImpl extends BaseServiceImpl<PointQujian, Long> implements PointFileService {

    @Autowired
    private PointQujianDao pointQujianDao;
    @Override
    public JpaRepository<PointQujian, Long> getDao() {
        return pointQujianDao;
    }

    @Override
    public ResponseModel pointFile(Long fileId) {
        ResponseModel responseModel = new ResponseModel();
        responseModel.setSuccess(false);
        PointQujian pointQujian = pointQujianDao.findByPointFileId(fileId);
        if (pointQujian == null){
            responseModel.setData(null);
            responseModel.setMessage("该文件没有色带信息");
            return responseModel;
        }
        else{
            JSONObject data = new JSONObject();
            String content = pointQujian.getQujian();
            List<Qujian> qujian = JSONArray.parseArray(content,Qujian.class);
            data.put("qujian",qujian);
            data.put("fid",pointQujian.getPointFileId());
            responseModel.setData(data);
        }
        responseModel.setSuccess(true);
        return responseModel;
    }

    @Override
    public ResponseModel pointFileByName(String fileName) {
        ResponseModel responseModel = new ResponseModel();
        responseModel.setSuccess(false);
        PointQujian pointQujian = pointQujianDao.findByPointFile(fileName);
        if (pointQujian == null){
            responseModel.setData(null);
            responseModel.setMessage("该文件没有色带信息");
            return responseModel;
        }
        else{
            JSONObject data = new JSONObject();
            String content = pointQujian.getQujian();
            List<Qujian> qujian = JSONArray.parseArray(content,Qujian.class);
            data.put("qujian",qujian);
            data.put("fid",pointQujian.getPointFileId());
            responseModel.setData(data);
        }
        responseModel.setSuccess(true);
        return responseModel;
    }

    @Override
    public ResponseModel colorChange(Long fileId, String colorJson) {
        ResponseModel responseModel = new ResponseModel();
        responseModel.setSuccess(false);

        PointQujian pointQujian = pointQujianDao.findByPointFileId(fileId);
        if (pointQujian == null){
            responseModel.setMessage("色带文件不存在");
            return responseModel;
        }
        pointQujian.setQujian(colorJson);
        pointQujianDao.save(pointQujian);

        responseModel.setSuccess(true);
        responseModel.setMessage("修改色带成功");
        return responseModel;
    }

    @Override
    public void save(PointQujian pointQujian) {
        pointQujianDao.save(pointQujian);
    }
}
