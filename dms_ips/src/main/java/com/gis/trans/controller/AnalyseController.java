package com.gis.trans.controller;

import com.gis.trans.model.ResponseModel;
import com.gis.trans.service.CollectAreaShiftService;
import com.gis.trans.service.CollectAreaSpeedService;
import com.gis.trans.service.CollectPointShiftService;
import com.gis.trans.service.CollectPointSpeedService;
import com.gis.trans.utils.JudgePointAreaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AnalyseController {

    //单个点速度
    @Autowired
    private CollectPointSpeedService collectPointSpeedService;

    //区域的点速度
    @Autowired
    private CollectAreaSpeedService collectAreaSpeedService;

    //单个点位移
    @Autowired
    private CollectPointShiftService collectPointShiftService;

    //区域的点位移
    @Autowired
    private CollectAreaShiftService collectAreaShiftService;

    //速度
    @PostMapping("/collectSpeed")
    public ResponseModel collectSpeed(@RequestParam String radarName,
                                      @RequestParam String startTime,
                                      @RequestParam String endTime,
                                      @RequestParam int day,
                                      @RequestParam String points) {
        ResponseModel responseModel = new ResponseModel();

        if (JudgePointAreaUtil.judge(points)) {
            return collectPointSpeedService.collectSpeed(radarName, startTime, endTime, day, points);
        } else {
            return collectAreaSpeedService.collectSpeed(radarName, startTime, endTime, day, points);
        }
    }

    //加速度
    @PostMapping("/collectAccspeed")
    public ResponseModel collectAccspeed(@RequestParam String radarName,
                                         @RequestParam String startTime,
                                         @RequestParam String endTime,
                                         @RequestParam int day,
                                         @RequestParam List points) {
        ResponseModel responseModel = new ResponseModel();

        return responseModel;
    }

    //位移
    @PostMapping("/collectShift")
    public ResponseModel collectShift(@RequestParam String radarName,
                                      @RequestParam String startTime,
                                      @RequestParam String endTime,
                                      @RequestParam int day,
                                      @RequestParam String points) {
        ResponseModel responseModel = new ResponseModel();

        if (JudgePointAreaUtil.judge(points)) {
            //进入点收集
            return collectPointShiftService.collectShift(radarName, startTime, endTime, day, points);
        } else {
            //进入区域收集
            return collectAreaShiftService.collectShift(radarName, startTime, endTime, day, points);
        }

    }
}
