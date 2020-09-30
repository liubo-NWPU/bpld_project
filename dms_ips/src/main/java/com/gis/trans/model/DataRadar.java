package com.gis.trans.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

//雷达数据采集表
@Entity
@Table(name = "data_radar")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class DataRadar {

    private Long id;
    private String deviceId;
    private Integer realDistance;
    private Integer averageDistance;
    private Integer changeValue;
    private Integer installLocation;
    private Float voltage;
    private Integer state;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date time;



    @Id
    @Column(name = "id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "deviceid")
    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
    @Column(name = "realdistance")
    public Integer getRealDistance() {
        return realDistance;
    }

    public void setRealDistance(Integer realDistance) {
        this.realDistance = realDistance;
    }
    @Column(name = "averagedistance")
    public Integer getAverageDistance() {
        return averageDistance;
    }

    public void setAverageDistance(Integer averageDistance) {
        this.averageDistance = averageDistance;
    }
    @Column(name = "changevalue")
    public Integer getChangeValue() {
        return changeValue;
    }

    public void setChangeValue(Integer changeValue) {
        this.changeValue = changeValue;
    }
    @Column(name = "installlocation")
    public Integer getInstallLocation() {
        return installLocation;
    }

    public void setInstallLocation(Integer installLocation) {
        this.installLocation = installLocation;
    }
    @Column(name = "voltage")
    public Float getVoltage() {
        return voltage;
    }

    public void setVoltage(Float voltage) {
        this.voltage = voltage;
    }
    @Column(name = "state")
    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
    @Column(name = "time")
    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
