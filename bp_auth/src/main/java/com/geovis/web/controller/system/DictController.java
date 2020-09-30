package com.geovis.web.controller.system;

import com.geovis.core.util.BeanUtil;
import com.geovis.core.util.ResponseUtil;
import com.geovis.core.util.StringUtil;
import com.geovis.core.util.SystemCacheUtil;
import com.geovis.web.domain.system.SysDict;
import com.geovis.web.service.system.DictService;
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

@Api(value = "DictController", description = "数据字典接口")
@RestController
@SuppressWarnings("all")
@RequestMapping("/system")
public class DictController {
    private static final Logger logger = LoggerFactory.getLogger(DictController.class);

    @Autowired
    private DictService dictService;

    /**
     * 查询方法
     * @param sysDict
     * @return
     */
    @ApiOperation(value="获取字典值", notes="获取字典值")
    @GetMapping("/dicts")
    //@RequiresPermissions(value={"system:dict:list"},logical= Logical.OR)
    public ResponseEntity listAll(@ApiParam SysDict sysDict){
        try {
            List<SysDict> dictList = this.dictService.listAll(sysDict);
            return ResponseUtil.success(new PageInfo<SysDict>(dictList));
        } catch (Exception e) {
           logger.error("[listAll Exception]:"+e.getMessage());
           return ResponseUtil.failure(e.getMessage());
        }
    }

    /**
     * 添加单个字典
     * @param request
     * @param sysDict
     * @return
     */
    @ApiOperation(value="添加单个字典", notes="添加单个字典")
    @PostMapping("/dicts")
    @RequiresPermissions(value={"system:dict:add","admin"},logical= Logical.OR)
    public ResponseEntity save(HttpServletRequest request,
                               @ApiParam @RequestBody SysDict sysDict){
        try {
            if(StringUtil.isEmpty(sysDict.getType()) || StringUtil.isEmpty(sysDict.getCode())){
                return ResponseUtil.failure("参数错误！");
            }

            //设置创建信息
            BeanUtil.setCreateUser(request, sysDict);

            //设置更新信息
            BeanUtil.setUpdateUser(request, sysDict);

            this.dictService.commonSave(SystemCacheUtil.getUserByRequest(request), sysDict);
            return ResponseUtil.success(sysDict);
        } catch (Exception e) {
            logger.error("[save Exception]:"+e.getMessage());
            return ResponseUtil.failure(e.getMessage());
        }
    }

    /**
     * 查看单个字典
     *
     * @param type
     * @param code
     * @return
     */
    @ApiOperation(value="查看单个字典", notes="查看单个字典")
    @GetMapping("/dicts/{type}/{code}")
    @RequiresPermissions(value={"system:dict:view","admin"},logical= Logical.OR)
    public ResponseEntity viewDict(@PathVariable(required = true) String type,
                             @PathVariable(required = true) String code){
        try {
            SysDict dict = new SysDict();
            dict.setType(type);
            dict.setCode(code);

            dict = this.dictService.selectDictByTypeAndCode(type, code);

            return ResponseUtil.success(dict);
        } catch (Exception e) {
            logger.error("[viewDict Exception]:"+e.getMessage());
            return ResponseUtil.failure(e.getMessage());
        }
    }

    /**
     * 编辑单个字典
     * @param request
     * @param sysDict
     * @return
     */
    @ApiOperation(value="编辑单个字典", notes="编辑单个字典")
    @PutMapping("/dicts")
    @RequiresPermissions(value={"system:dict:list","edit","admin"},logical= Logical.OR)
    public ResponseEntity updateDict(HttpServletRequest request,
                                 @ApiParam @RequestBody SysDict sysDict){
        try {
            if(StringUtil.isEmpty(sysDict.getType()) || StringUtil.isEmpty(sysDict.getCode())){
                return ResponseUtil.failure("参数错误！");
            }

            //设置更新信息
            BeanUtil.setUpdateUser(request, sysDict);

            this.dictService.commonSave(SystemCacheUtil.getUserByRequest(request), sysDict);
            return ResponseUtil.success(sysDict);
        } catch (Exception e) {
           logger.error("[updateDict Exception]:"+e.getMessage());
           return ResponseUtil.failure(e.getMessage());
        }
    }

    /**
     * 软删除单个字典（根据id删除）
     * @param type
     * @param code
     * @return
     */
    @ApiOperation(value="删除单个字典", notes="删除单个字典")
    @DeleteMapping("/dicts")
    @RequiresPermissions(value={"system:dict:list","remove","admin"},logical= Logical.OR)
    public ResponseEntity removeDict(HttpServletRequest request,
                                     @PathVariable(required = true) String type,
                                     @PathVariable(required = true) String code){
        try {
            SysDict result = this.dictService
                    .removeDictByTypeAndCode(SystemCacheUtil
                            .getUserByRequest(request), type, code);

            if(result != null){
                return ResponseUtil.failure("删除失败！");
            }

            return ResponseUtil.success();
        } catch (Exception e) {
            logger.error("[removeDict Exception]:"+e.getMessage());
            return ResponseUtil.failure(e.getMessage());
        }
    }

    /**
     * 根据类型查询
     *
     * @param type
     * @return
     */
    @ApiOperation(value="根据类型查询字典", notes="根据类型查询字典")
    @GetMapping("/dicts/{type}")
    public ResponseEntity listByType(@PathVariable(required = true) String type){
        try {
            List<SysDict> dictList = this.dictService.listByType(type);

            return ResponseUtil.success(dictList);
        } catch (Exception e) {
            logger.error("[listByType Exception]:"+e.getMessage());
            return ResponseUtil.failure(e.getMessage());
        }
    }
}
