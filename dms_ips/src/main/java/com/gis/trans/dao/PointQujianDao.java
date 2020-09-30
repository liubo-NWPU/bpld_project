package com.gis.trans.dao;

import com.gis.trans.model.PointCloud;
import com.gis.trans.model.PointQujian;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointQujianDao extends JpaRepository<PointQujian,Long> {
    PointQujian findByPointFile(String pointFile);

    PointQujian findByPointFileId(Long fileId);
}
