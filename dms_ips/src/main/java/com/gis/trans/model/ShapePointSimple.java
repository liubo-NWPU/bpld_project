package com.gis.trans.model;

import javax.persistence.*;
import java.math.BigDecimal;


public class ShapePointSimple {
    private Double x;
    private Double y;
    private Double z;
    private Double strain;

    public ShapePointSimple() {
    }

    public ShapePointSimple(Double x, Double y, Double z, Double strain) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.strain = strain;
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
    @Column(name = "strain")
    public Double getStrain() {
        return strain;
    }

    public void setStrain(BigDecimal strain) {
        if (strain == null)
            this.strain = 0.0;
        else
            this.strain = strain.doubleValue();
    }
}
