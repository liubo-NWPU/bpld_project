package com.gis.trans.controller;

import com.gis.trans.model.ResponseModel;
import com.gis.trans.service.DataRadarService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "DataRadarController", description = "雷达数据采集表")
@RestController
@RequestMapping("/dataRadarService")
public class DataRadarController {

    @Autowired
    private DataRadarService dataRadarService;

    @ApiOperation(value = "查询所有")
    @GetMapping("/searchAll")
    public ResponseModel searchAll(){
        return dataRadarService.searchAll();
    }

    @ApiOperation(value = "根据eviceId查询库最新水位采集信息")
    @PostMapping("/searchByDeviceId")
    public ResponseModel searchByDeviceId(String id){
        return dataRadarService.searchByDeviceId(id);
    }


    @ApiOperation(value = "根据时间查询")
    @PostMapping("/searchByDate")
    public ResponseModel searchByDate(String id ,String startTime, String endTime) {
        return dataRadarService.searchByDate(id ,startTime,endTime);
    }

}
