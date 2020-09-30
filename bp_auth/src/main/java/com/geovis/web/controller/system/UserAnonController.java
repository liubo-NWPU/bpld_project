package com.geovis.web.controller.system;

import com.alibaba.fastjson.JSONObject;
import com.geovis.core.constant.Constant;
import com.geovis.core.util.*;
import com.geovis.web.domain.system.SessYzm;
import com.geovis.web.domain.system.SysUser;
import com.geovis.web.service.system.SessYzmService;
import com.geovis.web.service.system.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

@Api(value = "UserAnonController", description = "用户（匿名）管理接口")
@RestController
@RequestMapping("/system")
public class UserAnonController {
    private static final Logger logger = LoggerFactory.getLogger(UserAnonController.class);

    private static final String RESULT = "result";
    

    @Value("${system.user.oriPwd}")
    private String oriPwd;

    @Autowired
    private SessYzmService sessYzmService;


    @Value("${web.upload.rootPath}")
    private String fileRootPath;

    @Autowired
    private UserService userService;

    @ApiOperation(value = "注册保存用户", notes = "注册保存用户")
    @PostMapping("/anon/user/save")
    public Map<String, String> save(HttpServletRequest request,
                                    @ApiParam @RequestParam(name = "user") String user1,
                                    @RequestParam(required = false) String yzm,
                                    @RequestParam(required = false)String sessid) {
        Map<String,String> resultMap=new HashMap<>();
        SysUser sysUser = JSONObject.parseObject(user1, SysUser.class);
        try {
            SessYzm sessYzm = sessYzmService.selectById(sessid);
            if (sessYzm!=null){
                if (!yzm.toString().toUpperCase().equals(sessYzm.getYzm())) {
                    sessYzmService.deleteById(sessid);
                    resultMap.put(RESULT,"fail");
                    resultMap.put("msg","验证码错误");
                    return resultMap;
                }
                if ((new Date().getTime() - Long.valueOf(sessYzm.getDate())) / 1000 >= 60) {
                    sessYzmService.deleteById(sessid);
                    resultMap.put(RESULT,"fail");
                    resultMap.put("msg","验证码失效");
                    return resultMap;
                }
            }else {
                sessYzmService.deleteById(sessid);
                resultMap.put(RESULT,"fail");
                resultMap.put("msg","验证码初始化失败");
                return resultMap;
            }
        }catch (Exception e){
            sessYzmService.deleteById(sessid);
            resultMap.put(RESULT,"fail");
            resultMap.put("msg","验证码初始化失败");
            return resultMap;
        }
        try {
            if (sysUser.getId() != null) {
                resultMap.put(RESULT,"fail");
                resultMap.put("msg","参数错误");
                return resultMap;
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

            sysUser.setUserType(sysUser.getUserType());

            //为用户加密
            PasswordUtil.encryptPassword(sysUser);

            //设置创建信息
            BeanUtil.setCreateUser(request, sysUser);

            //验证用户名合法性
            if (!this.validateUsername(sysUser.getUsername())) {
                resultMap.put(RESULT,"fail");
                resultMap.put("msg","该用户名已被占用");
                return resultMap;
            }

            //设置更新信息
            BeanUtil.setUpdateUser(request, sysUser);

            sysUser = this.userService.commonSave(null, sysUser);
            resultMap.put(RESULT, "success");
            resultMap.put("msg", sysUser.getId());
            return resultMap;
        } catch (Exception e) {
            logger.error("[save Exception]:" + e.getMessage());
            resultMap.put(RESULT, "fail");
            resultMap.put("msg", e.getMessage());
            return resultMap;
        }
    }

    @ApiOperation(value = "注册保存企业用户", notes = "注册保存企业用户")
    @PostMapping("/anon/user/saveBusiness")
    public Map<String, String> saveBusiness(HttpServletRequest request,
                                    @ApiParam @RequestParam(name = "user") String user1,
                                    @RequestParam(required = false) String yzm,
                                    @RequestParam(required = false)String sessid) {
        Map<String,String> resultMap=new HashMap<>();
        //验证码认证
        SysUser sysUser = JSONObject.parseObject(user1, SysUser.class);
        try {
            SessYzm sessYzm = sessYzmService.selectById(sessid);
            if (sessYzm!=null){
                if (!yzm.toString().toUpperCase().equals(sessYzm.getYzm())) {
                    sessYzmService.deleteById(sessid);
                    resultMap.put(RESULT,"fail");
                    resultMap.put("msg","验证码错误");
                    return resultMap;
                }
                if ((new Date().getTime() - Long.valueOf(sessYzm.getDate())) / 1000 >= 60) {
                    sessYzmService.deleteById(sessid);
                    resultMap.put(RESULT,"fail");
                    resultMap.put("msg","验证码失效");
                    return resultMap;
                }
            }else {
                sessYzmService.deleteById(sessid);
                resultMap.put(RESULT,"fail");
                resultMap.put("msg","验证码初始化失败");
                return resultMap;
            }
        }catch (Exception e){
            sessYzmService.deleteById(sessid);
            resultMap.put(RESULT,"fail");
            resultMap.put("msg","验证码初始化失败");
            return resultMap;
        }
        try {
            if (sysUser.getId() != null) {
                resultMap.put(RESULT,"fail");
                resultMap.put("msg","参数错误");
                return resultMap;
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
                sysUser.setState(Constant.USER_STATE_REGIST);
            }

            sysUser.setUserType(sysUser.getUserType());

            //为用户加密
            PasswordUtil.encryptPassword(sysUser);

            //设置创建信息
            BeanUtil.setCreateUser(request, sysUser);

            //验证用户名合法性
            if (!this.validateUsername(sysUser.getUsername())) {
                resultMap.put(RESULT,"fail");
                resultMap.put("msg","该用户名已被占用");
                return resultMap;
            }

            //设置更新信息
            BeanUtil.setUpdateUser(request, sysUser);

            sysUser = this.userService.commonSave(null, sysUser);

            resultMap.put(RESULT, "success");
            resultMap.put("msg", sysUser.getId());
            return resultMap;
        } catch (Exception e) {
            logger.error("[save Exception]:" + e.getMessage());
            resultMap.put(RESULT, "fail");
            resultMap.put("msg", e.getMessage());
            return resultMap;
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
    @GetMapping("/anon/user/check/{username}")
    public Boolean validateUsername(@PathVariable String username) {

        try {
            SysUser user = this.userService.selectUserByUsername(username);
            if (user != null) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            logger.error("[validateUsername Exception]:" + e.getMessage());
            return false;
        }
    }

    @RequestMapping(value = "/anon/user/uploadfile/{userId}")
    @ResponseBody
    public String upload(MultipartFile file, @PathVariable String userId) {
        try {
            if (file.isEmpty()) {
                return "文件为空";
            }
            // 获取文件名
            String fileName = file.getOriginalFilename();
            logger.info("上传的文件名为：" + fileName);
            // 获取文件的后缀名
            String suffixName = fileName.substring(fileName.lastIndexOf("."));
            logger.info("文件的后缀名为：" + suffixName);
            // 设置文件存储路径
            String filePath = fileRootPath;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/");
            String middlePath = sdf.format(new Date());
            String path = filePath + middlePath + fileName;
            File dest = new File(path);
            // 检测是否存在目录
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();// 新建文件夹
            }
            file.transferTo(dest);// 文件写入
            SysUser su = userService.selectUserById(userId);
            su.setUserFile(middlePath + fileName);
            userService.commonSave(null, su);
            return "上传成功";
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "上传失败";
    }

    //文件下载相关代码
    @RequestMapping("/anon/user/downloadfile/{userId}")
    @ResponseBody
    public String downloadFile(HttpServletResponse resp, @PathVariable String userId) {

        //设置文件路径
        String realPath = fileRootPath;
        String path = null;
        try {
            path = userService.selectUserById(userId).getUserFile();
        } catch (Exception e) {
            return "此账户无附件可下载。";
        }
        if (path == null) {

            return "此账户无附件可下载。";
        }
        String fp = fileRootPath + path;
        File fileT = new File(fp);
        if (!fileT.exists()) {
            return "目标文件不存在";
        }


        try( FileInputStream fin = new FileInputStream(fp);) {
            resp.setContentType("multipart/form-data");
            resp.addHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileT.getName(), "UTF-8"));

            int i = 0;
            while ((i = fin.read()) != -1) {
                resp.getOutputStream().write(i);
            }
            fin.close();
            return "下载成功";
        } catch (Exception e) {
            return "请求的文件不存在";
        }
    }
}
