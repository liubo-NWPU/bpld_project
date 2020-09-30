package com.gis.manager.model;

import javax.persistence.*;

@Entity
@Table(name = "mine_area", schema = "public", catalog = "bpld_shape")
public class MineArea {
    private Long id;
    private long mineId;
    private Double angle;
    private String companyName;
    private Double high;
    private Double lat;
    private String location;
    private Double lon;
    private String mineName;

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
    @Column(name = "mine_id")
    public long getMineId() {
        return mineId;
    }

    public void setMineId(long mineId) {
        this.mineId = mineId;
    }

    @Basic
    @Column(name = "angle")
    public Double getAngle() {
        return angle;
    }

    public void setAngle(Double angle) {
        this.angle = angle;
    }

    @Basic
    @Column(name = "company_name")
    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @Basic
    @Column(name = "high")
    public Double getHigh() {
        return high;
    }

    public void setHigh(Double high) {
        this.high = high;
    }

    @Basic
    @Column(name = "lat")
    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    @Basic
    @Column(name = "location")
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Basic
    @Column(name = "lon")
    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    @Basic
    @Column(name = "mine_name")
    public String getMineName() {
        return mineName;
    }

    public void setMineName(String mineName) {
        this.mineName = mineName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MineArea mineArea = (MineArea) o;

        if (mineId != mineArea.mineId) return false;
        if (angle != null ? !angle.equals(mineArea.angle) : mineArea.angle != null) return false;
        if (companyName != null ? !companyName.equals(mineArea.companyName) : mineArea.companyName != null)
            return false;
        if (high != null ? !high.equals(mineArea.high) : mineArea.high != null) return false;
        if (lat != null ? !lat.equals(mineArea.lat) : mineArea.lat != null) return false;
        if (location != null ? !location.equals(mineArea.location) : mineArea.location != null) return false;
        if (lon != null ? !lon.equals(mineArea.lon) : mineArea.lon != null) return false;
        if (mineName != null ? !mineName.equals(mineArea.mineName) : mineArea.mineName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (mineId ^ (mineId >>> 32));
        result = 31 * result + (angle != null ? angle.hashCode() : 0);
        result = 31 * result + (companyName != null ? companyName.hashCode() : 0);
        result = 31 * result + (high != null ? high.hashCode() : 0);
        result = 31 * result + (lat != null ? lat.hashCode() : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        result = 31 * result + (lon != null ? lon.hashCode() : 0);
        result = 31 * result + (mineName != null ? mineName.hashCode() : 0);
        return result;
    }
}
