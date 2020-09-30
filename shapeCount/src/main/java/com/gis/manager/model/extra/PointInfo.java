/**
 * @description:
 * @author: liuyan
 * @create: 2019-12-31 18:14
 **/

package com.gis.manager.model.extra;

public class PointInfo {
    private Long m;
    private Long n;
    private Double strain;
    private Double x;
    private Double y;
    private Double rAxis;
    private Double aAxis;

    public PointInfo() {
        /**
         * 构造函数
         */
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

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
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
}


