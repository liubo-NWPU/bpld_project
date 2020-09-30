package com.gis.trans.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

//矿场
@Entity
@Table(name = "mine_area")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class MineArea {

    private Long id;
    private Long mineId;//矿场id
    private String mineName;  //矿山名称
    private String companyName;  //采场名称  更新  公司名称
    private String location;//矿场所属地
    private Double lon;
    private Double lat;
    private Double high;
    private Double angle;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "mine_id", unique = true, nullable = false)
    public Long getMineId() {
        return mineId;
    }
    public void setMineId(Long mineId) {
        this.mineId = mineId;
    }
    @Column(name = "mine_name")
    public String getMineName() {
        return mineName;
    }

    public void setMineName(String mineName) {
        this.mineName = mineName;
    }
    @Column(name = "company_name")
    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @Column(name = "location")
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    @Column(name = "angle")
    public Double getAngle() {
        return angle;
    }

    public void setAngle(Double angle) {
        this.angle = angle;
    }
}
