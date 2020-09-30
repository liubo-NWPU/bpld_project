package com.gis.trans.dao;

import com.alibaba.fastjson.JSONObject;
import com.gis.trans.model.ShapePoint;
import com.gis.trans.model.ShapePointSimple;
import com.vividsolutions.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

//@Transactional(rollbackFor = Exception.class)
@Repository
public interface PlotInfDao extends JpaRepository<ShapePoint,String> {

    List<ShapePoint> findByFileId(Long fileid);


    List<ShapePoint> findByFileIdAndRadar(Long fileid,String radar);


    @Transactional
    @Modifying
    @Query("update ShapePoint set fileId = ?1,geo = ?2 where id = ?3 and radar = ?4")
    void updateShapePoint(Long fileid, Point point,String id,String radar);

//    @Query("select s.m as m,s.n as n,s.rAxis as rAxis,s.aAxis as aAxis,s.geo as geo,sum(s.strain) as strain from ShapePoint s where s.radar  = ?1 and s.strain > -1000.0 and (s.time>=?2 and s.time<=?3)  group by s.m,s.n,s.geo,s.rAxis,s.aAxis")
//    @Query(nativeQuery = true, value="SELECT W.*, C.sumChange  strain FROM ( SELECT SUM (A.change)  sumChange, A.n cn, A.m cm FROM shape_point A WHERE A.radar =:radar and A.change > - 1000 GROUP BY A . M, A .n ) C LEFT JOIN ( SELECT DISTINCT * FROM shape_point WHERE change > - 1000 ) W ON C .cm = W.m AND C .cn = W.n")
//    List<JSONObject> getShapePoint(@Param("radar")  String radar); //,Date start_time, Date end_time

    @Query(nativeQuery = true, value = "SELECT\n" +
            "        A.M AS \"m\",\n" +
            "        A.N AS \"n\",\n" +
            "        SUM(A.change) AS \"strain\",\n" +
            "        A.a_axis AS \"aAxis\",\n" +
            "        A.r_axis AS \"rAxis\"\n" +
            "        FROM\n" +
            "        shape_point A\n" +
            "        WHERE A.radar = ?1 and A.time > ?2 and A.time < ?3 GROUP BY A.M, A.n,A.a_axis,A.r_axis")
    List<ShapePointSimple> getShapePoint(String radar, Date startTime, Date endTime);

    public static void main(String[] args) {
        String s = "POINT (123.1050647977917 41.149390672866254)";
        String replace = s.replace("POINT (", "").replace(")", "");
        String[] split = replace.split(" ");

        System.out.println(Double.parseDouble(split[0]));
        System.out.println(Double.parseDouble(split[1]));
    }
}
