package com.gis.manager.utils;

public class DoubleUtils {
    public static int company(Double a,Double b){
        if (Double.doubleToLongBits(a) == Double.doubleToLongBits(b)){
            return 0;
        }
        else if (Double.doubleToLongBits(a) > Double.doubleToLongBits(b)){
            return 1;
        }
        else
            return -1;
    }
}
