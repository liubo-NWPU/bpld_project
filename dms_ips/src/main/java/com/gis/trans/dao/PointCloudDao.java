package com.gis.trans.dao;


import com.gis.trans.model.PointCloud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PointCloudDao extends JpaRepository<PointCloud,String> {

    List<PointCloud> findByRadar(String radar);

    @Query(value = "select * from point_cloud where radar = ?1 and m <> -1 and n <> -1 order by m,n",nativeQuery = true)
    List<PointCloud> findValidPointByRadar(String radar);
}
