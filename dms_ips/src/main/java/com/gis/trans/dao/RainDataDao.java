package com.gis.trans.dao;

import com.gis.trans.model.RainData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RainDataDao extends JpaRepository<RainData,Long>,JpaSpecificationExecutor {

    @Query("select r from RainData r where r.rDAddr = ?1 and r.rDDate = (select max(rd.rDDate) from RainData rd where rd.rDAddr =?1 ) ")
    RainData findByRDAddr(Integer rDAddr);
}
