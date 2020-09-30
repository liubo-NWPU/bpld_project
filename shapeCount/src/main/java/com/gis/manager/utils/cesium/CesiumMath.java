package com.gis.manager.utils.cesium;

/**
 * @Author:wangmeng
 * @Date:创建于2018/12/6 15:07
 * @Description:
 */
public class CesiumMath {
    public static double EPSILON1 = 0.1;
    public static double EPSILON12 = 0.000000000001;
    public static double DEGREES_PER_RADIAN = 180.0 / Math.PI;
    public static double sign(double value){
        if(value == 0.0 || Double.isNaN(value)){
            return value;
        }
        return value>0?1:-1;
        //return Math.signum(value);
    }

    public static double toDegrees(double radians) {
        return radians * CesiumMath.DEGREES_PER_RADIAN;
    }
}
