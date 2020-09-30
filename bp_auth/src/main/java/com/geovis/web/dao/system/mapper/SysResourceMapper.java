package com.geovis.web.dao.system.mapper;

import com.geovis.web.base.mapper.BaseMapper;
import com.geovis.web.domain.enums.ResourceType;
import com.geovis.web.domain.system.SysResource;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;
@Mapper
public interface SysResourceMapper extends BaseMapper<SysResource> {
	// 根据用户id获取用户资源
	@Select("SELECT " + "    DISTINCT sr.id, " + "    sr.name, " +"sr.enname,"+ "    sr.url, " + "    sr.permission, "
			+ "    sr.icon, " + "    sr.level, " + "    sr.type, " + "    sr.sort, " + "    sr.target, "
			+ "    sr.parent_id, " + "    sr.create_by, " + "    sr.creater, " + "    sr.create_date, "
			+ "    sr.update_by, " + "    sr.updater, " + "    sr.update_date, " + "    sr.del_flag, " +" srr.datapower ,"+ "srr.servicepower" +" FROM "
			+ "        sys_user_role sur "
			+ "        INNER JOIN sys_role_resource srr ON sur.role_id = srr.role_id AND srr.del_flag = 'NORMAL' "
			+ "        INNER JOIN sys_resource sr ON sr.id = srr.resource_id AND sr.del_flag = 'NORMAL' "
			+ "        WHERE " + "        sur.user_id = #{userId} and sur.del_flag = 'NORMAL' ")
	@Results(id = "SysResourceMap", value = { @Result(column = "id", property = "id", javaType = String.class),
			@Result(column = "name", property = "name", javaType = String.class),
			@Result(column = "enname", property = "enname", javaType = String.class),
			@Result(column = "url", property = "url", javaType = String.class),
			@Result(column = "permission", property = "permission", javaType = String.class),
			@Result(column = "icon", property = "icon", javaType = String.class),
			@Result(column = "level", property = "level", javaType = Integer.class),
			@Result(column = "type", property = "type", javaType = ResourceType.class),
			@Result(column = "sort", property = "sort", javaType = Integer.class),
			@Result(column = "target", property = "target", javaType = String.class),
			@Result(column = "parent_id", property = "parentId", javaType = String.class),
			@Result(column = "create_by", property = "createBy", javaType = String.class),
			@Result(column = "creater", property = "creater", javaType = String.class),
			@Result(column = "create_date", property = "createDate", javaType = Date.class),
			@Result(column = "update_by", property = "updateBy", javaType = String.class),
			@Result(column = "updater", property = "updater", javaType = String.class),
			@Result(column = "update_date", property = "updateDate", javaType = Date.class),
			@Result(column = "del_flag", property = "delFlag", javaType = String.class),
			@Result(column = "datapower", property = "datapower", javaType = String.class),
			@Result(column = "servicepower", property = "servicepower", javaType = String.class),
			@Result(column = "folderid", property = "folderid", javaType = Long.class),
			@Result(column = "checked", property = "checked", javaType = Boolean.class)})

	List<SysResource> listResourceByUserId(String userId);

	@Select("SELECT " + " sr.id, " + "    sr.name, "  +"sr.enname,"+ "    sr.url, " + "    sr.permission, "
			+ "    sr.icon, " + "    sr.level, " + "    sr.type, " + "    sr.sort, " + "    sr.target, "
			+ "    sr.parent_id, " + "    sr.create_by, " + "    sr.creater, " + "    sr.create_date, "
			+ "    sr.update_by, " + "    sr.updater, " + "    sr.update_date, " + "    sr.del_flag, " +" srr.datapower ,"+ "srr.servicepower,"+"sr.folderid" +" FROM "
			+ "        sys_user_role sur "
			+ "        INNER JOIN sys_role_resource srr ON sur.role_id = srr.role_id AND srr.del_flag = 'NORMAL' "
			+ "        INNER JOIN sys_resource sr ON sr.id = srr.resource_id AND sr.del_flag = 'NORMAL' "
			+ "        WHERE " + "        sur.user_id = #{userId}")
	@ResultMap("SysResourceMap")
	List<SysResource> listDataPowerByUserId(String userId);

	// 根据角色id获取角色资源
	@Select("SELECT" + "    sr.id," + "    sr.name,"  +"sr.enname,"+ "    sr.url," + "    sr.permission," + "    sr.icon,"
			+ "    sr.level," + "    sr.type," + "    sr.sort," + "    sr.target," + "    sr.parent_id,"
			+ "    sr.create_by," + "    sr.creater," + "    sr.create_date," + "    sr.update_by," + "    sr.updater,"
			+ "    sr.update_date," + "    sr.del_flag," +"  srr.datapower ,"+ "srr.servicepower,"+"sr.folderid" + " FROM"
			+ "        sys_role_resource srr"
			+ "        INNER JOIN sys_resource sr ON sr.id = srr.resource_id" + "        WHERE"
			+ "        srr.role_id = #{roleId}")
	@ResultMap("SysResourceMap")
	List<SysResource> listResourceByRoleId(String roleId);

	// 根据资源id查询父层级id（包括当前查询的id)
	List<String> listResParentIdUp(List<String> resourceIdList);

