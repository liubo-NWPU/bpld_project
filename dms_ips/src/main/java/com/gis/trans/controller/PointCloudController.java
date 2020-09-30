package com.gis.trans.controller;

import com.gis.trans.model.*;
import com.gis.trans.service.PlotInfSevice;
import com.gis.trans.service.RadarService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.gdal.osr.CoordinateTransformation;
import org.gdal.osr.SpatialReference;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.swing.*;
import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Api(value = "TestController", description = "测试操作")
@Controller
@RequestMapping("/test")
public class PointCloudController {

    private static final Logger logger = LoggerFactory.getLogger(PointCloudController.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private PlotInfSevice plotInfSevice;

    @Autowired
    private RadarService radarService;

    /**
     * 通过一个diffimage文件和点云数据进行配准，识别出点云中匹配成功的点的M,N坐标，并保存到数据库
     * @param diffimagePath
     * @param pointCloudPath
     * @param radarName
     * @return
     */
    @ApiOperation(value = "单个diffimage配准坐标")
    @ResponseBody
    @GetMapping("/dtop")
    public ResponseModel dtop(String diffimagePath,String pointCloudPath,String radarName) {
        return plotInfSevice.dtop(diffimagePath,pointCloudPath,radarName);
    }

    /**
     * @param radarName
     * @param startTime
     * @param endTime
     */
    @ApiOperation(value = "点云生成变形图接口")
    @ResponseBody
    @GetMapping("/ptostrain")
    public ResponseModel ptostrain(@RequestParam String radarName, @RequestParam String startTime, @RequestParam String endTime, @RequestParam Long parentId) {
        return plotInfSevice.ptostrain(radarName,startTime,endTime,parentId);
    }
}
