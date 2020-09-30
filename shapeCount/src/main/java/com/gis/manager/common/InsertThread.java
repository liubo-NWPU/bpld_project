/**
 * @description:
 * @author: liuyan
 * @create: 2019-12-17 16:52
 **/

package com.gis.manager.common;

import com.alibaba.fastjson.JSONObject;
import com.gis.manager.model.RadarInfo;
import com.gis.manager.model.extra.PointInfo;
import com.gis.manager.service.IRadarInfoService;
import com.gis.manager.utils.GeometryUtil;
import com.vividsolutions.jts.geom.Point;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jms.core.JmsMessagingTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class InsertThread extends Thread{

    private static final String sql_str = "insert into shape_point(file_id,m,n,change,time,geo,speed,accspeed,radar,id,range) values ";

    private static final int insert_num = 10000;

//    private static final String COMPUTE_POINT_STATISTICS = "ComputePlotAdd.queue";

    private JdbcTemplate jdbcTemplate;

    private JmsMessagingTemplate jmsMessagingTemplate;

    private IRadarInfoService radarInfoService;

    private String msg ;

    public InsertThread(JdbcTemplate jdbcTemplate, JmsMessagingTemplate jmsMessagingTemplate, IRadarInfoService radarInfoService, String msg) {
        this.jdbcTemplate = jdbcTemplate;
        this.jmsMessagingTemplate = jmsMessagingTemplate;
        this.radarInfoService = radarInfoService;
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public void run() {
        System.out.println((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date()) + Thread.currentThread().getName()+" : start");

        JSONObject json = JSONObject.parseObject(msg);
        List<PointInfo> pList = json.getJSONArray("list").toJavaList(PointInfo.class);
        String time = json.getString("time");
        String radar = json.getString("radar");
        Long fileId = json.getLong("fileId");

        RadarInfo radarInfo = radarInfoService.findOne(radar);
        if (radarInfo == null){
            System.out.println("雷达"+radar+"信息不存在");
            return;
        }
        String radarKey = radarInfo.getRadarKey();
        int index = 1;
        StringBuilder sql = new StringBuilder(sql_str);
        for(PointInfo sp : pList) {
            String id = UUID.randomUUID().toString().replace("-","");
            Long m = sp.getM();
            Long n = sp.getN();
            Double strain = sp.getStrain();
            String range = range(strain);
            Double x = sp.getX();
            Double y = sp.getY();
            Point point = getLonLatPoint(x,y);
//            Point point = GeometryUtil.createPoint(x, y);
//            point.setSRID(GeometryUtil.ProjectedSRID);
            String value = "(" + fileId
                    + "," + m
                    + "," + n
                    + "," + strain
                    + ",'" + time + "'"
                    + ",ST_GeomFromText('" + point.toText() + "',"+GeometryUtil.ProjectedSRID+")"
                    + ",0.00"
                    + ",0.00"
                    + ",'" + radarKey + "'"
                    + ",'"+id+"'"
                    +",'" +range+"')";
            sql = index == 1 ? sql.append(value) : sql.append("," + value);
            if (index == insert_num) {
                jdbcTemplate.update(sql.toString());
                index = 0;
                sql = new StringBuilder(sql_str);
            }
            index++;
        }
        if(index > 1){
            jdbcTemplate.update(sql.toString());
        }
        System.out.println((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date()) + ","+Thread.currentThread().getName()+" : over");
    }

    private String range(Double strain){
        if (strain >= -1 && strain <= -0.5){
            return "[-1,-0.5]";
        }
        else if (strain > -0.5 && strain <= -0.2){
            return "(-0.5,-0.2]";
        }
        else if (strain > -0.2 && strain <= 0.2){
            return "(-0.2,0.2]";
        }
        else if (strain > 0.2 && strain <= 0.5){
            return "(0.2,0.5]";
        }
        else if (strain > 0.5 && strain <= 1){
            return "(0.5,1]";
        }
        return "other";
    }

    private static Point get3857Point(double lon, double lat) {
        lon = lon * 20037508.34 / 180;
        lat = Math.log(Math.tan((90 + lat) * Math.PI / 360)) / (Math.PI / 180);
        lat = lat * 20037508.34 / 180;
        return GeometryUtil.createPoint(lon, lat);
    }

    private static Point getLonLatPoint(double x, double y) {
        Double lon = x / 20037508.34 * 180;
        Double lat = y / 20037508.34 * 180;
        lat = 180 / Math.PI * (2 * Math.atan(Math.exp(lat * Math.PI / 180)) - Math.PI/2);
        return GeometryUtil.createPoint(lon, lat);
    }
}


