package com.gis.trans.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Date;

//设备
@Entity
@Table(name = "equipment")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class Equipment {

    private Long id;//主键
    private String serialNumb; //设备序列号
    private String deviceName; //设备名称
    private String dataUrl; //数据源地址
    private Short status; //运行状态： 0-不在线  1-在线
    private Long radarid; //所属雷达
    private String radarName; //雷达名称
    private Long mineAreaid; //所属矿场id
    private String mineAreaName; //所属矿场名称
    private Double lon;
    private Double lat;
    private Double high;
    private String leaderName;
    private Date date;  //创建时间


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "serial_name")
    public String getSerialNumb() {
        return serialNumb;
    }

    public void setSerialNumb(String serialNumb) {
        this.serialNumb = serialNumb;
    }

    @Column(name = "device_name")
    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    @Column(name = "device_url")
    public String getDataUrl() {
        return dataUrl;
    }

    public void setDataUrl(String dataUrl) {
        this.dataUrl = dataUrl;
    }


    @Column(name = "status")
    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    @Column(name = "radar_id")
    public Long getRadarid() {
        return radarid;
    }

    public void setRadarid(Long radarid) {
        this.radarid = radarid;
    }

    @Column(name = "radar_name")
    public String getRadarName() {
        return radarName;
    }

    public void setRadarName(String radarName) {
        this.radarName = radarName;
    }

    @Column(name = "minearea_id")
    public Long getMineAreaid() {
        return mineAreaid;
    }

    public void setMineAreaid(Long mineAreaid) {
        this.mineAreaid = mineAreaid;
    }

    @Column(name = "minearea_name")
    public String getMineAreaName() {
        return mineAreaName;
    }

    public void setMineAreaName(String mineAreaName) {
        this.mineAreaName = mineAreaName;
    }

    @Column(name = "date")
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Column(name = "lon")
    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    @Column(name = "lat")

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    @Column(name = "high")

    public Double getHigh() {
        return high;
    }

    public void setHigh(Double high) {
        this.high = high;
    }

    @Column(name = "leader_name")
    public String getLeaderName() {
        return leaderName;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }

    @Override
    public String toString() {
        return "Equipment{" +
                "id=" + id +
                ", serialNumb='" + serialNumb + '\'' +
                ", deviceName='" + deviceName + '\'' +
                ", dataUrl='" + dataUrl + '\'' +
                ", status=" + status +
                ", radarid=" + radarid +
                ", radarName='" + radarName + '\'' +
                ", mineAreaid=" + mineAreaid +
                ", mineAreaName='" + mineAreaName + '\'' +
                ", lon=" + lon +
                ", lat=" + lat +
                ", high=" + high +
                ", leaderName='" + leaderName + '\'' +
                ", date=" + date +
                '}';
    }
}
