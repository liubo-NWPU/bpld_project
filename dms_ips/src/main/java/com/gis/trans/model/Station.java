package com.gis.trans.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/*
   GPS数据点位信息表
 */
@Entity
@Table(name = "station_list")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class Station {

    private String config;
    private String mName;
    private String mGroup;
    private String mAntennaName;
    private String mAntennaManufacturer;
    private String mReceiverName;
    private String mReceiverManufacturer;
    private Integer mStationId;
    private String mStationName;
    private String mStationCode;
    private String mMyOwnAgency;
    private String mMyOwnObserver;
    private Float mInstrumentHeight;
    private Float mOriginalPositionMX;
    private Float mOriginalPositionMY;
    private Float mOriginalPositionMZ;
    private String mOriginalPositionMTectonicPlate;
    private Date mOriginalPositionMPositionReferenceTime;
    private Float mOriginalPositionMXVelocity;
    private Float mOriginalPositionMYVelocity;
    private Float mOriginalPositionMZVelocity;
    private Date logTime;

    @Id
    @Column(name = "config", unique = true, nullable = false)
    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }
    @Column(name = "m_name")
    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }
    @Column(name = "m_group")
    public String getmGroup() {
        return mGroup;
    }

    public void setmGroup(String mGroup) {
        this.mGroup = mGroup;
    }
    @Column(name = "m_antennaname")
    public String getmAntennaName() {
        return mAntennaName;
    }

    public void setmAntennaName(String mAntennaName) {
        this.mAntennaName = mAntennaName;
    }
    @Column(name = "m_antennamanufacturer")
    public String getmAntennaManufacturer() {
        return mAntennaManufacturer;
    }

    public void setmAntennaManufacturer(String mAntennaManufacturer) {
        this.mAntennaManufacturer = mAntennaManufacturer;
    }
    @Column(name = "m_receivername")
    public String getmReceiverName() {
        return mReceiverName;
    }

    public void setmReceiverName(String mReceiverName) {
        this.mReceiverName = mReceiverName;
    }
    @Column(name = "m_receivermanufacturer")
    public String getmReceiverManufacturer() {
        return mReceiverManufacturer;
    }

    public void setmReceiverManufacturer(String mReceiverManufacturer) {
        this.mReceiverManufacturer = mReceiverManufacturer;
    }
    @Column(name = "m_stationid")
    public Integer getmStationId() {
        return mStationId;
    }

    public void setmStationId(Integer mStationId) {
        this.mStationId = mStationId;
    }
    @Column(name = "m_stationname")
    public String getmStationName() {
        return mStationName;
    }

    public void setmStationName(String mStationName) {
        this.mStationName = mStationName;
    }
    @Column(name = "m_stationcode",length = 20)
    public String getmStationCode() {
        return mStationCode;
    }

    public void setmStationCode(String mStationCode) {
        this.mStationCode = mStationCode;
    }
    @Column(name = "m_myownagency")
    public String getmMyOwnAgency() {
        return mMyOwnAgency;
    }

    public void setmMyOwnAgency(String mMyOwnAgency) {
        this.mMyOwnAgency = mMyOwnAgency;
    }
    @Column(name = "m_myownobserver")
    public String getmMyOwnObserver() {
        return mMyOwnObserver;
    }

    public void setmMyOwnObserver(String mMyOwnObserver) {
        this.mMyOwnObserver = mMyOwnObserver;
    }
    @Column(name = "m_instrumentheight")
    public Float getmInstrumentHeight() {
        return mInstrumentHeight;
    }

    public void setmInstrumentHeight(Float mInstrumentHeight) {
        this.mInstrumentHeight = mInstrumentHeight;
    }
    @Column(name = "m_originalposition_m_x")
    public Float getmOriginalPositionMX() {
        return mOriginalPositionMX;
    }

    public void setmOriginalPositionMX(Float mOriginalPositionMX) {
        this.mOriginalPositionMX = mOriginalPositionMX;
    }
    @Column(name = "m_originalposition_m_y")
    public Float getmOriginalPositionMY() {
        return mOriginalPositionMY;
    }

    public void setmOriginalPositionMY(Float mOriginalPositionMY) {
        this.mOriginalPositionMY = mOriginalPositionMY;
    }
    @Column(name = "m_originalposition_m_z")
    public Float getmOriginalPositionMZ() {
        return mOriginalPositionMZ;
    }

    public void setmOriginalPositionMZ(Float mOriginalPositionMZ) {
        this.mOriginalPositionMZ = mOriginalPositionMZ;
    }
    @Column(name = "m_originalposition_m_tectonicplate",length = 50)
    public String getmOriginalPositionMTectonicPlate() {
        return mOriginalPositionMTectonicPlate;
    }

    public void setmOriginalPositionMTectonicPlate(String mOriginalPositionMTectonicPlate) {
        this.mOriginalPositionMTectonicPlate = mOriginalPositionMTectonicPlate;
    }
    @Column(name = "m_originalposition_m_positionreferencetime")
    public Date getmOriginalPositionMPositionReferenceTime() {
        return mOriginalPositionMPositionReferenceTime;
    }

    public void setmOriginalPositionMPositionReferenceTime(Date mOriginalPositionMPositionReferenceTime) {
        this.mOriginalPositionMPositionReferenceTime = mOriginalPositionMPositionReferenceTime;
    }
    @Column(name = "m_originalposition_m_xvelocity")
    public Float getmOriginalPositionMXVelocity() {
        return mOriginalPositionMXVelocity;
    }

    public void setmOriginalPositionMXVelocity(Float mOriginalPositionMXVelocity) {
        this.mOriginalPositionMXVelocity = mOriginalPositionMXVelocity;
    }
    @Column(name = "m_originalposition_m_yvelocity")
    public Float getmOriginalPositionMYVelocity() {
        return mOriginalPositionMYVelocity;
    }

    public void setmOriginalPositionMYVelocity(Float mOriginalPositionMYVelocity) {
        this.mOriginalPositionMYVelocity = mOriginalPositionMYVelocity;
    }
    @Column(name = "m_originalposition_m_zvelocity")
    public Float getmOriginalPositionMZVelocity() {
        return mOriginalPositionMZVelocity;
    }

    public void setmOriginalPositionMZVelocity(Float mOriginalPositionMZVelocity) {
        this.mOriginalPositionMZVelocity = mOriginalPositionMZVelocity;
    }
    @Column(name = "logtime")
    public Date getLogTime() {
        return logTime;
    }

    public void setLogTime(Date logTime) {
        this.logTime = logTime;
    }

    @Override
    public String toString() {
        return "Station{" +
                "config='" + config + '\'' +
                ", mName='" + mName + '\'' +
                ", mGroup='" + mGroup + '\'' +
                ", mAntennaName='" + mAntennaName + '\'' +
                ", mAntennaManufacturer='" + mAntennaManufacturer + '\'' +
                ", mReceiveName='" + mReceiverName + '\'' +
                ", mReceiveManufacturer='" + mReceiverManufacturer + '\'' +
                ", mStationId=" + mStationId +
                ", mStationName='" + mStationName + '\'' +
                ", mStationCode='" + mStationCode + '\'' +
                ", mMyOwnAgency='" + mMyOwnAgency + '\'' +
                ", mMyOwnObserver='" + mMyOwnObserver + '\'' +
                ", mInstrumentHeight=" + mInstrumentHeight +
                ", mOriginalPositionMX=" + mOriginalPositionMX +
                ", mOriginalPositionMY=" + mOriginalPositionMY +
                ", mOriginalPositionMZ=" + mOriginalPositionMZ +
                ", mOriginalPositionMTectonicPlate='" + mOriginalPositionMTectonicPlate + '\'' +
                ", mOriginalPositionMPositionReferenceTime=" + mOriginalPositionMPositionReferenceTime +
                ", mOriginalPositionMXVelocity=" + mOriginalPositionMXVelocity +
                ", mOriginalPositionMYVelocity=" + mOriginalPositionMYVelocity +
                ", mOriginalPositionMZVelocity=" + mOriginalPositionMZVelocity +
                ", logTime=" + logTime +
                '}';
    }
}
