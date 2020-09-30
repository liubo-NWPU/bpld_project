package com.geovis.web.controller.system;

import com.geovis.core.util.BeanUtil;
import com.geovis.core.util.ResponseUtil;
import com.geovis.core.util.StringUtil;
import com.geovis.core.util.SystemCacheUtil;
import com.geovis.web.domain.system.SysResource;
import com.geovis.web.domain.system.SysRole;
import com.geovis.web.domain.system.SysUser;
import com.geovis.web.service.system.ResourceService;
import com.geovis.web.service.system.RoleService;
import com.geovis.web.service.system.UserService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(value = "RoleController", description = "角色管理接口")
@RestController
@SuppressWarnings("all")
@RequestMapping("/system")
public class RoleController {
    private static final Logger logger = LoggerFactory.getLogger(ResourceController.class);
    @Autowired
    private RoleService roleService;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private UserService userService;

    /**
     * 查询方法
     *
     * @param sysRole
     * @return
     */
    @ApiOperation(value = "获取所有角色", notes = "获取所有角色")
    @PostMapping("/roles")
    @RequiresPermissions(value = {"system:role:list", "admin"}, logical = Logical.OR)
    public ResponseEntity listAll(@ApiParam @RequestBody SysRole sysRole) {
        try {
            List<SysRole> roleList = this.roleService.listAll(sysRole);
            return ResponseUtil.success(new PageInfo<SysRole>(roleList));
        } catch (Exception e) {
            logger.error("[listAll Exception]:" + e.getMessage());
            return ResponseUtil.failure(e.getMessage());
        }
    }

    /**
     * 添加单个实体（id必须为空）
     *
     * @param request
     * @param sysRole
     * @return
     */
    @ApiOperation(value = "保存角色", notes = "保存角色")
    @PostMapping("/saveRole")
    @RequiresPermissions(value = {"system:role:add", "admin"}, logical = Logical.OR)
    public ResponseEntity save(HttpServletRequest request,
                               @ApiParam @RequestBody SysRole sysRole) {
        try {
            if (sysRole.getId() != null) {
                return ResponseUtil.failure("参数错误！");
            }

            //设置权限组：1-系统管理组；2-内容管理组；3-数据管理组
            sysRole.setRoleType("3");

            //设置创建信息
            BeanUtil.setCreateUser(request, sysRole);

            //设置更新信息
            BeanUtil.setUpdateUser(request, sysRole);

            this.roleService.commonSave(SystemCacheUtil.getUserByRequest(request), sysRole);

            return ResponseUtil.success(sysRole);
        } catch (Exception e) {
            logger.error("[save Exception]:" + e.getMessage());
            return ResponseUtil.failure(e.getMessage());
        }
    }

    /**
     * 查看单个实体
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "查看角色", notes = "查看角色")
    @GetMapping("/roles/{id}")
    @RequiresPermissions(value = {"system:role:view", "admin"}, logical = Logical.OR)
    public ResponseEntity view(@PathVariable String id) {
        try {
            SysRole sysRole = this.roleService.selectRoleById(id);
            return ResponseUtil.success(sysRole);
        } catch (Exception e) {
            logger.error("[view Exception]:" + e.getMessage());
            return ResponseUtil.failure(e.getMessage());
        }
    }

    /**
     * 编辑单个实体（id不可为空）
     *
     * @param request
     * @param sysRole
     * @return
     */
    @ApiOperation(value = "更新角色", notes = "更新角色")
    @PutMapping("/roles")
    @RequiresPermissions(value = {"system:role:edit", "admin"}, logical = Logical.OR)
    public ResponseEntity updateRole(HttpServletRequest request,
                                     @ApiParam @RequestBody SysRole sysRole) {
        try {
            //设置更新信息
            BeanUtil.setUpdateUser(request, sysRole);

            this.roleService.commonSave(SystemCacheUtil.getUserByRequest(request), sysRole);

            return ResponseUtil.success(sysRole);
        } catch (Exception e) {
            logger.error("[updateRole Exception]:" + e.getMessage());
            return ResponseUtil.failure(e.getMessage());
        }
    }

