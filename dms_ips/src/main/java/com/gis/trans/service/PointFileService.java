package com.gis.trans.service;

import com.gis.trans.model.PointQujian;
import com.gis.trans.model.ResponseModel;

public interface PointFileService extends IBaseService<PointQujian, Long> {
    ResponseModel pointFile(Long fileId);
    ResponseModel pointFileByName(String fileName);
    ResponseModel colorChange(Long fileId,String colorJson);
    void save(PointQujian pointQujian);
}
