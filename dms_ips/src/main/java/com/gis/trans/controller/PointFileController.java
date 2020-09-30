package com.gis.trans.controller;

import com.gis.trans.model.ReqColor;
import com.gis.trans.model.ResponseModel;
import com.gis.trans.service.PointFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Api(value = "PointFileController", description = "测试操作")
@Controller
@RequestMapping("/pointfile")
public class PointFileController {

    @Autowired
    private PointFileService pointFileService;

    @ApiOperation(value = "查看点云文件色带和id")
    @ResponseBody
    @GetMapping("/pointinfo")
    public ResponseModel pointFile(@RequestParam Long fileId) {
        return pointFileService.pointFile(fileId);
    }

    @ApiOperation(value = "色带修改")
    @ResponseBody
    @PostMapping("/colorchange")
    public ResponseModel colorChange(@RequestBody ReqColor reqColor) {
        Long fileId = reqColor.getFileid();
        String colorJson = reqColor.getColorJson();
        return pointFileService.colorChange(fileId,colorJson);
    }
}
