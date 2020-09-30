package com.geovis.web.service.impl.system;

import com.geovis.core.constant.Constant;
import com.geovis.core.util.BeanUtil;
import com.geovis.core.util.SystemCacheUtil;
import com.geovis.web.base.service.impl.AbstractService;
import com.geovis.web.dao.system.mapper.SysResourceMapper;
import com.geovis.web.dao.system.mapper.SysRoleMapper;
import com.geovis.web.dao.system.mapper.SysRoleResourceMapper;
import com.geovis.web.dao.system.mapper.SysUserRoleMapper;
import com.geovis.web.domain.enums.ResourceType;
import com.geovis.web.domain.system.*;
import com.geovis.web.service.system.ResourceService;
import com.geovis.web.service.system.RoleService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

@SuppressWarnings("all")
@Service
public class RoleServiceImpl extends AbstractService<SysRole> implements RoleService {
    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysRoleResourceMapper sysRoleResourceMapper;

    @Autowired
    private SysResourceMapper sysResourceMapper;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    private ResourceService resourceService;

    /**
     * 查询，支持模糊查询
     *
     * @param sysResource
     * @return
     */
    @Override
    public List<SysRole> listAll(SysRole sysResource) {
        if (sysResource.getPage() != null && sysResource.getRows() != null) {
            PageHelper.startPage(sysResource.getPage(), sysResource.getRows());
        }
        Example example = new Example(SysRole.class);
        Example.Criteria criteria = example.createCriteria();
        if (sysResource != null && sysResource.getName() != null) {
            criteria.andLike("name", "%" + sysResource.getName() + "%");
        }
        criteria.andEqualTo("id", sysResource.getId());
        //TODO other search params
        //创建时间 add by 20180828
        Date startDate = sysResource.getStartTime();
        if (startDate != null) {
            criteria.andGreaterThanOrEqualTo("createDate", startDate);
        }

        Date endDate = sysResource.getEndTime();
        if (endDate != null) {
            criteria.andLessThanOrEqualTo("createDate", endDate);
        }
        //获取删除标记为正常的记录
        criteria.andEqualTo("delFlag", sysResource.getDelFlag());
        //gaox_20181205_获取角色为3的role
        criteria.andEqualTo("roleType", "3");
        //获取可以显示的记录
        criteria.andEqualTo("canshow", "true");
        return this.sysRoleMapper.selectByExample(example);
    }

    /**
     * 将修改和添加封装在一起，便于缓存处理
     * 当数据发生变化时更新缓存
     *
     * @param sysRole
     * @return
     */
    @CacheEvict(value = SystemCacheUtil.CACHE_RESOURCE
            , key = "T(com.geovis.core.util.SystemCacheUtil).SYS_ROLE_ID + #sysRole.id"
            , condition = "#sysRole.resourceList != null and #sysRole.resourceList.size() > 0")
    @CachePut(value = SystemCacheUtil.CACHE_ROLE, key = "#sysRole.id")
    @Override
    @Transactional
    public SysRole commonSave(SysUser currentUser, SysRole sysRole) {
        if (sysRole.getId() == null) {
            super.save(sysRole);
        } else {
            this.updateNotNull(sysRole);
        }

        //判断角色资源
        if (sysRole.getResourceList() != null && sysRole.getResourceList().size() > 0) {
            //前端传入的资源列表
            List<ResourcePower> resIdList = new ArrayList<ResourcePower>();
            for (SysResource sysRes : sysRole.getResourceList()) {
                ResourcePower resourcePower = new ResourcePower();
                resourcePower.setPowerId(null);
                resourcePower.setResourceId(sysRes.getId());
            }
            this.setResource(currentUser, resIdList, sysRole.getId(), sysRole.getOperationType());
        }

        return this.selectRoleById(sysRole.getId());
    }

    /**
     * 根据ID查询单个实体
     *
     * @param id
     * @return
     */
    @Cacheable(value = SystemCacheUtil.CACHE_ROLE, key = "#id")
    @Override
    public SysRole selectRoleById(String id) {
        SysRole sysRole = null;

        Example example = new Example(SysRole.class);
        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("id", id);
        criteria.andEqualTo("delFlag", Constant.DEL_FLAG_NORMAL);

        List<SysRole> roleList = this.listByExample(example);

        if (roleList.size() == 1) {
            sysRole = roleList.get(0);
        }

        if (sysRole != null) {
            List<SysResource> resourceListList = this.resourceService.listResourceByRoleId(id);
            sysRole.setResourceList(resourceListList);
        }

        return sysRole;
    }

