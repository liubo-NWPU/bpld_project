package com.gis.trans.service;

import com.gis.trans.model.EquInf;
import com.gis.trans.model.ResponseModel;

import java.util.List;

public interface EquInfService extends IBaseService<EquInf, Long> {

    ResponseModel saveEquInf(EquInf equInf);

    ResponseModel deleteEquInf(Long id);

    List<EquInf> findAllEquInf();

    ResponseModel searchById(Long id);

    ResponseModel searchByType(String type);

    ResponseModel searchByTypeAndEquName(String type ,String equName);

    ResponseModel searchByEquIdAndType(String equId ,String type);

}
