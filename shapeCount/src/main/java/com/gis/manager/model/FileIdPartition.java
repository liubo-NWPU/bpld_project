/**
 * @description:
 * @author: liuyan
 * @create: 2019-12-18 14:04
 **/

package com.gis.manager.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "file_id_partition", schema = "public", catalog = "bpld_shape")
public class FileIdPartition {
    private long id;
    private String filePath;
    private String radar;
    private Date time;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "file_path", nullable = true)
    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Basic
    @Column(name = "radar", nullable = true)
    public String getRadar() {
        return radar;
    }

    public void setRadar(String radar) {
        this.radar = radar;
    }

    @Basic
    @Column(name = "time", nullable = true)
    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}


