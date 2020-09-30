package com.geovis.web.service.anon;

import com.geovis.web.base.service.BaseService;
import com.geovis.web.domain.anon.AnonUserRegist;

import java.util.List;

/**
 * Created by Administrator on 2018/12/4.
 */
public interface AnonUserRegistService extends BaseService<AnonUserRegist> {

    /**
     * 查询方法，支持模糊查询
     *
     * @param anonUserRegist
     * @return
     */
    List<AnonUserRegist> listAll(AnonUserRegist anonUserRegist);

    /**
     * 将修改和添加封装在一起，便于缓存处理
     * （更新缓存）
     *
     * @param currentUser
     * @param anonUserRegist
     * @return
     */
    AnonUserRegist commonSave(AnonUserRegist currentUser, AnonUserRegist anonUserRegist);

    /**
     * 根据用户名获取用户信息（封装部门、角色）
     *
     * @param username
     * @return
     */
    AnonUserRegist selectUserByUsername(String username);

    /**
     * 根据用户ID获取用户信息（封装部门、角色）
     *
     * @param userId
     * @return
     */
    AnonUserRegist selectUserById(String userId);

    /**
     * 软删除用户（清除缓存）
     *
     * @param currentUser
     * @param userId
     * @return
     */
    AnonUserRegist removeUserById(AnonUserRegist currentUser, String userId);

    /**
     * 根据roleId获取用户
     *
     * @param roleId
     * @return
     */
    List<AnonUserRegist> listUserByRoleId(String roleId);
}
