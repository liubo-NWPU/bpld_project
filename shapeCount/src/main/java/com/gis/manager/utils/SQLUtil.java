package com.gis.manager.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Polygon;

/**
 * @Author:wangmeng
 * @Date:创建于2018/3/16 14:08
 * @Description:
 */
public class SQLUtil {
    public static String constructFileCatalogSQL(String params){
        StringBuilder sb=new StringBuilder();
        JSONObject rootObj=JSONObject.parseObject(params);

        if(rootObj.containsKey("name")) {
            String name = rootObj.getString("name").trim();
            sb.append(" and f.fileName like '%"+name+"%' ");
        }

        if(rootObj.containsKey("note")) {
            String note = rootObj.getString("note").trim();
            sb.append(" and f.note like '%"+note+"%' ");
        }

        if(rootObj.containsKey("startUploadDate")) {
            String startUploadDate = rootObj.getString("startUploadDate").trim();
            sb.append(String.format(" and f.uploadTime >= '%s' ",startUploadDate));
        }
        if(rootObj.containsKey("endUploadDate")) {
            String endUploadDate = rootObj.getString("endUploadDate").trim();
            sb.append(String.format(" and f.uploadTime <= '%s' ",endUploadDate));
        }
        return sb.toString();
    }

    public static String constructImageSQL(String params){
        StringBuilder sb=new StringBuilder();
        JSONObject rootObj=JSONObject.parseObject(params);

        if(rootObj.containsKey("name")) {
            String name = rootObj.getString("name").trim();
            sb.append(" and i.name like '%"+name+"%' ");
        }

        if(rootObj.containsKey("satellite")) {
            String satellite = rootObj.getString("satellite").trim();
            sb.append(String.format(" and i.satellite = '%s' ", satellite));
        }

        if(rootObj.containsKey("level")) {
            String level = rootObj.getString("level").trim();
            sb.append(String.format(" and i.level = '%s' ",level));
        }

        if(rootObj.containsKey("sensor")) {
            String sensor = rootObj.getString("sensor").trim();
            sb.append(String.format(" and i.sensor = '%s' ",sensor));
        }

        if(rootObj.containsKey("sceneId")) {
            String sceneId = rootObj.getString("sceneId").trim();
            sb.append(String.format(" and i.sceneId = '%s' ",sceneId));
        }

        if(rootObj.containsKey("startUploadDate")) {
            String startUploadDate = rootObj.getString("startUploadDate").trim();
            sb.append(String.format(" and i.produceTime >= '%s' ",startUploadDate));
        }
        if(rootObj.containsKey("endUploadDate")) {
            String endUploadDate = rootObj.getString("endUploadDate").trim();
            sb.append(String.format(" and i.produceTime <= '%s' ",endUploadDate));
        }

        if(rootObj.containsKey("startReceiveDate")) {
            String startReceiveDate = rootObj.getString("startReceiveDate").trim();
            sb.append(String.format(" and i.receiveTime >= '%s' ",startReceiveDate));
        }
        if(rootObj.containsKey("endReceiveDate")) {
            String endReceiveDate = rootObj.getString("endReceiveDate").trim();
            sb.append(String.format(" and i.receiveTime <= '%s' ",endReceiveDate));
        }

        if(rootObj.containsKey("serviceId")) {
            String serviceId = rootObj.getString("serviceId").trim();
            sb.append(String.format(" and i.serviceId = '%s' ",serviceId));
        }

        Polygon polygon;
        if(rootObj.containsKey("code")){
            String code=rootObj.getString("code").trim();
            if(code.endsWith("0000")){
                sb.append(" and i.province like '%"+code+"%'");
            }
            else if(code.endsWith("00")){
                sb.append(" and i.city like '%"+code+"%'");
            }
            else{
                sb.append(" and i.county like '%"+code+"%'");
            }
        }
        else if(rootObj.containsKey("extent")){
            JSONObject extentObj=rootObj.getJSONObject("extent");
            double minLon=extentObj.getDouble("minLon");
            double minLat=extentObj.getDouble("minLat");
            double maxLon=extentObj.getDouble("maxLon");
            double maxLat=extentObj.getDouble("maxLat");
            polygon = GeometryUtil.createPolygonByExtent(minLon,minLat,maxLon,maxLat);
            sb.append(String.format(" and intersects(i.geo,ST_PolygonFromText('%s',%d))=true ",polygon.toText(), GeometryUtil.DefaultSRID));
        }
        else if(rootObj.containsKey("polygon")){
            JSONArray polygonObj=rootObj.getJSONArray("polygon");
            int count=polygonObj.size();
            Coordinate[] coords=new Coordinate[count+1];
            JSONObject coordObj;
            int i;
            for(i=0;i<count;i++){
                coordObj=polygonObj.getJSONObject(i);
                coords[i]=new Coordinate(coordObj.getDouble("lon"),coordObj.getDouble("lat"));
            }
            coords[i]=coords[0];
            polygon = GeometryUtil.createPolygon(coords);
            sb.append(String.format(" and intersects(i.geo,ST_PolygonFromText('%s',%d))=true ",polygon.toText(), GeometryUtil.DefaultSRID));
        }
        return sb.toString();
    }

