
package com.gis.trans.model;

import java.io.Serializable;
import java.util.Date;

public class MeanPosDefVo implements Serializable {
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getLogTime() {
        return logTime;
    }

    public void setLogTime(Date logTime) {
        this.logTime = logTime;
    }

    public Long getEventTime() {
        return eventTime;
    }

    public void setEventTime(Long eventTime) {
        this.eventTime = eventTime;
    }

    public Long getDataPeriod() {
        return dataPeriod;
    }

    public void setDataPeriod(Long dataPeriod) {
        this.dataPeriod = dataPeriod;
    }

    public Integer getStationId() {
        return stationId;
    }

    public void setStationId(Integer stationId) {
        this.stationId = stationId;
    }

    public Integer getStationKey() {
        return stationKey;
    }

    public void setStationKey(Integer stationKey) {
        this.stationKey = stationKey;
    }

    public Integer getBaseId() {
        return baseId;
    }

    public void setBaseId(Integer baseId) {
        this.baseId = baseId;
    }

    public Integer getBaseKey() {
        return baseKey;
    }

    public void setBaseKey(Integer baseKey) {
        this.baseKey = baseKey;
    }

    public Double getdN() {
        return dN;
    }

    public void setdN(Double dN) {
        this.dN = dN;
    }

    public Double getdE() {
        return dE;
    }

    public void setdE(Double dE) {
        this.dE = dE;
    }

    public Double getdH() {
        return dH;
    }

    public void setdH(Double dH) {
        this.dH = dH;
    }

    public Double getCovN() {
        return covN;
    }

    public void setCovN(Double covN) {
        this.covN = covN;
    }

    public Double getCovNE() {
        return covNE;
    }

    public void setCovNE(Double covNE) {
        this.covNE = covNE;
    }

    public Double getCovE() {
        return covE;
    }

    public void setCovE(Double covE) {
        this.covE = covE;
    }

    public Double getCovNH() {
        return covNH;
    }

    public void setCovNH(Double covNH) {
        this.covNH = covNH;
    }

    public Double getCovEH() {
        return covEH;
    }

    public void setCovEH(Double covEH) {
        this.covEH = covEH;
    }

    public Double getCovH() {
        return covH;
    }

    public void setCovH(Double covH) {
        this.covH = covH;
    }

    public Short getType() {
        return type;
    }

    public void setType(Short type) {
        this.type = type;
    }

    public Double getRotationAngle() {
        return rotationAngle;
    }

    public void setRotationAngle(Double rotationAngle) {
        this.rotationAngle = rotationAngle;
    }

    public Double getpDOP() {
        return pDOP;
    }

    public void setpDOP(Double pDOP) {
        this.pDOP = pDOP;
    }

    public Double getNumSat() {
        return numSat;
    }

    public void setNumSat(Double numSat) {
        this.numSat = numSat;
    }

    public Double getNumSatGPS() {
        return numSatGPS;
    }

    public void setNumSatGPS(Double numSatGPS) {
        this.numSatGPS = numSatGPS;
    }

    public Short getIsFixed() {
        return isFixed;
    }

    public void setIsFixed(Short isFixed) {
        this.isFixed = isFixed;
    }

    public Double getMinDN() {
        return minDN;
    }

    public void setMinDN(Double minDN) {
        this.minDN = minDN;
    }

    public Double getMinDE() {
        return minDE;
    }

    public void setMinDE(Double minDE) {
        this.minDE = minDE;
    }

    public Double getMinDH() {
        return minDH;
    }

    public void setMinDH(Double minDH) {
        this.minDH = minDH;
    }

    public Double getMaxDN() {
        return maxDN;
    }

    public void setMaxDN(Double maxDN) {
        this.maxDN = maxDN;
    }

    public Double getMaxDE() {
        return maxDE;
    }

    public void setMaxDE(Double maxDE) {
        this.maxDE = maxDE;
    }

    public Double getMaxDH() {
        return maxDH;
    }

    public void setMaxDH(Double maxDH) {
        this.maxDH = maxDH;
    }

    public Double getMin2D() {
        return min2D;
    }

    public void setMin2D(Double min2D) {
        this.min2D = min2D;
    }

    public Double getMax2D() {
        return max2D;
    }

    public void setMax2D(Double max2D) {
        this.max2D = max2D;
    }

    public Double getMin3D() {
        return min3D;
    }

    public void setMin3D(Double min3D) {
        this.min3D = min3D;
    }

