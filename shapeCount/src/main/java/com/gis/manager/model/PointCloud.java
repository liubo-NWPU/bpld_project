package com.gis.manager.model;

import org.hibernate.annotations.GenericGenerator;

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
    private Double M;
    private Double N;
    private String radar;

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
    @Column(name = "m")
    public Double getM() {
        return M;
    }

    public void setM(Double m) {
        M = m;
    }

    @Basic
    @Column(name = "n")
    public Double getN() {
        return N;
    }

    public void setN(Double n) {
        N = n;
    }

    @Basic
    @Column(name = "radar")
    public String getRadar() {
        return radar;
    }

    public void setRadar(String radar) {
        this.radar = radar;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PointCloud that = (PointCloud) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (x != null ? !x.equals(that.x) : that.x != null) return false;
        if (y != null ? !y.equals(that.y) : that.y != null) return false;
        if (z != null ? !z.equals(that.z) : that.z != null) return false;
        if (rAxis != null ? !rAxis.equals(that.rAxis) : that.rAxis != null) return false;
        if (aAxis != null ? !aAxis.equals(that.aAxis) : that.aAxis != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (x != null ? x.hashCode() : 0);
        result = 31 * result + (y != null ? y.hashCode() : 0);
        result = 31 * result + (z != null ? z.hashCode() : 0);
        result = 31 * result + (rAxis != null ? rAxis.hashCode() : 0);
        result = 31 * result + (aAxis != null ? aAxis.hashCode() : 0);
        return result;
    }
}
