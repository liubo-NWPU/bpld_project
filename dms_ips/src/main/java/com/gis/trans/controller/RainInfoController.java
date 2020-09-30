package com.gis.trans.controller;

import com.gis.trans.model.ResponseModel;
import com.gis.trans.service.RainInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "RainInfoController", description = "水量计分布")
@RestController
@RequestMapping("/rainInfoService")
public class RainInfoController {

    @Autowired
    private RainInfoService rainInfoService;

    @ApiOperation(value = "查询所有")
    @GetMapping("/searchAll")
    public ResponseModel searchAll() {
        return rainInfoService.searchAll();
    }
}
