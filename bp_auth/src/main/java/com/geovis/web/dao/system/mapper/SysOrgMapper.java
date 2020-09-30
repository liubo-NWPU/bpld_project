package com.geovis.web.dao.system.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.geovis.web.base.mapper.BaseMapper;
import com.geovis.web.domain.system.SysOrg;
@Mapper
public interface SysOrgMapper extends BaseMapper<SysOrg> {

    //根据用户id查询用户所属组织
	@Select("SELECT\r\n" + 
			"        so.id,\r\n" + 
			"        so.name,\r\n" + 
			"        so.description,\r\n" + 
			"        so.create_by,\r\n" + 
			"        so.create_date,\r\n" + 
			"        so.update_by,\r\n" + 
			"        so.update_date,\r\n" + 
			"        so.del_flag\r\n" + 
			"    FROM\r\n" + 
			"        sys_user_org suo\r\n" + 
			"    INNER JOIN sys_org so ON so.id = suo.org_id\r\n" + 
			"    WHERE\r\n" + 
			"        suo.user_id = #{userId}\r\n" + 
			"        AND suo.del_flag = 'NORMAL'")
	@Results(id = "SysRoleMap", value = { @Result(column = "id", property = "id", javaType = String.class),
			@Result(column = "name", property = "name", javaType = String.class),
			@Result(column = "description", property = "description", javaType = String.class),
			@Result(column = "create_by", property = "createBy", javaType = String.class),
			@Result(column = "create_date", property = "createDate", javaType = Date.class),
			@Result(column = "update_by", property = "updateBy", javaType = String.class),
			@Result(column = "update_date", property = "updateDate", javaType = Date.class),
			@Result(column = "del_flag", property = "delFlag", javaType = String.class)})
    List<SysOrg> listOrgByUserId(String userId);
}