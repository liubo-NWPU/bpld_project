
package com.geovis.web.controller.system;

import java.util.List;

import javax.security.auth.Subject;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.mgt.DefaultSubjectFactory;
import org.apache.shiro.web.mgt.DefaultWebSubjectFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.geovis.core.util.BeanUtil;
import com.geovis.core.util.ResponseUtil;
import com.geovis.core.util.SystemCacheUtil;
import com.geovis.web.domain.system.SysOrg;
import com.geovis.web.domain.system.SysUser;
import com.geovis.web.service.system.OrgService;
import com.github.pagehelper.PageInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(value = "OrgController", description = "组织机构管理接口")
@RestController
@SuppressWarnings("all")
@RequestMapping("/system")
public class OrgController {
    private static final Logger logger = LoggerFactory.getLogger(OrgController.class);

    @Autowired
    private OrgService orgService;

    /**
     * 查询方法
     *
     * @param sysOrg
     * @return
     */
    @ApiOperation(value = "获取所有组织机构", notes = "获取组织机构")
    @GetMapping("/orgs")
    @RequiresPermissions(value = {"system:org:list", "admin"}, logical = Logical.OR)
    public ResponseEntity listAll(@ApiParam SysOrg sysOrg) {
        try {
            List<SysOrg> orgList = this.orgService.listAll(sysOrg);
            return ResponseUtil.success(new PageInfo<SysOrg>(orgList));
        } catch (Exception e) {
            logger.error("[listAll Exception]:" + e.getMessage());
            return ResponseUtil.failure(e.getMessage());
        }
    }

    /**
     * 添加单个组织（id必须为空）
     *
     * @param request
     * @param sysOrg
     * @return
     */
    @ApiOperation(value = "添加单个组织", notes = "添加单个组织")
    @PostMapping("/orgs")
    @RequiresPermissions(value = {"system:org:add", "admin"}, logical = Logical.OR)
    public ResponseEntity save(HttpServletRequest request,
                               @ApiParam @RequestBody SysOrg sysOrg) {
        try {
            if (sysOrg.getId() != null) {
                return ResponseUtil.failure("参数错误！");
            }

            //TODO 其他参数验证

            //TODO 初始化参数

            //设置创建信息
            BeanUtil.setCreateUser(request, sysOrg);

            //设置更新信息
            BeanUtil.setUpdateUser(request, sysOrg);

            sysOrg = this.orgService.commonSave(SystemCacheUtil.getUserByRequest(request), sysOrg);

            return ResponseUtil.success(sysOrg);
        } catch (Exception e) {
            logger.error("[save Exception]:" + e.getMessage());
            return ResponseUtil.failure(e.getMessage());
        }
    }

    /**
     * 查看单个组织
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "查看单个组织", notes = "查看单个组织")
    @GetMapping("/orgs/{id}")
    @RequiresPermissions(value = {"system:org:view", "admin"}, logical = Logical.OR)
    public ResponseEntity view(@ApiParam @PathVariable String id) {
        try {
            SysOrg sysOrg = this.orgService.selectOrgById(id);
            return ResponseUtil.success(sysOrg);
        } catch (Exception e) {
            logger.error("[view Exception]:" + e.getMessage());
            return ResponseUtil.failure(e.getMessage());
        }
    }

    /**
     * 编辑单个组织（id不可为空）
     *
     * @param request
     * @param sysOrg
     * @return
     */
    @ApiOperation(value = "编辑单个组织", notes = "编辑单个组织")
    @PutMapping("/orgs")
    @RequiresPermissions(value = {"system:org:edit", "admin"}, logical = Logical.OR)
    public ResponseEntity update(HttpServletRequest request,
                                 @ApiParam @RequestBody SysOrg sysOrg) {
        try {
            if (sysOrg.getId() == null || "".equals(sysOrg.getId())) {
                return ResponseUtil.failure("参数错误！");
            }

            //设置更新信息
            BeanUtil.setUpdateUser(request, sysOrg);

            sysOrg = this.orgService.commonSave(SystemCacheUtil.getUserByRequest(request), sysOrg);

            return ResponseUtil.success(sysOrg);
        } catch (Exception e) {
            logger.error("[update Exception]:" + e.getMessage());
            return ResponseUtil.failure(e.getMessage());
        }
    }

    /**
     * 软删除单个组织（根据id删除）
     *
     * @param request
     * @param id
     * @return
     */
    @ApiOperation(value = "删除单个组织", notes = "删除单个组织")
    @DeleteMapping("/orgs/{id}")
    @RequiresPermissions(value = {"system:org:remove", "admin"}, logical = Logical.OR)
    public ResponseEntity remove(HttpServletRequest request, @PathVariable String id) {
        try {
            SysUser currentUser = SystemCacheUtil.getUserByRequest(request);

            SysOrg result = this.orgService.removeOrgById(currentUser, id);

            if (result != null) {
                return ResponseUtil.failure("删除失败！");
            }

            return ResponseUtil.success();
        } catch (Exception e) {
            logger.error("[remove Exception]:"+e.getMessage());
            return ResponseUtil.failure(e.getMessage());
        }
    }
}