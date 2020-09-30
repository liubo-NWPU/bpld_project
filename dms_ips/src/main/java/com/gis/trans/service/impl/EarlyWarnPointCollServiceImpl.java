package com.gis.trans.service.impl;

import com.gis.trans.constant.CRUD;
import com.gis.trans.dao.EarlyWarnPointCollDao;
import com.gis.trans.model.EarlyWarnPointColl;
import com.gis.trans.model.ResponseModel;
import com.gis.trans.service.EarlyWarnPointCollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EarlyWarnPointCollServiceImpl extends BaseServiceImpl<EarlyWarnPointColl, Long> implements EarlyWarnPointCollService {

    @Autowired
    private EarlyWarnPointCollDao earlyWarnPointCollDao;

    @Override
    public JpaRepository<EarlyWarnPointColl, Long> getDao() {
        return earlyWarnPointCollDao;
    }

    @Override
    public ResponseModel saveInf(EarlyWarnPointColl earlyWarnPointColl) {
        ResponseModel responseModel = new ResponseModel();
       try {
           earlyWarnPointCollDao.save(earlyWarnPointColl);
           responseModel.setSuccess(true);
           responseModel.setMessage(CRUD.SAVE_SUCCESS);
       }catch (Exception e){
           e.printStackTrace();
           responseModel.setSuccess(false);
           responseModel.setMessage(CRUD.SAVE_FAIL);
       }
       return  responseModel;
    }

    @Override
    public ResponseModel deleteOne(Long id) {
        ResponseModel responseModel = new ResponseModel();
        try {
            earlyWarnPointCollDao.delete(id);
            responseModel.setSuccess(true);
            responseModel.setMessage(CRUD.DELETE_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            responseModel.setSuccess(false);
            responseModel.setMessage(CRUD.DELETE_FAIL);
        }
        return  responseModel;
    }

    @Override
    public List<EarlyWarnPointColl> selectByfileId(Long fileId) {
        ResponseModel responseModel = new ResponseModel();
       List<EarlyWarnPointColl> earlyWarnList = new ArrayList<>();
        try {
            earlyWarnList = earlyWarnPointCollDao.findByFileId(fileId);
        }catch (Exception e){
            e.printStackTrace();
        }
        return  earlyWarnList;
    }
}
