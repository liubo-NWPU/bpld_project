package com.gis.manager.dao;

import com.gis.manager.model.HistoryShapePoint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

/**
 * (HistoryShapePoint)表数据库访问层
 *
 * @author liuyan
 * @since 2020-03-20 14:28:22
 */
public interface HistoryShapePointDao extends JpaRepository<HistoryShapePoint,Long> {

    int deleteByTime(Date time);

}