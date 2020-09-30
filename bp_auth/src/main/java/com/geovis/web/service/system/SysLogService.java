package com.geovis.web.service.system;

import com.geovis.web.base.service.BaseService;
import com.geovis.web.domain.system.SysLog;

import java.util.List;

public interface SysLogService extends BaseService<SysLog> {
    /**
     * 根据用户ID查询日志
     * @param userId
     * @return
     */
     List<SysLog> selectByUseId(String userId);

    /**
     * 根据创建时间查询日志
     * @param createDate
     * @return
     */
     List<SysLog> selectByCreateTime(String createDate);

    /**
     * 根据日志类型查询日志
     * @param type
     * @return
     */
     List<SysLog> selectByType(String type);

     List<SysLog> selectLoggers(SysLog sysLog);

}
