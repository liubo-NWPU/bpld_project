/**
 * @description: 筛选point信息
 * @author: liuyan
 * @create: 2020-01-10 16:50
 **/

package com.gis.manager.model.extra;

public class SifteShape {
    Double maxChange;
    Double avgChange;
    Double area;

    public SifteShape() {
    }

    public SifteShape(Double maxChange, Double avgChange, Double area) {
        this.maxChange = maxChange;
        this.avgChange = avgChange;
        this.area = area;
    }

    public Double getMaxChange() {
        return maxChange;
    }

    public void setMaxChange(Double maxChange) {
        this.maxChange = maxChange;
    }

    public Double getAvgChange() {
        return avgChange;
    }

    public void setAvgChange(Double avgChange) {
        this.avgChange = avgChange;
    }

    public Double getArea() {
        return area;
    }

    public void setArea(Double area) {
        this.area = area;
    }
}


