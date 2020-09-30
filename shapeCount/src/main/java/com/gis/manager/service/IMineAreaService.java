package com.gis.manager.service;

import com.gis.manager.model.MineArea;
import com.gis.manager.model.RadarInfo;
import com.gis.manager.model.reqentity.ReqMinearea;
import com.gis.manager.model.reqentity.ReqRadarInfo;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IMineAreaService extends IBaseService<MineArea,Long> {

    ModelMap mimnareaAdd(ReqMinearea reqMinearea);

    ModelMap mineAreaSearch(String mineareaname);

    ModelMap mineareaedit(MineArea mineArea);

}
