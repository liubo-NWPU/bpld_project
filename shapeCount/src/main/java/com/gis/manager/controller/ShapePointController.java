package com.gis.manager.controller;

import com.alibaba.fastjson.JSON;
import com.gis.manager.annotation.paramchecker.Check;
import com.gis.manager.model.extra.MaxMin;
import com.gis.manager.service.IShapePointService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/rest/shapepoint")
@Api(tags = "扫描点接口")
public class ShapePointController {
    @Resource
    private IShapePointService shapePointService;

    @Autowired
    private JmsTemplate jmsTemplate;

//    @RequestMapping(value = "/shapemaxmin", method = RequestMethod.GET)
//    @Check({"fileId"})
//    public ModelMap shapeMaxMin(@RequestParam Long fileId){
//        return shapePointService.MaxMin(fileId);
//    }
//
//    @RequestMapping(value = "/closedpoint", method = RequestMethod.GET)
//    @Check({"fileId","x","y"})
//    public ModelMap closedPoint(@RequestParam Long fileId,@RequestParam Double x, @RequestParam Double y){
//        return shapePointService.closedPoint(fileId,x,y);
//    }

    @RequestMapping(value = "/speed", method = RequestMethod.GET)
    @Check({"radar","startTime","endTime"})
    public ModelMap statitcs(@RequestParam String radar,@RequestParam String startTime,@RequestParam String endTime){
        return shapePointService.statitcsSpeed(radar,startTime,endTime);
    }

    @RequestMapping(value = "/range", method = RequestMethod.GET)
    @Check({"radar","startTime","endTime"})
    public ModelMap change(@RequestParam String radar,@RequestParam String startTime,@RequestParam String endTime){
        return shapePointService.statitcsRange(radar,startTime,endTime);
    }

//    @RequestMapping(value = "/sifteshape", method = RequestMethod.POST)
//    @Check({"map"})
//    public ModelMap sifteShape(@RequestBody Map map){
//        Long fileId = ((Integer)map.get("fileId")).longValue();
//        String polygon = JSON.toJSONString(map.get("polygon"));
//        return shapePointService.sifteShape(polygon,fileId);
//    }

    @RequestMapping(value = "/insertpointtest", method = RequestMethod.GET)
    public ModelMap change(@RequestParam String radar,@RequestParam Long fileId,@RequestParam Long m,@RequestParam Long n){
        return shapePointService.test(radar,fileId,m,n);
    }

    @RequestMapping(value = "/ct", method = RequestMethod.GET)
    public ModelMap ct(){
        return shapePointService.ct();
    }
}
