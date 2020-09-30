package com.gis.manager.service;

import com.gis.manager.model.ShapePoint;
import org.springframework.ui.ModelMap;

/**
 * @Author:wangmeng
 * @Date:创建于2018/3/21 17:03
 * @Description:
 */
public interface IShapePointService extends IBaseService<ShapePoint,String> {

//    ModelMap MaxMin(Long fileId);

//    ModelMap closedPoint(Long fileId,Double x,Double y);

    ModelMap statitcsSpeed(String radar,String startTime,String endTime);

    ModelMap statitcsRange(String radar,String startTime,String endTime);

//    ModelMap sifteShape(String polygon,Long fileId);

    ModelMap test(String radar,Long fileId,Long m,Long n);

    ModelMap ct();
}
