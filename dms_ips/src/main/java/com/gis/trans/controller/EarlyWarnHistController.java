package com.gis.trans.controller;

import com.alibaba.fastjson.JSONObject;
import com.gis.trans.model.EarlyWarnHist;
import com.gis.trans.model.ResponseModel;
import com.gis.trans.service.EarlyWarnHistService;
import com.gis.trans.utils.MrpUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Api(value = "EarlyWarnHistController", description = "预警记录操作")
@RestController
@RequestMapping("/earlyWarnHistService")
public class EarlyWarnHistController {
    
    @Autowired
    private EarlyWarnHistService earlyWarnHistService;


    @Value("${auth.local_url}")
    private String authUrl;

    @ApiOperation(value = "更新单个预警记录状态")
    @RequestMapping(value = "/updateOneStatus", method = RequestMethod.POST)
    public ResponseModel updateOneStatus(@RequestParam Long id) {
        ResponseModel responseModel = earlyWarnHistService.updateOneStatus(id);
        return responseModel;
    }
    @ApiOperation(value = "删除单个预警记录")
    @RequestMapping(value = "/deleteOne", method = RequestMethod.POST)
    public ResponseModel deleteOne(@RequestParam Long id) {
        ResponseModel responseModel = earlyWarnHistService.deleteOne(id);
        return responseModel;
    }

    @ApiOperation(value = "根据radarId查询预警记录")
    @RequestMapping(value = "/findByRadarId", method = RequestMethod.POST)
    public ResponseModel findByRadarId(@RequestParam Long radarId ,@RequestParam(required =false) Integer queryPage,@RequestParam(required =false) Integer pageSize,String userId) {
        ResponseModel responseModel = new ResponseModel();
        List<Long> mrpList = MrpUtils.getMrp(authUrl,userId);
        if (!mrpList.contains(radarId)){
            responseModel.setSuccess(false);
            responseModel.setMessage("该用户无设置权限");
            return responseModel;
        }
        List<EarlyWarnHist> colls = new ArrayList<>();
        colls = earlyWarnHistService.findByRadarId(radarId);
        int countResult = colls.size();

        if (colls.size()<1){
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
        colls = colls.subList(fromIndex, toIndex);
        responseModel.setSuccess(true);
        jsonObject.put("list",colls);
        jsonObject.put("count",countResult);
        return responseModel;
    }
    
}
