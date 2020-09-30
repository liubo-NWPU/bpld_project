package com.geovis.web.dao.system.mapper;

import org.apache.ibatis.annotations.Select;

import com.geovis.web.base.mapper.BaseMapper;
import com.geovis.web.domain.system.SysDict;

public interface SysDictMapper extends BaseMapper<SysDict> {
    @Select("SELECT MAX(sd.sort) FROM sys_dict sd WHERE sd.del_flag = 'NORMAL' AND sd.type = #{type}")
    Integer selectMaxSort(String type);
}