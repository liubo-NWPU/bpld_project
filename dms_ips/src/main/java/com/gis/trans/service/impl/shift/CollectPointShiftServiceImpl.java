package com.gis.trans.service.impl.shift;

import com.gis.trans.model.Radar;
import com.gis.trans.model.ResponseModel;
import com.gis.trans.model.ShapePoint;
import com.gis.trans.service.CollectPointShiftService;
import com.gis.trans.service.PlotInfSevice;
import com.gis.trans.service.RadarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollectPointShiftServiceImpl implements CollectPointShiftService {

    @Autowired
    private RadarService radarService;

    @Autowired
    private PlotInfSevice plotInfSevice;

    @Override
    public ResponseModel collectShift(String radarName, String startTime, String endTime, int day, String points) {
        ResponseModel responseModel = new ResponseModel();
        //String res=null;
        System.out.println("搜集点位移");

        Radar radarInfo = radarService.findByRadarName(radarName.trim());
        if (radarInfo == null) {
            responseModel.setSuccess(false);
            responseModel.setMessage("雷达不存在!");
            return responseModel;
        }
        String radarKey = radarInfo.getRadarKey();
        System.out.println("radarKey="+radarKey);

        List<ShapePoint> plotList = plotInfSevice.getPointByTime(radarKey,startTime,endTime);
        if (plotList == null) {
            responseModel.setSuccess(false);
            responseModel.setMessage("查询失败!");
            return responseModel;
        }
        //System.out.println("res="+res);
        responseModel.setSuccess(true);
        responseModel.setData(plotList);
        return responseModel;
    }

}
