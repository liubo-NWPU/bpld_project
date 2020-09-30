package com.gis.trans.service;

import com.gis.trans.model.EarlyWarnPointColl;
import com.gis.trans.model.ResponseModel;

import java.util.List;

public interface EarlyWarnPointCollService {

    ResponseModel saveInf(EarlyWarnPointColl earlyWarnPointColl);

    ResponseModel deleteOne(Long id);

    List<EarlyWarnPointColl> selectByfileId(Long fileId);
}