    public static String constructImageStatisticSQL(String params) {
        StringBuilder sb=new StringBuilder();
        JSONObject rootObj=JSONObject.parseObject(params);

        if(rootObj.containsKey("satellite")) {
            String satellite = rootObj.getString("satellite").trim();
            sb.append(String.format(" and satellite = '%s' ", satellite));
        }

        if(rootObj.containsKey("level")) {
            String level = rootObj.getString("level").trim();
            sb.append(String.format(" and level = '%s' ",level));
        }

        if(rootObj.containsKey("sensor")) {
            String sensor = rootObj.getString("sensor").trim();
            sb.append(String.format(" and sensor = '%s' ",sensor));
        }

        if(rootObj.containsKey("startUploadDate")) {
            String startUploadDate = rootObj.getString("startUploadDate").trim();
            sb.append(String.format(" and produce_time >= '%s' ",startUploadDate));
        }
        if(rootObj.containsKey("endUploadDate")) {
            String endUploadDate = rootObj.getString("endUploadDate").trim();
            sb.append(String.format(" and produce_time <= '%s' ",endUploadDate));
        }
        return sb.toString();
    }

    public static String constructShapeSQL(String params) {
        StringBuilder sb=new StringBuilder();
        Polygon polygon=null;
        JSONObject rootObj=JSONObject.parseObject(params);

        if(rootObj.containsKey("name")) {
            String name = rootObj.getString("name").trim();
            sb.append(" and s.name like '%"+name+"%' ");
        }

        if(rootObj.containsKey("startUploadDate")) {
            String startUploadDate = rootObj.getString("startUploadDate").trim();
            sb.append(String.format(" and s.uploadTime >= '%s' ",startUploadDate));
        }
        if(rootObj.containsKey("endUploadDate")) {
            String endUploadDate = rootObj.getString("endUploadDate").trim();
            sb.append(String.format(" and s.uploadTime <= '%s' ",endUploadDate));
        }

        if(rootObj.containsKey("serviceId")) {
            String serviceId = rootObj.getString("serviceId").trim();
            sb.append(String.format(" and s.serviceId = '%s' ",serviceId));
        }

        if(rootObj.containsKey("extent")){
            JSONObject extentObj=rootObj.getJSONObject("extent");
            double minLon=extentObj.getDouble("minLon");
            double minLat=extentObj.getDouble("minLat");
            double maxLon=extentObj.getDouble("maxLon");
            double maxLat=extentObj.getDouble("maxLat");
            polygon = GeometryUtil.createPolygonByExtent(minLon,minLat,maxLon,maxLat);
            sb.append(String.format(" and intersects(s.geo,ST_PolygonFromText('%s',%d))=true ",polygon.toText(), GeometryUtil.DefaultSRID));
        }
        else if(rootObj.containsKey("polygon")){
            JSONArray polygonObj=rootObj.getJSONArray("polygon");
            int count=polygonObj.size();
            Coordinate[] coords=new Coordinate[count+1];
            JSONObject coordObj;
            int i;
            for(i=0;i<count;i++){
                coordObj=polygonObj.getJSONObject(i);
                coords[i]=new Coordinate(coordObj.getDouble("lon"),coordObj.getDouble("lat"));
            }
            coords[i]=coords[0];
            polygon = GeometryUtil.createPolygon(coords);
            sb.append(String.format(" and intersects(s.geo,ST_PolygonFromText('%s',%d))=true ",polygon.toText(), GeometryUtil.DefaultSRID));
        }
        return sb.toString();
    }

