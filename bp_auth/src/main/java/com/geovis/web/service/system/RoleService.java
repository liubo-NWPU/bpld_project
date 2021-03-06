package com.geovis.web.service.system;

import com.geovis.web.base.service.BaseService;
import com.geovis.web.domain.system.ResourcePower;
import com.geovis.web.domain.system.SysRole;
import com.geovis.web.domain.system.SysUser;

import java.util.List;

public interface RoleService extends BaseService<SysRole> {

    /**
     * 查询方法，支持模糊查询
     *
     * @param sysRole
     * @return
     */
    List<SysRole> listAll(SysRole sysRole);

    /**
     * 将修改和添加封装在一起，便于缓存处理
     * （更新缓存）
     *
     * @param sysRole
     * @return
     */
    SysRole commonSave(SysUser currentUser, SysRole sysRole);

    /**
     * 查询单个角色（根据需要封装子记录）
     *
     * @param id
     * @return
     */
    SysRole selectRoleById(String id);

    /**
     * 软删除单个角色（清除缓存）
     * @param currentUser
     * @param id
     * @return
     */
    SysRole removeRoleById(SysUser currentUser ,String id) ;

    /**
     * 为角色设置资源
     * @param currentUser
     * @param resourceIdList
     * @param roleId
     */
    void setResource(SysUser currentUser, List<ResourcePower> resourceIdList , String roleId,String operationType);

    /**
     * 为角色设置用户
     * @param currentUser
     * @param userIdList
     * @param roleId
     */
    void setUser(SysUser currentUser, List<String> userIdList, String roleId);

}
