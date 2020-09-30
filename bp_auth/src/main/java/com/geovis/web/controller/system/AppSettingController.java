package com.geovis.web.controller.system;

import com.geovis.core.constant.Constant;
import com.geovis.core.util.*;
import com.geovis.web.domain.system.SysUser;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(value = "UserController", description = "用户管理接口")
@RestController
@RequestMapping("/app")
public class AppSettingController {
    private static final Logger logger = LoggerFactory.getLogger(AppSettingController.class);

    @Value("${app.file.host}")
    private String fileHost;

    @Value("${app.file.port}")
    private String filePort;

    /**
     * app文件服务器配置
     *
     * @param app
     * @return
     */
    @ApiOperation(value = "获取app配置", notes = "获取app配置")
    @GetMapping("/gethost")
    public ResponseEntity gethost() {
        try {
            String host = fileHost + ":" + filePort;
            return ResponseUtil.success(host);
        } catch (Exception e) {
            logger.error(String.format("[gethost Exception]: %s " , e.getMessage()));
            return ResponseUtil.failure(e.getMessage());
        }
    }
}
