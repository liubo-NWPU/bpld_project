package com.gis.trans.service;

import com.gis.trans.model.ResponseModel;

public interface DataRadarService {

    ResponseModel searchAll();

    ResponseModel searchByDeviceId(String id);

    ResponseModel searchByDate(String id ,String startTime, String endTime);

}
