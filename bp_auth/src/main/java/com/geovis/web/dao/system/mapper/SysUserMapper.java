package com.geovis.web.dao.system.mapper;

import com.geovis.web.base.mapper.BaseMapper;
import com.geovis.web.dao.system.sql.UserSqlBuilder;
import com.geovis.web.domain.system.SysUser;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 根据检索条件获取所有用户
     * @param sysUser
     * @return
     */
	@SelectProvider(type = UserSqlBuilder.class, method = "listAll")
	@Results(id = "SysUserMap", value = { @Result(column = "id", property = "id", javaType = String.class),
			@Result(column = "username", property = "username", javaType = String.class),
			@Result(column = "password", property = "password", javaType = String.class),
			@Result(column = "salt", property = "salt", javaType = String.class),
			@Result(column = "name", property = "name", javaType = String.class),
			@Result(column = "enter_date", property = "enterDate", javaType = Date.class),
			@Result(column = "sex", property = "sex", javaType = String.class),
			@Result(column = "telphone", property = "telphone", javaType = String.class),
			@Result(column = "mobile", property = "mobile", javaType = String.class),
			@Result(column = "email", property = "email", javaType = String.class),
			@Result(column = "idcard", property = "idcard", javaType = String.class),
			@Result(column = "ip", property = "ip", javaType = String.class),
			@Result(column = "ipstatus", property = "ipstatus", javaType = String.class),
			@Result(column = "mrp", property = "mrp", javaType = String.class),
			@Result(column = "state", property = "state", javaType = String.class),
			@Result(column = "user_sys_type", property = "userSysType", javaType = String.class),
			@Result(column = "user_group", property = "userGroup", javaType = String.class),
			@Result(column = "company", property = "company", javaType = String.class),
			@Result(column = "create_by", property = "createBy", javaType = String.class),
			@Result(column = "user_type", property = "userType", javaType = String.class),
			@Result(column = "user_file", property = "userFile", javaType = String.class),
			@Result(column = "create_date", property = "createDate", javaType = Date.class),
			@Result(column = "update_by", property = "updateBy", javaType = String.class),
			@Result(column = "update_date", property = "updateDate", javaType = Date.class),
			@Result(column = "del_flag", property = "delFlag", javaType = String.class)})
    List<SysUser> listAll(SysUser sysUser);

    /**
     * 根据roleId获取用户
     * @param roleId
     * @return
     */
	@Select("SELECT su.* " +
			"        FROM" +
			"        sys_user su" +
			"        LEFT JOIN sys_user_role sur ON sur.user_id = su.id" +
			"        AND sur.del_flag = 'NORMAL'" +
			"        WHERE su.del_flag = 'NORMAL'" +
			"        AND sur.role_id = #{roleId}")
	@ResultMap("SysUserMap")
    List<SysUser> listUserByRoleId(String roleId);

}