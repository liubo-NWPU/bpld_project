package com.geovis.web.controller.system;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.geovis.core.util.ResponseUtil;
import com.geovis.web.domain.system.DatalogDiff;
import com.geovis.web.domain.system.SysDatalog;
import com.geovis.web.service.system.DatalogService;
import com.github.pagehelper.PageInfo;

import io.swagger.annotations.ApiParam;


@RestController
@SuppressWarnings("all")
@RequestMapping("/system")
public class DatalogController {
    private static final Logger logger = LoggerFactory.getLogger(DatalogController.class);

    @Autowired
    private DatalogService datalogService;

    @GetMapping("/datalog")
    @RequiresPermissions("system:datalog:list")
    public ResponseEntity listAll(@ApiParam SysDatalog sysDatalog){
        try {
            List<SysDatalog> datalogList = this.datalogService.listAll(sysDatalog);
            return ResponseUtil.success(new PageInfo<SysDatalog>(datalogList));
        } catch (Exception e) {
            logger.error("[listAll Exception]:"+e.getMessage());
            return ResponseUtil.failure(e.getMessage());
        }
    }

    /**
     * 获取所有修订版本
     * @param tableName
     * @param dataId
     * @return
     */
    @GetMapping("/datalog/listVersion/{tableName}/{dataId}")
    public ResponseEntity listVersionNumber(@PathVariable String tableName, @PathVariable String dataId){
        try {
            List<SysDatalog> datalogList = this.datalogService.listVersionNumber(tableName, dataId);
            return ResponseUtil.success(datalogList);
        } catch (Exception e) {
            logger.error("[listVersionNumber Exception]:"+e.getMessage());
            return ResponseUtil.failure(e.getMessage());
        }
    }

    /**
     * 根据前台传过来的版本进行比对
     * @param tableName
     * @param dataId
     * @param version1
     * @param version2
     * @return
     */
    @GetMapping("/datalog/getDiffDataVersion/{tableName}/{dataId}/{version1}/{version2}")
    @RequiresPermissions("system:datalog:diff")
    public ResponseEntity diffDataVersion(@PathVariable String tableName,
                                          @PathVariable String dataId,
                                          @PathVariable String version1,
                                          @PathVariable String version2){
        try {
            List<DatalogDiff> diffList = this.datalogService.diffDataVersion(tableName, dataId, version1, version2);
            return ResponseUtil.success(diffList);
        } catch (Exception e) {
            logger.error("[diffDataVersion Exception]:"+e.getMessage());
            return ResponseUtil.failure(e.getMessage());
        }
    }

    /**
     * 直接获取该业务数据最后修改的两次差异
     * @param tableName
     * @param dataId
     * @return
     */
    @GetMapping("/datalog/getDiffLastVersion/{tableName}/{dataId}")
    @RequiresPermissions("system:datalog:diff")
    public ResponseEntity diffLastVersion(@PathVariable String tableName, @PathVariable String dataId){
        try {
            List<DatalogDiff> diffList = this.datalogService.diffLastVersion(tableName, dataId);
            return ResponseUtil.success(diffList);
        } catch (Exception e) {
            logger.error("[diffLastVersion Exception]:"+e.getMessage());
            return ResponseUtil.failure(e.getMessage());
        }
    }
}