    public static String constructTerrainSQL(String params) {
        StringBuilder sb=new StringBuilder();
        Polygon polygon=null;
        JSONObject rootObj=JSONObject.parseObject(params);

        if(rootObj.containsKey("name")) {
            String name = rootObj.getString("name").trim();
            sb.append(" and t.name like '%"+name+"%' ");
        }

        if(rootObj.containsKey("startUploadDate")) {
            String startUploadDate = rootObj.getString("startUploadDate").trim();
            sb.append(String.format(" and t.uploadTime >= '%s' ",startUploadDate));
        }
        if(rootObj.containsKey("endUploadDate")) {
            String endUploadDate = rootObj.getString("endUploadDate").trim();
            sb.append(String.format(" and t.uploadTime <= '%s' ",endUploadDate));
        }

        if(rootObj.containsKey("serviceId")) {
            String serviceId = rootObj.getString("serviceId").trim();
            sb.append(String.format(" and t.serviceId = '%s' ",serviceId));
        }

        if(rootObj.containsKey("extent")){
            JSONObject extentObj=rootObj.getJSONObject("extent");
            double minLon=extentObj.getDouble("minLon");
            double minLat=extentObj.getDouble("minLat");
            double maxLon=extentObj.getDouble("maxLon");
            double maxLat=extentObj.getDouble("maxLat");
            polygon = GeometryUtil.createPolygonByExtent(minLon,minLat,maxLon,maxLat);
            sb.append(String.format(" and intersects(t.geo,ST_PolygonFromText('%s',%d))=true ",polygon.toText(), GeometryUtil.DefaultSRID));
        }
        else if(rootObj.containsKey("polygon")){
            JSONArray polygonObj=rootObj.getJSONArray("polygon");
            int count=polygonObj.size();
            Coordinate[] coords=new Coordinate[count+1];
            JSONObject coordObj;
            int i;
            for(i=0;i<count;i++){
                coordObj=polygonObj.getJSONObject(i);
                coords[i]=new Coordinate(coordObj.getDouble("lon"),coordObj.getDouble("lat"));
            }
            coords[i]=coords[0];
            polygon = GeometryUtil.createPolygon(coords);
            sb.append(String.format(" and intersects(t.geo,ST_PolygonFromText('%s',%d))=true ",polygon.toText(), GeometryUtil.DefaultSRID));
        }
        return sb.toString();
    }

