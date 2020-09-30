package com.geovis.web.controller.system;

import com.alibaba.fastjson.JSONObject;
import com.geovis.core.util.ResponseUtil;
import com.geovis.web.domain.model.LoggerRequestModel;
import com.geovis.web.domain.system.SysLog;
import com.geovis.web.domain.system.SysUser;
import com.geovis.web.service.system.SysLogService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Api(value = "LoggerController", description = "日志服务")
@RestController
@SuppressWarnings("all")
@RequestMapping("/logger")
public class LoggerContronller {
    private static final Logger logger = LoggerFactory.getLogger(LoggerContronller.class);

    @Autowired
    private SysLogService sysLogService;
    @Autowired
    private UserService userService;

    /**
     * 查看单个用户资源
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "单个用户日志", notes = "单个用户日志")
    @GetMapping("/selectByUseId/{userid}")
    @RequiresPermissions(value = {"system:logger:selectByUseId", "admin"}, logical = Logical.OR)
    public ResponseEntity selectByUseId(@ApiParam @PathVariable String userId) {
        try {
            List<SysLog> sysLogs = this.sysLogService.selectByUseId(userId);
            return ResponseUtil.success(sysLogs);
        } catch (Exception e) {
            logger.error("[selectByUseId Exception]:" + e.getMessage());
            return ResponseUtil.failure(e.getMessage());
        }
    }

    @ApiOperation(value = "单个用户日志", notes = "单个用户日志")
    @GetMapping("/selectByCreateDate/{createDate}")
    @RequiresPermissions(value = {"system:logger:selectByCreateDate", "admin"}, logical = Logical.OR)
    public ResponseEntity selectByCreateTime(@ApiParam @PathVariable String createDate) {
        try {
            List<SysLog> sysLogs = this.sysLogService.selectByCreateTime(createDate);
            return ResponseUtil.success(sysLogs);
        } catch (Exception e) {
            logger.error("[selectByCreateTime Exception]:" + e.getMessage());
            return ResponseUtil.failure(e.getMessage());
        }
    }

    @ApiOperation(value = "单个用户日志", notes = "单个用户日志")
    @GetMapping("/selectByType/{type}")
    @RequiresPermissions(value = {"system:logger:selectByType", "admin"}, logical = Logical.OR)
    public ResponseEntity selectByType(@ApiParam @PathVariable String type) {
        try {
            List<SysLog> sysLogs = this.sysLogService.selectByType(type);
            return ResponseUtil.success(sysLogs);
        } catch (Exception e) {
            logger.error("[selectByType Exception]:" + e.getMessage());
            return ResponseUtil.failure(e.getMessage());
        }
    }

    @ApiOperation(value = "用户日志保存", notes = "用户日志保存")
    @PostMapping("/saveLog")
    @RequiresPermissions(value = {"system:logger:saveLog", "admin"}, logical = Logical.OR)
    public ResponseEntity saveLog(@ApiParam @RequestBody String messageInfo, HttpServletRequest request, HttpServletResponse response) {
        try {
            LoggerRequestModel loggerRequestModel = JSONObject.parseObject(messageInfo, LoggerRequestModel.class);
            SysUser sysUser = userService.selectUserById(loggerRequestModel.getUesrId());
            SysLogUtil.saveLog(request, sysUser, loggerRequestModel.getErrMsg(), loggerRequestModel.getDecription());
            return ResponseUtil.success("日志保存成功！");
        } catch (Exception e) {
            logger.error("[saveLog Exception]:" + e.getMessage());
            return ResponseUtil.failure("日志保存失败:" + e.getMessage());

        }

    }

    @ApiOperation(value = "用户日志查询", notes = "用户日志查询")
    @PostMapping("/selectLoggers")
    @RequiresPermissions(value = {"system:logger:selectLoggers", "admin"}, logical = Logical.OR)
    public ResponseEntity selectLoggers(@ApiParam @RequestBody String messageInfo, HttpServletRequest request, HttpServletResponse response) {
        try {
            SysLog sysLog = JSONObject.parseObject(messageInfo, SysLog.class);
            List<SysLog> sysLogs = this.sysLogService.selectLoggers(sysLog);
            return ResponseUtil.success(new PageInfo<SysLog>(sysLogs));
        } catch (Exception e) {
            logger.error("[selectLoggers Exception]:" + e.getMessage());
            return ResponseUtil.failure("用户日志查询:" + e.getMessage());


        }


    }


}
