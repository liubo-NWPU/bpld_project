package com.gis.trans.service.impl.shift;

import com.gis.trans.model.Radar;
import com.gis.trans.model.ResponseModel;
import com.gis.trans.service.CollectAreaShiftService;
import com.gis.trans.service.RadarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CollectAreaShiftServiceImpl implements CollectAreaShiftService {

    @Autowired
    private RadarService radarService;

    @Override
    public ResponseModel collectShift(String radarName, String startTime, String endTime, int day, String points) {
        ResponseModel responseModel = new ResponseModel();
        //String res=null;
        System.out.println("搜集区域的点位移");

        Radar radarInfo = radarService.findByRadarName(radarName.trim());
        String radarKey = radarInfo.getRadarKey();
        System.out.println("radarKey="+radarKey);

        // List<ShapePoint> plotList = getPointByTime(radarKey,startTime,endTime);

        //System.out.println("res="+res);
        responseModel.setSuccess(true);
        return responseModel;
    }

}
