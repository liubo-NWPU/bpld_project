package com.geovis.web.domain.model;

import java.util.Date;
public class ResponseDMFolderModel {
    private  long id;
    private  String fileName;
    private  int fileType;
    private Date uploadTime;
    private Date updateTime;
    private long parentId;
    private int edit;
    private long fileSize;
    private String relativePath;
    private long  auth_use_catalog;
    private int downloadCount;

    /**数据权限ID**/
    private String powerId;
    /**数据权限父ID**/
    private String  paraentPawerId;
    /**目录权限**/
    private String powerData;
    /**第三方服务权限*/
    private String servicePowerData;

    /**
     * 工作区
     */
    private String workArea;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public int getEdit() {
        return edit;
    }

    public void setEdit(int edit) {
        this.edit = edit;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }

    public String getPowerId() {
        return powerId;
    }

    public void setPowerId(String powerId) {
        this.powerId = powerId;
    }

    public String getParaentPawerId() {
        return paraentPawerId;
    }

    public void setParaentPawerId(String paraentPawerId) {
        this.paraentPawerId = paraentPawerId;
    }

    public String getPowerData() {
        return powerData;
    }

    public void setPowerData(String powerData) {
        this.powerData = powerData;
    }

	public String getServicePowerData() {
		return servicePowerData;
	}

	public void setServicePowerData(String servicePowerData) {
		this.servicePowerData = servicePowerData;
	}


	public String getWorkArea() {
		return workArea;
	}

	public void setWorkArea(String workArea) {
		this.workArea = workArea;
	}

    public long getAuth_use_catalog() {
        return auth_use_catalog;
    }

    public void setAuth_use_catalog(long auth_use_catalog) {
        this.auth_use_catalog = auth_use_catalog;
    }

    public int getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(int downloadCount) {
        this.downloadCount = downloadCount;
    }

    @Override
    public String toString() {
        return "ResponseDMFolderModel{" +
                "id=" + id +
                ", fileName='" + fileName + '\'' +
                ", fileType=" + fileType +
                ", uploadTime=" + uploadTime +
                ", updateTime=" + updateTime +
                ", parentId=" + parentId +
                ", edit=" + edit +
                ", fileSize=" + fileSize +
                ", relativePath='" + relativePath + '\'' +
                ", auth_use_catalog=" + auth_use_catalog +
                ", downloadCount=" + downloadCount +
                ", powerId='" + powerId + '\'' +
                ", paraentPawerId='" + paraentPawerId + '\'' +
                ", powerData='" + powerData + '\'' +
                ", servicePowerData='" + servicePowerData + '\'' +
                ", workArea='" + workArea + '\'' +
                '}';
    }
}
