package com.gis.manager.common;

import com.gis.manager.dao.HistoryShapePointDao;
import com.gis.manager.model.RadarInfo;
import com.gis.manager.service.IRadarInfoService;
import com.gis.manager.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class ScheduledTask {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private IRadarInfoService radarInfoService;

    @Autowired
    private HistoryShapePointDao historyShapePointDao;

    @Scheduled(cron="0 58 23 * * ?")
    public void backup() throws InterruptedException {
        List<RadarInfo> list = radarInfoService.findAll();
        for (RadarInfo radarInfo : list){
            String tableName = "shape_point_"+radarInfo.getRadarKey();
            String next = DateUtil.nextDayDate(new Date(),"yyyy-MM-dd",1);
            String nextt = DateUtil.nextDayDate(new Date(),"yyyy-MM-dd",2);
            String dateformat = next.replaceAll("-","");
            String sql = "CREATE TABLE IF NOT EXISTS "+tableName+"_"+dateformat+" (check (time > Date '"+next+"' and time < Date '"+nextt+"')) inherits ("+tableName+");";
            jdbcTemplate.execute(sql);
//            sql = "CREATE INDEX IF NOT EXISTS "+tableName+"_"+dateformat+"_file_id_index ON "+tableName+"_"+dateformat+"(file_id)";
//            jdbcTemplate.execute(sql);
            sql = "CREATE INDEX IF NOT EXISTS "+tableName+"_"+dateformat+"_time_index ON "+tableName+"_"+dateformat+"(time)";
            jdbcTemplate.execute(sql);
            sql = "CREATE INDEX IF NOT EXISTS "+tableName+"_"+dateformat+"_radar_index ON "+tableName+"_"+dateformat+"(radar)";
            jdbcTemplate.execute(sql);
        }
    }

    @Scheduled(cron="0 5 0 * * ?")
    public void clear() throws InterruptedException {
        List<RadarInfo> list = radarInfoService.findAll();
        for (RadarInfo radarInfo : list){
            String tableName = "shape_point_"+radarInfo.getRadarKey();
            String oneYearAgo = DateUtil.nextDayDate(new Date(),"yyyy-MM-dd",-365) ;
            String halfYearAgo = DateUtil.nextDayDate(new Date(),"yyyy-MM-dd",-180);
            String threeMonthAgo = DateUtil.nextDayDate(new Date(),"yyyy-MM-dd",-90);
            String oneMonthAgo = DateUtil.nextDayDate(new Date(),"yyyy-MM-dd",-30);

            historyShapePointDao.deleteByTime(DateUtil.stringToDate(threeMonthAgo,"yyyy-MM-dd"));
            String sql = "DROP TABLE IF EXISTS "+tableName+"_"+threeMonthAgo.replaceAll("-","");
            jdbcTemplate.execute(sql);

            jdbcTemplate.execute("SELECT clear_one_month('"+radarInfo.getRadarKey()+"','"+oneMonthAgo.replaceAll("-","")+"')");
        }
    }
}
