//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.gis.trans.service;

import com.gis.trans.model.MeanPosDefNew;
import com.gis.trans.model.ResponseModel;

import java.util.List;

public interface MeanPosDefNewService {
    ResponseModel searchAll();

    List<MeanPosDefNew> searchByStationId(Integer var1);

    List<MeanPosDefNew> searchByDate(Integer var1, String var2, String var3);
}