    public static String constructControlPointSQL(String params) {
        StringBuilder sb=new StringBuilder();
        Polygon polygon=null;
        JSONObject rootObj=JSONObject.parseObject(params);

        if(rootObj.containsKey("startUploadDate")) {
            String startUploadDate = rootObj.getString("startUploadDate").trim();
            sb.append(String.format(" and c.uploadTime >= '%s' ",startUploadDate));
        }
        if(rootObj.containsKey("endUploadDate")) {
            String endUploadDate = rootObj.getString("endUploadDate").trim();
            sb.append(String.format(" and c.uploadTime <= '%s' ",endUploadDate));
        }

        if(rootObj.containsKey("extent")){
            JSONObject extentObj=rootObj.getJSONObject("extent");
            double minLon=extentObj.getDouble("minLon");
            double minLat=extentObj.getDouble("minLat");
            double maxLon=extentObj.getDouble("maxLon");
            double maxLat=extentObj.getDouble("maxLat");
            polygon = GeometryUtil.createPolygonByExtent(minLon,minLat,maxLon,maxLat);
            sb.append(String.format(" and intersects(c.geo,ST_PolygonFromText('%s',%d))=true ",polygon.toText(), GeometryUtil.DefaultSRID));
        }
        else if(rootObj.containsKey("polygon")){
            JSONArray polygonObj=rootObj.getJSONArray("polygon");
            int count=polygonObj.size();
            Coordinate[] coords=new Coordinate[count+1];
            JSONObject coordObj;
            int i;
            for(i=0;i<count;i++){
                coordObj=polygonObj.getJSONObject(i);
                coords[i]=new Coordinate(coordObj.getDouble("lon"),coordObj.getDouble("lat"));
            }
            coords[i]=coords[0];
            polygon = GeometryUtil.createPolygon(coords);
            sb.append(String.format(" and intersects(c.geo,ST_PolygonFromText('%s',%d))=true ",polygon.toText(), GeometryUtil.DefaultSRID));
        }
        return sb.toString();
    }

    public static String constructTilesetSQL(String params) {
        StringBuilder sb=new StringBuilder();
        Polygon polygon=null;
        JSONObject rootObj=JSONObject.parseObject(params);

        if(rootObj.containsKey("id")) {
            Long id = rootObj.getLong("id");
            sb.append(" and t.id = "+id+" ");
        }

        if(rootObj.containsKey("name")) {
            String name = rootObj.getString("name").trim();
            sb.append(" and t.name like '%"+name+"%' ");
        }

        if(rootObj.containsKey("startUploadDate")) {
            String startUploadDate = rootObj.getString("startUploadDate").trim();
            sb.append(String.format(" and t.uploadTime >= '%s' ",startUploadDate));
        }
        if(rootObj.containsKey("endUploadDate")) {
            String endUploadDate = rootObj.getString("endUploadDate").trim();
            sb.append(String.format(" and t.uploadTime <= '%s' ",endUploadDate));
        }

        if(rootObj.containsKey("extent")){
            JSONObject extentObj=rootObj.getJSONObject("extent");
            double minLon=extentObj.getDouble("minLon");
            double minLat=extentObj.getDouble("minLat");
            double maxLon=extentObj.getDouble("maxLon");
            double maxLat=extentObj.getDouble("maxLat");
            polygon = GeometryUtil.createPolygonByExtent(minLon,minLat,maxLon,maxLat);
            sb.append(String.format(" and intersects(t.geo,ST_PolygonFromText('%s',%d))=true ",polygon.toText(), GeometryUtil.DefaultSRID));
        }
        else if(rootObj.containsKey("polygon")){
            JSONArray polygonObj=rootObj.getJSONArray("polygon");
            int count=polygonObj.size();
            Coordinate[] coords=new Coordinate[count+1];
            JSONObject coordObj;
            int i;
            for(i=0;i<count;i++){
                coordObj=polygonObj.getJSONObject(i);
                coords[i]=new Coordinate(coordObj.getDouble("lon"),coordObj.getDouble("lat"));
            }
            coords[i]=coords[0];
            polygon = GeometryUtil.createPolygon(coords);
            sb.append(String.format(" and intersects(t.geo,ST_PolygonFromText('%s',%d))=true ",polygon.toText(), GeometryUtil.DefaultSRID));
        }
        return sb.toString();
    }

