package com.gis.trans.service.impl;

import com.gis.trans.constant.CRUD;
import com.gis.trans.dao.RainInfoDao;
import com.gis.trans.model.RainInfo;
import com.gis.trans.model.ResponseModel;
import com.gis.trans.service.RainInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
public class RainInfoServiceImpl extends BaseServiceImpl<RainInfo, Integer> implements RainInfoService {

    @Autowired
    private RainInfoDao rainInfoDao;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public JpaRepository<RainInfo, Integer> getDao() {
        return rainInfoDao;
    }

    @Override
    public ResponseModel searchAll() {
        ResponseModel responseModel = new ResponseModel();
        try {
            responseModel.setData(queryAll());
            responseModel.setSuccess(true);
            responseModel.setMessage(CRUD.SEARCH_SUCCESS);
        }catch (Exception e){
            responseModel.setSuccess(false);
            responseModel.setMessage(CRUD.SEARCH_FAIL);
            e.printStackTrace();
        }
        return responseModel;
    }

}
