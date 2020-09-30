/**
 * @description:
 * @author: liuyan
 * @create: 2019-12-20 13:43
 **/

package com.gis.manager.model.extra;

public class MaxMin {
    private Double max;
    private Double min;

    public MaxMin() {
        /**
         * 构造函数
         */
    }

    public MaxMin(Double max, Double min) {
        this.max = max;
        this.min = min;
    }

    public Double getMax() {
        return max;
    }

    public void setMax(Double max) {
        this.max = max;
    }

    public Double getMin() {
        return min;
    }

    public void setMin(Double min) {
        this.min = min;
    }
}


