package com.gis.trans.dao;

import com.gis.trans.model.EarlyWarn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(rollbackFor = Exception.class)
@Repository
public interface EarlyWarnDao extends JpaRepository<EarlyWarn,Long> {

    EarlyWarn findByRadarId(Long radarId);
}
