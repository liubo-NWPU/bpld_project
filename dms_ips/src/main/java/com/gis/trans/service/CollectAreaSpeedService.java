package com.gis.trans.service;
import com.gis.trans.model.ResponseModel;

public interface CollectAreaSpeedService {
    ResponseModel collectSpeed(String radarName,
                               String startTime,
                               String endTime,
                               int day,
                               String points);
}
