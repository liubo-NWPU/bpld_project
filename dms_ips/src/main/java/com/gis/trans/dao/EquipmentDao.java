package com.gis.trans.dao;

import com.gis.trans.model.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(rollbackFor = Exception.class)
@Repository
public interface EquipmentDao extends JpaRepository<Equipment,Long> {

    List<Equipment> findByDeviceName(String name);
}
