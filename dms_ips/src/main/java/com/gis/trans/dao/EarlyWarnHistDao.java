package com.gis.trans.dao;

import com.gis.trans.model.EarlyWarnHist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(rollbackFor = Exception.class)
@Repository
public interface EarlyWarnHistDao extends JpaRepository<EarlyWarnHist,Long> {

    @Transactional
    @Modifying
    @Query("update EarlyWarnHist set status = 1 where id = ?1")
    void  updateOneStatus(Long id);

    EarlyWarnHist findById(Long id);

    List<EarlyWarnHist> findByRadarId(Long radarId);
}
