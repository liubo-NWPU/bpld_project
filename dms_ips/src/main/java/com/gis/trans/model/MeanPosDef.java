package com.gis.trans.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/*
陇南紫金尾矿库 GPS数据点位信息表
 */
@Entity
@Table(name = "meanpos_def")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class MeanPosDef implements Serializable {

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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "logtime")
    public Date getLogTime() {
        return logTime;
    }

    public void setLogTime(Date logTime) {
        this.logTime = logTime;
    }

    @Column(name = "eventtime",nullable = false)
    public Long getEventTime() {
        return eventTime;
    }

    public void setEventTime(Long eventTime) {
        this.eventTime = eventTime;
    }
    @Column(name = "dataperiod")
    public Long getDataPeriod() {
        return dataPeriod;
    }

    public void setDataPeriod(Long dataPeriod) {
        this.dataPeriod = dataPeriod;
    }
    @Column(name = "stationid")
    public Integer getStationId() {
        return stationId;
    }

    public void setStationId(Integer stationId) {
        this.stationId = stationId;
    }


    @Column(name = "stationkey",nullable = false)
    public Integer getStationKey() {
        return stationKey;
    }

    public void setStationKey(Integer stationKey) {
        this.stationKey = stationKey;
    }
    @Column(name = "baseid")
    public Integer getBaseId() {
        return baseId;
    }

    public void setBaseId(Integer baseId) {
        this.baseId = baseId;
    }

    @Column(name = "basekey",nullable = false)
    public Integer getBaseKey() {
        return baseKey;
    }

    public void setBaseKey(Integer baseKey) {
        this.baseKey = baseKey;
    }
    @Column(name = "dn")
    public Double getdN() {
        return dN;
    }

    public void setdN(Double dN) {
        this.dN = dN;
    }
    @Column(name = "de")
    public Double getdE() {
        return dE;
    }

    public void setdE(Double dE) {
        this.dE = dE;
    }
    @Column(name = "dh")
    public Double getdH() {
        return dH;
    }

    public void setdH(Double dH) {
        this.dH = dH;
    }
    @Column(name = "cov_n")
    public Double getCovN() {
        return covN;
    }

    public void setCovN(Double covN) {
        this.covN = covN;
    }
    @Column(name = "cov_ne")
    public Double getCovNE() {
        return covNE;
    }

    public void setCovNE(Double covNE) {
        this.covNE = covNE;
    }
    @Column(name = "cov_e")
    public Double getCovE() {
        return covE;
    }

    public void setCovE(Double covE) {
        this.covE = covE;
    }
    @Column(name = "cov_nh")
    public Double getCovNH() {
        return covNH;
    }

    public void setCovNH(Double covNH) {
        this.covNH = covNH;
    }
    @Column(name = "cov_eh")
    public Double getCovEH() {
        return covEH;
    }

    public void setCovEH(Double covEH) {
        this.covEH = covEH;
    }
    @Column(name = "cov_h")
    public Double getCovH() {
        return covH;
    }

    public void setCovH(Double covH) {
        this.covH = covH;
    }
    @Column(name = "type")
    public Short getType() {
        return type;
    }

    public void setType(Short type) {
        this.type = type;
    }
    @Column(name = "rotationangle")
    public Double getRotationAngle() {
        return rotationAngle;
    }

    public void setRotationAngle(Double rotationAngle) {
        this.rotationAngle = rotationAngle;
    }
    @Column(name = "pdop")
    public Double getpDOP() {
        return pDOP;
    }

    public void setpDOP(Double pDOP) {
        this.pDOP = pDOP;
    }
    @Column(name = "numsat")
    public Double getNumSat() {
        return numSat;
    }

    public void setNumSat(Double numSat) {
        this.numSat = numSat;
    }
    @Column(name = "numsatgps")
    public Double getNumSatGPS() {
        return numSatGPS;
    }

    public void setNumSatGPS(Double numSatGPS) {
        this.numSatGPS = numSatGPS;
    }
    @Column(name = "isfixed")
    public Short getIsFixed() {
        return isFixed;
    }

    public void setIsFixed(Short isFixed) {
        this.isFixed = isFixed;
    }
    @Column(name = "min_dn")
    public Double getMinDN() {
        return minDN;
    }

    public void setMinDN(Double minDN) {
        this.minDN = minDN;
    }
    @Column(name = "min_de")
    public Double getMinDE() {
        return minDE;
    }

    public void setMinDE(Double minDE) {
        this.minDE = minDE;
    }
    @Column(name = "min_dh")
    public Double getMinDH() {
        return minDH;
    }

    public void setMinDH(Double minDH) {
        this.minDH = minDH;
    }
    @Column(name = "max_dn")
    public Double getMaxDN() {
        return maxDN;
    }

    public void setMaxDN(Double maxDN) {
        this.maxDN = maxDN;
    }
    @Column(name = "max_de")
    public Double getMaxDE() {
        return maxDE;
    }

    public void setMaxDE(Double maxDE) {
        this.maxDE = maxDE;
    }
    @Column(name = "max_dh")
    public Double getMaxDH() {
        return maxDH;
    }

    public void setMaxDH(Double maxDH) {
        this.maxDH = maxDH;
    }
    @Column(name = "min_2d")
    public Double getMin2D() {
        return min2D;
    }

    public void setMin2D(Double min2D) {
        this.min2D = min2D;
    }
    @Column(name = "max_2d")
    public Double getMax2D() {
        return max2D;
    }

    public void setMax2D(Double max2D) {
        this.max2D = max2D;
    }
    @Column(name = "min_3d")
    public Double getMin3D() {
        return min3D;
    }

    public void setMin3D(Double min3D) {
        this.min3D = min3D;
    }
    @Column(name = "max_3d")
    public Double getMax3D() {
        return max3D;
    }

    public void setMax3D(Double max3D) {
        this.max3D = max3D;
    }
    @Column(name = "min_sigman")
    public Double getMinSigmaN() {
        return minSigmaN;
    }

    public void setMinSigmaN(Double minSigmaN) {
        this.minSigmaN = minSigmaN;
    }
    @Column(name = "min_sigmae")
    public Double getMinSigmaE() {
        return minSigmaE;
    }

    public void setMinSigmaE(Double minSigmaE) {
        this.minSigmaE = minSigmaE;
    }
    @Column(name = "min_sigmah")
    public Double getMinSigmaH() {
        return minSigmaH;
    }

    public void setMinSigmaH(Double minSigmaH) {
        this.minSigmaH = minSigmaH;
    }
    @Column(name = "min_sigma2d")
    public Double getMinSigma2D() {
        return minSigma2D;
    }

    public void setMinSigma2D(Double minSigma2D) {
        this.minSigma2D = minSigma2D;
    }
    @Column(name = "min_sigma3d")
    public Double getMinSigma3D() {
        return minSigma3D;
    }

    public void setMinSigma3D(Double minSigma3D) {
        this.minSigma3D = minSigma3D;
    }
    @Column(name = "max_sigman")
    public Double getMaxSigmaN() {
        return maxSigmaN;
    }

    public void setMaxSigmaN(Double maxSigmaN) {
        this.maxSigmaN = maxSigmaN;
    }
    @Column(name = "max_sigmae")
    public Double getMaxSigmaE() {
        return maxSigmaE;
    }

    public void setMaxSigmaE(Double maxSigmaE) {
        this.maxSigmaE = maxSigmaE;
    }
    @Column(name = "max_sigmah")
    public Double getMaxSigmaH() {
        return maxSigmaH;
    }

    public void setMaxSigmaH(Double maxSigmaH) {
        this.maxSigmaH = maxSigmaH;
    }
    @Column(name = "max_sigma2d")
    public Double getMaxSigma2D() {
        return maxSigma2D;
    }

    public void setMaxSigma2D(Double maxSigma2D) {
        this.maxSigma2D = maxSigma2D;
    }
    @Column(name = "max_sigma3d")
    public Double getMaxSigma3D() {
        return maxSigma3D;
    }

    public void setMaxSigma3D(Double maxSigma3D) {
        this.maxSigma3D = maxSigma3D;
    }
    @Column(name = "numdatasets")
    public Integer getNumDataSets() {
        return numDataSets;
    }

    public void setNumDataSets(Integer numDataSets) {
        this.numDataSets = numDataSets;
    }
    @Column(name = "mintime")
    public Long getMinTime() {
        return minTime;
    }

    public void setMinTime(Long minTime) {
        this.minTime = minTime;
    }
    @Column(name = "maxtime")
    public Long getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(Long maxTime) {
        this.maxTime = maxTime;
    }

    @Override
    public String toString() {
        return "MeanPosDef{" +
                ", logTime=" + logTime +
                ", eventTime=" + eventTime +
                ", dataPeriod=" + dataPeriod +
                ", stationId=" + stationId +
                ", stationKey=" + stationKey +
                ", baseId=" + baseId +
                ", baseKey=" + baseKey +
                ", dN=" + dN +
                ", dE=" + dE +
                ", dH=" + dH +
                ", covN=" + covN +
                ", covNE=" + covNE +
                ", covE=" + covE +
                ", covNH=" + covNH +
                ", covEH=" + covEH +
                ", covH=" + covH +
                ", type=" + type +
                ", rotationAngle=" + rotationAngle +
                ", pDOP=" + pDOP +
                ", numSat=" + numSat +
                ", numSatGPS=" + numSatGPS +
                ", isFixed=" + isFixed +
                ", minDN=" + minDN +
                ", minDE=" + minDE +
                ", minDH=" + minDH +
                ", maxDN=" + maxDN +
                ", maxDE=" + maxDE +
                ", maxDH=" + maxDH +
                ", min2D=" + min2D +
                ", max2D=" + max2D +
                ", min3D=" + min3D +
                ", max3D=" + max3D +
                ", minSigmaN=" + minSigmaN +
                ", minSigmaE=" + minSigmaE +
                ", minSigmaH=" + minSigmaH +
                ", minSigma2D=" + minSigma2D +
                ", minSigma3D=" + minSigma3D +
                ", maxSigmaN=" + maxSigmaN +
                ", maxSigmaE=" + maxSigmaE +
                ", maxSigmaH=" + maxSigmaH +
                ", maxSigma2D=" + maxSigma2D +
                ", maxSigma3D=" + maxSigma3D +
                ", numDataSets=" + numDataSets +
                ", minTime=" + minTime +
                ", maxTime=" + maxTime +
                '}';
    }
}
