package com.gis.trans.service;

import com.gis.trans.model.Radar;
import com.gis.trans.model.ResponseModel;

import java.util.List;

public interface RadarService extends IBaseService<Radar, Long> {

    ResponseModel addRadar(Radar radar,String userId);

    ResponseModel deleteAllRadar(Long[] ids);

    ResponseModel deleteRadar(Long id);

    List<Radar> queryAll();

    List<Radar> searchByName(String name,String userId);

    Radar checkByName(String name);

    Radar searchByRadarId(Long radarId,String userId);

    Radar findByRadarKey(String radarKey);

    Radar findByRadarName(String radarName);

    ResponseModel search(Radar radar,String userId);

}
