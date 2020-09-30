package com.gis.manager.controller;

import com.gis.manager.model.MineArea;
import com.gis.manager.model.reqentity.ReqMinearea;
import com.gis.manager.service.IMineAreaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
@Api(tags = "矿场接口")
public class MineAreaController {

    @Autowired
    private IMineAreaService iMineAreaService;

    @ApiOperation(value = "新增矿区")
    @RequestMapping(value = "/mimnareaadd", method = RequestMethod.POST)
    public ModelMap radarTime(@RequestBody ReqMinearea reqMinearea){
        return iMineAreaService.mimnareaAdd(reqMinearea);
    }

    //
//    @ApiOperation(value = "雷达删除")
//
    @ApiOperation(value = "矿区列表查询")
    @RequestMapping(value = "/mimnareasearch", method = RequestMethod.POST)
    public ModelMap radarsearch(@RequestParam String mineareaname){
        return iMineAreaService.mineAreaSearch(mineareaname);
    }

    @ApiOperation(value = "矿区信息修改")
    @RequestMapping(value = "/mimnareaedit", method = RequestMethod.POST)
    public ModelMap radarsearch(@RequestBody MineArea mineArea){
        return iMineAreaService.mineareaedit(mineArea);
    }
}
