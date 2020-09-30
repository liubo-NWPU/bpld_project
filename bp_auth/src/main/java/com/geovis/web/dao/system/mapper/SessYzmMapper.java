package com.geovis.web.dao.system.mapper;


import com.geovis.web.base.mapper.BaseMapper;
import com.geovis.web.domain.system.SessYzm;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessYzmMapper extends BaseMapper<SessYzm> {

    @Update("insert into sess_yzm values(#{sessid},#{yzm},#{date}) ")
    void insertYzm(@Param("sessid")String sessid, @Param("yzm")String yzm,@Param("date") long date);

    @Select("select * from sess_yzm se where se.sessid= #{sessid}")
    SessYzm selectById(@Param("sessid")String sessid);

    @Delete("delete from sess_yzm se where se.sessid= #{sessid}")
    void deleteById(@Param("sessid")String sessid);

    @Delete("delete from sess_yzm se where se.date < #{date}")
    void deleteByTime(@Param("date")long date);

}
