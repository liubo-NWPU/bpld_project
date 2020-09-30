/**
 * @description:
 * @author: liuyan
 * @create: 2019-12-16 14:37
 **/

package com.gis.manager.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vividsolutions.jts.geom.Point;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "shape_point")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
public class ShapePoint {
    private String id;
    private Long fileId;
    private Long m;
    private Long n;
    private Double change;
    private Point geo;
    private Date time;
    private String radar;
    private String range;
    private Double speed;
    private Double accSpeed;
    private Double angle;
    private String geom;

    @Id
    @Column(name = "id", nullable = true)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "file_id", nullable = true)
    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    @Basic
    @Column(name = "m", nullable = true)
    public Long getM() {
        return m;
    }

    public void setM(Long m) {
        this.m = m;
    }

    @Basic
    @Column(name = "n", nullable = true)
    public Long getN() {
        return n;
    }

    public void setN(Long n) {
        this.n = n;
    }

    @Basic
    @Column(name = "change", nullable = true)
    public Double getChange() {
        return change;
    }

    public void setChange(Double change) {
        this.change = change;
    }

    @JsonIgnore
    @Type(type = "com.vividsolutions.jts.geom.Geometry")
    @Column(name="geo",columnDefinition="Geometry(Point,3857)")
    public Point getPosition() {
        return geo;
    }

    public void setPosition(Point position) {
        this.geo = position;
    }

    @Basic
    @Column(name = "time", nullable = true)
    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    @Basic
    @Column(name = "radar", length = 20, nullable = true)
    public String getRadar() {
        return radar;
    }

    public void setRadar(String radar) {
        this.radar = radar;
    }

    @Basic
    @Column(name = "range", length = 20, nullable = true)
    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    @Basic
    @Column(name = "speed", nullable = true)
    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    @Basic
    @Column(name = "accspeed", nullable = true)
    public Double getAccSpeed() {
        return accSpeed;
    }

    public void setAccSpeed(Double accSpeed) {
        this.accSpeed = accSpeed;
    }

    @Basic
    @Column(name = "angle", nullable = true)
    public Double getAngle() {
        return angle;
    }

    public void setAngle(Double angle) {
        this.angle = angle;
    }

    @Transient
    public String getGeom() {
        return this.geo==null?null:this.geo.toText();
    }
    public void setGeom(String geom) {
        this.geom = geom;
    }

}


