package com.gis.trans.service.impl;

import com.gis.trans.constant.CRUD;
import com.gis.trans.dao.TesaaaDao;
import com.gis.trans.model.ResponseModel;
import com.gis.trans.model.Tesaaa;
import com.gis.trans.service.TesaaaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TesaaaServiceImpl implements TesaaaService {

    @Autowired
    private TesaaaDao tesaaaDao;

    @Override
    public ResponseModel insertaaa(Tesaaa tesaaa) {
        ResponseModel responseModel = new ResponseModel();
        try {
            tesaaaDao.save(tesaaa);
            responseModel.setSuccess(true);
            responseModel.setMessage(CRUD.SEARCH_SUCCESS);
        } catch (Exception e) {
            responseModel.setSuccess(false);
            responseModel.setMessage(CRUD.SEARCH_FAIL);
            e.printStackTrace();
        }
        return responseModel;
    }

}
