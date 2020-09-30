package com.gis.trans.service;

import com.gis.trans.model.EarlyWarn;
import com.gis.trans.model.ResponseModel;

import java.util.List;

public interface EarlyWarnService {

    ResponseModel saveInf(EarlyWarn earlyWarn,String userId);

    ResponseModel deleteOne(Long id);

    EarlyWarn selectByRadarId(Long radarId);

    List<EarlyWarn> selectListByUserId(String userId);
}
