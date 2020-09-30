package com.gis.trans.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.gis.trans.config.WebSocketEnPoint;
import com.gis.trans.constant.CRUD;
import com.gis.trans.dao.EarlyWarnHistDao;
import com.gis.trans.model.*;
import com.gis.trans.service.EarlyWarnHistService;
import com.gis.trans.service.EarlyWarnPointCollService;
import com.gis.trans.service.EarlyWarnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class EarlyWarnHistServiceImpl extends BaseServiceImpl<EarlyWarnHist, Long> implements EarlyWarnHistService {

    @Autowired
    private EarlyWarnHistDao earlyWarnHistDao;

    private static final double value = 0.9;

    @Override
    public JpaRepository<EarlyWarnHist, Long> getDao() {
        return earlyWarnHistDao;
    }

    @Override
    public void addEarlyWarnHist(EarlyWarnHist earlyWarnHist) {
        insert(earlyWarnHist);
    }

    @Resource
    private EarlyWarnService earlyWarnService;

    @Resource
    private EarlyWarnPointCollService earlyWarnPointCollService;


    @Override
    public ResponseModel updateOneStatus(Long id) {
        ResponseModel responseModel = new ResponseModel();
        try {
            EarlyWarnHist coll = earlyWarnHistDao.findById(id);
            if (coll == null) {
                responseModel.setSuccess(false);
                responseModel.setMessage("该记录不存在");
                return responseModel;
            }
            earlyWarnHistDao.updateOneStatus(id);
            responseModel.setSuccess(true);
            responseModel.setMessage(CRUD.UPDATE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            responseModel.setSuccess(false);
            responseModel.setMessage(CRUD.UPDATE_FAIL);
        }
        return responseModel;
    }


    @Override
    public ResponseModel deleteOne(Long id) {
        ResponseModel responseModel = new ResponseModel();
        try {
            earlyWarnHistDao.delete(id);
            responseModel.setSuccess(true);
            responseModel.setMessage(CRUD.DELETE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            responseModel.setSuccess(false);
            responseModel.setMessage(CRUD.DELETE_FAIL);
        }
        return responseModel;
    }

    @Override
    public void addEarlyInf(List<ShapePoint> plotInfs, Long fileId, Long radarId) {
        ResponseModel responseModel = new ResponseModel();
        int count = 0;
        for (ShapePoint plotInf : plotInfs) {
            if (plotInf.getStrain() > 100) {
                count++;
            }
        }
        try {
            EarlyWarn earlyWarn = earlyWarnService.selectByRadarId(radarId);
            if (earlyWarn != null && count * value > earlyWarn.getArea()) {
                EarlyWarnHist earlyWarnHist = new EarlyWarnHist();
                earlyWarnHist.setArea(earlyWarn.getArea());
                earlyWarnHist.setWarntime(new Date());
                this.addEarlyWarnHist(earlyWarnHist);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("fileId", fileId);
                WebSocketEnPoint.sendInf(jsonObject.toJSONString());
                for (ShapePoint plotInf : plotInfs) {
                    if (plotInf.getStrain() > 100) {
                        EarlyWarnPointColl earlyWarnPointColl = new EarlyWarnPointColl();
                        earlyWarnPointColl.setFileId(fileId);
                        earlyWarnPointColl.setEarlyWarnHistId(earlyWarnHist.getId());
                        earlyWarnPointColl.setX(plotInf.getX());
                        earlyWarnPointColl.setY(plotInf.getY());
                        earlyWarnPointColl.setStrain(plotInf.getStrain());
                        earlyWarnPointCollService.saveInf(earlyWarnPointColl);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<EarlyWarnHist> findByRadarId(Long radarId) {
        List<EarlyWarnHist> list  = new ArrayList<>();
        try {
           list =  earlyWarnHistDao.findByRadarId(radarId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
