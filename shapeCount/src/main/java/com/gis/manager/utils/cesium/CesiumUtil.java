package com.gis.manager.utils.cesium;

/**
 * @Author:wangmeng
 * @Date:创建于2018/12/7 10:05
 * @Description:
 */
public class CesiumUtil {
    public static double radius = 6378137.0;
    public static double computeLongitudeDt(double lat,double partDistance){
        double perDegreeLength = 2.0*Math.PI * radius / 360.0;
        double perLatLongitudeLength=perDegreeLength*Math.cos(Math.PI*lat/180.0);
        return partDistance/perLatLongitudeLength;
    }

    public static double computeLatitudeDt(double partDistance){
        double perDegreeLength = 2.0*Math.PI * radius / 360.0;
        return partDistance/perDegreeLength;
    }
}
