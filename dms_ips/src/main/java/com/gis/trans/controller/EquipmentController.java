package com.gis.trans.controller;

import com.alibaba.fastjson.JSONObject;
import com.gis.trans.model.Equipment;
import com.gis.trans.model.ResponseModel;
import com.gis.trans.service.EquipmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Api(value = "EquipmentController", description = "视频设备操作")
@RestController
@RequestMapping("/equipmentService")
public class EquipmentController {
    
    @Autowired
    private EquipmentService equipmentService;

    @ApiOperation(value = "增加")
    @RequestMapping(value = "/addEquipment", method = RequestMethod.POST)
    public ResponseModel getEquipmentData(@RequestBody Equipment equipment) {
        equipment.setDate(new Date());
        return  equipmentService.addEquipment(equipment);

    }

    @ApiOperation(value = "删除")
    @RequestMapping(value = "/deleteEquipment", method = RequestMethod.POST)
    public ResponseModel deleteData(@RequestBody String param) {
        JSONObject jsonObject = JSONObject.parseObject(param);
        return equipmentService.deleteEquipment(jsonObject.getLong("id"));

    }

    @ApiOperation(value = "修改")
    @RequestMapping(value = "/updateEquipment", method = RequestMethod.POST)
    public ResponseModel updateEquipment(Equipment equipment) {
        return equipmentService.updateEquipment(equipment);
    }

    @ApiOperation(value = "查询所有")
    @RequestMapping(value = "/findAll", method = RequestMethod.POST)
    public ResponseModel findAll(@RequestParam(required =false) Integer queryPage,@RequestParam(required =false) Integer pageSize) {
        ResponseModel responseModel = new ResponseModel();
        List<Equipment> allEquip = equipmentService.findAllEquip();
        int countResult = allEquip.size();
        if (allEquip.size()<1){
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
        allEquip = allEquip.subList(fromIndex, toIndex);
        responseModel.setSuccess(true);
        jsonObject.put("list",allEquip);
        jsonObject.put("count",countResult);
        responseModel.setData(jsonObject);
        return responseModel;
    }


    @ApiOperation(value = "根据名称查询")
    @RequestMapping(value = "/searchByName", method = RequestMethod.GET)
    public Equipment searchByName(String name) {
        Equipment equipments = equipmentService.searchByName(name);
        return equipments;
    }
    
}
