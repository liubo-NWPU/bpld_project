package com.gis.trans.service;

import com.gis.trans.model.StationNew;

import java.util.List;

public interface StationNewService {
    List<StationNew> searchAll();

    List<StationNew> findByMStationName(String stationName);

}