    /**
     * 根据id删除单个实体(软删除）
     *
     * @param currentUser
     * @param id
     * @return
     */
    @Caching(evict = {
            @CacheEvict(value = SystemCacheUtil.CACHE_ROLE, key = "#id"),
            @CacheEvict(value = SystemCacheUtil.CACHE_USER, allEntries = true),
            @CacheEvict(value = SystemCacheUtil.CACHE_RESOURCE, key = "T(com.geovis.core.util.SystemCacheUtil).SYS_ROLE_ID + #id")
    })
    @Override
    @Transactional
    public SysRole removeRoleById(SysUser currentUser, String id) {
        SysRole sysRole = new SysRole();

        sysRole.setId(id);
        sysRole.setDelFlag(Constant.DEL_FLAG_DELETE);
        BeanUtil.setUpdateUser(currentUser, sysRole);
        SysRole role = this.commonSave(currentUser, sysRole);

        //需要删除角色用户关系表（软删除）
        SysUserRole sysUserRole = new SysUserRole();
        sysUserRole.setDelFlag(Constant.DEL_FLAG_DELETE);

        Example example = new Example(SysUserRole.class);
        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("roleId", id);
        criteria.andEqualTo("delFlag", Constant.DEL_FLAG_NORMAL);
        this.sysUserRoleMapper.updateByExampleSelective(sysUserRole, example);

        //删除角色资源关系
        SysRoleResource sysRoleResource = new SysRoleResource();
        sysRoleResource.setDelFlag(Constant.DEL_FLAG_DELETE);

        Example example2 = new Example(SysRoleResource.class);
        Example.Criteria criteria2 = example2.createCriteria();

        criteria2.andEqualTo("roleId", id);
        criteria2.andEqualTo("delFlag", Constant.DEL_FLAG_NORMAL);
        this.sysRoleResourceMapper.updateByExampleSelective(sysRoleResource, example2);
        return role;
    }

//    /**
//     * 前端只传勾选的资源，后台将勾选的数据对应的父层级数据保存
//     * @param resourceIdList
//     * @param roleId
//     */
//    @Caching(evict = {
//            @CacheEvict(value = SystemCacheUtil.CACHE_RESOURCE, key = "T(com.geovis.core.util.SystemCacheUtil).SYS_ROLE_ID + #roleId"),
//            @CacheEvict(value = SystemCacheUtil.CACHE_USER, allEntries = true)
//    })
//    @Override
//    public void setResource(SysUser currentUser, List<String> resourceIdList , String roleId){
//        //1.根据资源id查询父层级ids
////        List<String> parentList = this.sysResourceMapper.listResParentIdUp(resourceIdList);
////
//        Set<String> allResIdList = new HashSet<String>();
////        for(String str : parentList){
////            if(str == null || "".equals(str)){
////                continue;
////            }
////            String[]  arr = str.split(",");
////            for(String s : arr){
////                if("0".equals(s)){
////                    continue;
////                }
////                allResIdList.add(s);
////            }
////        }
//        for(String str : resourceIdList){
//            if(str == null || "".equals(str)){
//                continue;
//            }
//            if("0".equals(str)){
//                continue;
//            }
//            allResIdList.add(str);
//        }
//
//        //2.处理角色资源（新增或删除）
//        Example example = new Example(SysRoleResource.class);
//        Example.Criteria criteria = example.createCriteria();
//
//        criteria.andEqualTo("roleId", roleId);
//        criteria.andEqualTo("delFlag", Constant.DEL_FLAG_NORMAL);
//
//        List<SysRoleResource> currRoleResList = this.sysRoleResourceMapper.selectByExample(example);
//
//        Set<String> currResIdList = new HashSet<>();
//
//        if(currRoleResList != null && currRoleResList.size() > 0){
//            for(SysRoleResource roleResource : currRoleResList){
//                currResIdList.add(roleResource.getResourceId());
//            }
//        }
//
//        Map<String,Set<String>> map = BeanUtil.diffList(allResIdList, currResIdList);
//
//        Set<String> addRoleResIdList = map.get(BeanUtil.ADD);
//        Set<String> removeRoleResList = map.get(BeanUtil.REMOVE);
//
//        //删除数据
//        if(removeRoleResList != null && removeRoleResList.size() > 0){
//            for(String removeResId : removeRoleResList){
//                SysRoleResource reoleRes = new SysRoleResource();
//                BeanUtil.setUpdateUser(currentUser, reoleRes);
//                reoleRes.setDelFlag(Constant.DEL_FLAG_DELETE);
//
//                Example remExample = new Example(SysRoleResource.class);
//                Example.Criteria remCriteria = remExample.createCriteria();
//                //获取删除标记为正常的记录
//                remCriteria.andEqualTo("roleId", roleId);
//                remCriteria.andEqualTo("resourceId", removeResId);
//                remCriteria.andNotEqualTo("delFlag", Constant.DEL_FLAG_DELETE);
//                this.sysRoleResourceMapper.updateByExampleSelective(reoleRes, remExample);
//            }
//        }
//
//        //添加资源
//        if(addRoleResIdList != null && addRoleResIdList.size() > 0){
//            for(String resId : addRoleResIdList){
//                SysRoleResource roleRes = new SysRoleResource();
//                roleRes.setRoleId(roleId);
//                roleRes.setResourceId(resId);
//
//                BeanUtil.setCreateUser(currentUser, roleRes);
//                BeanUtil.setUpdateUser(currentUser, roleRes);
//
//                this.sysRoleResourceMapper.insert(roleRes);
//            }
//        }
//    }


