package com.gis.trans.controller;

import com.gis.trans.model.ResponseModel;
import com.gis.trans.model.Tesaaa;
import com.gis.trans.service.TesaaaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "TesaaaController", description = "雷达数据采集表")
@RestController
@RequestMapping("/tesaaaService")
public class TesaaaController {

    @Autowired
    private TesaaaService tesaaaService;

    @ApiOperation(value = "查询所有")
    @PostMapping("/insert")
    public ResponseModel searchAll(@RequestBody Tesaaa t){
        return tesaaaService.insertaaa(t);
    }

}
