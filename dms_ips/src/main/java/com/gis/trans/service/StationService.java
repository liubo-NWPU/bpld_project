package com.gis.trans.service;

import com.gis.trans.model.ResponseModel;
import com.gis.trans.model.Station;

import java.util.List;

public interface StationService {
    ResponseModel searchAll();

    List<Station> findByMStationName(String stationName);
}