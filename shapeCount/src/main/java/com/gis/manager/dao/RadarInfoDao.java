package com.gis.manager.dao;

import com.gis.manager.model.RadarInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RadarInfoDao extends JpaRepository<RadarInfo,Long> {
    RadarInfo findByRadarName(String radarName);
    RadarInfo findByRadarKey(String radarKey);
    List<RadarInfo> findByRadarNameLike(String radarName);

}
