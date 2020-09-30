package com.geovis.web.dao.system.sql;

import java.util.Map;

import org.apache.ibatis.jdbc.SQL;

import com.geovis.web.domain.system.SysUser;
@SuppressWarnings("all")
public class UserSqlBuilder {
	public String listAll(Map<String, Object> para) {
		SysUser sysUser = (SysUser) para.get(0);
		StringBuffer sbSQL = new StringBuffer();
		String  id = sysUser.getId();
		String username = sysUser.getUsername();
		String name = sysUser.getName();
		String position = sysUser.getPosition();
		String orgName = sysUser.getOrgName();
		
		sbSQL.append("");
		String ss = new SQL() {
			{
				SELECT("tutor_id as tutorId, name, email");
				FROM("tutors");
				WHERE("tutor_id=" + "123");
			}
		}.toString();
		return ss;
	}
}
