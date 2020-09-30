
package com.gis.trans.dao;

import com.gis.trans.model.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(rollbackFor = {Exception.class})
@Repository
public interface StationDao extends JpaRepository<Station, String> {

    List<Station> findByMStationName(String stationName);

}