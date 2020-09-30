package com.gis.trans.service.impl.speed;

import com.gis.trans.model.ResponseModel;
import com.gis.trans.service.CollectPointSpeedService;
import org.springframework.stereotype.Service;

@Service
public class CollectPointSpeedServiceImpl implements CollectPointSpeedService {

    public ResponseModel collectSpeed(String radarName,
                                      String startTime,
                                      String endTime,
                                      int day,
                                      String point) {
        ResponseModel responseModel = new ResponseModel();
        System.out.println("进入点");
        return responseModel;
    }
}
