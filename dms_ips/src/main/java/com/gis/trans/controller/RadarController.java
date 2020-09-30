package com.gis.trans.controller;

import com.gis.trans.model.Radar;
import com.gis.trans.model.ResponseModel;
import com.gis.trans.service.RadarService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "RadarController", description = "雷达操作")
@RestController
@RequestMapping("/radarService")
public class RadarController {
    
    @Autowired
    private RadarService radarService;

    @ApiOperation(value = "增加/修改")
    @RequestMapping(value = "/addRadar", method = RequestMethod.POST)
    public ResponseModel getRadarData(@RequestBody Radar radar,String userId) {
        return  radarService.addRadar(radar,userId);
    }

    @ApiOperation(value = "删除")
    @RequestMapping(value = "/deleteRadar", method = RequestMethod.POST)
    public ResponseModel deleteData(@RequestParam(value="radarId[]")Long[] radarIds) {
        return radarService.deleteAllRadar(radarIds);
    }

    @ApiOperation(value = "根据雷达name查询")
    @RequestMapping(value = "/searchByName", method = RequestMethod.GET)
    public List<Radar> searchByName(String name,String userId) {
        return  radarService.searchByName(name,userId);
    }
    @ApiOperation(value = "根据雷达id查询")
    @RequestMapping(value = "/searchByRadarId", method = RequestMethod.GET)
    public Radar searchByRadarId(@RequestParam Long radarId,String userId) {
        return radarService.searchByRadarId(radarId,userId);
    }
//by max
    @ApiOperation(value = "多添件查询雷达")
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public ResponseModel search(@RequestBody Radar radar,String userId) {
        return radarService.search(radar,userId);
    }
}
