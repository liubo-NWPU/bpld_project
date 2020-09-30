package com.gis.manager.dao;

import com.gis.manager.model.MineArea;
import com.gis.manager.model.RadarInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MineAreaDao extends JpaRepository<MineArea,Long> {
    MineArea findByMineName(String mineName);
    List<MineArea> findByMineNameLike(String mineName);

}
