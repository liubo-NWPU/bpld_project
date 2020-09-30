package com.gis.trans.service.impl.speed;

import com.gis.trans.model.ResponseModel;
import com.gis.trans.service.CollectAreaSpeedService;
import org.springframework.stereotype.Service;

@Service
public class CollectAreaSpeedServiceImpl implements CollectAreaSpeedService {
    public ResponseModel collectSpeed(String radarName,
                                      String startTime,
                                      String endTime,
                                      int day,
                                      String points) {
        ResponseModel responseModel = new ResponseModel();
        System.out.println("进入面");
        responseModel.setSuccess(true);
        return responseModel;
    }
}