    public static String constructGltfMetaSQL(String params) {
        StringBuilder sb=new StringBuilder();
        JSONObject rootObj=JSONObject.parseObject(params);

        if(rootObj.containsKey("id")) {
            Long id = rootObj.getLong("id");
            sb.append(" and g.id = "+id+" ");
        }

        if(rootObj.containsKey("name")) {
            String name = rootObj.getString("name").trim();
            sb.append(" and g.name like '%"+name+"%' ");
        }

        if(rootObj.containsKey("title")) {
            String title = rootObj.getString("title").trim();
            sb.append(" and g.title like '%"+title+"%' ");
        }


        if(rootObj.containsKey("startUploadDate")) {
            String startUploadDate = rootObj.getString("startUploadDate").trim();
            sb.append(String.format(" and g.uploadTime >= '%s' ",startUploadDate));
        }
        if(rootObj.containsKey("endUploadDate")) {
            String endUploadDate = rootObj.getString("endUploadDate").trim();
            sb.append(String.format(" and g.uploadTime <= '%s' ",endUploadDate));
        }
        return sb.toString();
    }

    public static String constructOSGBSQL(String params) {
        StringBuilder sb=new StringBuilder();
        Polygon polygon=null;
        JSONObject rootObj=JSONObject.parseObject(params);

        if(rootObj.containsKey("id")) {
            Long id = rootObj.getLong("id");
            sb.append(" and o.id = "+id+" ");
        }

        if(rootObj.containsKey("name")) {
            String name = rootObj.getString("name").trim();
            sb.append(" and o.name like '%"+name+"%' ");
        }

        if(rootObj.containsKey("startUploadDate")) {
            String startUploadDate = rootObj.getString("startUploadDate").trim();
            sb.append(String.format(" and o.uploadTime >= '%s' ",startUploadDate));
        }
        if(rootObj.containsKey("endUploadDate")) {
            String endUploadDate = rootObj.getString("endUploadDate").trim();
            sb.append(String.format(" and o.uploadTime <= '%s' ",endUploadDate));
        }

        if(rootObj.containsKey("extent")){
            JSONObject extentObj=rootObj.getJSONObject("extent");
            double minLon=extentObj.getDouble("minLon");
            double minLat=extentObj.getDouble("minLat");
            double maxLon=extentObj.getDouble("maxLon");
            double maxLat=extentObj.getDouble("maxLat");
            polygon = GeometryUtil.createPolygonByExtent(minLon,minLat,maxLon,maxLat);
            sb.append(String.format(" and intersects(o.geo,ST_PolygonFromText('%s',%d))=true ",polygon.toText(), GeometryUtil.DefaultSRID));
        }
        else if(rootObj.containsKey("polygon")){
            JSONArray polygonObj=rootObj.getJSONArray("polygon");
            int count=polygonObj.size();
            Coordinate[] coords=new Coordinate[count+1];
            JSONObject coordObj;
            int i;
            for(i=0;i<count;i++){
                coordObj=polygonObj.getJSONObject(i);
                coords[i]=new Coordinate(coordObj.getDouble("lon"),coordObj.getDouble("lat"));
            }
            coords[i]=coords[0];
            polygon = GeometryUtil.createPolygon(coords);
            sb.append(String.format(" and intersects(o.geo,ST_PolygonFromText('%s',%d))=true ",polygon.toText(), GeometryUtil.DefaultSRID));
        }
        return sb.toString();
    }

    public static String constructPointCloudSQL(String params) {
        StringBuilder sb=new StringBuilder();
        JSONObject rootObj=JSONObject.parseObject(params);

        if(rootObj.containsKey("name")) {
            String name = rootObj.getString("name").trim();
            sb.append(" and p.name like '%"+name+"%' ");
        }

        if(rootObj.containsKey("uploadTime")) {
            String startUploadDate = rootObj.getString("uploadTime").trim();
            sb.append(String.format(" and p.uploadTime >= '%s' ",startUploadDate));
        }
        if(rootObj.containsKey("endUploadDate")) {
            String endUploadDate = rootObj.getString("endUploadDate").trim();
            sb.append(String.format(" and p.uploadTime <= '%s' ",endUploadDate));
        }
        return sb.toString();
    }
}
