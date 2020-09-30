//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.gis.trans.service;

import com.gis.trans.model.MeanPosDefVo;
import com.gis.trans.model.ResponseModel;

import java.util.List;

public interface MeanPosDefService {

    ResponseModel searchAll();

    ResponseModel searchByStationId(String stationName);

    ResponseModel searchByDate(String stationName, String var2, String var3);

    List<MeanPosDefVo> exportExcel(String stationName, String startTime, String endTime);
}
