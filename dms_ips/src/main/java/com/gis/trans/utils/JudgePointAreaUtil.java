package com.gis.trans.utils;


import java.util.ArrayList;
import java.util.List;

public class JudgePointAreaUtil {

    public static boolean judge(String points) {
        //String s = "[21.9272898319766, 44.41452651738163],[21.931026298280752, 44.413510104658144],[21.929106717224283, 44.4110344215659],[21.92639794447511, 44.4114633594325],[21.925516667940407, 44.41275006356679]";
        //String s = "[21.9272898319766, 44.41452651738163]";
        String[] split = points.split(",");
        List<List<Double>> listOut = new ArrayList();
        List<Double> listInner = null;
        int count = 1;
        for (String str : split) {
            str = str.replaceAll("\\[", "").replaceAll("]", "").trim();
            Double floatStr = Double.parseDouble(str);
            if (count % 2 != 0) {
                listInner = new ArrayList();
            }
            listInner.add(floatStr);
            if (count % 2 == 0) {
                listOut.add(listInner);
            }
            count++;
        }
        //listOut.size()=1 代表是单个点
        if (listOut.size() == 1) {
            return true;
        //代表是区域
        } else {
            return false;
        }
    }
}
