package com.gis.trans.dao;

import com.gis.trans.model.SqliteFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;

public interface SqliteFileDao  extends JpaRepository<SqliteFile,String> {
    int deleteByRadarKeyAndTime(String radarKey,Date time);

    @Query(value = "select * from sqlite_file where radar_key = ?1 and time < ?2 order by time DESC limit 1",nativeQuery = true)
    SqliteFile findLastFile(String radarKey, Date time);
}
