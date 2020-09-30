package com.gis.manager.model.reqentity;

import com.gis.manager.model.MineArea;

public class ReqMinearea {
    private MineArea mineArea;
    private Long parentid;

    public MineArea getMineArea() {
        return mineArea;
    }

    public void setMineArea(MineArea mineArea) {
        this.mineArea = mineArea;
    }

    public Long getParentid() {
        return parentid;
    }

    public void setParentid(Long parentid) {
        this.parentid = parentid;
    }
}
