package com.geovis.web.service.impl.system;

import com.geovis.core.constant.Constant;
import com.geovis.core.util.BeanUtil;
import com.geovis.core.util.StringUtil;
import com.geovis.core.util.SystemCacheUtil;
import com.geovis.web.base.service.impl.AbstractService;
import com.geovis.web.dao.system.mapper.SysResourceMapper;
import com.geovis.web.dao.system.mapper.SysRoleResourceMapper;
import com.geovis.web.domain.enums.ResourceType;
import com.geovis.web.domain.system.SysResource;
import com.geovis.web.domain.system.SysRoleResource;
import com.geovis.web.domain.system.SysUser;
import com.geovis.web.service.system.ResourceService;
import com.github.pagehelper.PageHelper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("all")
@Service
public class ResourceServiceImpl extends AbstractService<SysResource> implements ResourceService {

    @Autowired
    private SysResourceMapper sysResourceMapper;

    @Autowired
    private SysRoleResourceMapper sysRoleResourceMapper;

    /**
     * 查询方法，支持模糊查询
     *
     * @param sysResource
     * @return
     */
    public List<SysResource> listAll(SysResource sysResource) {
        if (sysResource.getPage() != null && sysResource.getRows() != null) {
            PageHelper.startPage(sysResource.getPage(), sysResource.getRows());
        }

        Example example = new Example(SysResource.class);
        Example.Criteria criteria = example.createCriteria();

        if (sysResource != null && sysResource.getName() != null) {
            criteria.andLike("name", "%" + sysResource.getName() + "%");
        }

        if (sysResource != null && sysResource.getId() != null) {
            criteria.andEqualTo("id", sysResource.getId());
        }

        if (sysResource != null && sysResource.getParentId() != null) {
            criteria.andEqualTo("parentId", sysResource.getParentId());
        }

        if (sysResource != null && sysResource.getType() != null) {
            criteria.andEqualTo("type", sysResource.getType());
        }
        //TODO other search params

        //获取删除标记为正常的记录
        criteria.andEqualTo("delFlag", sysResource.getDelFlag());

        return this.sysResourceMapper.selectByExample(example);
    }

    /**
     * 将修改和添加封装在一起，便于缓存处理
     * （更新缓存）
     *
     * @param currentUser
     * @param sysResource
     * @return
     */
    @Caching(evict = {
            @CacheEvict(value = SystemCacheUtil.CACHE_RESOURCE, allEntries = true),
            @CacheEvict(value = SystemCacheUtil.CACHE_USER, allEntries = true),
            @CacheEvict(value = SystemCacheUtil.CACHE_ROLE, allEntries = true)
    })
    @CachePut(value = SystemCacheUtil.CACHE_RESOURCE, key = "#sysResource.id")
    @Override
    @Transactional
    public SysResource commonSave(SysUser currentUser, SysResource sysResource) {
        ResourceType type = sysResource.getType();
        String typeValue = type.getValue();
        String del_flag = sysResource.getDelFlag();
        //对于数据资源特殊处理，以便于同数据管理服务的表数据对应上
        if (typeValue.equals("FOLDER")) {

            if (del_flag != null && del_flag.equalsIgnoreCase(Constant.DEL_FLAG_DELETE)) {
                this.updateNotNull(sysResource);
            } else {
               String parentID= sysResource.getParentId();
               if(parentID!=null&&parentID.length()>0){
               parentID=(parentID.equalsIgnoreCase("-1"))?"0":parentID;
               sysResource.setParentId(parentID);
               }
                this.save(sysResource);
            }
        } else {
            if (sysResource.getId() == null) {
                if (StringUtil.isEmpty(sysResource.getParentId())) {
                    sysResource.setParentId("0");
                }
                this.save(sysResource);
            } else {
                this.updateNotNull(sysResource);
            }
        }


        return this.getResourceById(sysResource.getId());
    }

    /**
     * 查询单个资源
     *
     * @param id
     * @return
     */
    @Cacheable(value = SystemCacheUtil.CACHE_RESOURCE, key = "#id")
    @Override
    public SysResource getResourceById(String id) {
        SysResource sysResource = null;

        Example example = new Example(SysResource.class);
        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("id", id);
        //获取删除标记为正常的记录
        criteria.andEqualTo("delFlag", Constant.DEL_FLAG_NORMAL);

        List<SysResource> resList = this.sysResourceMapper.selectByExample(example);
        if (resList.size() == 1) {
            sysResource = resList.get(0);
        }

        return sysResource;
    }

