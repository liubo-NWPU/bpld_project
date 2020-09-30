/**
 * @description:
 * @author: liuyan
 * @create: 2020-01-06 19:11
 **/

package com.gis.manager.model.extra;

public class RangeCount {
    private Long count;
    private String range;

    public RangeCount(Long count, String range) {
        this.count = count;
        this.range = range;
    }

    public RangeCount() {
        /**
         * 构造函数
         */
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }
}


