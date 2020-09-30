package com.gis.trans.service.impl;

import com.gis.trans.constant.CRUD;
import com.gis.trans.dao.EquipmentDao;
import com.gis.trans.model.Equipment;
import com.gis.trans.model.ResponseModel;
import com.gis.trans.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquipmentServiceImpl extends BaseServiceImpl<Equipment, Long> implements EquipmentService {

    @Autowired
    private EquipmentDao equipmentDao;

    @Override
    public JpaRepository<Equipment, Long> getDao() {
        return equipmentDao;
    }

    @Override
    public ResponseModel addEquipment(Equipment equipment) {
        ResponseModel responseModel = new ResponseModel();
        try {
            insert(equipment);
            responseModel.setSuccess(true);
            responseModel.setMessage(CRUD.INSERT_SUCCESS);
        } catch (Exception e) {
            responseModel.setSuccess(false);
            responseModel.setMessage(CRUD.INSERT_FAIL);
            responseModel.setException(e.getMessage());
        }
        return responseModel;
    }


    @Override
    public ResponseModel deleteAllEquipment(Long[] ids) {
        ResponseModel responseModel = new ResponseModel();
        try {
            for (Long id : ids) {
                deleteEquipment(id);
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
    public ResponseModel deleteEquipment(Long id) {
        ResponseModel responseModel = new ResponseModel();
        try {
            Equipment equipment = getById(id);
            if (equipment != null) {
                delete(equipment);
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
    public ResponseModel updateEquipment(Equipment equipment) {
        ResponseModel responseModel = new ResponseModel();
        try {
            update(equipment);
            responseModel.setSuccess(true);
            responseModel.setMessage(CRUD.UPDATE_SUCCESS);
        } catch (Exception e) {
            responseModel.setSuccess(false);
            responseModel.setMessage(CRUD.UPDATE_FAIL);
            responseModel.setException(e.getMessage());
        }
        return responseModel;
    }

    @Override
    public Equipment searchByName(String name) {
        List<Equipment> list = equipmentDao.findByDeviceName(name);
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<Equipment> findAllEquip() {
        return queryAll();
    }
}
