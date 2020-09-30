package com.gis.trans.dao;

import com.gis.trans.model.EquInf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(rollbackFor = Exception.class)
@Repository
public interface EquInfDao extends JpaRepository<EquInf,Long> {

    List<EquInf> findByType(String type);

    List<EquInf> findByTypeAndEquName(String type , String equName);

    List<EquInf> findByEquIdAndType(String equId ,String type);
}
