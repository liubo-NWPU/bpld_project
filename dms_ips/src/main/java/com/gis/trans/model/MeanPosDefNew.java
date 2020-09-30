//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.gis.trans.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "meanpos_def_new")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class MeanPosDefNew implements Serializable {
    private Long id;
    private Date logTime;
    private Long eventTime;
    private Long dataPeriod;
    private Integer stationId;
    private Integer stationKey;
    private Integer baseId;
    private Integer baseKey;
    private Double dN;
    private Double dE;
    private Double dH;
    private Double covN;
    private Double covNE;
    private Double covE;
    private Double covNH;
    private Double covEH;
    private Double covH;
    private Short type;
    private Double rotationAngle;
    private Double pDOP;
    private Double numSat;
    private Double numSatGPS;
    private Short isFixed;
    private Double minDN;
    private Double minDE;
    private Double minDH;
    private Double maxDN;
    private Double maxDE;
    private Double maxDH;
    private Double min2D;
    private Double max2D;
    private Double min3D;
    private Double max3D;
    private Double minSigmaN;
    private Double minSigmaE;
    private Double minSigmaH;
    private Double minSigma2D;
    private Double minSigma3D;
    private Double maxSigmaN;
    private Double maxSigmaE;
    private Double maxSigmaH;
    private Double maxSigma2D;
    private Double maxSigma3D;
    private Integer numDataSets;
    private Long minTime;
    private Long maxTime;

    public MeanPosDefNew() {
    }

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column(
            name = "id",
            unique = true,
            nullable = false
    )
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
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

    @Column(
            name = "eventtime",
            nullable = false
    )
    public Long getEventTime() {
        return this.eventTime;
    }

    public void setEventTime(Long eventTime) {
        this.eventTime = eventTime;
    }

    @Column(
            name = "dataperiod"
    )
    public Long getDataPeriod() {
        return this.dataPeriod;
    }

    public void setDataPeriod(Long dataPeriod) {
        this.dataPeriod = dataPeriod;
    }

    @Column(
            name = "stationid"
    )
    public Integer getStationId() {
        return this.stationId;
    }

    public void setStationId(Integer stationId) {
        this.stationId = stationId;
    }

    @Column(
            name = "stationkey",
            nullable = false
    )
    public Integer getStationKey() {
        return this.stationKey;
    }

    public void setStationKey(Integer stationKey) {
        this.stationKey = stationKey;
    }

    @Column(
            name = "baseid"
    )
    public Integer getBaseId() {
        return this.baseId;
    }

    public void setBaseId(Integer baseId) {
        this.baseId = baseId;
    }

    @Column(
            name = "basekey",
            nullable = false
    )
    public Integer getBaseKey() {
        return this.baseKey;
    }

    public void setBaseKey(Integer baseKey) {
        this.baseKey = baseKey;
    }

    @Column(
            name = "dn"
    )
    public Double getdN() {
        return this.dN;
    }

    public void setdN(Double dN) {
        this.dN = dN;
    }

    @Column(
            name = "de"
    )
    public Double getdE() {
        return this.dE;
    }

    public void setdE(Double dE) {
        this.dE = dE;
    }

    @Column(
            name = "dh"
    )
    public Double getdH() {
        return this.dH;
    }

    public void setdH(Double dH) {
        this.dH = dH;
    }

    @Column(
            name = "cov_n"
    )
    public Double getCovN() {
        return this.covN;
    }

    public void setCovN(Double covN) {
        this.covN = covN;
    }

    @Column(
            name = "cov_ne"
    )
    public Double getCovNE() {
        return this.covNE;
    }

    public void setCovNE(Double covNE) {
        this.covNE = covNE;
    }

    @Column(
            name = "cov_e"
    )
    public Double getCovE() {
        return this.covE;
    }

    public void setCovE(Double covE) {
        this.covE = covE;
    }

    @Column(
            name = "cov_nh"
    )
    public Double getCovNH() {
        return this.covNH;
    }

    public void setCovNH(Double covNH) {
        this.covNH = covNH;
    }

    @Column(
            name = "cov_eh"
    )
    public Double getCovEH() {
        return this.covEH;
    }

    public void setCovEH(Double covEH) {
        this.covEH = covEH;
    }

    @Column(
            name = "cov_h"
    )
    public Double getCovH() {
        return this.covH;
    }

    public void setCovH(Double covH) {
        this.covH = covH;
    }

    @Column(
            name = "type"
    )
    public Short getType() {
        return this.type;
    }

    public void setType(Short type) {
        this.type = type;
    }

    @Column(
            name = "rotationangle"
    )
    public Double getRotationAngle() {
        return this.rotationAngle;
    }

    public void setRotationAngle(Double rotationAngle) {
        this.rotationAngle = rotationAngle;
    }

    @Column(
            name = "pdop"
    )
    public Double getpDOP() {
        return this.pDOP;
    }

    public void setpDOP(Double pDOP) {
        this.pDOP = pDOP;
    }

    @Column(
            name = "numsat"
    )
    public Double getNumSat() {
        return this.numSat;
    }

    public void setNumSat(Double numSat) {
        this.numSat = numSat;
    }

    @Column(
            name = "numsatgps"
    )
    public Double getNumSatGPS() {
        return this.numSatGPS;
    }

    public void setNumSatGPS(Double numSatGPS) {
        this.numSatGPS = numSatGPS;
    }

    @Column(
            name = "isfixed"
    )
    public Short getIsFixed() {
        return this.isFixed;
    }

    public void setIsFixed(Short isFixed) {
        this.isFixed = isFixed;
    }

    @Column(
            name = "min_dn"
    )
    public Double getMinDN() {
        return this.minDN;
    }

    public void setMinDN(Double minDN) {
        this.minDN = minDN;
    }

    @Column(
            name = "min_de"
    )
    public Double getMinDE() {
        return this.minDE;
    }

    public void setMinDE(Double minDE) {
        this.minDE = minDE;
    }

    @Column(
            name = "min_dh"
    )
    public Double getMinDH() {
        return this.minDH;
    }

    public void setMinDH(Double minDH) {
        this.minDH = minDH;
    }

    @Column(
            name = "max_dn"
    )
    public Double getMaxDN() {
        return this.maxDN;
    }

    public void setMaxDN(Double maxDN) {
        this.maxDN = maxDN;
    }

    @Column(
            name = "max_de"
    )
    public Double getMaxDE() {
        return this.maxDE;
    }

    public void setMaxDE(Double maxDE) {
        this.maxDE = maxDE;
    }

    @Column(
            name = "max_dh"
    )
    public Double getMaxDH() {
        return this.maxDH;
    }

    public void setMaxDH(Double maxDH) {
        this.maxDH = maxDH;
    }

    @Column(
            name = "min_2d"
    )
    public Double getMin2D() {
        return this.min2D;
    }

    public void setMin2D(Double min2D) {
        this.min2D = min2D;
    }

    @Column(
            name = "max_2d"
    )
    public Double getMax2D() {
        return this.max2D;
    }

    public void setMax2D(Double max2D) {
        this.max2D = max2D;
    }

    @Column(
            name = "min_3d"
    )
    public Double getMin3D() {
        return this.min3D;
    }

    public void setMin3D(Double min3D) {
        this.min3D = min3D;
    }

    @Column(
            name = "max_3d"
    )
    public Double getMax3D() {
        return this.max3D;
    }

    public void setMax3D(Double max3D) {
        this.max3D = max3D;
    }

    @Column(
            name = "min_sigman"
    )
    public Double getMinSigmaN() {
        return this.minSigmaN;
    }

    public void setMinSigmaN(Double minSigmaN) {
        this.minSigmaN = minSigmaN;
    }

    @Column(
            name = "min_sigmae"
    )
    public Double getMinSigmaE() {
        return this.minSigmaE;
    }

    public void setMinSigmaE(Double minSigmaE) {
        this.minSigmaE = minSigmaE;
    }

    @Column(
            name = "min_sigmah"
    )
    public Double getMinSigmaH() {
        return this.minSigmaH;
    }

    public void setMinSigmaH(Double minSigmaH) {
        this.minSigmaH = minSigmaH;
    }

    @Column(
            name = "min_sigma2d"
    )
    public Double getMinSigma2D() {
        return this.minSigma2D;
    }

    public void setMinSigma2D(Double minSigma2D) {
        this.minSigma2D = minSigma2D;
    }

    @Column(
            name = "min_sigma3d"
    )
    public Double getMinSigma3D() {
        return this.minSigma3D;
    }

    public void setMinSigma3D(Double minSigma3D) {
        this.minSigma3D = minSigma3D;
    }

    @Column(
            name = "max_sigman"
    )
    public Double getMaxSigmaN() {
        return this.maxSigmaN;
    }

    public void setMaxSigmaN(Double maxSigmaN) {
        this.maxSigmaN = maxSigmaN;
    }

    @Column(
            name = "max_sigmae"
    )
    public Double getMaxSigmaE() {
        return this.maxSigmaE;
    }

    public void setMaxSigmaE(Double maxSigmaE) {
        this.maxSigmaE = maxSigmaE;
    }

    @Column(
            name = "max_sigmah"
    )
    public Double getMaxSigmaH() {
        return this.maxSigmaH;
    }

    public void setMaxSigmaH(Double maxSigmaH) {
        this.maxSigmaH = maxSigmaH;
    }

    @Column(
            name = "max_sigma2d"
    )
    public Double getMaxSigma2D() {
        return this.maxSigma2D;
    }

    public void setMaxSigma2D(Double maxSigma2D) {
        this.maxSigma2D = maxSigma2D;
    }

    @Column(
            name = "max_sigma3d"
    )
    public Double getMaxSigma3D() {
        return this.maxSigma3D;
    }

    public void setMaxSigma3D(Double maxSigma3D) {
        this.maxSigma3D = maxSigma3D;
    }

    @Column(
            name = "numdatasets"
    )
    public Integer getNumDataSets() {
        return this.numDataSets;
    }

    public void setNumDataSets(Integer numDataSets) {
        this.numDataSets = numDataSets;
    }

    @Column(
            name = "mintime"
    )
    public Long getMinTime() {
        return this.minTime;
    }

    public void setMinTime(Long minTime) {
        this.minTime = minTime;
    }

    @Column(
            name = "maxtime"
    )
    public Long getMaxTime() {
        return this.maxTime;
    }

    public void setMaxTime(Long maxTime) {
        this.maxTime = maxTime;
    }

    public String toString() {
        return "MeanPosDef{, logTime=" + this.logTime + ", eventTime=" + this.eventTime + ", dataPeriod=" + this.dataPeriod + ", stationId=" + this.stationId + ", stationKey=" + this.stationKey + ", baseId=" + this.baseId + ", baseKey=" + this.baseKey + ", dN=" + this.dN + ", dE=" + this.dE + ", dH=" + this.dH + ", covN=" + this.covN + ", covNE=" + this.covNE + ", covE=" + this.covE + ", covNH=" + this.covNH + ", covEH=" + this.covEH + ", covH=" + this.covH + ", type=" + this.type + ", rotationAngle=" + this.rotationAngle + ", pDOP=" + this.pDOP + ", numSat=" + this.numSat + ", numSatGPS=" + this.numSatGPS + ", isFixed=" + this.isFixed + ", minDN=" + this.minDN + ", minDE=" + this.minDE + ", minDH=" + this.minDH + ", maxDN=" + this.maxDN + ", maxDE=" + this.maxDE + ", maxDH=" + this.maxDH + ", min2D=" + this.min2D + ", max2D=" + this.max2D + ", min3D=" + this.min3D + ", max3D=" + this.max3D + ", minSigmaN=" + this.minSigmaN + ", minSigmaE=" + this.minSigmaE + ", minSigmaH=" + this.minSigmaH + ", minSigma2D=" + this.minSigma2D + ", minSigma3D=" + this.minSigma3D + ", maxSigmaN=" + this.maxSigmaN + ", maxSigmaE=" + this.maxSigmaE + ", maxSigmaH=" + this.maxSigmaH + ", maxSigma2D=" + this.maxSigma2D + ", maxSigma3D=" + this.maxSigma3D + ", numDataSets=" + this.numDataSets + ", minTime=" + this.minTime + ", maxTime=" + this.maxTime + '}';
    }
}