	// 根据roleId获取所有资源，并标识该资源是否checked
//	@Select("SELECT" + "    sr.id," + "    sr.name," + "    sr.url," + "    sr.permission," + "    sr.icon,"
//			+ "    sr.level," + "    sr.type," + "    sr.sort," + "    sr.target," + "    sr.parent_id,"
//			+ "    sr.create_by," + "    sr.creater," + "    sr.create_date," + "    sr.update_by," + "    sr.updater,"
//			+ "    sr.update_date," + "    sr.del_flag," +" srr.datapower,"+ "  'false' checked" + "        FROM"
//			+ "        sys_resource sr" + "        WHERE" + "        NOT EXISTS (" + "        SELECT" + "        *"
//			+ "        FROM" + "        sys_role_resource srr" + "        WHERE" + "        srr.resource_id = sr.id"
//			+ "        AND srr.role_id = #{roleId}" + "        AND srr.del_flag = 'NORMAL'" + "        )"
//			+ "        UNION ALL" + "        SELECT" + "        sr.id," + "    sr.name," + "    sr.url,"
//			+ "    sr.permission," + "    sr.icon," + "    sr.level," + "    sr.type," + "    sr.sort,"
//			+ "    sr.target," + "    sr.parent_id," + "    sr.create_by," + "    sr.creater," + "    sr.create_date,"
//			+ "    sr.update_by," + "    sr.updater," + "    sr.update_date," + "    sr.del_flag, 'true' checked"
//			+ "        FROM" + "        sys_resource sr" + "        WHERE" + "        EXISTS (" + "        SELECT"
//			+ "        *" + "        FROM" + "        sys_role_resource srr" + "        WHERE"
//			+ "        srr.resource_id = sr.id" + "        AND srr.role_id = #{roleId}"
//			+ "        AND srr.del_flag = 'NORMAL'" + " )")
//	@ResultMap("SysResourceMap")
//	List<SysResource> listAllResourceByRoleId(String roleId);

	@Select("SELECT" + "    sr.id," + "    sr.name,"  +"sr.enname,"+ "    sr.url," + "    sr.permission," + "    sr.icon,"
			+ "    sr.level," + "    sr.type," + "    sr.sort," + "    sr.target," + "    sr.parent_id,"
			+ "    sr.create_by," + "    sr.creater," + "    sr.create_date," + "    sr.update_by," + "    sr.updater,"
			+ "    sr.update_date," + "    sr.del_flag," +" '00000' datapower,"+ "'00000'servicepower,"+"sr.folderid," + " false checked"
			+ "    FROM sys_resource sr "
			+"        WHERE  NOT EXISTS (" + "        SELECT" + "        *"
			+ "        FROM" + "        sys_role_resource srr" + "        WHERE" + "        srr.resource_id = sr.id"
			+ "        AND srr.role_id = #{roleId}" + "        AND srr.del_flag = 'NORMAL'" + "        ) "
            +  "       AND sr.del_flag = 'NORMAL' "
			+ "  UNION ALL" + "  SELECT" + "  sr.id," + " sr.name," +"sr.enname,"+ "    sr.url,"
			+ "    sr.permission," + "    sr.icon," + "    sr.level," + "    sr.type," + "   sr.sort,"
			+ "    sr.target," + "    sr.parent_id," + "    sr.create_by," + "    sr.creater," + "    sr.create_date,"
			+ "    sr.update_by," + "    sr.updater," + "    sr.update_date," + "   sr.del_flag,"+ " srr.datapower," +"srr.servicepower,"+"sr.folderid," +" true checked"
			+ "        FROM sys_resource sr ,sys_role_resource srr WHERE srr.resource_id = sr.id"
			+ "        AND srr.role_id = #{roleId}"
			+ "        AND sr.del_flag = 'NORMAL'"
			+ "        AND srr.del_flag = 'NORMAL'")
	@ResultMap("SysResourceMap")
	List<SysResource> listAllResourceByRoleId(String roleId);

	
	// 根据用户id获取用户资源
	@Select("SELECT " + "    DISTINCT sr.id, " + "    sr.name, " +"sr.enname,"+ "    sr.url, " + "    sr.permission, "
			+ "    sr.icon, " + "    sr.level, " + "    sr.type, " + "    sr.sort, " + "    sr.target, "
			+ "    sr.parent_id, " + "    sr.create_by, " + "    sr.creater, " + "    sr.create_date, "
			+ "    sr.update_by, " + "    sr.updater, " + "    sr.update_date, " + "    sr.del_flag, " +" srr.datapower ,"+ "srr.servicepower," +"sr.folderid"+ " FROM "
			+ "        sys_user_role sur "
			+ "        INNER JOIN sys_role_resource srr ON sur.role_id = srr.role_id AND srr.del_flag = 'NORMAL' "
			+ "        INNER JOIN sys_resource sr ON sr.id = srr.resource_id AND sr.del_flag = 'NORMAL' "
			+ "        WHERE  sur.user_id = #{userId} "
	        + "        and  sr.type = #{type}  " )
	@ResultMap("SysResourceMap")
	List<SysResource> listSourceByType(@Param("userId") String userId,@Param("type") String type);

    @Delete("delete from sys_resource sr where sr.id = #{id} and sr.type = 'FOLDER'")
	void  deleteById(@Param("id") String id);
}