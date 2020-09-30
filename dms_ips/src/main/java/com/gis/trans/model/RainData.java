package com.gis.trans.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Date;

//雨量数据表
@Entity
@Table(name = "rain_data")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class RainData {

    private Long id;
    private Integer rDAddr; //地区代号
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date rDDate; //日期
    private Integer rDSum; //总量
    private String  rDData; //单位
    private Double rDUnit; //降雨数据
    private Double  rDTemp; //温度

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "rd_addr",length = 30)
    public Integer getrDAddr() {
        return rDAddr;
    }

    public void setrDAddr(Integer rDAddr) {
        this.rDAddr = rDAddr;
    }
    @Column(name = "rd_date")
    public Date getrDDate() {
        return rDDate;
    }

    public void setrDDate(Date rDDate) {
        this.rDDate = rDDate;
    }
    @Column(name = "rd_sum")
    public Integer getrDSum() {
        return rDSum;
    }

    public void setrDSum(Integer rDSum) {
        this.rDSum = rDSum;
    }
    @Column(name = "rd_data")
    public String getrDData() {
        return rDData;
    }

    public void setrDData(String rDData) {
        this.rDData = rDData;
    }
    @Column(name = "rd_unit")
    public Double getrDUnit() {
        return rDUnit;
    }

    public void setrDUnit(Double rDUnit) {
        this.rDUnit = rDUnit;
    }
    @Column(name = "rd_temp")
    public Double getrDTemp() {
        return rDTemp;
    }

    public void setrDTemp(Double rDTemp) {
        this.rDTemp = rDTemp;
    }
}
