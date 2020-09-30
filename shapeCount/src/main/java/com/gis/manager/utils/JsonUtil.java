package com.gis.manager.utils;

import com.alibaba.fastjson.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author:wangmeng
 * @Date:创建于2018/1/3 10:14
 * @Description:
 */
public class JsonUtil {
    public static List<Long> convertJSONArrayToLongList(JSONArray jsonArray){
        List<Long> list=new ArrayList<Long>();
        for(int i=0;i<jsonArray.size();i++){
            list.add(jsonArray.getLong(i));
        }
        return list;
    }
}