    /**
     * 软删除资源（清除缓存）
     *
     * @param currentUser
     * @param id
     * @return
     */
    @Caching(evict = {
            @CacheEvict(value = SystemCacheUtil.CACHE_RESOURCE, allEntries = true),
            @CacheEvict(value = SystemCacheUtil.CACHE_USER, allEntries = true),
            @CacheEvict(value = SystemCacheUtil.CACHE_ROLE, allEntries = true)
    })
    @Override
    @Transactional
    public SysResource removeResourceById(SysUser currentUser, String id,String type) {
        SysResource sysResource = new SysResource();
        sysResource.setId(id);
        sysResource.setType(ResourceType.getResourceType(type));
        sysResource.setDelFlag(Constant.DEL_FLAG_DELETE);
        BeanUtil.setUpdateUser(currentUser, sysResource);
        SysResource resource = this.commonSave(currentUser, sysResource);

        //需要删除角色资源关系（软删除）
        SysRoleResource sysRoleResource = new SysRoleResource();
        sysRoleResource.setDelFlag(Constant.DEL_FLAG_DELETE);

        Example example = new Example(SysRoleResource.class);
        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("resourceId", id);
        criteria.andEqualTo("delFlag", Constant.DEL_FLAG_NORMAL);
        this.sysRoleResourceMapper.updateByExampleSelective(sysRoleResource, example);
        return resource;
    }

    /**
     * 根据userId获取用户资源
     *
     * @param userId
     * @return
     */
    @Cacheable(cacheNames = SystemCacheUtil.CACHE_RESOURCE, key = "T(com.geovis.core.util.SystemCacheUtil).SYS_USER_ID + #userId")
    @Override
    public List<SysResource> listResourceByUserId(String userId) {

        return this.sysResourceMapper.listResourceByUserId(userId);
    }


    @Cacheable(cacheNames = SystemCacheUtil.CACHE_RESOURCE, key = "T(com.geovis.core.util.SystemCacheUtil).SYS_POWER_ID + #userId")
    @Override
    public List<SysResource> listDataPowerByUserId(String userId) {
        return this.sysResourceMapper.listResourceByUserId(userId);
    }

    /**
     * 根据roleId获取角色资源
     *
     * @param roleId
     * @return
     */
    @Cacheable(value = SystemCacheUtil.CACHE_RESOURCE, key = "T(com.geovis.core.util.SystemCacheUtil).SYS_ROLE_ID + #roleId")
    @Override
    public List<SysResource> listResourceByRoleId(String roleId) {
        return this.sysResourceMapper.listResourceByRoleId(roleId);
    }

    /**
     * 根据roleId获取所有资源，标识是否选中状态
     *
     * @param roleId
     * @return
     */
    @Override
    public List<SysResource> listAllResourceByRoleId(String roleId) {
        return this.sysResourceMapper.listAllResourceByRoleId(roleId);
    }

    /**
     * 获取用户的菜单
     *
     * @param userId
     * @return
     */
    @Cacheable(cacheNames = SystemCacheUtil.CACHE_RESOURCE, key = "T(com.geovis.core.util.SystemCacheUtil).SYS_USER_MENU + #userId")
    @Override
    public List<SysResource> listMenuTreeByUserId(String userId) {
        List<SysResource> allResList = this.listResourceByUserId(userId);
        List<SysResource> menuList = new ArrayList<SysResource>();
        for (SysResource resource : allResList) {
            if (ResourceType.MENU.equals(resource.getType())) {
                menuList.add(resource);
            }
        }

        List<SysResource> resultList = null;
        return this.encaseResourceByTree(menuList, resultList, "0");
    }


