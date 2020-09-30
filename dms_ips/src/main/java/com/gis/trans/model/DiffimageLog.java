package com.gis.trans.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Date;

//解析diffimage入库日志信息
@Entity
@Table(name = "diffimage_log")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class DiffimageLog {

    private Long id;
    private Long flag;//是否异常 1正常 0不正常
    private String radarName; //雷达名称
    private Date beforeTime ; //上批次时间
    private Date thisTime ; //这批次时间

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    @Column
    public Long getFlag() {
        return flag;
    }
    public void setFlag(Long flag) {
        this.flag = flag;
    }

    @Column(name = "radar_name")
    public String getRadarName() {
        return radarName;
    }
    public void setRadarName(String radarName) {
        this.radarName = radarName;
    }

    @Column(name = "before_time")
    public Date getBeforeTime() {
        return beforeTime;
    }
    public void setBeforeTime(Date beforeTime) {
        this.beforeTime = beforeTime;
    }

    @Column(name = "this_time")
    public Date getThisTime() {
        return thisTime;
    }

    public void setThisTime(Date thisTime) {
        this.thisTime = thisTime;
    }
}
