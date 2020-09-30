package com.geovis.web.service.system;

import java.util.List;
import java.util.Map;

import com.geovis.web.base.service.BaseService;
import com.geovis.web.domain.demo.Demo;
import com.geovis.web.domain.system.SysUser;

public interface DemoService extends BaseService<Demo> {

    /**
     * 查询方法，支持模糊查询
     *
     * @param Demo
     * @return
     */
    List<Demo> listAll(Demo demo);
    /**
     * 将修改和添加封装在一起，便于缓存处理
     * （更新缓存）
     *
     * @param Demo
     * @return
     */
    Demo commonSave(SysUser currentUser, Demo Demo);
    /**
     * 软删除demo（清除缓存）
     * @param currentUser
     * @param userId
     * @return
     */
    boolean removeDemoById(SysUser currentUser , String Id) ;
    
    /**
     * 自定义sql查询相关对象
     * @param currentUser
     * @param name
     * @return
     */
    List<Demo> getByName(SysUser currentUser , String name) ;
    /**
     * 自定义sql查询相关对象
     * @param currentUser
     * @param name
     * @return
     */
    List<Demo> getByType(SysUser currentUser , String type) ;
    /**
     * 自定义sql查询相关对象
     * @param currentUser
     * @param name
     * @return
     */
    List<Demo> getByNameAndType(SysUser currentUser , String name, String type) ;
    /**
     * 自定义sql查询相关对象
     * @param currentUser
     * @param name
     * @return
     */
    Map<String,Object> getByIdToMap(SysUser currentUser , String id) ;
    /**
     * 自定义sql查询相关对象
     * @param currentUser
     * @param name
     * @return
     */
    Map<String,Demo> getByNameToMaps(SysUser currentUser , String name) ;


}
