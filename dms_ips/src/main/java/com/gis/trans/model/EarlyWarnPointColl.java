package com.gis.trans.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

//预警信息点
@Entity
@Table(name = "early_warn_point_coll")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class EarlyWarnPointColl {

    private Long id;
    private Long fileId;  //点来源文件
    private Long earlyWarnHistId; //预警记录Id
    private Double x; //x
    private Double y;  //y
    private Double strain;  //型变量

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "file_id")
    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    @Column(name = "early_warn_hist_id")
    public Long getEarlyWarnHistId() {
        return earlyWarnHistId;
    }

    public void setEarlyWarnHistId(Long earlyWarnHistId) {
        this.earlyWarnHistId = earlyWarnHistId;
    }

    @Column
    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    @Column
    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    @Column
    public Double getStrain() {
        return strain;
    }

    public void setStrain(Double strain) {
        this.strain = strain;
    }
}
