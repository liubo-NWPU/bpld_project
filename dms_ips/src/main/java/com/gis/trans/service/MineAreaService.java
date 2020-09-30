package com.gis.trans.service;

import com.gis.trans.model.MineArea;
import com.gis.trans.model.ResponseModel;

import java.util.List;

public interface MineAreaService extends IBaseService<MineArea, Long> {
    ResponseModel addMineArea(MineArea mineArea,String userID);

    ResponseModel deleteMineArea(Long mineId);

    ResponseModel deleteAllMineArea(Long[] ids);

    List<MineArea> queryAll();

    List<MineArea> searchByName(String name,String userId);

    MineArea searchByMineId(Long mineId,String userId);

    ResponseModel search(MineArea mineArea,String userId);
}
