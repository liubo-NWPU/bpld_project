package com.geovis.web.dao.system.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.geovis.web.base.mapper.BaseMapper;
import com.geovis.web.domain.system.SysRole;

@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {

    //根据用户id查询用户角色
	@Select("SELECT" + 
			"        sr.id," + 
			"        sr.name," + 
			"        sr.description," + 
			"        sr.create_by," + 
			"        sr.create_date," + 
			"        sr.update_by," + 
			"        sr.update_date," + 
			"        sr.del_flag" + 
			"    FROM" + 
			"        sys_user_role sur" + 
			"    INNER JOIN sys_role sr ON sr.ID = sur.role_id" + 
			"    WHERE" + 
			"        sur.user_id = #{userId}" + 
			"        AND sur.del_flag = 'NORMAL'")
	@Results(id = "SysRoleMap", value = { @Result(column = "id", property = "id", javaType = String.class),
			@Result(column = "name", property = "name", javaType = String.class),
			@Result(column = "description", property = "description", javaType = String.class),
			@Result(column = "create_by", property = "createBy", javaType = String.class),
			@Result(column = "create_date", property = "createDate", javaType = Date.class),
			@Result(column = "update_by", property = "updateBy", javaType = String.class),
			@Result(column = "update_date", property = "updateDate", javaType = Date.class),
			@Result(column = "del_flag", property = "delFlag", javaType = String.class)})
    List<SysRole> listRoleByUserId(String userId);
}