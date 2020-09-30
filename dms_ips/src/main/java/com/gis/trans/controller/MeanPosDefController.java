//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.gis.trans.controller;

import com.gis.trans.model.MeanPosDefVo;
import com.gis.trans.model.RainData;
import com.gis.trans.model.ResponseModel;
import com.gis.trans.service.MeanPosDefService;
import com.gis.trans.utils.DateUtil;
import com.gis.trans.utils.ExcelData;
import com.gis.trans.utils.ExportExcelUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Api(
        value = "MeanPosDefController",
        description = "陇南紫金尾矿库 GPS数据点位信息表"
)
@RestController
@RequestMapping({"/meanPosDefService"})
public class MeanPosDefController {
    @Autowired
    private MeanPosDefService meanPosDefService;

    public MeanPosDefController() {
    }

    @ApiOperation("查询所有")
    @GetMapping({"/searchAll"})
    public ResponseModel searchAll() {
        return this.meanPosDefService.searchAll();
    }

    @ApiOperation("根据stationId查询GPS信息")
    @PostMapping({"/searchByStationId"})
    public ResponseModel searchByStationId(@RequestParam String stationName) {
        return this.meanPosDefService.searchByStationId(stationName);
    }

    @ApiOperation("根据时间查询")
    @PostMapping({"/searchByDate"})
    public ResponseModel searchByDate(String stationName, String startTime, String endTime) {
        return this.meanPosDefService.searchByDate(stationName, startTime, endTime);
    }

    @ApiOperation(value = "GNSS数据导出Excel", notes = "GNSS数据导出Excel")
    @GetMapping("/defDataExportExcel")
    public void excel(HttpServletRequest request, HttpServletResponse response) throws Exception {

        List<List<Object>> rows = new ArrayList(); //返回结果
        List<MeanPosDefVo> dataList = new ArrayList(); // 要导出的数据(雨量数据表))
        List<String> titles = new ArrayList(); //列标题

        try {
            String stationName = request.getParameter("stationName");
            String startTime = request.getParameter("startTime");
            String endTime = request.getParameter("endTime");
            dataList = this.meanPosDefService.exportExcel(stationName, startTime, endTime);
            ExcelData data = new ExcelData();
            data.setName("GNSS数据");
            titles.add("id");
            titles.add("logTime");
            titles.add("eventTime");
            titles.add("dataPeriod");
            titles.add("stationId");
            titles.add("stationKey");
            titles.add("baseId");
            titles.add("baseKey");
            titles.add("dN");
            titles.add("dE");
            titles.add("dH");
            titles.add("covN");
            titles.add("covNE");
            titles.add("covE");
            titles.add("covNH");
            titles.add("covEH");
            titles.add("covH");
            titles.add("type");
            titles.add("rotationAngle");
            titles.add("pDOP");
            titles.add("numSat");
            titles.add("numSatGPS");
            titles.add("isFixed");
            titles.add("minDN");
            titles.add("minDE");
            titles.add("minDH");
            titles.add("maxDN");
            titles.add("maxDE");
            titles.add("maxDH");
            titles.add("min2D");
            titles.add("max2D");
            titles.add("min3D");
            titles.add("max3D");
            titles.add("minSigmaN");
            titles.add("minSigmaE");
            titles.add("minSigmaH");
            titles.add("minSigma2D");
            titles.add("minSigma3D");
            titles.add("maxSigmaN");
            titles.add("maxSigmaE");
            titles.add("maxSigmaH");
            titles.add("maxSigma2D");
            titles.add("maxSigma3D");
            titles.add("numDataSets");
            titles.add("minTime");

            data.setTitles(titles);
            // 列对应的行数据
            for (MeanPosDefVo meanPosDefVo : dataList) {
                List<Object> row = new ArrayList();
                row.add(meanPosDefVo.getId());
                row.add(DateUtil.formatDate(meanPosDefVo.getLogTime(), "yyyy-MM-dd HH:mm:ss"));
                row.add(meanPosDefVo.getEventTime());
                row.add(meanPosDefVo.getDataPeriod());
                row.add(meanPosDefVo.getStationId());
                row.add(meanPosDefVo.getStationKey());
                row.add(meanPosDefVo.getBaseId());
                row.add(meanPosDefVo.getBaseKey());
                row.add(meanPosDefVo.getdN());
                row.add(meanPosDefVo.getdE());
                row.add(meanPosDefVo.getdH());
                row.add(meanPosDefVo.getCovN());
                row.add(meanPosDefVo.getCovNE());
                row.add(meanPosDefVo.getCovE());
                row.add(meanPosDefVo.getCovNH());
                row.add(meanPosDefVo.getCovEH());
                row.add(meanPosDefVo.getCovH());
                row.add(meanPosDefVo.getType());
                row.add(meanPosDefVo.getRotationAngle());
                row.add(meanPosDefVo.getpDOP());
                row.add(meanPosDefVo.getNumSat());
                row.add(meanPosDefVo.getNumSatGPS());
                row.add(meanPosDefVo.getIsFixed());
                row.add(meanPosDefVo.getMinDN());
                row.add(meanPosDefVo.getMinDE());
                row.add(meanPosDefVo.getMinDH());
                row.add(meanPosDefVo.getMaxDH());
                row.add(meanPosDefVo.getMaxDE());
                row.add(meanPosDefVo.getMaxDH());
                row.add(meanPosDefVo.getMin2D());
                row.add(meanPosDefVo.getMax2D());
                row.add(meanPosDefVo.getMin3D());
                row.add(meanPosDefVo.getMax3D());
                row.add(meanPosDefVo.getMinSigmaN());
                row.add(meanPosDefVo.getMinSigmaE());
                row.add(meanPosDefVo.getMinSigmaH());
                row.add(meanPosDefVo.getMinSigma2D());
                row.add(meanPosDefVo.getMinSigma3D());
                row.add(meanPosDefVo.getMaxSigmaN());
                row.add(meanPosDefVo.getMaxSigmaE());
                row.add(meanPosDefVo.getMaxSigmaH());
                row.add(meanPosDefVo.getMaxSigma2D());
                row.add(meanPosDefVo.getMaxSigma3D());
                row.add(meanPosDefVo.getNumDataSets());
                row.add(meanPosDefVo.getMinTime());

                rows.add(row);
            }
            data.setRows(rows);
            //生成本地
        /*File f = new File("D:/rainInfo.xlsx");
        FileOutputStream out = new FileOutputStream(f);
        ExportExcelUtils.exportExcel(data, out);
        out.close();*/
            ExportExcelUtils.exportExcel(response, "gnssData.xlsx", data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
