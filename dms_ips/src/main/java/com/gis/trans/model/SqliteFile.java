package com.gis.trans.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "sqlite_file", schema = "public", catalog = "bpld_shape")
public class SqliteFile {
    private int id;
    private String radarKey;
    private String diffimageFile;
    private Date time;
    private String sqlitePath;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "radar_key")
    public String getRadarKey() {
        return radarKey;
    }

    public void setRadarKey(String radarKey) {
        this.radarKey = radarKey;
    }

    @Basic
    @Column(name = "diffimage_file")
    public String getDiffimageFile() {
        return diffimageFile;
    }

    public void setDiffimageFile(String diffimageFile) {
        this.diffimageFile = diffimageFile;
    }

    @Basic
    @Column(name = "time")
    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    @Basic
    @Column(name = "sqlite_path")
    public String getSqlitePath() {
        return sqlitePath;
    }

    public void setSqlitePath(String sqlitePath) {
        this.sqlitePath = sqlitePath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SqliteFile that = (SqliteFile) o;

        if (id != that.id) return false;
        if (radarKey != null ? !radarKey.equals(that.radarKey) : that.radarKey != null) return false;
        if (diffimageFile != null ? !diffimageFile.equals(that.diffimageFile) : that.diffimageFile != null)
            return false;
        if (time != null ? !time.equals(that.time) : that.time != null) return false;
        if (sqlitePath != null ? !sqlitePath.equals(that.sqlitePath) : that.sqlitePath != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (radarKey != null ? radarKey.hashCode() : 0);
        result = 31 * result + (diffimageFile != null ? diffimageFile.hashCode() : 0);
        result = 31 * result + (time != null ? time.hashCode() : 0);
        result = 31 * result + (sqlitePath != null ? sqlitePath.hashCode() : 0);
        return result;
    }
}
