package com.gis.trans.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vividsolutions.jts.geom.Point;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Table(name = "point_cloud", schema = "public", catalog = "bpld_shape")
public class PointCloud {
    private Long id;
    private Double x;
    private Double y;
    private Double z;
    private Double rAxis;
    private Double aAxis;
    private int M;
    private int N;
    private String radar;
    private Point geo;

    private Double lot = 0.0;
    private Double lat = 0.0;

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
    @Column(name = "x")
    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    @Basic
    @Column(name = "y")
    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    @Basic
    @Column(name = "z")
    public Double getZ() {
        return z;
    }

    public void setZ(Double z) {
        this.z = z;
    }

    @Basic
    @Column(name = "r_axis")
    public Double getrAxis() {
        return rAxis;
    }

    public void setrAxis(Double rAxis) {
        this.rAxis = rAxis;
    }

    @Basic
    @Column(name = "a_axis")
    public Double getaAxis() {
        return aAxis;
    }

    public void setaAxis(Double aAxis) {
        this.aAxis = aAxis;
    }

    @Basic
    @Column(name = "radar")
    public String getRadar() {
        return radar;
    }

    public void setRadar(String radar) {
        this.radar = radar;
    }

    @Basic
    @Column(name = "m")
    public int getM() {
        return M;
    }

    public void setM(int m) {
        M = m;
    }

    @Basic
    @Column(name = "n")
    public int getN() {
        return N;
    }

    public void setN(int n) {
        N = n;
    }

    @JsonIgnore
    @Type(type = "com.vividsolutions.jts.geom.Geometry")
    @Column(name="geo",columnDefinition="Geometry(Point,3857)")
    public Point getGeo() {
        return geo;
    }

    public void setGeo(Point geo) {
        this.geo = geo;
    }

    public Double getLot() {
        return lot;
    }

    public void setLot(Double lot) {
        this.lot = lot;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }
}
