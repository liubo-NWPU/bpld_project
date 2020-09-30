package com.geovis.web.dao.system.mapper;

import com.geovis.web.base.mapper.BaseMapper;
import com.geovis.web.domain.system.SysLog;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;
@Mapper
public interface SysLogMapper extends BaseMapper<SysLog> {

    @Select("select id," +
            "type," +
            "description," +
            "ip," +
            "browser," +
            "uri," +
            "method," +
            "params," +
            "exception," +
            "create_by," +
            "creater," +
            "create_date," +
            "update_by," +
            "update_date," +
            "del_flag," +
            "ssxt "+"FROM sys_log WHERE create_by =#{createrBy}")
    @Results(id = "SysLogMap", value = {
            @Result(column = "id", property = "id", javaType = String.class),
            @Result(column = "type", property = "type", javaType = String.class),
            @Result(column = "description", property = "description", javaType = String.class),
            @Result(column = "ip", property = "ip", javaType = String.class),
            @Result(column = "browser", property = "browser", javaType = String.class),
            @Result(column = "uri", property = "uri", javaType = String.class),
            @Result(column = "method", property = "method", javaType = String.class),
            @Result(column = "params", property = "params", javaType = String.class),
            @Result(column = "exception", property = "exception", javaType = String.class),
            @Result(column = "params", property = "params", javaType = String.class),
            @Result(column = "creater_by", property = "createrBy", javaType = String.class),
            @Result(column = "creater", property = "creater", javaType = String.class),
            @Result(column = "create_date", property = "createDate", javaType = Date.class),
            @Result(column = "update_by", property = "updateBy", javaType = String.class),
            @Result(column = "update_date", property = "updateDate", javaType = Date.class),
            @Result(column = "del_flag", property = "delFlag", javaType = String.class),
            @Result(column = "ssxt", property = "ssxt", javaType = String.class)})
    List<SysLog> selectByUseId(String userId);

    @Select("select id," +
            "type," +
            "description," +
            "ip," +
            "browser," +
            "uri," +
            "method," +
            "params," +
            "exception," +
            "create_by," +
            "creater," +
            "create_date," +
            "update_by," +
            "update_date," +
            "del_flag," +"ssxt "+"FROM sys_log WHERE create_date <=#{createDate}")
    @ResultMap("SysLogMap")
    List<SysLog> selectByCreateTime(String createDate);

    @Select("select id," +
            "type," +
            "description," +
            "ip," +
            "browser," +
            "uri," +
            "method," +
            "params," +
            "exception," +
            "create_by," +
            "creater," +
            "create_date," +
            "update_by," +
            "update_date," +
            "del_flag," +"ssxt "+"FROM sys_log WHERE type <=#{type}")
    @ResultMap("SysLogMap")
    List<SysLog>selectByType(String type);
}