    @Override
    public void setResource(SysUser currentUser, List<ResourcePower> resourceIdList, String roleId, String operationType) {

        Set<ResourcePower> allResIdList = new HashSet<ResourcePower>();

        for (ResourcePower resourcePower : resourceIdList) {
            if (resourcePower == null) {
                continue;
            }
            allResIdList.add(resourcePower);
        }

        //2.处理角色资源（新增或删除）
//        Example example = new Example(SysRoleResource.class);
//        Example.Criteria criteria = example.createCriteria();
//
//        criteria.andEqualTo("roleId", roleId);
//        criteria.andEqualTo("delFlag", Constant.DEL_FLAG_NORMAL);
//        List<SysRoleResource> currRoleResList = this.sysRoleResourceMapper.selectByExample(example);
        List<SysRoleResource> currRoleResList = null;
        if (operationType.equals(ResourceType.FOLDER.getValue()) || operationType.equals(ResourceType.SERVICE.getValue())) {
            currRoleResList = this.sysRoleResourceMapper.selectSysRoleResourceByFolder(roleId, Constant.DEL_FLAG_NORMAL, ResourceType.FOLDER.getValue());
        } else {
            currRoleResList = this.sysRoleResourceMapper.selectSysRoleResourceByOther(roleId, Constant.DEL_FLAG_NORMAL, ResourceType.FOLDER.getValue());

        }

        Set<ResourcePower> currResIdList = new HashSet<ResourcePower>();

        if (currRoleResList != null && currRoleResList.size() > 0) {
            for (SysRoleResource roleResource : currRoleResList) {
                ResourcePower currentResourcePower = new ResourcePower();
                currentResourcePower.setResourceId(roleResource.getResourceId());
                String datapower = roleResource.getDatapower() == null ? "00000" : roleResource.getDatapower();
                currentResourcePower.setPowerId(datapower);
                String servicepower = roleResource.getServicepower() == null ? "00000" : roleResource.getServicepower();
                currentResourcePower.setDataserviceId(servicepower);
                currResIdList.add(currentResourcePower);
            }
        }

       Map<String, Set<ResourcePower>> map = BeanUtil.diffListObject(allResIdList, currResIdList);
        if (map == null) {
            return;
        }
        Set<ResourcePower> addRoleResIdList = map.get(BeanUtil.ADD);
        Set<ResourcePower> removeRoleResList = map.get(BeanUtil.REMOVE);

        //删除数据
        if (removeRoleResList != null && removeRoleResList.size() > 0) {
            //过滤删除的数据
            for (ResourcePower removeResPower : removeRoleResList) {

                SysRoleResource reoleRes = new SysRoleResource();
                BeanUtil.setUpdateUser(currentUser, reoleRes);
                reoleRes.setDelFlag(Constant.DEL_FLAG_DELETE);

                Example remExample = new Example(SysRoleResource.class);

                Example.Criteria remCriteria = remExample.createCriteria();

                //获取删除标记为正常的记录
                remCriteria.andEqualTo("roleId", roleId);
                remCriteria.andEqualTo("resourceId", removeResPower.getResourceId());
                remCriteria.andEqualTo("datapower", removeResPower.getPowerId());
                remCriteria.andEqualTo("servicepower", removeResPower.getDataserviceId());
                remCriteria.andNotEqualTo("delFlag", Constant.DEL_FLAG_DELETE);

                this.sysRoleResourceMapper.updateByExampleSelective(reoleRes, remExample);
            }
        }

        //添加资源
        if (addRoleResIdList != null && addRoleResIdList.size() > 0) {
            for (ResourcePower resPower : addRoleResIdList) {
                SysRoleResource roleRes = new SysRoleResource();
                roleRes.setRoleId(roleId);
                roleRes.setResourceId(resPower.getResourceId());
                roleRes.setDatapower(resPower.getPowerId());
                roleRes.setServicepower(resPower.getDataserviceId());

                BeanUtil.setCreateUser(currentUser, roleRes);
                BeanUtil.setUpdateUser(currentUser, roleRes);

                this.sysRoleResourceMapper.insert(roleRes);
            }
        }
    }

