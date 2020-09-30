package com.gis.trans.controller;

import com.gis.trans.dao.DiffimageLogDao;
import com.gis.trans.model.*;
import com.gis.trans.mq.Producer;
import com.gis.trans.service.EarlyWarnHistService;
import com.gis.trans.service.PlotInfSevice;
import com.gis.trans.service.RadarService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Api(value = "PlotInfController", description = "diff相关点集合")
@RestController
@RequestMapping("/plotService")
public class PlotInfController {

    private static final Logger logger = LoggerFactory.getLogger(PlotInfController.class);

    @Autowired
    private Producer producer;

    @Autowired
    private PlotInfSevice plotInfSevice;

    @Autowired
    private RadarService radarService;

    private static final String sql_str = "insert into diffimage_log(radar_name,flag,before_time,this_time) values ";

    //测试
    @ApiOperation(value = "测试")
    @RequestMapping(value = "/test", method = RequestMethod.POST)
    @Transactional
    public ResponseModel test(@RequestParam String radarkey,@RequestParam String startTime,@RequestParam String endTime) {
        logger.info("start");
        plotInfSevice.getPointByTime(radarkey,startTime,endTime);
        logger.info("end");
        return new ResponseModel();
    }

    //批量上传
    @ApiOperation(value = "批量解析DiffImage文件")
    @RequestMapping(value = "/parseDiffImageMore", method = RequestMethod.POST)
    @Transactional
    public ResponseModel parseDiffImage(@RequestParam(required = false) String filePath,@RequestParam(required = false) String[] filePaths,String radar) {
        return plotInfSevice.parseDiffImage(filePath,filePaths,radar);
    }

    @ApiOperation(value = "增加")
    @RequestMapping(value = "/insertPlotList", method = RequestMethod.POST)
    public ResponseModel insertPlotList(String filePath, String radar) {
        return plotInfSevice.insertPlotList(filePath,radar);
    }

    @ApiOperation(value = "空间筛选和屏蔽")
    @RequestMapping(value = "/insertPartPlotList", method = RequestMethod.POST)
    public ResponseModel insertPartPlotList(@RequestParam(required = false) Long fileId, @RequestParam String polygon, @RequestParam(required = false) String fileName, @RequestParam Short type) {
        ResponseModel responseModel = new ResponseModel();
        List<ShapePoint> plotList = new ArrayList<ShapePoint>();
        if (type == 1) {  //空间筛选
            plotList = plotInfSevice.searchContain(fileId, polygon);
        } else { //空间屏蔽
            plotList = plotInfSevice.searchExclude(fileId, polygon);
        }
        if (plotList.size() > 0) {
            String substring = "";
            String radar = plotList.get(0).getRadar();
            Radar radarInfo = radarService.findByRadarKey(radar);
            Long parentId = -1L;
            if (radarInfo != null) {
                parentId =radarInfo.getRadarId();
            } else {
                responseModel.setSuccess(false);
                responseModel.setMessage("雷达表中无相关雷达信息");
                return responseModel;
            }
            int i = fileName.lastIndexOf(".");
            if (i != -1) {
                substring = fileName.substring(0, i) + new Date().getTime();
            } else {
                substring = fileName + new Date().getTime();
            }

            Result s = plotInfSevice.makeShape(plotList, substring, parentId);
            if (s.getFlag()) {
                responseModel.setSuccess(true);
                responseModel.setData(s.getData());
                responseModel.setMessage("解析-生成shp-上传-执行成功");

            } else {
                responseModel.setSuccess(false);
                responseModel.setMessage("解析-生成shp-上传-执行失败");
            }
        } else {
            responseModel.setSuccess(false);
            responseModel.setMessage("所选择范围不在指定区域内");
        }
        return responseModel;

    }

    @ApiOperation(value = "shape文件校正")
    @RequestMapping(value = "/updateShapeInf", method = RequestMethod.POST)
    public ResponseModel updateShapeInf(@RequestParam(required = false, defaultValue = "0.0") Double x, @RequestParam(required = false, defaultValue = "0.0") Double y, @RequestParam(required = false, defaultValue = "0.0") Double angle, Long fileid) {
        return plotInfSevice.updateShapeInf(x, y, angle, fileid);
    }

    @RequestMapping(value = "/insertPartPlotList1", method = RequestMethod.POST)
    public ResponseModel insertPartPlotList1(@RequestParam(required = false) Long fileId, @RequestBody String polygon, @RequestParam(required = false) String fileName, @RequestParam Short type) {
        ResponseModel responseModel = new ResponseModel();
        List<ShapePoint> plotList = new ArrayList<ShapePoint>();
        if (type == 1) {
            plotList = plotInfSevice.searchContain(fileId, polygon);
        } else {
            plotList = plotInfSevice.searchExclude(fileId, polygon);
        }
        if (plotList.size() > 0) {
            String substring = "";
            String radar = plotList.get(0).getRadar();
            Radar radarInfo = radarService.findByRadarKey(radar);
            Long parentId = -1L;
            if (radarInfo != null) {
                parentId = radarInfo.getRadarId();
            } else {
                responseModel.setSuccess(false);
                responseModel.setMessage("雷达表中无相关雷达信息");
                return responseModel;
            }
            int i = fileName.lastIndexOf(".");
            if (i != -1) {
                substring = fileName.substring(0, i) + new Date().getTime();
            } else {
                substring = fileName + new Date().getTime();
            }
            Result s = plotInfSevice.makeShape(plotList, substring, parentId);
            if (s.getFlag()) {
                responseModel.setSuccess(true);
                responseModel.setData(s.getData());
                responseModel.setMessage("解析-生成shp-上传-执行成功");
            } else {
                responseModel.setSuccess(false);
                responseModel.setMessage("解析-生成shp-上传-执行失败");
            }
        } else {
            responseModel.setSuccess(false);
            responseModel.setMessage("所选择范围不在指定区域内");
        }
        return responseModel;

    }

    @ApiOperation(value = "test_sd")
    @RequestMapping(value = "/findByFileId", method = RequestMethod.POST)
    public List<ShapePoint> findByFileId(Long fileId, String radar) {
        return plotInfSevice.findByFileId(fileId, radar);
    }

    @ApiOperation(value = "ted")
    @RequestMapping(value = "/getShape", method = RequestMethod.POST)
    public ResponseModel getShape(String parentId, String radar, String startTime, String endTime) {
        return plotInfSevice.getShape(parentId, radar, startTime, endTime);
    }

    @ApiOperation(value = "teda")
    @RequestMapping(value = "/getsa", method = RequestMethod.POST)
    public ResponseModel getShapesa(String path) {
        return plotInfSevice.getShapepath(path);
    }

    @ApiOperation(value = "count")
    @RequestMapping(value = "/getCount", method = RequestMethod.POST)
    public ResponseModel getCount(String radar, String startTime, String endTime) {
        return plotInfSevice.getCount(radar, startTime, endTime);
    }

    @ApiOperation(value = "getStrain")
    @RequestMapping(value = "/getStrain", method = RequestMethod.POST)
    public ResponseModel getStrain(String radar, String startTime, String endTime) {
        return plotInfSevice.getStrain(radar, startTime, endTime);
    }
}