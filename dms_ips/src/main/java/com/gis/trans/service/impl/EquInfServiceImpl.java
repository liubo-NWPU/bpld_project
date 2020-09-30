package com.gis.trans.service.impl;

import com.gis.trans.constant.CRUD;
import com.gis.trans.dao.EquInfDao;
import com.gis.trans.model.EquInf;
import com.gis.trans.model.RainData;
import com.gis.trans.model.ResponseModel;
import com.gis.trans.service.EquInfService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

@Service
public class EquInfServiceImpl extends BaseServiceImpl<EquInf, Long> implements EquInfService {

    @Autowired
    private EquInfDao equInfDao;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public JpaRepository<EquInf, Long> getDao() {
        return equInfDao;
    }

    @Override
    public ResponseModel saveEquInf(EquInf equInf) {
        ResponseModel responseModel = new ResponseModel();
        try {
            equInf.setDate(new Date());
            equInfDao.save(equInf);
            responseModel.setSuccess(true);
            responseModel.setMessage(CRUD.SAVE_SUCCESS);
        } catch (Exception e) {
            responseModel.setSuccess(false);
            responseModel.setMessage(CRUD.SAVE_FAIL);
            responseModel.setException(e.getMessage());
        }
        return responseModel;
    }

    @Override
    public ResponseModel deleteEquInf(Long id) {
        ResponseModel responseModel = new ResponseModel();
        try {
            EquInf EquInf = getById(id);
            if (EquInf != null) {
                delete(EquInf);
            }
            responseModel.setSuccess(true);
            responseModel.setMessage(CRUD.DELETE_SUCCESS);
        } catch (Exception e) {
            responseModel.setSuccess(false);
            responseModel.setMessage(CRUD.DELETE_FAIL);
            responseModel.setException(e.getMessage());
        }
        return responseModel;
    }

    @Override
    public List<EquInf> findAllEquInf() {
        return queryAll();
    }

    @Override
    public ResponseModel searchById(Long id) {
        ResponseModel responseModel = new ResponseModel();
        try {
            responseModel.setData(this.getById(id));
            responseModel.setSuccess(true);
            responseModel.setMessage(CRUD.SEARCH_SUCCESS);
        }catch (Exception e){
            responseModel.setSuccess(false);
            responseModel.setMessage(CRUD.SEARCH_FAIL);
            responseModel.setException(e.getMessage());
        }
        return responseModel;
    }

    @Override
    public ResponseModel searchByType(String type) {
        ResponseModel responseModel = new ResponseModel();
        try {
            responseModel.setData(equInfDao.findByType(type));
            responseModel.setSuccess(true);
            responseModel.setMessage(CRUD.SEARCH_SUCCESS);
        }catch (Exception e){
            responseModel.setSuccess(false);
            responseModel.setMessage(CRUD.SEARCH_FAIL);
            responseModel.setException(e.getMessage());
        }
        return responseModel;
    }

    @Override
    public ResponseModel searchByTypeAndEquName(String type ,String equName) {
        ResponseModel responseModel = new ResponseModel();
        try {
            responseModel.setData(equInfDao.findByTypeAndEquName(type,equName));
            responseModel.setSuccess(true);
            responseModel.setMessage(CRUD.SEARCH_SUCCESS);
        }catch (Exception e){
            responseModel.setSuccess(false);
            responseModel.setMessage(CRUD.SEARCH_FAIL);
            responseModel.setException(e.getMessage());
        }
        return responseModel;
    }
    @Override
    public ResponseModel searchByEquIdAndType(String equId ,String type) {
        ResponseModel responseModel = new ResponseModel();
        List<EquInf> list=null ;
        try {

            String baseSql = "select r from EquInf r where 1=1 ";
            StringBuilder stringBuilder = new StringBuilder(baseSql);
            if (!StringUtils.isEmpty(equId)) {
                stringBuilder.append(String.format(" and r.equId = '%s' ",equId));
            }
            if (!StringUtils.isEmpty(type)) {
                stringBuilder.append(String.format(" and r.type = '%s' ",type));
            }
            stringBuilder.append(" order by r.date ");
            TypedQuery<EquInf> query = entityManager.createQuery(stringBuilder.toString(),EquInf.class);
            list = query.getResultList();

            responseModel.setData(list);
            responseModel.setSuccess(true);
            responseModel.setMessage(CRUD.SEARCH_SUCCESS);
        }catch (Exception e){
            responseModel.setSuccess(false);
            responseModel.setMessage(CRUD.SEARCH_FAIL);
            responseModel.setException(e.getMessage());
        }
        return responseModel;
    }

}