    /**
     * 为角色设置用户
     *
     * @param currentUser
     * @param userIdList
     * @param roleId
     */
    @Override
    public void setUser(SysUser currentUser, List<String> userIdList, String roleId) {
        Set<String> compareUserIdList = new HashSet<String>();
        if (userIdList != null && userIdList.size() > 0) {
            for (String userId : userIdList) {
                compareUserIdList.add(userId);
            }
        }

        //获取该角色的用户
        Example example = new Example(SysUserRole.class);
        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("roleId", roleId);
        criteria.andEqualTo("delFlag", Constant.DEL_FLAG_NORMAL);

        List<SysUserRole> userRoleList = this.sysUserRoleMapper.selectByExample(example);

        Set<String> currUserIdList = new HashSet<String>();
        if (userRoleList != null && userRoleList.size() > 0) {
            for (SysUserRole userRole : userRoleList) {
                currUserIdList.add(userRole.getUserId());
            }
        }

        Map<String, Set<String>> map = BeanUtil.diffList(compareUserIdList, currUserIdList);

        if (map == null || map.size() == 0) return;

        Set<String> addUserIdList = map.get(BeanUtil.ADD);
        Set<String> removeUserIdList = map.get(BeanUtil.REMOVE);

        //删除数据
        if (removeUserIdList != null && removeUserIdList.size() > 0) {
            for (String removeUserId : removeUserIdList) {
                SysUserRole userRole = new SysUserRole();
                BeanUtil.setUpdateUser(currentUser, userRole);
                userRole.setDelFlag(Constant.DEL_FLAG_DELETE);

                Example remExample = new Example(SysUserRole.class);
                Example.Criteria remCriteria = remExample.createCriteria();
                //获取删除标记为正常的记录
                remCriteria.andEqualTo("userId", removeUserId);
                remCriteria.andEqualTo("roleId", roleId);
                remCriteria.andNotEqualTo("delFlag", Constant.DEL_FLAG_DELETE);
                this.sysUserRoleMapper.updateByExampleSelective(userRole, remExample);
            }
        }

        //添加数据
        if (addUserIdList != null && addUserIdList.size() > 0) {
            for (String userId : addUserIdList) {
                SysUserRole userRole = new SysUserRole();
                userRole.setRoleId(roleId);
                userRole.setUserId(userId);

                BeanUtil.setCreateUser(currentUser, userRole);
                BeanUtil.setUpdateUser(currentUser, userRole);

                this.sysUserRoleMapper.insert(userRole);
            }
        }
    }
}
