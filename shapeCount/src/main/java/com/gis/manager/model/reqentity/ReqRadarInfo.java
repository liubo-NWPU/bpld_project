package com.gis.manager.model.reqentity;

import com.gis.manager.model.RadarInfo;

public class ReqRadarInfo {
    private RadarInfo radarInfo;
    private Long parentid;

    public RadarInfo getRadarInfo() {
        return radarInfo;
    }

    public void setRadarInfo(RadarInfo radarInfo) {
        this.radarInfo = radarInfo;
    }

    public Long getParentid() {
        return parentid;
    }

    public void setParentid(Long parentid) {
        this.parentid = parentid;
    }
}
