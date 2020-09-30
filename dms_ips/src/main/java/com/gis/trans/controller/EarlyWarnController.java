package com.gis.trans.controller;

import com.gis.trans.model.EarlyWarn;
import com.gis.trans.model.ResponseModel;
import com.gis.trans.service.EarlyWarnService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "EarlyWarnController", description = "预警信息设置操作")
@RestController
@RequestMapping("/earlyWarnService")
public class EarlyWarnController {

    @Autowired
    private EarlyWarnService earlyWarnService;

    @ApiOperation(value = "添加/修改预警设置信息")
    @PostMapping("/save")
    public ResponseModel update(@RequestBody EarlyWarn earlyWarn,String userId){
        return earlyWarnService.saveInf(earlyWarn,userId);
    }

    @ApiOperation(value = "删除预警设置信息")
    @PostMapping("/deleteOne")
    public ResponseModel deleteOne(@RequestParam Long id){
        return earlyWarnService.deleteOne(id);
    }

    @ApiOperation(value = "根据radarId查询预警设置信息")
    @PostMapping("/selectByRadarId")
    public EarlyWarn selectByMineId(@RequestParam Long radarId){
        return earlyWarnService.selectByRadarId(radarId);
    }
    @ApiOperation(value = "根据userId查询预警设置信息")
    @PostMapping("/selectByUserId")
    public List<EarlyWarn> selectListByUserId(@RequestParam(required = false) String userId){
        return earlyWarnService.selectListByUserId(userId);
    }
}
