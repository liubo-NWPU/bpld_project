package com.gis.trans.utils;

import org.apache.commons.beanutils.ConvertUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MrpUtils {

    public static List<Long> getMrp(String authUrl ,String userId){
        Map map = new HashMap<>();
        map.put("id", userId);
        String mrps = HttpClientUtil.get(authUrl+"/system/mrp", map);
        String[] split = mrps.split(",");
        Long[] convert = (Long[]) ConvertUtils.convert(split, Long.class);
        return Arrays.asList(convert);
    }
}
