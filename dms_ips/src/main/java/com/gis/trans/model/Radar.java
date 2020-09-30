package com.gis.trans.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Date;

//雷达
@Entity
@Table(name = "radar")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class Radar {
    private long id;
    private Long radarId; //雷达id
    private String radarName;  //雷达名称
    private Double lon; //经度
    private Double lat;  //纬度
    private Long mineAreaid; //所属矿场id
    private String  mineName; //所属矿场名称
    private Double sampInterval;  //采样间隔
    private Double trackLength;   //轨道长度
    private Double observNear;    //观测近距
    private Double observFar;    //观测远距
    private String status;    //雷达状态
    private Date createTime;    //建站时间
    private String storage;    //存储空间
    private String radarKey;  //雷达标识
    private Double leftlon;  //左轨x
    private Double leftlat;  //左轨y
    private Double rightlon;  //右轨x
    private Double rightlat;  //右轨y
    private Double lx;
    private Double ly;
    private Double lz;
    private Double rx;
    private Double ry;
    private Double rz;

    private double driftX;
    private double driftY;
    private double driftZ;
    private String wkt;

    //雷达的状态信息
    private Double scanInterval; //时间间隔
    private Double scanLen; //扫描长度
    private Double observeMin; //观测近距
    private Double observeMax; //观测远距
    private Double diskFree; //磁盘剩余
    private Long radarStatus; //雷达状态
    private Long trackStatus; //轨道状态

    private String  companyName; //所属公司名称

    @Id
    @Column(name = "id", unique = true, nullable = false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(name = "radar_id")
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


    @Column(name = "samp_interval")
    public Double getSampInterval() {
        return sampInterval;
    }

    public void setSampInterval(Double sampInterval) {
        this.sampInterval = sampInterval;
    }
    @Column(name = "track_length")
    public Double getTrackLength() {
        return trackLength;
    }

    public void setTrackLength(Double trackLength) {
        this.trackLength = trackLength;
    }
    @Column(name = "observ_near")
    public Double getObservNear() {
        return observNear;
    }

    public void setObservNear(Double observNear) {
        this.observNear = observNear;
    }

    @Column(name = "observ_far")
    public Double getObservFar() {
        return observFar;
    }

    public void setObservFar(Double observFar) {
        this.observFar = observFar;
    }

    @Column(name = "status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    @Column(name = "create_time")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(name = "storage")
    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    @Column(name = "radar_key")
    public String getRadarKey() {
        return radarKey;
    }

    public void setRadarKey(String radarKey) {
        this.radarKey = radarKey;
    }
    @Column(name = "mine_area_id")
    public Long getMineAreaid() {
        return mineAreaid;
    }

    public void setMineAreaid(Long mineAreaid) {
        this.mineAreaid = mineAreaid;
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

    @Column(name = "left_lon")
    public Double getLeftlon() {
        return leftlon;
    }

    public void setLeftlon(Double leftlon) {
        this.leftlon = leftlon;
    }

    @Column(name = "left_lat")
    public Double getLeftlat() {
        return leftlat;
    }

    public void setLeftlat(Double leftlat) {
        this.leftlat = leftlat;
    }

    @Column(name = "right_lon")
    public Double getRightlon() {
        return rightlon;
    }

    public void setRightlon(Double rightlon) {
        this.rightlon = rightlon;
    }

    @Column(name = "right_lat")
    public Double getRightlat() {
        return rightlat;
    }

    public void setRightlat(Double rightlat) {
        this.rightlat = rightlat;
    }

    @Column(name = "lx")
    public Double getLx() {
        return lx;
    }

    public void setLx(Double lx) {
        this.lx = lx;
    }

    @Column(name = "ly")
    public Double getLy() {
        return ly;
    }

    public void setLy(Double ly) {
        this.ly = ly;
    }

    @Column(name = "lz")
    public Double getLz() {
        return lz;
    }

    public void setLz(Double lz) {
        this.lz = lz;
    }

    @Column(name = "rx")
    public Double getRx() {
        return rx;
    }

    public void setRx(Double rx) {
        this.rx = rx;
    }

    @Column(name = "ry")
    public Double getRy() {
        return ry;
    }

    public void setRy(Double ry) {
        this.ry = ry;
    }

    @Column(name = "rz")
    public Double getRz() {
        return rz;
    }

    public void setRz(Double rz) {
        this.rz = rz;
    }

    @Basic
    @Column(name = "driftx", nullable = true)
    public double getDriftX() {
        return driftX;
    }

    public void setDriftX(double driftX) {
        this.driftX = driftX;
    }

    @Basic
    @Column(name = "drifty", nullable = true)
    public double getDriftY() {
        return driftY;
    }

    public void setDriftY(double driftY) {
        this.driftY = driftY;
    }

    @Basic
    @Column(name = "driftz", nullable = true)
    public double getDriftZ() {
        return driftZ;
    }

    public void setDriftZ(double driftZ) {
        this.driftZ = driftZ;
    }

    @Column(name = "wkt", nullable = true)
    public String getWkt() {
        return wkt;
    }

    public void setWkt(String wkt) {
        this.wkt = wkt;
    }

    @Basic
    @Column(name = "scan_interval", nullable = true, precision = 0)
    public Double getScanInterval() {
        return scanInterval;
    }

    public void setScanInterval(Double scanInterval) {
        this.scanInterval = scanInterval;
    }

    @Basic
    @Column(name = "scan_len", nullable = true, precision = 0)
    public Double getScanLen() {
        return scanLen;
    }

    public void setScanLen(Double scanLen) {
        this.scanLen = scanLen;
    }

    @Basic
    @Column(name = "observe_min", nullable = true, precision = 0)
    public Double getObserveMin() {
        return observeMin;
    }

    public void setObserveMin(Double observeMin) {
        this.observeMin = observeMin;
    }

    @Basic
    @Column(name = "observe_max", nullable = true, precision = 0)
    public Double getObserveMax() {
        return observeMax;
    }

    public void setObserveMax(Double observeMax) {
        this.observeMax = observeMax;
    }

    @Basic
    @Column(name = "disk_free", nullable = true, precision = 0)
    public Double getDiskFree() {
        return diskFree;
    }

    public void setDiskFree(Double diskFree) {
        this.diskFree = diskFree;
    }

    @Basic
    @Column(name = "radar_status", nullable = true)
    public Long getRadarStatus() {
        return radarStatus;
    }

    public void setRadarStatus(Long radarStatus) {
        this.radarStatus = radarStatus;
    }

    @Basic
    @Column(name = "track_status", nullable = true)
    public Long getTrackStatus() {
        return trackStatus;
    }

    public void setTrackStatus(Long trackStatus) {
        this.trackStatus = trackStatus;
    }
}
