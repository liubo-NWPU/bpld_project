package com.gis.trans.controller;

import com.gis.trans.model.RainData;
import com.gis.trans.model.ResponseModel;
import com.gis.trans.service.RainDataService;
import com.gis.trans.utils.DateUtil;
import com.gis.trans.utils.ExcelData;
import com.gis.trans.utils.ExportExcelUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Api(value = "RainDataController", description = "雷达数据采集表")
@RestController
@RequestMapping("/rainDataService")
public class RainDataController {

    @Autowired
    private RainDataService rainDataService;

    @ApiOperation(value = "查询所有")
    @GetMapping("/searchAll")
    public ResponseModel searchAll(){
        return rainDataService.searchAll();
    }

    @ApiOperation(value = "根据rDAddr查询采集信息")
    @PostMapping("/searchByRDAddr")
    public ResponseModel searchByRDAddr(Integer rDAddr){
        return rainDataService.searchByRDAddr(rDAddr);
    }


    @ApiOperation(value = "根据时间查询")
    @PostMapping("/searchByDate")
    public ResponseModel searchByDate(Integer rDAddr,String startTime, String endTime) {
        return rainDataService.searchByDate(rDAddr ,startTime,endTime);
    }

    @ApiOperation(value = "雨量数据导出Excel", notes = "雨量数据导出Excel")
    @GetMapping("/rainDataExportExcel")
    public void excel(HttpServletRequest request, HttpServletResponse response) throws Exception {

        List<List<Object>> rows = new ArrayList(); //返回结果
        List<RainData> rainDataList = new ArrayList(); // 要导出的数据(雨量数据表))
        List<String> titles = new ArrayList(); //列标题

        try {
            String rDAddr = request.getParameter("rDAddri");
            String startTime = request.getParameter("startTime");
            String endTime = request.getParameter("endTime");
            rainDataList = this.rainDataService.rainDataExportExcel(rDAddr, startTime, endTime);
            ExcelData data = new ExcelData();
            data.setName("雨量数据");
            titles.add("id");
            titles.add("地区代号");
            titles.add("日期");
            titles.add("总量");
            titles.add("单位");
            titles.add("降雨数据");
            titles.add("温度");
            data.setTitles(titles);

            // 列对应的行数据
            for (RainData rainData : rainDataList) {
                List<Object> row = new ArrayList();
                row.add(rainData.getId());
                row.add(rainData.getrDAddr());
                row.add(DateUtil.formatDate(rainData.getrDDate(), "yyyy-MM-dd HH:mm:ss"));
                row.add(rainData.getrDSum());
                row.add(rainData.getrDUnit());
                row.add(rainData.getrDData());
                row.add(rainData.getrDTemp());
                rows.add(row);
            }
            data.setRows(rows);
            //生成本地
        /*File f = new File("D:/rainInfo.xlsx");
        FileOutputStream out = new FileOutputStream(f);
        ExportExcelUtils.exportExcel(data, out);
        out.close();*/
            ExportExcelUtils.exportExcel(response, "rainData.xlsx", data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
