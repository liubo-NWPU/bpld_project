package com.gis.trans.service;

import com.gis.trans.model.EarlyWarnHist;
import com.gis.trans.model.ResponseModel;
import com.gis.trans.model.ShapePoint;

import java.util.List;

public interface EarlyWarnHistService {

    void addEarlyWarnHist(EarlyWarnHist earlyWarnHist);

    ResponseModel updateOneStatus(Long id);

    ResponseModel deleteOne(Long id);

    void addEarlyInf(List<ShapePoint> list, Long fileId,Long radarId);

    List<EarlyWarnHist> findByRadarId(Long radarId);
}
