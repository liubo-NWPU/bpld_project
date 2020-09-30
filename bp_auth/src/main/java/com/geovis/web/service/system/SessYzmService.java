package com.geovis.web.service.system;

import com.geovis.web.domain.system.SessYzm;

import java.util.List;

public interface SessYzmService {

    void insertYzm(String sessionid,String yzm,long date);

    SessYzm selectById(String sessid);

    void deleteById(String sessid);

    void deleteByTime(long time);
}
