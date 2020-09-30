package com.gis.trans.dao;

import com.gis.trans.model.DiffimageLog;
import com.gis.trans.model.EarlyWarn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(rollbackFor = Exception.class)
@Repository
public interface DiffimageLogDao extends JpaRepository<DiffimageLog,Long> {
   // DiffimageLog findByRadarId(Long radarId);
}
