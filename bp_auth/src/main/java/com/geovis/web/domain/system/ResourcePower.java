package com.geovis.web.domain.system;

public class ResourcePower {
    /**
     * 资源ID
     **/
    private String resourceId;
    /**
     * 数据权限ID
     **/
    private String powerId;
    
    /**
     * 第三方服务权限id
     */
    private String dataserviceId;

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getPowerId() {
        return powerId;
    }

    public void setPowerId(String powerId) {
        this.powerId = powerId;
    }
    
    
	public String getDataserviceId() {
		return dataserviceId;
	}

	public void setDataserviceId(String dataserviceId) {
		this.dataserviceId = dataserviceId;
	}

    @Override
    public boolean equals(Object obj) {

        if (obj == null) return false;
        if (this == obj) return true;
        if(this.getClass()!= obj.getClass()) {
            return false;
        }
        if (obj instanceof ResourcePower) {
            ResourcePower resourcePower = (ResourcePower) obj;
            if (resourcePower.getResourceId() != null && resourcePower.getResourceId().equals(this.getResourceId())) {
                return true;
            } else if (resourcePower.getPowerId() != null && resourcePower.getPowerId().equals(this.getPowerId())) {
                return true;
            } else if(resourcePower.getDataserviceId() != null && resourcePower.getDataserviceId().equals(this.getDataserviceId())){
            	return true;
            }
        }
            return false;

    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.getPowerId().hashCode();
        result = prime * result + this.getResourceId().hashCode();
        result = prime * result + this.getDataserviceId().hashCode();

        return result;
    }


    @Override
    public String toString() {
        return "ResourcePower{" +
                "resourceId='" + resourceId + '\'' +
                ", powerId='" + powerId + '\'' +
                ", dataserviceId='" + dataserviceId + '\'' +
                '}';
    }


}
