package com.gis.trans.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

//视频
@Entity
@Table(name = "video")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class Video {

    private Long id;
    private String name;
    private String videoURL;
    private Long radarId;//所属雷达ID
    private String createTime;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    @Column
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Column(name = "video_url")
    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }
    @Column(name = "radar_id")
    public Long getRadarId() {
        return radarId;
    }

    public void setRadarId(Long radarId) {
        this.radarId = radarId;
    }

    @Column(name = "create_time")
    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