    @Cacheable(cacheNames = SystemCacheUtil.CACHE_RESOURCE, key = "T(com.geovis.core.util.SystemCacheUtil).SYS_USER_FOLDER + #userId")
    @Override
    public List<SysResource> listFolderTreeByUserId(String userId) {
        List<SysResource> allResList = this.listDataPowerByUserId(userId);
        List<SysResource> folderList = new ArrayList<SysResource>();
        for (SysResource resource : allResList) {
            if (ResourceType.FOLDER.equals(resource.getType())) {
                if (folderList.size() == 0) {
                    folderList.add(resource);
                }
                for (int i = 0; i < folderList.size(); i++) {
                    if (resource.getId().equals(folderList.get(i).getId())) {
                        if (!StringUtil.isEmpty(resource.getDatapower()) && !StringUtil.isEmpty(folderList.get(i).getDatapower())) {
                            Integer integer = Integer.parseInt(folderList.get(i).getDatapower()) + Integer.parseInt(resource.getDatapower());
                            String datapower = new DecimalFormat("00000").format(integer).replaceAll("2", "1");
                            folderList.get(i).setDatapower(datapower);
                        }
                        break;
                    }
                    if (i == folderList.size() - 1) {
                        folderList.add(resource);
                    }
                }

            }
        }

        List<SysResource> resultList = null;
        return this.encaseResourceByTree(folderList, resultList, "0");
    }


    @Cacheable(cacheNames = SystemCacheUtil.CACHE_RESOURCE, key = "T(com.geovis.core.util.SystemCacheUtil).SYS_USER_SERVICE_FOLDER + #userId")
    @Override
    public List<SysResource> listServiceFolderTreeByUserId(String userId) {
        List<SysResource> allResList = this.listDataPowerByUserId(userId);
        List<SysResource> folderList = new ArrayList<SysResource>();
        for (SysResource resource : allResList) {
            if (ResourceType.FOLDER.equals(resource.getType())) {
                if (folderList.size() == 0) {
                    folderList.add(resource);
                }
                for (int i = 0; i < folderList.size(); i++) {
                    if (resource.getId().equals(folderList.get(i).getId())) {
                        if (!StringUtil.isEmpty(resource.getServicepower()) && !StringUtil.isEmpty(folderList.get(i).getServicepower())) {
                            Integer integer = Integer.parseInt(folderList.get(i).getServicepower()) + Integer.parseInt(resource.getServicepower());
                            String servicepower = new DecimalFormat("00000").format(integer).replaceAll("2", "1");
                            folderList.get(i).setServicepower(servicepower);
                        }
                        break;
                    }
                    if (i == folderList.size() - 1) {
                        folderList.add(resource);
                    }
                }

            }
        }

        List<SysResource> resultList = null;
        return this.encaseServiceByTree(folderList, resultList, "0");
    }


    /**
     * 获取用户的权限
     *
     * @param userId
     * @return
     */
    @Cacheable(cacheNames = SystemCacheUtil.CACHE_RESOURCE, key = "T(com.geovis.core.util.SystemCacheUtil).SYS_USER_PERMISSION + #userId")
    @Override
    public List<SysResource> listPermissionByUserId(String userId) {
        List<SysResource> allResList = this.listResourceByUserId(userId);
        List<SysResource> permissionList = new ArrayList<SysResource>();
        for (SysResource resource : allResList) {
            if (ResourceType.BUTTON.equals(resource.getType())) {
                permissionList.add(resource);
            }
        }

        return permissionList;
    }

    /**
     * 获取所有菜单的路径名称
     *
     * @return
     */
    @Cacheable(cacheNames = SystemCacheUtil.CACHE_RESOURCE, key = "T(com.geovis.core.util.SystemCacheUtil).SYS_MENU_MAP")
    @Override
    public Map<String, String> listAllMenuTree() {
        SysResource sysResource = new SysResource();
        sysResource.setRows(0);
        List<SysResource> menuList = this.listAll(sysResource);

        List<SysResource> resultList = null;

        List<SysResource> listTree = this.encaseResourceByTree(menuList, resultList, "0");

        Map<String, String> menuMap = null;

        menuMap = this.encaseMenuPath(listTree, null, menuMap);

        return menuMap;
    }

//-----------------------------------------------------工具方法-----------------------------------------------------

    /**
     * 用于将资源封装成树桩结构
     *
     * @param resList    需要封装的源数据
     * @param resultList 返回的结果
     * @param parentId   从哪个父id开始封装
     * @return
     */
    public List<SysResource> encaseResourceByTree(List<SysResource> resList, List<SysResource> resultList, String parentId) {

        if (parentId == null || "".equals(parentId)) {
            this.encaseResourceByTree(resList, resultList, "0");
        } else {
            for (SysResource resource : resList) {
                if (parentId.equals(resource.getParentId())) {
                    if (resultList == null) {
                        resultList = new ArrayList<SysResource>();
                    }
                    resultList.add(resource);
                    if (this.judgeExistsChild(resList, resource.getId())) {
                        if (resource.getChilds() == null) {
                            resource.setChilds(new ArrayList<SysResource>());
                        }
                        this.encaseResourceByTree(resList, resource.getChilds(), resource.getId());
                    }
                }
            }
        }

        return resultList;
    }

