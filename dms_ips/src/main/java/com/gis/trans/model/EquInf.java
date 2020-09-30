package com.gis.trans.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Date;

//同步设备
@Entity
@Table(name = "equ_inf")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class EquInf {

    private Long id;//主键
    private String equId; //设备id
    private String equName; //设备名称
    private String type; //设备类型 1 GNSS 2 雨量计 3 库水位雷达
    private Double lon; //经度
    private Double lat; //纬度
    private Date date;  //更新时间


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "equ_id")
    public String getEquId() {
        return equId;
    }

    public void setEquId(String equId) {
        this.equId = equId;
    }
    @Column(name = "equ_name")
    public String getEquName() {
        return equName;
    }

    public void setEquName(String equName) {
        this.equName = equName;
    }
    @Column(name = "type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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


    @Column(name = "date")
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "EquInf{" +
                "id=" + id +
                ", equId='" + equId + '\'' +
                ", equName='" + equName + '\'' +
                ", type='" + type + '\'' +
                ", lon=" + lon +
                ", lat=" + lat +
                ", date=" + date +
                '}';
    }
}
