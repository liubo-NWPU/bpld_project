package com.gis.manager.model;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.Date;

/**
 * (HistoryShapePoint)实体类
 *
 * @author liuyan
 * @since 2020-03-20 14:28:22
 */
@Entity
@Table(name="history_shape_point")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class HistoryShapePoint {
     
    private Long id;
     
    private String radar;
     
    private Date time;
     
    private Double maxchange;
     
    private Double minchange;
     
    private Double avgchange;

    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    @Basic
    @Column(name = "radar")
    public String getRadar() {
        return radar;
    }

    public void setRadar(String radar) {
        this.radar = radar;
    }
    
    @Basic
    @Column(name = "time")
    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
    
    @Basic
    @Column(name = "maxchange")
    public Double getMaxchange() {
        return maxchange;
    }

    public void setMaxchange(Double maxchange) {
        this.maxchange = maxchange;
    }
    
    @Basic
    @Column(name = "minchange")
    public Double getMinchange() {
        return minchange;
    }

    public void setMinchange(Double minchange) {
        this.minchange = minchange;
    }
    
    @Basic
    @Column(name = "avgchange")
    public Double getAvgchange() {
        return avgchange;
    }

    public void setAvgchange(Double avgchange) {
        this.avgchange = avgchange;
    }
    
 
}