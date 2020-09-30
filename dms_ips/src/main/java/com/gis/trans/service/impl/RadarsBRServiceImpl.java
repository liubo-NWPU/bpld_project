package com.gis.trans.service.impl;

import com.gis.trans.constant.CRUD;
import com.gis.trans.dao.RadarsBRDao;
import com.gis.trans.model.RadarsBR;
import com.gis.trans.model.ResponseModel;
import com.gis.trans.service.RadarsBRService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class RadarsBRServiceImpl extends BaseServiceImpl<RadarsBR, String> implements RadarsBRService {

    @Autowired
    private RadarsBRDao radarsBRDao;

    @Override
    public JpaRepository<RadarsBR, String> getDao() {
        return radarsBRDao;
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
