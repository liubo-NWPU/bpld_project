//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.gis.trans.dao;

import com.gis.trans.model.MeanPosDef;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeanPosDefDao extends JpaRepository<MeanPosDef, Long>, JpaSpecificationExecutor {
    @Query("select m from MeanPosDef m where m.stationId = ?1 and m.eventTime = (select max(mp.eventTime) from MeanPosDef mp where mp.stationId =?1 ) and m.baseId = (select max(mpb.baseId) from MeanPosDef mpb where mpb.stationId =?1 )")
    List<MeanPosDef> findByStationId(Integer var1);

    @Query("select m from MeanPosDef m where m.stationId =?1 and m.eventTime >?2 and m.eventTime <?3 order by m.eventTime")
    List<MeanPosDef> findByStationIdAndTime(Integer var1, long var2, long var4);
}
