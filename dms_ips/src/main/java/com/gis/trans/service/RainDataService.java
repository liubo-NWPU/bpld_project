package com.gis.trans.service;

import com.gis.trans.model.RainData;
import com.gis.trans.model.ResponseModel;

import java.util.List;

public interface RainDataService {

    ResponseModel searchAll();

    ResponseModel searchByRDAddr(Integer rDAddr);

    ResponseModel searchByDate(Integer rDAddr, String startTime, String endTime);

    List<RainData> rainDataExportExcel(String rDAddr , String startTime, String endTime);
}
