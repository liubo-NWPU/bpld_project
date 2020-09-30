package com.geovis.web.dao.system.sql;

import java.util.Map;

import com.geovis.core.util.StringUtil;
@SuppressWarnings("all")
public class DemoSqlBuilder {
	
	public String getByName(Map<String, Object> para) {
		//根据名称查demo对象
		StringBuffer sbSQL = new StringBuffer();
		String name = para.get("name").toString();
		sbSQL.append("select * from demo ");
		if(!StringUtil.isEmpty(name)) {
			sbSQL.append(" where name ='"+name+"'");
		}
		return sbSQL.toString();
	}
	
	public String getByNameAndType(Map<String, Object> para) {
		//根据名称查demo对象
		StringBuffer sbSQL = new StringBuffer();
		String name = para.get("name").toString();
		String type = para.get("type").toString();
		sbSQL.append("select * from demo where 1=1 ");
		if(!StringUtil.isEmpty(name)) {
			sbSQL.append(" and name ='"+name+"'");
		}
		if(!StringUtil.isEmpty(type)) {
			sbSQL.append(" and type ='"+type+"'");
		}
		return sbSQL.toString();
	}
	
	public String getByType(String type) {
		//根据名称查demo对象
		StringBuffer sbSQL = new StringBuffer();
		sbSQL.append("select * from demo ");
		if(!StringUtil.isEmpty(type)) {
			sbSQL.append(" where type ='"+type+"'");
		}
		return sbSQL.toString();
	}
	
	public String getByIdToMap(String id) {
		//根据名称查demo对象
		StringBuffer sbSQL = new StringBuffer();
		sbSQL.append("select id,name,type from demo ");
		if(!StringUtil.isEmpty(id)) {
			sbSQL.append(" where id ='"+id+"'");
		}
		return sbSQL.toString();
	}
	public String getByNameToMaps(String name) {
		//根据名称查demo对象
		StringBuffer sbSQL = new StringBuffer();
		sbSQL.append("select id,name,type from demo ");
		if(!StringUtil.isEmpty(name)) {
			sbSQL.append(" where name ='"+name+"'");
		}
		return sbSQL.toString();
	}
}
