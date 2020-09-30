package com.gis.trans.dao;

import com.gis.trans.model.DataRadar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface DataRadarDao extends JpaRepository<DataRadar,Long> ,JpaSpecificationExecutor {

    @Query("select d from DataRadar d where d.deviceId = ?1 and d.time = (select max(dr.time) from DataRadar dr where dr.deviceId =?1 ) ")
    DataRadar findByDeviceId(String deviceId);
}