    /**
     * 用于将资源封装成树桩结构
     *
     * @param resList    需要封装的源数据
     * @param resultList 返回的结果
     * @param parentId   从哪个父id开始封装
     * @return
     */
    public List<SysResource> encaseServiceByTree(List<SysResource> resList, List<SysResource> resultList, String parentId) {

        if (parentId == null || "".equals(parentId)) {
            this.encaseServiceByTree(resList, resultList, "0");
        } else {
            for (SysResource resource : resList) {
                if (parentId.equals(resource.getParentId())) {
                    if (resultList == null) {
                        resultList = new ArrayList<SysResource>();
                    }
                    resultList.add(resource);
                    if (this.judgeExistsChild(resList, resource.getId())) {
                        if (resource.getChilds() == null) {
                            resource.setChilds(new ArrayList<SysResource>());
                        }
                        this.encaseServiceByTree(resList, resource.getChilds(), resource.getId());
                    }
                }
            }
        }

        return resultList;
    }

    /**
     * 用于将资源封装成树桩结构
     *
     * @param resList    需要封装的源数据
     * @param resultList 返回的结果
     * @param parentId   从哪个父id开始封装
     * @return
     */
    public List<SysResource> encaseDataPowerByTree(List<SysResource> resList, List<SysResource> resultList, String parentId) {

        if (parentId == null || "".equals(parentId)) {
            this.encaseResourceByTree(resList, resultList, "0");
        } else {
            for (SysResource resource : resList) {
                if (parentId.equals(resource.getParentId())) {
                    if (resultList == null) {
                        resultList = new ArrayList<SysResource>();
                    }
                    resultList.add(resource);
                    if (this.judgeExistsChild(resList, resource.getId())) {
                        if (resource.getChilds() == null) {
                            resource.setChilds(new ArrayList<SysResource>());
                        }
                        this.encaseResourceByTree(resList, resource.getChilds(), resource.getId());
                    }
                }
            }
        }

        return resultList;
    }


    //判断是否存在子记录
    public boolean judgeExistsChild(List<SysResource> resList, String parentId) {
        for (SysResource resource : resList) {
            if (parentId.equals(resource.getParentId())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 封装菜单路径名称
     *
     * @param listTree
     * @param namePathList
     * @param menuMap
     * @return
     */
    public Map<String, String> encaseMenuPath(List<SysResource> listTree, List<String> namePathList, Map<String, String> menuMap) {

        if (namePathList == null) {
            namePathList = new ArrayList<>();
        }

        if (menuMap == null) {
            menuMap = new HashMap<String, String>();
        }

        for (SysResource sysRes : listTree) {
            if (Constant.DEL_FLAG_DELETE.equals(sysRes.getDelFlag())) {
                continue;
            }
            if ("0".equals(sysRes.getParentId())) {
                namePathList = new ArrayList<>();
            }

            String namePath = "";

            List<String> cloneNamePathList = new ArrayList<>();
            for (String str : namePathList) {
                cloneNamePathList.add(str);
            }

            cloneNamePathList.add(sysRes.getName());

            namePath = StringUtil.join(cloneNamePathList, "-");

            if (StringUtil.isNotEmpty(sysRes.getUrl())) {
                menuMap.put(sysRes.getUrl(), namePath);
            } else if (StringUtil.isNotEmpty(sysRes.getPermission())) {
                for (String p : StringUtil.split(sysRes.getPermission())) {
                    menuMap.put(p, namePath);
                }
            }

            if (CollectionUtils.isNotEmpty(sysRes.getChilds())) {
                this.encaseMenuPath(sysRes.getChilds(), cloneNamePathList, menuMap);
            }
        }

        return menuMap;
    }

    //   @Cacheable(cacheNames = SystemCacheUtil.CACHE_RESOURCE, key = "T(com.geovis.core.util.SystemCacheUtil).SYS_USER_TYPE_ID + #userId")
    @Override
    public List<SysResource> listSourceByType(String userId, String type) {
        // TODO Auto-generated method stub
        return sysResourceMapper.listSourceByType(userId, type);
    }

    @Override
    public boolean deleteResourceById(String id) {
        try {
            sysRoleResourceMapper.deleteById(id);
            sysResourceMapper.deleteById(id);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }
}
