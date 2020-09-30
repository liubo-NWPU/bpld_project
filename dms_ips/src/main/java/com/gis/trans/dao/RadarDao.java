package com.gis.trans.dao;

import com.gis.trans.model.Radar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(rollbackFor = Exception.class)
@Repository
public interface RadarDao  extends JpaRepository<Radar,Long> {

    Radar findByRadarName(String radarName);

    List<Radar> findByRadarNameLike(String radarName);

    Radar findByRadarId(Long radarId);

    Radar findByRadarKey(String radarKey);


}
