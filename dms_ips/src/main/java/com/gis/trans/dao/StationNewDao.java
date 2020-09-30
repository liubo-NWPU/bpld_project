
package com.gis.trans.dao;

import com.gis.trans.model.StationNew;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(rollbackFor = {Exception.class})
@Repository
public interface StationNewDao extends JpaRepository<StationNew, String> {

    @Query("select m from StationNew m where m.mStationName =?1")
    List<StationNew> findByMStationName(String stationName);

}