package com.geovis.web.domain.system;

import com.geovis.web.base.domain.BaseDomain;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

@Table(name = "sys_role")
public class SysRole extends BaseDomain {
    /**
     * 角色名称
     */
    private String name;

    /**
     * 角色描述
     */
    private String description;

    //-----------------------------------------------------扩展字段-----------------------------------------------------
    /**
     * 角色资源
     */
    @Transient
    private List<SysResource> resourceList;

    @Transient
    private List<ResourcePower> resourceIdList;

    @Transient
    private List<String> userIdList;

    @Transient
    private String operationType;
    @Transient
    private Date startTime;
    @Transient
    private Date endTime;

    @Transient
    /**折扣价**/
    private double discount;

    @Column(name = "role_type")
    private String roleType;

    @Column(name = "candelete")
    private String candelete;

    @Column(name = "canshow")
    private String canshow;

    /**
     * 获取角色名称
     *
     * @return name - 角色名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置角色名称
     *
     * @param name 角色名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取角色描述
     *
     * @return description - 角色描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置角色描述
     *
     * @param description 角色描述
     */
    public void setDescription(String description) {
        this.description = description;
    }

    public List<SysResource> getResourceList() {
        return resourceList;
    }

    public void setResourceList(List<SysResource> resourceList) {
        this.resourceList = resourceList;
    }

    public List<ResourcePower> getResourceIdList() {
        return resourceIdList;
    }

    public void setResourceIdList(List<ResourcePower> resourceIdList) {
        this.resourceIdList = resourceIdList;
    }

    public List<String> getUserIdList() {
        return userIdList;
    }

    public void setUserIdList(List<String> userIdList) {
        this.userIdList = userIdList;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String getRoleType() {
        return roleType;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }

    public String getCandelete() {
        return candelete;
    }

    public void setCandelete(String candelete) {
        this.candelete = candelete;
    }

    public String getCanshow() {
        return canshow;
    }

    public void setCanshow(String canshow) {
        this.canshow = canshow;
    }
}