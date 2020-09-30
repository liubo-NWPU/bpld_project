package com.gis.trans.model;

import java.text.DecimalFormat;

public class Qujian {
    DecimalFormat format = new DecimalFormat("0.00000");
    private Double min;
    private Double max;
    private String color;
    private Double Opacity;

    public Qujian(double min, double max, String color, Double Opacity) {
        this.min = min;
        this.max = max;
        this.color = color;
        this.Opacity = Opacity;
    }

    public Double getMin() {
        return min;
    }

    public void setMin(Double min) {
        this.min = min;
    }

    public Double getMax() {
        return max;
    }

    public void setMax(Double max) {
        this.max = max;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Double getOpacity() {
        return Opacity;
    }

    public void setOpacity(Double opacity) {
        Opacity = opacity;
    }
}
