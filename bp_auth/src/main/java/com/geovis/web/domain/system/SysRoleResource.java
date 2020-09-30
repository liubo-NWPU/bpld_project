package com.geovis.web.domain.system;

import com.geovis.web.base.domain.BaseDomain;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "sys_role_resource")
public class SysRoleResource extends BaseDomain {
    /**
     * 角色ID
     */
    @Column(name = "role_id")
    private String roleId;


    /**
     * 资源ID
     */
    @Column(name = "resource_id")
    private String resourceId;
    /**
     * 权限
     */
    @Column(name = "datapower")
    private String datapower;
    
    /**
     * 第三方服务权限
     */
    @Column(name = "servicepower")
    private String servicepower;

    /**
     * 获取角色ID
     *
     * @return role_id - 角色ID
     */
    public String getRoleId() {
        return roleId;
    }

    /**
     * 设置角色ID
     *
     * @param roleId 角色ID
     */
    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    /**
     * 获取资源ID
     *
     * @return resource_id - 资源ID
     */
    public String getResourceId() {
        return resourceId;
    }

    /**
     * 设置资源ID
     *
     * @param resourceId 资源ID
     */
    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }



    public String getDatapower() {
        return datapower;
    }

    public void setDatapower(String datapower) {
        this.datapower = datapower;
    }

	public String getServicepower() {
		return servicepower;
	}

	public void setServicepower(String servicepower) {
		this.servicepower = servicepower;
	}

}