package com.gis.manager.utils;

import java.util.ArrayList;
import java.util.List;

public class NUllToStrUtils {

    public static List replaceNullToStr(List list){
        List list1 = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i)==null){
                list1.add("");
            }else {
                list1.add(list.get(i));
            }
        }
        return list1;
    }
}
