package com.gis.manager.service;

import com.gis.manager.model.RadarInfo;
import com.gis.manager.model.reqentity.ReqRadarInfo;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author:wangmeng
 * @Date:创建于2018/3/21 17:03
 * @Description:
 */
public interface IRadarInfoService extends IBaseService<RadarInfo,Long> {

    RadarInfo findOne(String radarName);

    List<RadarInfo> findAll();

    ModelMap radarFile(MultipartFile data, String radar);

    ModelMap radarStatus(String radar,Double scaninterval,Double scanlen,Double observemin,Double observemax,Double diskfree,Long radarstatus,Long trackstatus);

    ModelMap radarTime(String radar,String time);

    ModelMap drift(String radar, double driftX, double driftY, double driftZ);

    ModelMap radarAdd(ReqRadarInfo reqRadarInfo);

    ModelMap radarsearch(String radarname);

    ModelMap radaredit(RadarInfo radarInfo);
}
