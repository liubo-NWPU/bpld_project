package com.geovis.web.service.impl.system;

import com.geovis.web.base.service.impl.AbstractService;
import com.geovis.web.dao.system.mapper.SysLogMapper;
import com.geovis.web.domain.system.SysLog;
import com.geovis.web.domain.system.SysResource;
import com.geovis.web.service.system.SysLogService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@SuppressWarnings("all")
@Service
public class LoggerServiceImpl extends AbstractService<SysLog> implements SysLogService {

    @Autowired
    private SysLogMapper sysLogMapper;

    @Override
    public List<SysLog> selectByUseId(String userId) {
        List<SysLog> sysLogs = new ArrayList<SysLog>();
        sysLogs = sysLogMapper.selectByUseId(userId);
        return sysLogs;
    }

    @Override
    public List<SysLog> selectByCreateTime(String createDate) {
        List<SysLog> sysLogs = new ArrayList<SysLog>();
        sysLogs = sysLogMapper.selectByCreateTime(createDate);
        return sysLogs;
    }

    @Override
    public List<SysLog> selectByType(String type) {
        List<SysLog> sysLogs = new ArrayList<SysLog>();
        sysLogs = sysLogMapper.selectByType(type);
        return sysLogs;
    }

    @Override
    public List<SysLog> selectLoggers(SysLog sysLog) {

        if (sysLog.getPage() != null && sysLog.getRows() != null) {
            PageHelper.startPage(sysLog.getPage(), sysLog.getRows());
        }
        /*Example example = new Example(SysResource.class);*/
        Example example = new Example(SysLog.class);
        Example.Criteria criteria = example.createCriteria();
        String createBy = sysLog.getCreateBy();
        if (createBy != null && createBy.length() > 0) {
            criteria.andEqualTo("createBy", createBy);
        }

        Date startDate = sysLog.getStartDate();
        if (startDate != null) {
            criteria.andGreaterThanOrEqualTo("createDate", startDate);
        }

        Date endDate = sysLog.getEndDate();
        if (endDate != null) {
            criteria.andLessThanOrEqualTo("createDate", endDate);
        }


        String method = sysLog.getMethod();
        if (method != null && method.length() > 0) {
            criteria.andEqualTo("method", method);
        }

        String ip = sysLog.getIp();
        if (ip != null && ip.length() > 0) {
            criteria.andEqualTo("ip", ip);
        }

        String description = sysLog.getDescription();
        if (description != null && description.length() > 0) {
            criteria.andLike("description", description);
        }

        String browser = sysLog.getBrowser();
        if (browser != null && browser.length() > 0) {
            criteria.andLike("browser", browser);
        }

        String uri = sysLog.getUri();
        if (uri != null && uri.length() > 0) {
            criteria.andLike("uri", uri);
        }

        String paramss = sysLog.getParams();
        if (paramss != null && paramss.length() > 0) {
            criteria.andLike("params", paramss);
        }

        String exceptions = sysLog.getException();
        if (exceptions != null && exceptions.length() > 0) {
            criteria.andLike("exception", exceptions);
        }


        example.orderBy("createDate").desc();

        String[] logType = sysLog.getLogType();
        if (logType != null && logType.length > 0) {
            List<String> resultList = new ArrayList<>(logType.length);
            Collections.addAll(resultList, logType);
            criteria.andIn("type", resultList);
        }
        return this.sysLogMapper.selectByExample(example);
    }


}
