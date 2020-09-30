package com.gis.trans.controller;

import com.gis.trans.model.ResponseModel;
import com.gis.trans.service.RadarsBRService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.Date;

@Api(value = "RadarsBRController", description = "雷达 点表名")
@RestController
@RequestMapping("/radarsBRService")
public class RadarsBRController {

    @Autowired
    private RadarsBRService radarsBRService;

    @ApiOperation(value = "查询所有")
    @GetMapping("/searchAll")
    public ResponseModel searchAll(){
        return radarsBRService.searchAll();
    }

}
