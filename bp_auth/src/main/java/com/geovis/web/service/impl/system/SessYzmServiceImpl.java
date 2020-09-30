package com.geovis.web.service.impl.system;

import com.geovis.web.dao.system.mapper.SessYzmMapper;
import com.geovis.web.domain.system.SessYzm;
import com.geovis.web.service.system.SessYzmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SessYzmServiceImpl  implements SessYzmService{

    @Autowired
    private SessYzmMapper sessYzmMapper;

    @Override
    public void insertYzm(String sessionid, String yzm, long date) {
        try {
            sessYzmMapper.insertYzm(sessionid,yzm,date);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public SessYzm selectById(String sessid) {
        try {
            return sessYzmMapper.selectById(sessid);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void deleteById(String sessid) {
        try {
            sessYzmMapper.deleteById(sessid);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void deleteByTime(long time) {
        try {
            sessYzmMapper.deleteByTime(time);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
