package com.geovis.web.dao.system.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import com.geovis.web.base.mapper.BaseMapper;
import com.geovis.web.dao.system.sql.DemoSqlBuilder;
import com.geovis.web.domain.demo.Demo;

public interface DemoMapper extends BaseMapper<Demo> {

    /**
     * 测试demo
     * @param demo
     * @return
     */
	@Select("select * from demo where demo.id = #{id}")
	@Results(id = "DemoMap", value = { @Result(column = "id", property = "id", javaType = String.class),
			@Result(column = "name", property = "name", javaType = String.class),
			@Result(column = "type", property = "type", javaType = String.class),
			@Result(column = "create_by", property = "createBy", javaType = String.class),
			@Result(column = "create_date", property = "createDate", javaType = Date.class),
			@Result(column = "update_by", property = "updateBy", javaType = String.class),
			@Result(column = "update_date", property = "updateDate", javaType = Date.class),
			@Result(column = "del_flag", property = "delFlag", javaType = String.class)})
    List<Demo> selecDemoById(String id);
	
	//************************测试传参*********************
	/**
	 * 测试demo 一个参数用map传参
	 * @param demo
	 * @return
	 */
	@SelectProvider(type = DemoSqlBuilder.class, method = "getByName")
	@ResultMap("DemoMap")
	List<Demo> getByName(@Param("name") String name);
	/**
	 * 测试demo 两个参数用map传参
	 * @param demo
	 * @return
	 */
	@SelectProvider(type = DemoSqlBuilder.class, method = "getByNameAndType")
	@ResultMap("DemoMap")
	List<Demo> getByNameAndType(@Param("name") String name,@Param("type") String type);
	/**
	 * 测试demo 一个参数直接传参
	 * @param demo
	 * @return
	 */
	@SelectProvider(type = DemoSqlBuilder.class, method = "getByType")
	@ResultMap("DemoMap")
	List<Demo> getByType(String name);
	//*******************测试返回类型**********************
	/**
	 * 测试demo 一个参数直接传参
	 * @param demo
	 * @return
	 */
	@SelectProvider(type = DemoSqlBuilder.class, method = "getByIdToMap")
	List<Map<String,Object>> getByIdToMap(String id);
	/**
	 * 测试demo 一个参数直接传参
	 * @param demo
	 * @return
	 */
	@SelectProvider(type = DemoSqlBuilder.class, method = "getByNameToMaps")
	@ResultMap("DemoMap")
	@MapKey("id")
	Map<String,Demo> getByNameToMaps(String name);
}