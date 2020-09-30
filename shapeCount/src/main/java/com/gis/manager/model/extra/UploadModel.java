package com.gis.manager.model.extra;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Author:wangmeng
 * @Date:创建于2018/11/19 10:29
 * @Description:
 */
@Configuration
@ConfigurationProperties(prefix = "upload")
public class UploadModel {
    private String saveFilePath;
    private Long partFileSize;
    private Boolean packageCoordinate;
    private Boolean fileDelete;
    private String defaultGBKCharset;

    public String getSaveFilePath() {
        return saveFilePath;
    }
    public void setSaveFilePath(String saveFilePath) {
        this.saveFilePath = saveFilePath;
    }

    public Long getPartFileSize() {
        return partFileSize;
    }
    public void setPartFileSize(Long partFileSize) {
        this.partFileSize = partFileSize;
    }

    public Boolean getPackageCoordinate() {
        return packageCoordinate;
    }
    public void setPackageCoordinate(Boolean packageCoordinate) {
        this.packageCoordinate = packageCoordinate;
    }

    public Boolean getFileDelete() {
        return fileDelete;
    }
    public void setFileDelete(Boolean fileDelete) {
        this.fileDelete = fileDelete;
    }

    public String getDefaultGBKCharset() {
        return defaultGBKCharset;
    }
    public void setDefaultGBKCharset(String defaultGBKCharset) {
        this.defaultGBKCharset = defaultGBKCharset;
    }
}
