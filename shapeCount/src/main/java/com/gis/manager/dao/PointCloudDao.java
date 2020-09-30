package com.gis.manager.dao;

import com.gis.manager.model.PointCloud;
import org.springframework.data.jpa.repository.JpaRepository;

public interface  PointCloudDao extends JpaRepository<PointCloud,String> {
}
