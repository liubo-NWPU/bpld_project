package com.geovis.web.dao.system.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.geovis.web.base.mapper.BaseMapper;
import com.geovis.web.domain.system.SysDatalog;

public interface SysDatalogMapper extends BaseMapper<SysDatalog> {

    //获取最大版本号
    @Select("select max(sd.version_number) from sys_datalog sd where sd.table_name= #{tableName} and sd.data_id= #{dataId}")
    Integer getMaxVersionNum(@Param("tableName") String tableName, @Param("dataId") String dataId);

}