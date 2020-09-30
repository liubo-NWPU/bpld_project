package com.gis.trans.model;

public class ReqColor {
    Long fileid;
    String colorJson;

    public ReqColor() {
    }

    public ReqColor(Long fileid, String colorJson) {
        this.fileid = fileid;
        this.colorJson = colorJson;
    }

    public Long getFileid() {
        return fileid;
    }

    public void setFileid(Long fileid) {
        this.fileid = fileid;
    }

    public String getColorJson() {
        return colorJson;
    }

    public void setColorJson(String colorJson) {
        this.colorJson = colorJson;
    }
}
