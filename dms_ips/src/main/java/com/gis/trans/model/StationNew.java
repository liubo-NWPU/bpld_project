//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.gis.trans.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "station_list_new")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class StationNew {
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
    @Column(
            name = "config",
            unique = true,
            nullable = false
    )
    public String getConfig() {
        return this.config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    @Column(
            name = "m_name"
    )
    public String getmName() {
        return this.mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    @Column(
            name = "m_group"
    )
    public String getmGroup() {
        return this.mGroup;
    }

    public void setmGroup(String mGroup) {
        this.mGroup = mGroup;
    }

    @Column(
            name = "m_antennaname"
    )
    public String getmAntennaName() {
        return this.mAntennaName;
    }

    public void setmAntennaName(String mAntennaName) {
        this.mAntennaName = mAntennaName;
    }

    @Column(
            name = "m_antennamanufacturer"
    )
    public String getmAntennaManufacturer() {
        return this.mAntennaManufacturer;
    }

    public void setmAntennaManufacturer(String mAntennaManufacturer) {
        this.mAntennaManufacturer = mAntennaManufacturer;
    }

    @Column(
            name = "m_receivername"
    )
    public String getmReceiverName() {
        return this.mReceiverName;
    }

    public void setmReceiverName(String mReceiverName) {
        this.mReceiverName = mReceiverName;
    }

    @Column(
            name = "m_receivermanufacturer"
    )
    public String getmReceiverManufacturer() {
        return this.mReceiverManufacturer;
    }

    public void setmReceiverManufacturer(String mReceiverManufacturer) {
        this.mReceiverManufacturer = mReceiverManufacturer;
    }

    @Column(
            name = "m_stationid"
    )
    public Integer getmStationId() {
        return this.mStationId;
    }

    public void setmStationId(Integer mStationId) {
        this.mStationId = mStationId;
    }

    @Column(
            name = "m_stationname"
    )
    public String getmStationName() {
        return this.mStationName;
    }

    public void setmStationName(String mStationName) {
        this.mStationName = mStationName;
    }

    @Column(
            name = "m_stationcode",
            length = 20
    )
    public String getmStationCode() {
        return this.mStationCode;
    }

    public void setmStationCode(String mStationCode) {
        this.mStationCode = mStationCode;
    }

    @Column(
            name = "m_myownagency"
    )
    public String getmMyOwnAgency() {
        return this.mMyOwnAgency;
    }

    public void setmMyOwnAgency(String mMyOwnAgency) {
        this.mMyOwnAgency = mMyOwnAgency;
    }

    @Column(
            name = "m_myownobserver"
    )
    public String getmMyOwnObserver() {
        return this.mMyOwnObserver;
    }

    public void setmMyOwnObserver(String mMyOwnObserver) {
        this.mMyOwnObserver = mMyOwnObserver;
    }

    @Column(
            name = "m_instrumentheight"
    )
    public Float getmInstrumentHeight() {
        return this.mInstrumentHeight;
    }

    public void setmInstrumentHeight(Float mInstrumentHeight) {
        this.mInstrumentHeight = mInstrumentHeight;
    }

    @Column(
            name = "m_originalposition_m_x"
    )
    public Float getmOriginalPositionMX() {
        return this.mOriginalPositionMX;
    }

    public void setmOriginalPositionMX(Float mOriginalPositionMX) {
        this.mOriginalPositionMX = mOriginalPositionMX;
    }

    @Column(
            name = "m_originalposition_m_y"
    )
    public Float getmOriginalPositionMY() {
        return this.mOriginalPositionMY;
    }

    public void setmOriginalPositionMY(Float mOriginalPositionMY) {
        this.mOriginalPositionMY = mOriginalPositionMY;
    }

    @Column(
            name = "m_originalposition_m_z"
    )
    public Float getmOriginalPositionMZ() {
        return this.mOriginalPositionMZ;
    }

    public void setmOriginalPositionMZ(Float mOriginalPositionMZ) {
        this.mOriginalPositionMZ = mOriginalPositionMZ;
    }

    @Column(
            name = "m_originalposition_m_tectonicplate",
            length = 50
    )
    public String getmOriginalPositionMTectonicPlate() {
        return this.mOriginalPositionMTectonicPlate;
    }

    public void setmOriginalPositionMTectonicPlate(String mOriginalPositionMTectonicPlate) {
        this.mOriginalPositionMTectonicPlate = mOriginalPositionMTectonicPlate;
    }

    @Column(
            name = "m_originalposition_m_positionreferencetime"
    )
    public Date getmOriginalPositionMPositionReferenceTime() {
        return this.mOriginalPositionMPositionReferenceTime;
    }

    public void setmOriginalPositionMPositionReferenceTime(Date mOriginalPositionMPositionReferenceTime) {
        this.mOriginalPositionMPositionReferenceTime = mOriginalPositionMPositionReferenceTime;
    }

    @Column(
            name = "m_originalposition_m_xvelocity"
    )
    public Float getmOriginalPositionMXVelocity() {
        return this.mOriginalPositionMXVelocity;
    }

    public void setmOriginalPositionMXVelocity(Float mOriginalPositionMXVelocity) {
        this.mOriginalPositionMXVelocity = mOriginalPositionMXVelocity;
    }

    @Column(
            name = "m_originalposition_m_yvelocity"
    )
    public Float getmOriginalPositionMYVelocity() {
        return this.mOriginalPositionMYVelocity;
    }

    public void setmOriginalPositionMYVelocity(Float mOriginalPositionMYVelocity) {
        this.mOriginalPositionMYVelocity = mOriginalPositionMYVelocity;
    }

    @Column(
            name = "m_originalposition_m_zvelocity"
    )
    public Float getmOriginalPositionMZVelocity() {
        return this.mOriginalPositionMZVelocity;
    }

    public void setmOriginalPositionMZVelocity(Float mOriginalPositionMZVelocity) {
        this.mOriginalPositionMZVelocity = mOriginalPositionMZVelocity;
    }

    @Column(
            name = "logtime"
    )
    public Date getLogTime() {
        return this.logTime;
    }

    public void setLogTime(Date logTime) {
        this.logTime = logTime;
    }

    public String toString() {
        return "Station{config='" + this.config + '\'' + ", mName='" + this.mName + '\'' + ", mGroup='" + this.mGroup + '\'' + ", mAntennaName='" + this.mAntennaName + '\'' + ", mAntennaManufacturer='" + this.mAntennaManufacturer + '\'' + ", mReceiveName='" + this.mReceiverName + '\'' + ", mReceiveManufacturer='" + this.mReceiverManufacturer + '\'' + ", mStationId=" + this.mStationId + ", mStationName='" + this.mStationName + '\'' + ", mStationCode='" + this.mStationCode + '\'' + ", mMyOwnAgency='" + this.mMyOwnAgency + '\'' + ", mMyOwnObserver='" + this.mMyOwnObserver + '\'' + ", mInstrumentHeight=" + this.mInstrumentHeight + ", mOriginalPositionMX=" + this.mOriginalPositionMX + ", mOriginalPositionMY=" + this.mOriginalPositionMY + ", mOriginalPositionMZ=" + this.mOriginalPositionMZ + ", mOriginalPositionMTectonicPlate='" + this.mOriginalPositionMTectonicPlate + '\'' + ", mOriginalPositionMPositionReferenceTime=" + this.mOriginalPositionMPositionReferenceTime + ", mOriginalPositionMXVelocity=" + this.mOriginalPositionMXVelocity + ", mOriginalPositionMYVelocity=" + this.mOriginalPositionMYVelocity + ", mOriginalPositionMZVelocity=" + this.mOriginalPositionMZVelocity + ", logTime=" + this.logTime + '}';
    }
}
