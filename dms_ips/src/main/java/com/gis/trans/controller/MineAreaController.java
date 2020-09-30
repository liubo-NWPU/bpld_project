package com.gis.trans.controller;

import com.gis.trans.model.MineArea;
import com.gis.trans.model.ResponseModel;
import com.gis.trans.service.MineAreaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "MineAreaController", description = "矿场操作")
@RestController
@RequestMapping("/mineAreaService")
public class MineAreaController {

    @Autowired
    private MineAreaService mineAreaService;

    @ApiOperation(value = "增加/修改")
    @RequestMapping(value = "/addMineArea", method = RequestMethod.POST)
    public ResponseModel getMineAreaData(@RequestBody MineArea mineArea,String userId) {
        return  mineAreaService.addMineArea(mineArea,userId);
    }

    @ApiOperation(value = "删除")
    @RequestMapping(value = "/deleteMineArea", method = RequestMethod.POST)
    public ResponseModel deleteData(@RequestParam(value="mineId[]") Long[] mineIds) {
        return mineAreaService.deleteAllMineArea(mineIds);
    }


    @ApiOperation(value = "根据矿场name查询")
    @RequestMapping(value = "/searchByName", method = RequestMethod.GET)
    public List<MineArea> searchByName(String name,String userId) {
        return  mineAreaService.searchByName(name,userId);
    }

    @ApiOperation(value = "根据矿场id查询")
    @RequestMapping(value = "/searchByMineId", method = RequestMethod.POST)
    public MineArea searchByMineId(@RequestParam Long mineId,@RequestParam String userId) {
        return  mineAreaService.searchByMineId(mineId,userId);
    }

    @ApiOperation(value = "多条件查询矿场")
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public ResponseModel search(@RequestBody MineArea mineArea,String userId) {
        return  mineAreaService.search(mineArea,userId);
    }
}
