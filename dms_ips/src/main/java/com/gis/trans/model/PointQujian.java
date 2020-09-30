package com.gis.trans.model;

import javax.persistence.*;

@Entity
@Table(name = "point_qujian", schema = "public", catalog = "bpld_shape")
public class PointQujian {
    private Long id;
    private String pointFile;
    private Long pointFileId;
    private String qujian;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "point_file")
    public String getPointFile() {
        return pointFile;
    }

    public void setPointFile(String pointFile) {
        this.pointFile = pointFile;
    }

    @Basic
    @Column(name = "point_file_id")
    public Long getPointFileId() {
        return pointFileId;
    }

    public void setPointFileId(Long pointFileId) {
        this.pointFileId = pointFileId;
    }

    @Basic
    @Column(name = "qujian")
    public String getQujian() {
        return qujian;
    }

    public void setQujian(String qujian) {
        this.qujian = qujian;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PointQujian that = (PointQujian) o;

        if (id != that.id) return false;
        if (pointFile != null ? !pointFile.equals(that.pointFile) : that.pointFile != null) return false;
        if (pointFileId != null ? !pointFileId.equals(that.pointFileId) : that.pointFileId != null) return false;
        if (qujian != null ? !qujian.equals(that.qujian) : that.qujian != null) return false;

        return true;
    }
}
