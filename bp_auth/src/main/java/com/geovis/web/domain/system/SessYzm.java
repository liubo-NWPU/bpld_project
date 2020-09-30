package com.geovis.web.domain.system;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "sess_yzm")
public class SessYzm {

    private String sessid;
    private String yzm;
    private long date;

    @Id
    @Column
    public String getSessid() {
        return sessid;
    }

    public void setSessid(String sessid) {
        this.sessid = sessid;
    }

    @Column
    public String getYzm() {
        return yzm;
    }

    public void setYzm(String yzm) {
        this.yzm = yzm;
    }
    @Column
    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "SessYzm{" +
                "sessid='" + sessid + '\'' +
                ", yzm='" + yzm + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
