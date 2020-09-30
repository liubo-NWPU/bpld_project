package com.geovis.web.dao.system.mapper;

import com.geovis.web.base.mapper.BaseMapper;
import com.geovis.web.domain.system.SysRoleResource;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

public interface SysRoleResourceMapper extends BaseMapper<SysRoleResource> {



    @Select("SELECT srr.id," +"srr.role_id,"+"srr.resource_id,"+"srr.create_by,"+"srr.create_date,"+"srr.update_by,"+"srr.update_date,"+"srr.del_flag,"+"srr.datapower,"+"srr.servicepower"
            + "        FROM sys_role_resource srr , sys_resource sr "+" WHERE srr.resource_id = sr.id"
            + "        AND srr.role_id = #{roleId}"
            +"         AND  sr.type=#{operationType}"
            + "        AND srr.del_flag = #{delFlag}"
            )
    @Results(id = "SysRoleResourceMap", value = {
            @Result(column = "id", property = "id", javaType = String.class),
            @Result(column = "role_id", property = "roleId", javaType = String.class),
            @Result(column = "resource_id", property = "resourceId", javaType = String.class),
            @Result(column = "create_by", property = "createBy", javaType = String.class),
            @Result(column = "create_date", property = "createDate", javaType = Date.class),
            @Result(column = "update_by", property = "updateBy", javaType = String.class),
            @Result(column = "update_date", property = "updateDate", javaType = Date.class),
            @Result(column = "del_flag", property = "delFlag", javaType = String.class),
            @Result(column = "servicepower", property = "servicepower", javaType = String.class),
            @Result(column = "datapower", property = "datapower", javaType = String.class)})

    List<SysRoleResource> selectSysRoleResourceByFolder(@Param("roleId")String roleId, @Param("delFlag")String delFlag, @Param("operationType")String operationType);


    @Select("SELECT srr.id," +"srr.role_id,"+"srr.resource_id,"+"srr.create_by,"+"srr.create_date,"+"srr.update_by,"+"srr.update_date,"+"srr.del_flag,"+"srr.datapower,"+"srr.servicepower"
            + "        FROM sys_role_resource srr , sys_resource sr "+" WHERE srr.resource_id = sr.id"
            + "        AND srr.role_id = #{roleId}"
            +"         AND  sr.type!=#{operationType}"
            + "        AND srr.del_flag = #{delFlag}"
    )
    @ResultMap("SysRoleResourceMap")
    List<SysRoleResource> selectSysRoleResourceByOther(@Param("roleId")String roleId, @Param("delFlag")String delFlag, @Param("operationType")String operationType);

    @Delete("delete from sys_role_resource srr where srr.resource_id = #{id}")
    void  deleteById(@Param("id") String id);
}