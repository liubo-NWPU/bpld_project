package com.geovis.web.service.system;

import com.geovis.web.base.service.BaseService;
import com.geovis.web.domain.system.SysResource;
import com.geovis.web.domain.system.SysUser;

import java.util.List;
import java.util.Map;

public interface ResourceService extends BaseService<SysResource> {

    /**
     * 查询方法，支持模糊查询
     *
     * @param sysResource
     * @return
     */
    List<SysResource> listAll(SysResource sysResource);

    /**
     * 将修改和添加封装在一起，便于缓存处理
     * （更新缓存）
     *
     * @param sysResource
     * @return
     */
    SysResource commonSave(SysUser currentUser, SysResource sysResource);

    /**
     * 查询单个资源
     *
     * @param id
     * @return
     */
    SysResource getResourceById(String id);

    /**
     * 软删除资源（清除缓存）
     * @param currentUser
     * @param id
     * @return
     */
    SysResource removeResourceById(SysUser currentUser, String id,String type) ;

    /**
     * 根据userId获取用户资源
     * @param userId
     * @return
     */
    List<SysResource> listResourceByUserId(String userId);

    /**
     * 根据userId获取数据权限
     * @param userId
     * @return
     */
    List<SysResource>  listDataPowerByUserId(String userId);

    /**
     * 根据roleId获取角色资源
     * @param roleId
     * @return
     */
    List<SysResource> listResourceByRoleId(String roleId);

    /**
     * 根据roleId获取所有资源，标识是否选中状态
     * @param roleId
     * @return
     */
    List<SysResource> listAllResourceByRoleId(String roleId);

    /**
     * 获取用户的菜单
     * @param userId
     * @return
     */
    List<SysResource> listMenuTreeByUserId(String userId);


    /**
     * 获取用户数据权限
     * @param userId
     * @return
     */
    List<SysResource>  listFolderTreeByUserId(String userId);
    
    
    /**
     * 获取用户服务权限
     * @param userId
     * @return
     */
    List<SysResource>  listServiceFolderTreeByUserId(String userId);
    /**
     * 获取用户的权限
     *
     * @param userId
     * @return
     */
    List<SysResource> listPermissionByUserId(String userId);

    /**
     * 获取所有菜单
     * @return
     */
    Map<String, String> listAllMenuTree();
    
    
    /**
     * 获取用户的权限
     *
     * @param userId
     * @return
     */
    List<SysResource> listSourceByType(String userId,String type);


    boolean deleteResourceById(String id);

}
