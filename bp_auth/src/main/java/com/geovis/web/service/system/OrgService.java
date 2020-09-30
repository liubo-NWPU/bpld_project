package com.geovis.web.service.system;


import java.util.List;

import com.geovis.web.base.service.BaseService;
import com.geovis.web.domain.system.SysOrg;
import com.geovis.web.domain.system.SysUser;

public interface OrgService extends BaseService<SysOrg> {

    /**
     * 查询方法，支持模糊查询
     *
     * @param sysOrg
     * @return
     */
    List<SysOrg> listAll(SysOrg sysOrg);

    /**
     * 将修改和添加封装在一起，便于缓存处理
     * （更新缓存）
     *
     * @param sysOrg
     * @return
     */
    SysOrg commonSave(SysUser currentUser, SysOrg sysOrg);

    /**
     * 查询单个组织（根据需要封装子记录）
     *
     * @param id
     * @return
     */
    SysOrg selectOrgById(String id);

    /**
     * 软删除单个组织（清除缓存）
     * @param currentUser
     * @param id
     * @return
     */
    SysOrg removeOrgById(SysUser currentUser , String id) ;
}
