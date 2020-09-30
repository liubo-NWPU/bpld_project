package com.gis.trans.controller;

import com.gis.trans.model.EarlyWarnPointColl;
import com.gis.trans.service.EarlyWarnPointCollService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(value = "EarlyWarnPointCollController", description = "预警信息点操作")
@RestController
@RequestMapping("/earlyWarnPointCollService")
public class EarlyWarnPointCollController {

    @Autowired
    private EarlyWarnPointCollService earlyWarnPointCollService;

    @ApiOperation(value = "根据fileId查询预警点分布信息")
    @PostMapping("/selectByFileId")
    public List<EarlyWarnPointColl> selectByMineId(@RequestParam Long fileId){
        return earlyWarnPointCollService.selectByfileId(fileId);
    }
}
