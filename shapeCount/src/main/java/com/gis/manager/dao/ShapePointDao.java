package com.gis.manager.dao;

import com.gis.manager.model.ShapePoint;
import com.gis.manager.model.extra.MaxMin;
import com.gis.manager.model.extra.RangeCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface ShapePointDao extends JpaRepository<ShapePoint,String> {
    @Query(value = "select new com.gis.manager.model.extra.MaxMin(max(change),min(change)) from ShapePoint where radar = ?1 and time = ?2 and file_id = ?3 and change <> -1000 ")
    MaxMin findMaxMinChange(String radar, Date time, Long fileId);

    @Query(value = "select ST_AsText(geo) from shape_point where time = ?1 and file_id = ?2 order by geo <-> ST_PointFromText(?3,3857) limit 1 ",nativeQuery = true)
    String findClosedPoint(Date time,Long fileId,String point);

    @Query(value = "select * from shape_point where radar = ?1 and time between ?2 and ?3 and n = 1 and m between 10 and 30 order by time ",nativeQuery = true)
    List<ShapePoint> findStatitcsSpeed(String radar,Date startTime,Date endTime);

    @Query(value = "select new com.gis.manager.model.extra.RangeCount(count(t),range) from ShapePoint t where radar = ?1 and time between ?2 and ?3 group by range ")
    List<RangeCount> findStatitcsRange(String radar, Date startTime, Date endTime);

    @Query(value = "select * from shape_point where file_id = ?1 and m = 1",nativeQuery = true)
    List<ShapePoint> findByFileId(Long fileId);
}
