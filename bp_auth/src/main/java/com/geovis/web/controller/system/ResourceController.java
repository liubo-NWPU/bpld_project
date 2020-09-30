
package com.geovis.web.controller.system;

import com.alibaba.fastjson.JSONObject;
import com.geovis.core.util.BeanUtil;
import com.geovis.core.util.ResponseUtil;
import com.geovis.core.util.SystemCacheUtil;
import com.geovis.web.domain.enums.ResourceType;
import com.geovis.web.domain.system.SysResource;
import com.geovis.web.domain.system.SysUser;
import com.geovis.web.service.system.ResourceService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import liquibase.util.MD5Util;
import org.apache.ibatis.annotations.ResultType;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.PostUpdate;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(value = "ResourceController", description = "资源管理接口")
@RestController
@SuppressWarnings("all")
@RequestMapping("/system")
public class ResourceController {
    private static final Logger logger = LoggerFactory.getLogger(ResourceController.class);
    @Autowired
    private ResourceService resourceService;

    /**
     * 查询方法
     *
     * @param sysResource
     * @return
     */
    @ApiOperation(value = "获取所有资源", notes = "获取所有资源")
    @GetMapping("/resources")
    @RequiresPermissions(value = {"system:resource:list", "admin"}, logical = Logical.OR)
    public ResponseEntity listAll(@ApiParam SysResource sysResource) {
        try {
            List<SysResource> resourceList = this.resourceService.listAll(sysResource);
            return ResponseUtil.success(new PageInfo<SysResource>(resourceList));
        } catch (Exception e) {
            logger.error("[listAll Exception]:" + e.getMessage());
            return ResponseUtil.failure(e.getMessage());
        }
    }

    /**
     * 添加单个资源（id必须为空）
     *
     * @param request
     * @param sysResource
     * @return
     */
    @ApiOperation(value = "保存资源", notes = "保存资源")
    @PostMapping("/resources")
    @RequiresPermissions(value = {"system:resource:add", "admin"}, logical = Logical.OR)
    public ResponseEntity save(HttpServletRequest request,
                               @ApiParam @RequestBody SysResource sysResource) {
        try {
            System.out.println("接收到的sysResource参数：" + sysResource.toString());
            ResourceType type = sysResource.getType();
            String typeValue = type.getValue();
            if (!typeValue.equals("FOLDER")) {
                if (sysResource.getId() != null) {
                    return ResponseUtil.failure("参数错误！");
                }
            }

            //设置创建信息
            BeanUtil.setCreateUser(request, sysResource);

            //设置更新信息
            BeanUtil.setUpdateUser(request, sysResource);

            //TODO 暂定将levle设定为1，以后有需要再处理
            sysResource.setLevel(1);
            this.resourceService.commonSave(SystemCacheUtil.getUserByRequest(request), sysResource);

            return ResponseUtil.success(sysResource);
        } catch (Exception e) {
            logger.error("[save Exception]:" + e.getMessage());
            return ResponseUtil.failure(e.getMessage());
        }
    }

    /**
     * 查看单个资源
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "查看单个资源", notes = "查看单个资源")
    @GetMapping("/resources/{id}")
    @RequiresPermissions(value = {"system:role:setUser", "admin"}, logical = Logical.OR)
    public ResponseEntity view(@ApiParam @PathVariable String id) {
        try {
            SysResource sysResource = this.resourceService.getResourceById(id);
            return ResponseUtil.success(sysResource);
        } catch (Exception e) {
            logger.error("[view Exception]:" + e.getMessage());
            return ResponseUtil.failure(e.getMessage());
        }
    }

    /**
     * 编辑单个资源（id不可为空）
     *
     * @param request
     * @param sysResource
     * @return
     */
    @ApiOperation(value = "编辑单个资源", notes = "编辑单个资源")
    @PutMapping("/resources")
    @RequiresPermissions(value = {"system:resource:edit", "admin"}, logical = Logical.OR)
    public ResponseEntity update(HttpServletRequest request,
                                 @ApiParam @RequestBody SysResource sysResource) {
        try {
            if (sysResource.getId() == null || "".equals(sysResource.getId())) {
                return ResponseUtil.failure("参数错误！");
            }
            //设置更新信息
            BeanUtil.setUpdateUser(request, sysResource);
            sysResource = this.resourceService.commonSave(SystemCacheUtil.getUserByRequest(request), sysResource);
            return ResponseUtil.success(sysResource);
        } catch (Exception e) {
            logger.error("[update Exception]:" + e.getMessage());
            return ResponseUtil.failure(e.getMessage());
        }
    }

    /**
     * 软删除单个资源（根据id删除）
     *
     * @param request
     * @param id
     * @return
     */
    @ApiOperation(value = "删除单个资源", notes = "删除单个资源")
    @DeleteMapping("/resources/{id}/{type}")
    @RequiresPermissions(value = {"system:resource:remove", "admin"}, logical = Logical.OR)
    public ResponseEntity remove(HttpServletRequest request, @PathVariable String id, @PathVariable String type) {
        try {
            SysUser currentUser = SystemCacheUtil.getUserByRequest(request);
            SysResource result = this.resourceService.removeResourceById(currentUser, id, type);

            if (result != null) {
                return ResponseUtil.failure("删除失败！");
            }
            return ResponseUtil.success();
        } catch (Exception e) {
            logger.error("[remove Exception]:" + e.getMessage());
            return ResponseUtil.failure(e.getMessage());
        }
    }

    /**
     * 软删除单个资源（根据id删除）
     *
     * @param request
     * @param id
     * @return
     */
    @ApiOperation(value = "删除单个文件夹资源", notes = "删除单个文件夹资源")
    @PostMapping("/resources/deleteFolder")
    public ResponseEntity remove1(HttpServletRequest request, @RequestBody String param) {
        String id = JSONObject.parseObject(param).get("id").toString();
        boolean b = resourceService.deleteResourceById(id);
        if (b) {
            return ResponseUtil.success();
        } else {
            logger.error("[deleteException]:");
            return ResponseUtil.failure("deleteException");
        }
    }

}
