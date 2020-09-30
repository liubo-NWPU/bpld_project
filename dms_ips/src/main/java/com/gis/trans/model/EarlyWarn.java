package com.gis.trans.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Date;

//当前预警设置信息
@Entity
@Table(name = "early_warn")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class EarlyWarn {

    private Long id;
    private Long mineId;//矿场id
    private Long radarId ; //雷达Id
    private String radarName; //雷达名称
    private Double area; //预警面积
    private Date updatetime ; //预警设置时间


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
    public Long getMineId() {
        return mineId;
    }

    public void setMineId(Long mineId) {
        this.mineId = mineId;
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
    public Double getArea() {
        return area;
    }

    public void setArea(Double area) {
        this.area = area;
    }
    @Column
    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }
}