    public Double getMax3D() {
        return max3D;
    }

    public void setMax3D(Double max3D) {
        this.max3D = max3D;
    }

    public Double getMinSigmaN() {
        return minSigmaN;
    }

    public void setMinSigmaN(Double minSigmaN) {
        this.minSigmaN = minSigmaN;
    }

    public Double getMinSigmaE() {
        return minSigmaE;
    }

    public void setMinSigmaE(Double minSigmaE) {
        this.minSigmaE = minSigmaE;
    }

    public Double getMinSigmaH() {
        return minSigmaH;
    }

    public void setMinSigmaH(Double minSigmaH) {
        this.minSigmaH = minSigmaH;
    }

    public Double getMinSigma2D() {
        return minSigma2D;
    }

    public void setMinSigma2D(Double minSigma2D) {
        this.minSigma2D = minSigma2D;
    }

    public Double getMinSigma3D() {
        return minSigma3D;
    }

    public void setMinSigma3D(Double minSigma3D) {
        this.minSigma3D = minSigma3D;
    }

    public Double getMaxSigmaN() {
        return maxSigmaN;
    }

    public void setMaxSigmaN(Double maxSigmaN) {
        this.maxSigmaN = maxSigmaN;
    }

    public Double getMaxSigmaE() {
        return maxSigmaE;
    }

    public void setMaxSigmaE(Double maxSigmaE) {
        this.maxSigmaE = maxSigmaE;
    }

    public Double getMaxSigmaH() {
        return maxSigmaH;
    }

    public void setMaxSigmaH(Double maxSigmaH) {
        this.maxSigmaH = maxSigmaH;
    }

    public Double getMaxSigma2D() {
        return maxSigma2D;
    }

    public void setMaxSigma2D(Double maxSigma2D) {
        this.maxSigma2D = maxSigma2D;
    }

    public Double getMaxSigma3D() {
        return maxSigma3D;
    }

    public void setMaxSigma3D(Double maxSigma3D) {
        this.maxSigma3D = maxSigma3D;
    }

    public Integer getNumDataSets() {
        return numDataSets;
    }

    public void setNumDataSets(Integer numDataSets) {
        this.numDataSets = numDataSets;
    }

    public Long getMinTime() {
        return minTime;
    }

    public void setMinTime(Long minTime) {
        this.minTime = minTime;
    }

    public Long getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(Long maxTime) {
        this.maxTime = maxTime;
    }

    private Long maxTime;

    public MeanPosDefVo() {
    }


    public String toString() {
        return "MeanPosDef{, logTime=" + this.logTime + ", eventTime=" + this.eventTime + ", dataPeriod=" + this.dataPeriod + ", stationId=" + this.stationId + ", stationKey=" + this.stationKey + ", baseId=" + this.baseId + ", baseKey=" + this.baseKey + ", dN=" + this.dN + ", dE=" + this.dE + ", dH=" + this.dH + ", covN=" + this.covN + ", covNE=" + this.covNE + ", covE=" + this.covE + ", covNH=" + this.covNH + ", covEH=" + this.covEH + ", covH=" + this.covH + ", type=" + this.type + ", rotationAngle=" + this.rotationAngle + ", pDOP=" + this.pDOP + ", numSat=" + this.numSat + ", numSatGPS=" + this.numSatGPS + ", isFixed=" + this.isFixed + ", minDN=" + this.minDN + ", minDE=" + this.minDE + ", minDH=" + this.minDH + ", maxDN=" + this.maxDN + ", maxDE=" + this.maxDE + ", maxDH=" + this.maxDH + ", min2D=" + this.min2D + ", max2D=" + this.max2D + ", min3D=" + this.min3D + ", max3D=" + this.max3D + ", minSigmaN=" + this.minSigmaN + ", minSigmaE=" + this.minSigmaE + ", minSigmaH=" + this.minSigmaH + ", minSigma2D=" + this.minSigma2D + ", minSigma3D=" + this.minSigma3D + ", maxSigmaN=" + this.maxSigmaN + ", maxSigmaE=" + this.maxSigmaE + ", maxSigmaH=" + this.maxSigmaH + ", maxSigma2D=" + this.maxSigma2D + ", maxSigma3D=" + this.maxSigma3D + ", numDataSets=" + this.numDataSets + ", minTime=" + this.minTime + ", maxTime=" + this.maxTime + '}';
    }
}
