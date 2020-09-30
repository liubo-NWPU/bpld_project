package com.geovis.web.controller.system;

import com.alibaba.druid.sql.visitor.functions.If;
import com.alibaba.fastjson.JSONObject;
import com.geovis.core.common.exception.ForbiddenException;
import com.geovis.core.constant.AuthConstant;
import com.geovis.core.constant.Constant;
import com.geovis.core.util.*;
import com.geovis.web.domain.system.SessYzm;
import com.geovis.web.domain.system.SysResource;
import com.geovis.web.domain.system.SysUser;
import com.geovis.web.service.system.ResourceService;
import com.geovis.web.service.system.SessYzmService;
import com.geovis.web.service.system.UserService;
import com.geovis.web.util.system.SysLogUtil;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import tk.mybatis.mapper.common.Mapper;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.*;

@Api(value = "LoginController", description = "用户登录登出接口")
@RestController
@SuppressWarnings("all")
@RequestMapping("/system")
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    private int currentUserNum = 0;

    private static final String CODEIMAGE = "codeImage";
    private static final String EXPIREDATE = "expireDate";
    private static final String FALSE = "false";

    @Autowired
    private UserService userService;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private SessYzmService sessYzmService;

    @Value("${system.wx.appid}")
    private String appid;

    @Value("${system.wx.secret}")
    private String secret;

    @Value("${system.wx.granttype}")
    private String granttype;

    @Autowired
    private RestTemplate restTemplate;


    /**
     * @param user
     * @param response
     * @return
     * @Description 用户登录【增强版】
     */
    @ApiOperation(value = "用户登录", notes = "用户登录接口")
    @CrossOrigin
    @PostMapping(value = "/login/{type}")
    public ResponseEntity login2(@RequestBody SysUser user, HttpServletRequest request, HttpServletResponse response, @PathVariable String type) {
        /*response.addHeader("Access-Control-Allow-Origin","*");
        response.addHeader("Access-Control-Allow-Methods","POST");
        response.addHeader("Access-Control-Allow_Headers","x-requested-width,content-type");*/
        //验证用户是否具备使用该系统的权限
        try {
            String[] userSysTypes = userService.selectUserByUsername(user.getUsername()).getUserSysType().toString().split("@");
            boolean isHave = false;
            for (int i = 0; i < userSysTypes.length; i++) {
                if (type.equals(userSysTypes[i])) {
                    isHave = true;
                }
            }
            if (!isHave) {
                return ResponseUtil.failure("该用户不具备使用该系统的权限！");
            }
        } catch (Exception ex) {
            logger.error("[login Excetpion]:" + ex.getMessage());
            return ResponseUtil.failure(ex.getMessage(), "该用户不具备使用该系统的权限！");

        }

        try {
            SysUser sysUser = null;
            if (!StringUtil.isEmpty(user.getWxcode())) {
                try {
                    //如过微信code不为空的话先获取微信id
                    String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + appid
                            + "&secret=" + secret + "&grant_type=" + granttype + "&js_code=";
                    String wxopenid = this.restTemplate.getForObject(url + user.getWxcode(), String.class);
                    JSONObject js = JSONObject.parseObject(wxopenid);
                    user.setOpenid(js.get("openid") != null ? js.get("openid").toString() : "");
                } catch (RestClientException e) {
                    return ResponseEntity.ok().body("微信登录有错误！");
                }
            }
            int loginFlag = 0;  //web端登录的话值为 0 ,手机端登录的话值为 1 ,微信绑定用户为 2
            if (StringUtil.isEmpty(user.getUsername()) || StringUtil.isEmpty(user.getPassword())) {
                if (StringUtil.isEmpty(user.getOpenid())) {
                    throw new ForbiddenException("用户名或密码不可为空！");
                } else {
                    //微信用户登录
                    sysUser = this.userService.selectUserByWxOpenId(user.getOpenid());
                    if (sysUser == null) {
                        //微信未绑定账户，请先绑定账户
                        return ResponseEntity.ok().body("0");
                    }
                    loginFlag = 1;
                }
            } else {
                //用户名密码都不为空的情况下且openid也不为空，逻辑为通过微信绑定用户
                if (!StringUtil.isEmpty(user.getOpenid())) {
                    loginFlag = 2;
                }
            }
            String errorMsg = "";
            if (sysUser == null) {
                //如果不是微信登录，web登录的话需要通过username获取用户信息
                logger.debug(user.getUsername() + "登录请求");
                sysUser = this.userService.selectUserByUsername(user.getUsername());
            }

            if (sysUser == null) {
                errorMsg = "用户不存在！";
            } else if (Constant.USER_STATE_LOCK.equals(sysUser.getState())) {
                errorMsg = "用户被锁定！";
            } else if (Constant.USER_STATE_REGIST.equals(sysUser.getState())) {
                errorMsg = "新注册用户，请等待管理员审批通过。";
            }

            if (StringUtil.isNotEmpty(errorMsg)) {
                SysLogUtil.saveLog(request, sysUser, user.getUsername() + "：" + errorMsg, "系统登录");
                throw new ForbiddenException(errorMsg);
            }

            //验证密码是否正确
            String password = "";
            if (!StringUtil.isEmpty(user.getUsername()) && !StringUtil.isEmpty(user.getPassword())) {
                password = PasswordUtil.encryptPassword(user.getPassword(), user.getUsername() + sysUser.getSalt());
            }
            if (password.equals(sysUser.getPassword()) || loginFlag == 1) {
                //验证通过后并且此请求是微信用户绑定账户的话
                if (loginFlag == 2) {
                    sysUser.setOpenid(user.getOpenid());
                    userService.commonSave(sysUser, sysUser);
                }
                String accessToken = JwtTokenUtil.createToken(sysUser.getUsername(), AuthConstant.TTLMILLS, AuthConstant.SECRETKET);
                response.setHeader(AuthConstant.DEFAULT_TOKEN_NAME, accessToken);
                response.setHeader(AuthConstant.CLIENT_PARAM_USERNAME, sysUser.getUsername());
                Map<String, String> authMap = new HashMap<String, String>();
                authMap.put(AuthConstant.DEFAULT_TOKEN_NAME, accessToken);
                authMap.put(AuthConstant.CLIENT_PARAM_USERNAME, sysUser.getUsername());
                authMap.put(AuthConstant.CLIENT_PARAM_USERID, sysUser.getId());
                authMap.put(AuthConstant.CLIENT_PARAM_USERSTATE, sysUser.getState());

                SysLogUtil.saveLog(request, sysUser, "系统登录");
                currentUserNum += 1;
                return ResponseEntity.ok().body(authMap);
            } else {
                SysLogUtil.saveLog(request, sysUser, user.getUsername() + "：密码错误！", "系统登录");
                //throw new ForbiddenException("密码错误！");
                //return ResponseEntity.ok().body("密码错误！");
                return ResponseUtil.failure("登录失败", "密码错误!");
            }
        } catch (Exception e) {
            logger.error("[login Excetpion]:" + e.getMessage());
            return ResponseUtil.failure(e.getMessage(), "密码错误！");
        }
    }

    /**
     * @param user
     * @param response
     * @return
     * @Description 用户登录【增强版】
     */
    @ApiOperation(value = "用户登录", notes = "用户登录接口")
    @CrossOrigin
    @PostMapping(value = "/login3")
    public ResponseEntity login3(@RequestParam(value = "user") String user1, @RequestParam(required = false) String yzm, @RequestParam(required = false) String sessid, HttpServletRequest request, HttpServletResponse response) {
        /*response.addHeader("Access-Control-Allow-Origin","*");
        response.addHeader("Access-Control-Allow-Methods","POST");
        response.addHeader("Access-Control-Allow_Headers","x-requested-width,content-type");*/
        //验证码认证
        SysUser user = JSONObject.parseObject(user1, SysUser.class);
       try {
            SessYzm sessYzm = sessYzmService.selectById(sessid);
            if (sessYzm!=null){
                if (!yzm.toString().toUpperCase().equals(sessYzm.getYzm())) {
                    sessYzmService.deleteById(sessid);
                    return ResponseUtil.failure("登录失败", "验证码错误");
                }
                if ((new Date().getTime() - Long.valueOf(sessYzm.getDate())) / 1000 >= 30) {
                    sessYzmService.deleteById(sessid);
                    return ResponseUtil.failure("登录失败", "验证码失效");
                }
            }else {
                sessYzmService.deleteById(sessid);
                return ResponseUtil.failure("登录失败", "验证码初始化失败");
            }
        }catch (Exception e){
            sessYzmService.deleteById(sessid);
            return ResponseUtil.failure("登录失败", "验证码初始化失败");
        }
        sessYzmService.deleteById(sessid);
        //验证用户是否具备使用该系统的权限
        try {
            String type = user.getUserSysType();
            String[] userSysTypes = userService.selectUserByUsername(user.getUsername()).getUserSysType().toString().split("@");
            boolean isHave = false;
            for (int i = 0; i < userSysTypes.length; i++) {
                if (type.equals(userSysTypes[i])) {
                    isHave = true;
                }
            }
            if (!isHave) {
                return ResponseUtil.failure("该用户不具备使用该系统的权限！");
            }
        } catch (Exception ex) {
            logger.error("[login Excetpion]:" + ex.getMessage());
            return ResponseUtil.failure(ex.getMessage(), "该用户不具备使用该系统的权限！");

        }
        try {
            SysUser sysUser = null;
            if (!StringUtil.isEmpty(user.getWxcode())) {
                try {
                    //如过微信code不为空的话先获取微信id
                    String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + appid
                            + "&secret=" + secret + "&grant_type=" + granttype + "&js_code=";
                    String wxopenid = this.restTemplate.getForObject(url + user.getWxcode(), String.class);
                    JSONObject js = JSONObject.parseObject(wxopenid);
                    user.setOpenid(js.get("openid") != null ? js.get("openid").toString() : "");
                } catch (RestClientException e) {
                    return ResponseEntity.ok().body("微信登录有错误！");
                }
            }
            int loginFlag = 0;  //web端登录的话值为 0 ,手机端登录的话值为 1 ,微信绑定用户为 2
            if (StringUtil.isEmpty(user.getUsername()) || StringUtil.isEmpty(user.getPassword())) {
                if (StringUtil.isEmpty(user.getOpenid())) {
                    throw new ForbiddenException("用户名或密码不可为空！");
                } else {
                    //微信用户登录
                    sysUser = this.userService.selectUserByWxOpenId(user.getOpenid());
                    if (sysUser == null) {
                        //微信未绑定账户，请先绑定账户
                        return ResponseEntity.ok().body("0");
                    }
                    loginFlag = 1;
                }
            } else {
                //用户名密码都不为空的情况下且openid也不为空，逻辑为通过微信绑定用户
                if (!StringUtil.isEmpty(user.getOpenid())) {
                    loginFlag = 2;
                }
            }
            String errorMsg = "";
            if (sysUser == null) {
                //如果不是微信登录，web登录的话需要通过username获取用户信息
                logger.debug(user.getUsername() + "登录请求");
                sysUser = this.userService.selectUserByUsername(user.getUsername());
            }

            if (sysUser == null) {
                errorMsg = "用户不存在！";
                return ResponseUtil.failure("登录失败",errorMsg);
            } else if (Constant.USER_STATE_LOCK.equals(sysUser.getState())||Constant.USER_STATE_LOCKFREE.equals(sysUser.getState())) {
                errorMsg = "用户被锁定！";
                return ResponseUtil.failure("登录失败",errorMsg);
            } else if (Constant.USER_STATE_REGIST.equals(sysUser.getState())) {
                errorMsg = "新注册用户，请等待管理员审批通过。";
                return ResponseUtil.failure("登录失败",errorMsg);
            }

            if (StringUtil.isNotEmpty(errorMsg)) {
                SysLogUtil.saveLog(request, sysUser, user.getUsername() + "：" + errorMsg, "系统登录");
                throw new ForbiddenException(errorMsg);
            }
            //验证IP绑定
            if ("1".equals(sysUser.getIpstatus())){
                if (!user.getIp().equals(sysUser.getIp())){
                    SysLogUtil.saveLog(request, sysUser, user.getUsername() + "：人机校验错误！", "系统登录");
                    return ResponseUtil.failure("登录失败", "人机校验错误!");                }
            }
            //验证密码是否正确
            String password = "";
            if (!StringUtil.isEmpty(user.getUsername()) && !StringUtil.isEmpty(user.getPassword())) {
                password = PasswordUtil.encryptPassword(user.getPassword(), user.getUsername() + sysUser.getSalt());
            }
            if (password.equals(sysUser.getPassword()) || loginFlag == 1) {
                //验证通过后并且此请求是微信用户绑定账户的话
                if (loginFlag == 2) {
                    sysUser.setOpenid(user.getOpenid());
                    userService.commonSave(sysUser, sysUser);
                }
                String accessToken = JwtTokenUtil.createToken(sysUser.getUsername(), AuthConstant.TTLMILLS, AuthConstant.SECRETKET);
                response.setHeader(AuthConstant.DEFAULT_TOKEN_NAME, accessToken);
                response.setHeader(AuthConstant.CLIENT_PARAM_USERNAME, sysUser.getUsername());
                Map<String, String> authMap = new HashMap<String, String>();
                authMap.put(AuthConstant.DEFAULT_TOKEN_NAME, accessToken);
                authMap.put(AuthConstant.CLIENT_PARAM_USERNAME, sysUser.getUsername());
                authMap.put(AuthConstant.CLIENT_PARAM_USERID, sysUser.getId());
                authMap.put(AuthConstant.CLIENT_PARAM_USERSTATE, sysUser.getState());
                authMap.put(AuthConstant.CLIENT_PARAM_USERSYSTYPE, sysUser.getUserSysType());

                SysLogUtil.saveLog(request, sysUser, "系统登录");
                currentUserNum += 1;
                return ResponseEntity.ok().body(authMap);
            } else {
                SysLogUtil.saveLog(request, sysUser, user.getUsername() + "：密码错误！", "系统登录");
                //throw new ForbiddenException("密码错误！");
                //return ResponseEntity.ok().body("密码错误！");
                return ResponseUtil.failure("登录失败", "密码错误!");
            }
        } catch (Exception e) {
            logger.error("[login Excetpion]:" + e.getMessage());
            return ResponseUtil.failure(e.getMessage(), "密码错误！");
        }

    }

    /**
     * @param user
     * @param response
     * @return
     * @Description 用户登录
     */
    @ApiOperation(value = "用户登录", notes = "用户登录接口")
    @PostMapping(value = "/login")
    public ResponseEntity login(@RequestBody SysUser user, HttpServletRequest request, HttpServletResponse response) {

        try {
            SysUser sysUser = null;
            if (!StringUtil.isEmpty(user.getWxcode())) {
                try {
                    //如过微信code不为空的话先获取微信id
                    String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + appid
                            + "&secret=" + secret + "&grant_type=" + granttype + "&js_code=";
                    String wxopenid = this.restTemplate.getForObject(url + user.getWxcode(), String.class);
                    JSONObject js = JSONObject.parseObject(wxopenid);
                    user.setOpenid(js.get("openid") != null ? js.get("openid").toString() : "");
                } catch (RestClientException e) {
                    return ResponseEntity.ok().body("微信登录有错误！");
                }
            }
            int loginFlag = 0;  //web端登录的话值为 0 ,手机端登录的话值为 1 ,微信绑定用户为 2
            if (StringUtil.isEmpty(user.getUsername()) || StringUtil.isEmpty(user.getPassword())) {
                if (StringUtil.isEmpty(user.getOpenid())) {
                    throw new ForbiddenException("用户名或密码不可为空！");
                } else {
                    //微信用户登录
                    sysUser = this.userService.selectUserByWxOpenId(user.getOpenid());
                    if (sysUser == null) {
                        //微信未绑定账户，请先绑定账户
                        return ResponseEntity.ok().body("0");
                    }
                    loginFlag = 1;
                }
            } else {
                //用户名密码都不为空的情况下且openid也不为空，逻辑为通过微信绑定用户
                if (!StringUtil.isEmpty(user.getOpenid())) {
                    loginFlag = 2;
                }
            }
            String errorMsg = "";
            if (sysUser == null) {
                //如果不是微信登录，web登录的话需要通过username获取用户信息
                logger.debug(user.getUsername() + "登录请求");
                sysUser = this.userService.selectUserByUsername(user.getUsername());
            }

            if (sysUser == null) {
                errorMsg = "用户不存在！";
            } else if (Constant.USER_STATE_LOCK.equals(sysUser.getState())) {
                errorMsg = "用户被锁定！";
            }

            if (StringUtil.isNotEmpty(errorMsg)) {
                SysLogUtil.saveLog(request, sysUser, user.getUsername() + "：" + errorMsg, "系统登录");
                throw new ForbiddenException(errorMsg);
            }

            //验证密码是否正确
            String password = "";
            if (!StringUtil.isEmpty(user.getUsername()) && !StringUtil.isEmpty(user.getPassword())) {
                password = PasswordUtil.encryptPassword(user.getPassword(), user.getUsername() + sysUser.getSalt());
            }
            if (password.equals(sysUser.getPassword()) || loginFlag == 1) {
                //验证通过后并且此请求是微信用户绑定账户的话
                if (loginFlag == 2) {
                    sysUser.setOpenid(user.getOpenid());
                    userService.commonSave(sysUser, sysUser);
                }
                String accessToken = JwtTokenUtil.createToken(sysUser.getUsername(), AuthConstant.TTLMILLS, AuthConstant.SECRETKET);
                response.setHeader(AuthConstant.DEFAULT_TOKEN_NAME, accessToken);
                response.setHeader(AuthConstant.CLIENT_PARAM_USERNAME, sysUser.getUsername());
                Map<String, String> authMap = new HashMap<String, String>();
                authMap.put(AuthConstant.DEFAULT_TOKEN_NAME, accessToken);
                authMap.put(AuthConstant.CLIENT_PARAM_USERNAME, sysUser.getUsername());
                authMap.put(AuthConstant.CLIENT_PARAM_USERID, sysUser.getId());
                authMap.put(AuthConstant.CLIENT_PARAM_USERSTATE, sysUser.getState());

                SysLogUtil.saveLog(request, sysUser, "系统登录");
                currentUserNum += 1;
                return ResponseEntity.ok().body(authMap);
            } else {
                SysLogUtil.saveLog(request, sysUser, user.getUsername() + "：密码错误！", "系统登录");
                //throw new ForbiddenException("密码错误！");
                //return ResponseEntity.ok().body("密码错误！");
                return ResponseUtil.failure("登录失败", "密码错误!");
            }
        } catch (Exception e) {
            logger.error("[login Excetpion]:" + e.getMessage());
            return ResponseUtil.failure(e.getMessage(), "密码错误！");
        }
    }

    /**
     * 获取当前人的菜单
     *
     * @param request
     * @return
     */
    @GetMapping("/listMenu")
    public ResponseEntity listMenu(HttpServletRequest request) {

        try {
            SysUser sysUser = SystemCacheUtil.getUserByRequest(request);
            List<SysResource> resourceList = this.resourceService.listMenuTreeByUserId(sysUser.getId());
            return ResponseUtil.success(resourceList);
        } catch (Exception e) {
            logger.error("[listMenu Exception]:" + e.getMessage());
            return ResponseUtil.failure(e.getMessage());
        }
    }

    /**
     * 获取当前人的菜单
     *
     * @param request
     * @return
     */
    @GetMapping("/listFolder")
    public ResponseEntity listFolder(HttpServletRequest request) {
        try {
            SysUser sysUser = SystemCacheUtil.getUserByRequest(request);
            List<SysResource> resourceList = this.resourceService.listFolderTreeByUserId(sysUser.getId());
            return ResponseUtil.success(resourceList);
        } catch (Exception e) {
            logger.error("[listFolder Exception]:" + e.getMessage());
            return ResponseUtil.failure(e.getMessage());
        }
    }

    /**
     * 获取当前人的权限
     *
     * @param request
     * @return
     */
    @GetMapping("/listPermission")
    public ResponseEntity listPermissions(HttpServletRequest request) {
        try {
            SysUser sysUser = SystemCacheUtil.getUserByRequest(request);
            List<SysResource> resourceList = this.resourceService.listPermissionByUserId(sysUser.getId());

            return ResponseUtil.success(resourceList);
        } catch (Exception e) {
            logger.error("[listPermissions Exception]:" + e.getMessage());
            return ResponseUtil.failure(e.getMessage());
        }
    }


    /**
     * 获取当前人的资源
     *
     * @param request
     * @return
     */
    @GetMapping("/list/{type}")
    public ResponseEntity listSourceByType(HttpServletRequest request, @ApiParam @PathVariable String type) {
        try {
            SysUser sysUser = SystemCacheUtil.getUserByRequest(request);
            List<SysResource> resourceList = this.resourceService.listSourceByType(sysUser.getId(), type);

            return ResponseUtil.success(resourceList);
        } catch (Exception e) {
            logger.error("[listSourceByType Excetpion]:" + e.getMessage());
            return ResponseUtil.failure(e.getMessage());
        }
    }


    /**
     * @description 登出处理
     */
    @GetMapping("/logout")
    public ResponseEntity logout(HttpServletRequest request) {

        try {

            String username = request.getHeader("username");

            logger.debug("Logout Success...");
            SysUser sysUser = SystemCacheUtil.getUserByRequest(request);

            /*SysLogUtil.saveLog(request, sysUser, "登出系统 ");
            return ResponseUtil.success(sysUser.getUsername(), "登出系统");*/

            SysLogUtil.saveLog(request, userService.selectUserByUsername(username), "登出系统 ");
            currentUserNum -= 1;
            return ResponseUtil.success(username, "登出系统");
        } catch (Exception e) {
            logger.error("[logout Exception]:" + e.getMessage());
            return ResponseUtil.failure(e.getMessage());
        }
    }

    @GetMapping("/cleanCache")
    @Caching(evict = {
            @CacheEvict(value = SystemCacheUtil.CACHE_RESOURCE, allEntries = true),
            @CacheEvict(value = SystemCacheUtil.CACHE_USER, allEntries = true),
            @CacheEvict(value = SystemCacheUtil.CACHE_ROLE, allEntries = true),
            @CacheEvict(value = SystemCacheUtil.CACHE_ORG, allEntries = true),
            @CacheEvict(value = SystemCacheUtil.CACHE_ORG, allEntries = true),
            @CacheEvict(value = SystemCacheUtil.CACHE_ORG, allEntries = true),
    })
    public void cleanCache() {
        logger.info("[cleanCache]");
    }

    @GetMapping("/currentUserNum")
    public int currentUserNum(HttpServletRequest request) {
        return SystemCacheUtil.getAllUserCacheCount();
        //return currentUserNum;
    }

    @GetMapping("/getImage")
    public Map getCode() {
        Map resultMap = new HashMap<>();
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();) {
            Map<String, Object> map = CodeUtil.generateCodeAndPic();
            String code = map.get("code").toString();
            ImageIO.write((BufferedImage) map.get("codePic"), "jpeg", out);
            byte[] bytes = out.toByteArray();
            String s = Base64.encode(bytes);
            String sessid = YzmUtils.verifyCode();
            long time = new Date().getTime();
            sessYzmService.deleteByTime(time-1000*60*60);
            sessYzmService.insertYzm(sessid, code,time);
            resultMap.put("data", s);
            resultMap.put("sessid",sessid);
        } catch (IOException e) {
            e.getMessage();
        }
        return resultMap;
    }

    @ApiOperation(value = "获取用户mrp", notes = "根据ID获取用户mrp")
    @GetMapping("/mrp")
    public ResponseEntity getUserMrp(@ApiParam String id) {
        try {
            SysUser sysUser = this.userService.selectUserById(id);
            return ResponseUtil.success(sysUser.getMrp());
        } catch (Exception e) {
            logger.error("[view Excetpion]:" + e.getMessage());
            return ResponseUtil.failure(e.getMessage());
        }
    }

}