package com.gis.trans.dao;

import com.gis.trans.model.RadarsBR;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RadarsBRDao extends JpaRepository<RadarsBR,String>,JpaSpecificationExecutor {

    @Query("select r from RadarsBR r")
    List<RadarsBR> searchDate();


}
