package com.geovis.web.controller.system;

import com.geovis.core.constant.Constant;
import com.geovis.core.util.*;
import com.geovis.web.domain.system.SysUser;
import com.geovis.web.service.system.RoleService;
import com.geovis.web.service.system.UserService;
import com.geovis.web.util.system.SysLogUtil;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Api(value = "UserController", description = "用户管理接口")
@RestController
@RequestMapping("/system")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Value("${system.user.oriPwd}")
    private String oriPwd;

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    /**
     * 获取所有用户
     *
     * @param sysUser
     * @return
     */
    @ApiOperation(value = "获取所有用户", notes = "获取所有用户")
    @PostMapping("/users")
    @RequiresPermissions(value = {"system:user:list", "admin"}, logical = Logical.OR)
    public ResponseEntity listAll(@ApiParam @RequestBody SysUser sysUser, HttpServletRequest request) {

        try {
            List<SysUser> userList = this.userService.listAll(sysUser);
            SysLogUtil.saveLog(request, sysUser, "查询所有用户");
            return ResponseUtil.success(new PageInfo<SysUser>(userList));
        } catch (Exception e) {
            logger.error("[listAll Exception]:" + e.getMessage());
            return ResponseUtil.failure(e.getMessage());
        }
    }

    @ApiOperation(value = "保存用户", notes = "保存用户")
    @PostMapping("/saveUser")
    @RequiresPermissions(value = {"system:user:add", "admin"}, logical = Logical.OR)
    public ResponseEntity save(HttpServletRequest request,
                               @ApiParam @RequestBody SysUser sysUser) {
        try {
            if (sysUser.getId() != null) {
                return ResponseUtil.failure("参数错误！");
            }

            if (sysUser.getPassword() == null || "".equals(sysUser.getPassword())) {
                //给出默认密码
                sysUser.setPassword(this.oriPwd);
            }

            //随机盐
            if (sysUser.getSalt() == null || "".equals(sysUser.getPassword())) {
                sysUser.setSalt(IdGenUtil.uuid().substring(0, 10));
            }

            //初始化用户状态
            if (sysUser.getState() == null || "".equals(sysUser.getState())) {
                sysUser.setState(Constant.USER_STATE_NORMAL);
            }

            //为用户加密
            PasswordUtil.encryptPassword(sysUser);

            //设置创建信息
            BeanUtil.setCreateUser(request, sysUser);

            //验证用户名合法性
            if (!this.validateUsername(sysUser.getUsername())) {
                return ResponseUtil.failure("该用户名已被占用！", sysUser);
            }

            sysUser.setUserType("1");

            //设置更新信息
            BeanUtil.setUpdateUser(request, sysUser);

            sysUser = this.userService.commonSave(SystemCacheUtil.getUserByRequest(request), sysUser);

            List<String> userIdList=new ArrayList<>();
            userIdList.add(sysUser.getId());
            if(sysUser.getUserGroup().equals("1")){//系统管理组，赋权
                this.roleService.setUser(SystemCacheUtil.getUserByRequest(request), userIdList, "be10c93dabd746a3815624edeb6ab446");
            }else if(sysUser.getUserGroup().equals("2")){//内容管理组，赋权
                this.roleService.setUser(SystemCacheUtil.getUserByRequest(request), userIdList, "13dfa9ac87a44551857626b25e40e0d9");
            }else{

            }

            return ResponseUtil.success(sysUser);
        } catch (Exception e) {
            logger.error("[save Exception]:" + e.getMessage());
            return ResponseUtil.failure(e.getMessage());
        }
    }

    /**
     * 验证用户名是否存在
     * 存在返回false，不存在返回true
     *
     * @param username
     * @return
     */
    @ApiOperation(value = "验证用户是否存在", notes = "验证用户是否存在")
    @GetMapping("/users/validate/{username}")
    public Boolean validateUsername(@PathVariable String username) {

        try {
            SysUser user = this.userService.selectUserByUsername(username);
            if (user != null) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            logger.error(String.format("[validateUsername Exception]: %s " , e.getMessage()));
            return false;
        }
    }

    @ApiOperation(value = "获取用户", notes = "根据ID获取用户")
    @GetMapping("/users/{id}")
    // @RequiresPermissions(value={"system:user:view","admin"},logical=Logical.OR)
    public ResponseEntity view(@ApiParam @PathVariable String id) {
        try {
            SysUser sysUser = this.userService.selectUserById(id);
            return ResponseUtil.success(sysUser);
        } catch (Exception e) {
            logger.error("[view Excetpion]:" + e.getMessage());
            return ResponseUtil.failure(e.getMessage());
        }
    }

    @ApiOperation(value = "获取用户mrp", notes = "根据ID获取用户mrp")
    @GetMapping("/users/mrp/a")
    // @RequiresPermissions(value={"system:user:view","admin"},logical=Logical.OR)
    public ResponseEntity getUserMrp2(@ApiParam String id) {
        try {
            SysUser sysUser = this.userService.selectUserById(id);
            return ResponseUtil.success(sysUser.getMrp());
        } catch (Exception e) {
            logger.error("[view Excetpion]:" + e.getMessage());
            return ResponseUtil.failure(e.getMessage());
        }
    }

    @ApiOperation(value = "更新用户", notes = "更新用户")
    @PutMapping("/users")
    @RequiresPermissions(value = {"system:user:edit", "admin"}, logical = Logical.OR)
    public ResponseEntity update(HttpServletRequest request,
                                 @RequestBody SysUser sysUser) {
        try {
            if (sysUser.getId() == null || "".equals(sysUser.getId())) {
                return ResponseUtil.failure("参数错误！参数Id为不为null或''");
            }

            SysUser user = this.userService.selectUserById(sysUser.getId());

            if (user == null) {
                return ResponseUtil.failure("未找到该用户！");
            }

            sysUser.setUsername(user.getUsername());//不可修改用户名

            if (StringUtil.isNotEmpty(sysUser.getPassword())&&!user.getPassword().equals(sysUser.getPassword())) {
                sysUser.setSalt(user.getSalt());
                PasswordUtil.encryptPassword(sysUser);
            }

            //如果选择“系统管理组”/“内容管理组”，则程序自动赋权
            List<String> userIdList=new ArrayList<>();
            userIdList.add(sysUser.getId());
            if(StringUtil.isNotEmpty(sysUser.getUserGroup())) {
                if (sysUser.getUserGroup().equals("1")) {//系统管理组，赋权
                    this.roleService.setUser(SystemCacheUtil.getUserByRequest(request), userIdList, "be10c93dabd746a3815624edeb6ab446");
                } else if (sysUser.getUserGroup().equals("2")) {//内容管理组，赋权
                    this.roleService.setUser(SystemCacheUtil.getUserByRequest(request), userIdList, "13dfa9ac87a44551857626b25e40e0d9");
                } else {

                }
            }

            //设置更新信息
            BeanUtil.setUpdateUser(request, sysUser);
            sysUser = this.userService.commonSave(SystemCacheUtil.getUserByRequest(request), sysUser);
            return ResponseUtil.success(sysUser);
        } catch (Exception e) {
            logger.error("[update Exception]:" + e.getMessage());
            return ResponseUtil.failure(e.getMessage());
        }
    }

    /**
     * 用于修改本人信息，不需要权限码
     *
     * @param request
     * @param sysUser
     * @return
     */
    @ApiOperation(value = "修改用户信息", notes = "修改用户信息")
    @PostMapping("/users/self")
    public ResponseEntity updateUserSelf(HttpServletRequest request,
                                         @RequestBody SysUser sysUser) {
        try {
            /*验证是否当前人*/
            if (sysUser.getId() == null || "".equals(sysUser.getId())) {
                return ResponseUtil.failure("参数错误！");
            }

            SysUser user = this.userService.selectUserById(sysUser.getId());

            if (user == null) {
                return ResponseUtil.failure("未找到该用户！");
            }

            if (!user.getUsername().equals(request.getHeader("username"))) {
                return ResponseUtil.failure("只能修改本人的信息！");
            }

            sysUser.setUsername(user.getUsername());//不可修改用户名
            if (StringUtil.isNotEmpty(sysUser.getPassword())) {
                sysUser.setSalt(user.getSalt());

                PasswordUtil.encryptPassword(sysUser);
            }

            //设置更新信息
            BeanUtil.setUpdateUser(request, sysUser);

            sysUser = this.userService.commonSave(SystemCacheUtil.getUserByRequest(request), sysUser);
            SysLogUtil.saveLog(request, sysUser, "修改用户信息");
            return ResponseUtil.success(sysUser);
        } catch (Exception e) {
            logger.error("[updateUserSelf Exception]:" + e.getMessage());
            return ResponseUtil.failure(e.getMessage());
        }
    }

    @ApiOperation(value = "删除用户", notes = "删除用户")
    @DeleteMapping("/users/{id}")
    @RequiresPermissions(value = {"system:user:remove", "admin"}, logical = Logical.OR)
    public ResponseEntity remove(HttpServletRequest request, @PathVariable String id) {
        try {
            SysUser currentUser = SystemCacheUtil.getUserByRequest(request);
//        if("admin".equals(currentUser.getUsername())) {
//        	return ResponseUtil.failure("不允许删除管理员！");
//        }
            if ("0".equals(id)) {
                return ResponseUtil.failure("不允许删除管理员！");
            }
            SysUser res = this.userService.removeUserById(currentUser, id);

            if (res != null) {
                return ResponseUtil.failure("删除失败！");
            }

            return ResponseUtil.success("删除成功！");
        } catch (Exception e) {
            logger.error("[remove Excetpion]:" + e.getMessage());
            return ResponseUtil.failure(e.getMessage());
        }
    }

    /**
     * 仅用于当前人修改自己的密码
     *
     * @param request
     * @param sysUser
     * @return
     */
    @ApiOperation(value = "修改该用户密码", notes = "修改该用户密码")
    @PostMapping("/users/pwd")
    public ResponseEntity modifyPwd(HttpServletRequest request, @RequestBody SysUser sysUser) {

        try {
            if (sysUser.getUsername() == null || "".equals(sysUser.getUsername())) {
                return ResponseUtil.failure("用户名不能为空！");
            }

            if (!sysUser.getUsername().equals(request.getHeader("username"))) {
                return ResponseUtil.failure("只能修改本人的密码！");
            }

            if (sysUser.getOriPassword() == null || "".equals(sysUser.getOriPassword())) {
                return ResponseUtil.failure("原密码不能为空！");
            }

            if (sysUser.getPassword() == null || "".equals(sysUser.getPassword())) {
                return ResponseUtil.failure("修改后密码不能为空！");
            }

            //验证原始密码合法性
            SysUser user = this.userService.selectUserByUsername(sysUser.getUsername());
            String oriPassword = PasswordUtil.encryptPassword(sysUser.getOriPassword(), sysUser.getUsername() + user.getSalt());

            if (!oriPassword.equals(user.getPassword())) {
                return ResponseUtil.failure("输入的原始密码不正确！");
            }

            //更新密码
            String newPwd = PasswordUtil.encryptPassword(sysUser.getPassword(), user.getUsername() + user.getSalt());
            SysUser modifiedUser = new SysUser();
            modifiedUser.setId(user.getId());
            modifiedUser.setUsername(user.getUsername());
            modifiedUser.setPassword(newPwd);

            //设置更新信息
            BeanUtil.setUpdateUser(request, modifiedUser);

            modifiedUser = this.userService.commonSave(SystemCacheUtil.getUserByRequest(request), modifiedUser);

            return ResponseUtil.success(modifiedUser);
        } catch (Exception e) {
            logger.error("[modifyPwd Exception]:"+e.getMessage());
            return ResponseUtil.failure(e.getMessage());
        }
    }

    @ApiOperation(value = "重置密码", notes = "重置密码")
    @CrossOrigin
    @PostMapping("/users/restPwd")
    @RequiresPermissions(value = {"system:user:restPwd", "admin"}, logical = Logical.OR)
    public ResponseEntity restPwd(HttpServletRequest request, @RequestBody SysUser sysUser) {

        try {
            if (sysUser.getUsername() == null || "".equals(sysUser.getUsername())) {
                return ResponseUtil.failure("用户名不能为空！");
            }

            if (sysUser.getPassword() == null || "".equals(sysUser.getPassword())) {
                sysUser.setPassword(this.oriPwd);
            }

            //验证用户合法性
            SysUser user = this.userService.selectUserByUsername(sysUser.getUsername());
            if (user == null) {
                return ResponseUtil.failure("未找到用户！");
            }

            //更新密码
            String newPwd = PasswordUtil.encryptPassword(sysUser.getPassword(), user.getUsername() + user.getSalt());
            SysUser modifiedUser = new SysUser();
            modifiedUser.setId(user.getId());
            modifiedUser.setUsername(user.getUsername());
            modifiedUser.setPassword(newPwd);

            //设置更新信息
            BeanUtil.setUpdateUser(request, modifiedUser);

            modifiedUser = this.userService.commonSave(SystemCacheUtil.getUserByRequest(request), modifiedUser);

            return ResponseUtil.success(modifiedUser);
        } catch (Exception e) {
            logger.error("[restPwd Exception]:"+e.getMessage());
            return ResponseUtil.failure(e.getMessage());
        }
    }
}