    /**
     * 软删除单个实体（根据id删除）
     *
     * @param request
     * @param id
     * @return
     */
    @ApiOperation(value = "删除角色", notes = "删除角色")
    @DeleteMapping("/roles/{id}")
    @RequiresPermissions(value = {"system:role:remove", "admin"}, logical = Logical.OR)
    public ResponseEntity remove(HttpServletRequest request, @PathVariable String id) {
        try {
            SysUser currentUser = SystemCacheUtil.getUserByRequest(request);

            String canDelete = "false";
            try {
                canDelete = this.roleService.selectRoleById(id).getCandelete();
            } catch (Exception ex) {
                canDelete = "false";
            }

            if (canDelete == "false") {
                return ResponseUtil.failure("此角色被系统保护，禁止删除。");
            }

            SysRole result = this.roleService.removeRoleById(currentUser, id);

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
     * 获取某个角色的资源
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "根据角色获取相应资源", notes = "根据角色获取相应资源")
    @GetMapping("/roles/resource/{id}")
    public ResponseEntity listRoleResource(@PathVariable String id) {
        try {
            List<SysResource> listResource = this.resourceService.listResourceByRoleId(id);
            return ResponseUtil.success(listResource);
        } catch (Exception e) {
            logger.error("[listRoleResource Exception]:" + e.getMessage());
            return ResponseUtil.failure(e.getMessage());
        }
    }

    /**
     * 给角色设置资源
     *
     * @param request
     * @param sysRole
     * @return
     */
    @ApiOperation(value = "给角色设置资源", notes = "给角色设置资源")
    @PostMapping("/roles/setResources")
    @RequiresPermissions(value = {"system:role:setResource", "admin"}, logical = Logical.OR)
    public ResponseEntity setResource(HttpServletRequest request, @RequestBody SysRole sysRole) {
        try {
            if (StringUtil.isEmpty(sysRole.getId())) {
                return ResponseUtil.failure("参数错误！");
            }
            this.roleService.setResource(SystemCacheUtil.getUserByRequest(request), sysRole.getResourceIdList(), sysRole.getId(), sysRole.getOperationType());
            return ResponseUtil.success();
        } catch (Exception e) {
            logger.error("[setResource Exception]:" + e.getMessage());
            return ResponseUtil.failure(e.getMessage());
        }
    }

    /**
     * 为角色设置用户
     *
     * @param request
     * @param sysRole
     * @return
     */
    @ApiOperation(value = "为角色设置用户", notes = "为角色设置用户")
    @PostMapping("/roles/setUser")
    @RequiresPermissions(value = {"system:role:setUser", "admin"}, logical = Logical.OR)
    public ResponseEntity setUser(HttpServletRequest request, @RequestBody SysRole sysRole) {
        try {
            if (StringUtil.isEmpty(sysRole.getId())) {
                return ResponseUtil.failure("参数错误！");
            }
            this.roleService.setUser(SystemCacheUtil.getUserByRequest(request), sysRole.getUserIdList(), sysRole.getId());
            return ResponseUtil.success();
        } catch (Exception e) {
            logger.error("[setUser Exception]:" + e.getMessage());
            return ResponseUtil.failure(e.getMessage());

        }
    }

    /**
     * 根据角色获取用户列表
     *
     * @param roleId
     * @return
     */
    @ApiOperation(value = "根据角色获取用户列表", notes = "根据角色获取用户列表")
    @GetMapping("/roles/listuser/{roleId}")
    public ResponseEntity listuser(@ApiParam @PathVariable String roleId) {
        try {
            List<SysUser> listUser = this.userService.listUserByRoleId(roleId);
            return ResponseUtil.success(listUser);
        } catch (Exception e) {
            logger.error("[listuser Exception]:" + e.getMessage());
            return ResponseUtil.failure(e.getMessage());
        }
    }

    /**
     * 根据roleId获取所有资源，标识是否选中状态
     *
     * @param roleId
     * @return
     */
    @ApiOperation(value = "根据roleId获取资源，标识是否选中状态", notes = "根据roleId获取所有资源，标识是否选中状态")
    @GetMapping("/roles/listRes/{roleId}")
    public ResponseEntity listResourceByRoleId(@PathVariable String roleId) {
        try {
            List<SysResource> listResource = this.resourceService.listAllResourceByRoleId(roleId);
            return ResponseUtil.success(listResource);
        } catch (Exception e) {
            logger.error("[listResourceByRoleId Exception]:" + e.getMessage());
            return ResponseUtil.failure(e.getMessage());
        }
    }
}
