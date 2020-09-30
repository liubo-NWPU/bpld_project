package com.gis.trans.controller;

import com.alibaba.fastjson.JSONObject;
import com.gis.trans.model.EquInf;
import com.gis.trans.model.ResponseModel;
import com.gis.trans.service.EquInfService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "EquInfController", description = "同步设备操作")
@RestController
@RequestMapping("/equInfService")
public class EquInfController {

    @Autowired
    private EquInfService equInfService;

    @ApiOperation(value = "增加")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseModel getEquipmentData(@RequestBody EquInf equInf) {
        return equInfService.saveEquInf(equInf);
    }

    @ApiOperation(value = "根据设备Id和类型查询")
    @RequestMapping(value = "/searchByEquIdAndType", method = RequestMethod.POST)
    public ResponseModel searchByEquIdAndType( String equId ,String type) {
        return equInfService.searchByEquIdAndType(equId ,type);
    }

    @ApiOperation(value = "删除")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResponseModel delete(Long id) {
        return equInfService.deleteEquInf(id);

    }

    @ApiOperation(value = "查询所有")
    @RequestMapping(value = "/findAll", method = RequestMethod.POST)
    public ResponseModel findAll(@RequestParam(required = false) Integer queryPage, @RequestParam(required = false) Integer pageSize) {
        ResponseModel responseModel = new ResponseModel();
        List<EquInf> allEquInf = equInfService.findAllEquInf();
        int countResult = allEquInf.size();
        if (allEquInf.size() < 1) {
            return null;
        }
        int queryPageParam = 1;
        if (queryPage == null) {
            queryPage = queryPageParam;
        }

        int pageSizeParam = 10;
        if (pageSize == null) {
            pageSize = pageSizeParam;
        }

        int fromIndex = 0;
        if (queryPage > 1) {
            fromIndex = (queryPage - 1) * pageSize;
        }

        int toIndex = fromIndex + pageSize;
        if (toIndex > countResult) {
            toIndex = countResult;
        }
        JSONObject jsonObject = new JSONObject();
        allEquInf = allEquInf.subList(fromIndex, toIndex);
        responseModel.setSuccess(true);
        jsonObject.put("list", allEquInf);
        jsonObject.put("count", countResult);
        responseModel.setData(jsonObject);
        return responseModel;
    }

    @ApiOperation(value = "根据id查询")
    @RequestMapping(value = "/searchById", method = RequestMethod.GET)
    public ResponseModel searchById(Long id) {
        return equInfService.searchById(id);
    }

    @ApiOperation(value = "根据类型查询")
    @RequestMapping(value = "/searchByType", method = RequestMethod.GET)
    public ResponseModel searchByType(String type) {
        return equInfService.searchByType(type);
    }

    @ApiOperation(value = "根据类型和名称查询")
    @RequestMapping(value = "/searchByTypeAndEquName", method = RequestMethod.POST)
    public ResponseModel searchByTypeAndEquName(String type, String equName) {
        return equInfService.searchByTypeAndEquName(type, equName);
    }

}
