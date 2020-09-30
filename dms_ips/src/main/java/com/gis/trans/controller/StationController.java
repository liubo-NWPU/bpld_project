//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.gis.trans.controller;

import com.gis.trans.model.ResponseModel;
import com.gis.trans.service.StationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(
        value = "StationController",
        description = "GPS数据点位信息表"
)
@RestController
@RequestMapping({"/stationService"})
public class StationController {
    @Autowired
    private StationService stationService;

    public StationController() {
    }

    @ApiOperation("查询所有")
    @GetMapping({"/searchAll"})
    public ResponseModel searchAll() {
        return this.stationService.searchAll();
    }
}
