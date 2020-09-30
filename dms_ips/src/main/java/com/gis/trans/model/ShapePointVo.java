package com.gis.trans.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vividsolutions.jts.geom.Point;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

public class ShapePointVo {

    private Long fileId;
    private Long m;
    private Long n;
    private Double strain;
    private Date time;
    private Point geo;
    private Double speed;
    private Double accspeed;
    private String radar;
    private String id;
    private String range;

    /*临时存储*/
    private Double rAxis;   //距离
    private Double aAxis;  //偏移角度
    private Double X;    //x
    private Double Y;    //Y
    private  String fileName; //文件名称

    public ShapePointVo() {
    }

    public ShapePointVo(Long m, Long n, Double rAxis, Double aAxis, Point geo, Double strain) {
        this.m = m;
        this.n = n;
        this.strain = strain;
        this.geo = geo;
        this.rAxis = rAxis;
        this.aAxis = aAxis;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public Long getM() {
        return m;
    }

    public void setM(Long m) {
        this.m = m;
    }


    public Long getN() {
        return n;
    }

    public void setN(Long n) {
        this.n = n;
    }

    public Double getStrain() {
        return strain;
    }

    public void setStrain(Double strain) {
        this.strain = strain;
    }


    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Point getGeo() {
        return geo;
    }
    public void setGeo(Point geo) {
        this.geo = geo;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Double getAccspeed() {
        return accspeed;
    }

    public void setAccspeed(Double accspeed) {
        this.accspeed = accspeed;
    }

    public String getRadar() {
        return radar;
    }

    public void setRadar(String radar) {
        this.radar = radar;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public Double getrAxis() {
        return rAxis;
    }

    public void setrAxis(Double rAxis) {
        this.rAxis = rAxis;
    }

    public Double getaAxis() {
        return aAxis;
    }

    public void setaAxis(Double aAxis) {
        this.aAxis = aAxis;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShapePointVo that = (ShapePointVo) o;
        return Objects.equals(fileId, that.fileId) &&
                Objects.equals(m, that.m) &&
                Objects.equals(n, that.n) &&
                Objects.equals(strain, that.strain) &&
                Objects.equals(time, that.time) &&
                Objects.equals(geo, that.geo) &&
                Objects.equals(speed, that.speed) &&
                Objects.equals(accspeed, that.accspeed) &&
                Objects.equals(radar, that.radar) &&
                Objects.equals(id, that.id) &&
                Objects.equals(rAxis, that.rAxis) &&
                Objects.equals(aAxis, that.aAxis) &&
                Objects.equals(range, that.range);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileId, m, n, strain, time, geo, speed, accspeed, radar, id, range,rAxis,aAxis);
    }

    public Double getX() {
        return X;
    }

    public void setX(Double x) {
        X = x;
    }

    public Double getY() {
        return Y;
    }

    public void setY(Double y) {
        Y = y;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
