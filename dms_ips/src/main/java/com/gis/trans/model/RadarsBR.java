package com.gis.trans.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

//雷达 点名表
@Entity
@Table(name = "radar_br")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class RadarsBR {

    private String id;
    private String name;
    private String description;
    private Integer installLocation;
    private Date addTime;

    @Id
    @Column(name = "id", unique = true, nullable = false)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    @Column(name = "installlocation")
    public Integer getInstallLocation() {
        return installLocation;
    }

    public void setInstallLocation(Integer installLocation) {
        this.installLocation = installLocation;
    }
    @Column(name = "addtime")
    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }
}
