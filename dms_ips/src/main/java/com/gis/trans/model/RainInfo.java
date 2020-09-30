package com.gis.trans.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Date;

//雨量信息表
@Entity
@Table(name = "rain_gauge")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class RainInfo {

    private Integer rIAddrId; //代号
    private String rIAddrName; //地名
    private String rIStation; //区站号
    private String rITelephone; //电话号码
    private String rILongitude;  //经度
    private String rILatiude; //纬度
    private String rIMachine;  //机器编码
    private String rIInfo;  //厂家信息
    private Double rIBaudRate; //波特率
    private Double rIUnit; //单位
    private Double rIOrder; //是否有效
    private String rIKey; //加密与否
    private Double rILink; //连接数
    private Date rILastData;// 最后时间
    private Date rILastDial;// 最后处理时间
    private Date rILastComm;// 最后连接时间
    private String rISheng;  //省
    private String rIShixian;  //市/县


    @Id
    @Column(name = "ri_addrid", unique = true, nullable = false)
    public Integer getrIAddrId() {
        return rIAddrId;
    }

    public void setrIAddrId(Integer rIAddrId) {
        this.rIAddrId = rIAddrId;
    }

    @Column(name = "ri_addrname")
    public String getrIAddrName() {
        return rIAddrName;
    }

    public void setrIAddrName(String rIAddrName) {
        this.rIAddrName = rIAddrName;
    }
    @Column(name = "ri_station")
    public String getrIStation() {
        return rIStation;
    }

    public void setrIStation(String rIStation) {
        this.rIStation = rIStation;
    }
    @Column(name = "ri_telephone")

    public String getrITelephone() {
        return rITelephone;
    }

    public void setrITelephone(String rITelephone) {
        this.rITelephone = rITelephone;
    }

    @Column(name = "ri_longitude")
    public String getrILongitude() {
        return rILongitude;
    }

    public void setrILongitude(String rILongitude) {
        this.rILongitude = rILongitude;
    }

    @Column(name = "ri_latitude")
    public String getrILatiude() {
        return rILatiude;
    }

    public void setrILatiude(String rILatiude) {
        this.rILatiude = rILatiude;
    }
    @Column(name = "ri_machine")
    public String getrIMachine() {
        return rIMachine;
    }

    public void setrIMachine(String rIMachine) {
        this.rIMachine = rIMachine;
    }
    @Column(name = "ri_info")
    public String getrIInfo() {
        return rIInfo;
    }

    public void setrIInfo(String rIInfo) {
        this.rIInfo = rIInfo;
    }

    @Column(name = "ri_baudrate")
    public Double getrIBaudRate() {
        return rIBaudRate;
    }

    public void setrIBaudRate(Double rIBaudRate) {
        this.rIBaudRate = rIBaudRate;
    }
    @Column(name = "ri_unit")
    public Double getrIUnit() {
        return rIUnit;
    }

    public void setrIUnit(Double rIUnit) {
        this.rIUnit = rIUnit;
    }

    @Column(name = "ri_order")
    public Double getrIOrder() {
        return rIOrder;
    }

    public void setrIOrder(Double rIOrder) {
        this.rIOrder = rIOrder;
    }


    @Column(name = "ri_key")
    public String getrIKey() {
        return rIKey;
    }

    public void setrIKey(String rIKey) {
        this.rIKey = rIKey;
    }


    @Column(name = "ri_link")
    public Double getrILink() {
        return rILink;
    }

    public void setrILink(Double rILink) {
        this.rILink = rILink;
    }


    @Column(name = "ri_lastdata")
    public Date getrILastData() {
        return rILastData;
    }

    public void setrILastData(Date rILastData) {
        this.rILastData = rILastData;
    }


    @Column(name = "ri_lastdial")
    public Date getrILastDial() {
        return rILastDial;
    }

    public void setrILastDial(Date rILastDial) {
        this.rILastDial = rILastDial;
    }


    @Column(name = "ri_lastcomm")
    public Date getrILastComm() {
        return rILastComm;
    }

    public void setrILastComm(Date rILastComm) {
        this.rILastComm = rILastComm;
    }
    @Column(name = "ri_sheng")
    public String getrISheng() {
        return rISheng;
    }

    public void setrISheng(String rISheng) {
        this.rISheng = rISheng;
    }

    @Column(name = "ri_shixian")
    public String getrIShixian() {
        return rIShixian;
    }

    public void setrIShixian(String rIShixian) {
        this.rIShixian = rIShixian;
    }


}
