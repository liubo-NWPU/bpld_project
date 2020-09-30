package com.gis.trans.service;

import com.gis.trans.model.Equipment;
import com.gis.trans.model.ResponseModel;

import java.util.List;

public interface EquipmentService extends IBaseService<Equipment, Long> {

    ResponseModel addEquipment(Equipment equipment);

    ResponseModel deleteEquipment(Long id);

    ResponseModel deleteAllEquipment(Long[] ids);

    ResponseModel updateEquipment(Equipment equipment);

    Equipment searchByName(String name);

    List<Equipment> findAllEquip();
}
