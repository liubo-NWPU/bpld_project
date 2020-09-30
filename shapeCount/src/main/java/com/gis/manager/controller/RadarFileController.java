package com.gis.manager.controller;

import com.gis.manager.model.RadarInfo;
import com.gis.manager.model.reqentity.ReqRadarInfo;
import com.gis.manager.service.IRadarInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/")
@Api(tags = "雷达接口")
public class RadarFileController {

    @Autowired
    private IRadarInfoService iRadarInfoService;

    @ApiOperation(value = "接收diffimage")
    @RequestMapping(value = "/plotService/radarfile", method = RequestMethod.POST)
    public ModelMap radarFile(@RequestParam("data") MultipartFile data, @RequestParam String radar){
        return iRadarInfoService.radarFile(data,radar);
    }

    @ApiOperation(value = "接收雷达基本信息")
    @RequestMapping(value = "/minesafe/keep", method = RequestMethod.GET)
    public ModelMap radarKeep(@RequestParam String radar,
                              @RequestParam Double scaninterval,
                              @RequestParam Double scanlen,
                              @RequestParam Double observemin,
                              @RequestParam Double observemax,
                              @RequestParam Double diskfree,
                              @RequestParam Long radarstatus,
                              @RequestParam Long trackstatus){
        return iRadarInfoService.radarStatus(radar,scaninterval,scanlen,observemin,observemax,diskfree,radarstatus,trackstatus);
    }

    //为某一个雷达增加一个日期表
    @ApiOperation(value = "指定雷达日期建表接口")
    @RequestMapping(value = "/plotService/radartime", method = RequestMethod.POST)
    public ModelMap radarTime(@RequestParam String radar, @RequestParam String time){
        return iRadarInfoService.radarTime(radar,time);
    }

    @ApiOperation(value = "雷达电源偏移量")
    @RequestMapping(value = "/plotService/drift", method = RequestMethod.POST)
    public ModelMap radarTime(@RequestParam String radar, @RequestParam double driftX, @RequestParam double driftY, @RequestParam double driftZ){
        return iRadarInfoService.drift(radar,driftX,driftY,driftZ);
    }

    @ApiOperation(value = "新增雷达")
    @RequestMapping(value = "/plotService/radaradd", method = RequestMethod.POST)
    public ModelMap radarTime(@RequestBody ReqRadarInfo reqRadarInfo){
        return iRadarInfoService.radarAdd(reqRadarInfo);
    }

//
//    @ApiOperation(value = "雷达删除")
//
    @ApiOperation(value = "雷达列表查询")
    @RequestMapping(value = "/plotService/radarsearch", method = RequestMethod.POST)
    public ModelMap radarsearch(@RequestParam String radarname){
        return iRadarInfoService.radarsearch(radarname);
    }

    @ApiOperation(value = "雷达信息修改")
    @RequestMapping(value = "/plotService/radaredit", method = RequestMethod.POST)
    public ModelMap radarsearch(@RequestBody RadarInfo radarInfo){
        return iRadarInfoService.radaredit(radarInfo);
    }
}
