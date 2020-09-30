package com.gis.trans.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Date;

//历史预警记录
@Entity
@Table(name = "early_warn_hist")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class EarlyWarnHist {

    private Long id;
    private Double area; //预警设置面积
    private Long fileId;//预警信息来源（shp文件Id）
    private Long radarId ; //雷达Id
    private String radarName; //雷达名称
    private Date warntime; //预警时间
    private Double truearea ; //预警面积
    private Short status; //未处理0   已处理1

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

    @Column
    public Double getArea() {
        return area;
    }

    public void setArea(Double area) {
        this.area = area;
    }

    @Column
    public Long getRadarId() {
        return radarId;
    }

    public void setRadarId(Long radarId) {
        this.radarId = radarId;
    }

    @Column(name = "radar_name")
    public String getRadarName() {
        return radarName;
    }

    public void setRadarName(String radarName) {
        this.radarName = radarName;
    }

    @Column
    public Date getWarntime() {
        return warntime;
    }

    public void setWarntime(Date warntime) {
        this.warntime = warntime;
    }
    @Column
    public Double getTruearea() {
        return truearea;
    }

    public void setTruearea(Double truearea) {
        this.truearea = truearea;
    }
    @Column
    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }
}
