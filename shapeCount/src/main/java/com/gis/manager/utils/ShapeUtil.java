package com.gis.manager.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import org.gdal.gdal.gdal;
import org.gdal.ogr.*;
import org.gdal.osr.SpatialReference;
import org.springframework.ui.ModelMap;

import java.io.File;
import java.util.Vector;

/**
 * @Author:wangmeng
 * @Date:创建于2018/3/21 17:23
 * @Description:
 */
public class ShapeUtil {
//    public static ShapeMeta getShapeMetaInfo(String filePath,String fileName) {
//        ShapeMeta shapeMeta=null;
//        File file=new File(filePath,fileName);
//        if (file.exists()) {
//            try {
//                // 打开数据
//                DataSource ds = ogr.Open(file.getPath(),0);
//                if (ds == null) {
//                    System.err.println("打开shp失败:" + file.getPath());
//                    return null;
//                }
//                // 获取该数据源中的图层个数，一般shp数据图层只有一个，如果是mdb、dxf等图层就会有多个
//                Layer layer = ds.GetLayer(0);
//                if (layer == null)         {
//                    System.out.println("获取图层失败:"+ file.getPath());
//                    return null;
//                }
//                shapeMeta=new ShapeMeta();
//                shapeMeta.setType(ogr.GeometryTypeToName(layer.GetGeomType()));
//                double[] extents=layer.GetExtent();
//                shapeMeta.setMinX(extents[0]);
//                shapeMeta.setMinY(extents[2]);
//                shapeMeta.setMaxX(extents[1]);
//                shapeMeta.setMaxY(extents[3]);
//                Polygon polygon = GeometryUtil.createPolygonByExtent(shapeMeta.getMinX(), shapeMeta.getMinY(), shapeMeta.getMaxX(), shapeMeta.getMaxY());
//                try {
//                    SpatialReference sourceReference=layer.GetSpatialRef();
//                    if(sourceReference!=null) {
//                        String wkt = sourceReference.ExportToWkt();
//                        Integer epsg = GeometryUtil.getEPSG(wkt);
//                        if (epsg == null) {
//                            shapeMeta.setProjection(wkt);
//                        } else {
//                            shapeMeta.setEpsg(epsg);
//                        }
//                        if (sourceReference.IsSame(GeometryUtil.getTargetReference()) == 0) {
//                            polygon = GeometryUtil.createPolygon(GeometryUtil.project(polygon.getCoordinates(), sourceReference, GeometryUtil.getTargetReference()));
//                        }
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                polygon.setSRID(GeometryUtil.DefaultSRID);
//                shapeMeta.setGeo(polygon);
//
//                layer.delete();
//                ds.delete();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return shapeMeta;
//    }

    public static JSONArray ShapeFields(String filePath, String fileName,String charset) {
        JSONArray jsonArray = new JSONArray();
        File file=new File(filePath,fileName);
        if (file.exists()) {
            try {
                // 为了使属性表字段支持中文，请添加下面这句
                gdal.SetConfigOption("SHAPE_ENCODING",charset);
                // 打开数据
                DataSource ds = ogr.Open(file.getPath(),0);
                if (ds == null) {
                    System.err.println("打开shp失败:" + file.getPath());
                    return null;
                }
                // 获取该数据源中的图层个数，一般shp数据图层只有一个，如果是mdb、dxf等图层就会有多个
                Layer layer = ds.GetLayer(0);
                if (layer == null)         {
                    System.out.println("获取图层失败:"+ file.getPath());
                    return null;
                }

                FeatureDefn featureDefn =layer.GetLayerDefn();

                JSONObject fieldObj = new JSONObject();
                fieldObj.put("name","FID");
                fieldObj.put("type","Long");
                jsonArray.add(fieldObj);

                fieldObj = new JSONObject();
                fieldObj.put("name","Shape");
                fieldObj.put("type",ogr.GeometryTypeToName(layer.GetGeomType()));
                jsonArray.add(fieldObj);

                int iFieldCount =featureDefn.GetFieldCount();
                FieldDefn fieldDefn;
                for (int iAttr = 0; iAttr<iFieldCount;iAttr++){
                    fieldDefn =featureDefn.GetFieldDefn(iAttr);
                    fieldObj = new JSONObject();
                    fieldObj.put("name",fieldDefn.GetName());
                    fieldObj.put("type",fieldDefn.GetFieldTypeName(fieldDefn.GetFieldType()));
                    jsonArray.add(fieldObj);
                }
                layer.delete();
                ds.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return jsonArray;
    }

    public static ModelMap ShapeSearch(String filePath,
                                       String fileName,
                                       String charset,
                                       String attrCon,
                                       String geoCon,
                                       Integer pageNumber,
                                       Integer pageSize,
                                       Boolean withArea,
                                       Boolean withGeometry,
                                       Boolean isClip,
                                       Boolean hasProject) {
        ModelMap modelMap = new ModelMap();
        modelMap.put("success",false);
        JSONArray fields = new JSONArray();
        File file=new File(filePath,fileName);
        if (file.exists()) {
            try {
                // 为了使属性表字段支持中文，请添加下面这句
                gdal.SetConfigOption("SHAPE_ENCODING",charset);
                // 打开数据
                DataSource ds = ogr.Open(file.getPath(), 0);
                if (ds == null) {
                    System.err.println("打开shp失败:" + file.getPath());
                    return null;
                }
                // 获取该数据源中的图层个数，一般shp数据图层只有一个，如果是mdb、dxf等图层就会有多个
                Layer layer = ds.GetLayer(0);
                Geometry geometry,clipGeometry=null;
                if (layer == null) {
                    System.out.println("获取图层失败:" + file.getPath());
                    return null;
                }
                if(attrCon!=null && attrCon.trim().length()>0){
                    try {
                        layer.SetAttributeFilter(attrCon);
                    }
                    catch(Exception attrexe){
                        modelMap.put("describe","属性查询条件错误");
                        return modelMap;
                    }
                }
                if(geoCon!=null && geoCon.trim().length()>0){
                    clipGeometry = Geometry.CreateFromWkt(geoCon);
                    if(clipGeometry==null){
                        modelMap.put("describe","几何(wkt)查询条件错误");
                        return modelMap;
                    }
                    else{
                        clipGeometry.AssignSpatialReference(GeometryUtil.getTargetReference());
                        if(hasProject){
                            clipGeometry.TransformTo(layer.GetSpatialRef());
                        }
                        layer.SetSpatialFilter(clipGeometry);
                    }
                }
                Feature feature;
                layer.SetNextByIndex((pageNumber-1)*pageSize);
                int index = 0,iField;
                FeatureDefn featureDefn = layer.GetLayerDefn();
                FieldDefn fieldDefn;
                int fieldCount = featureDefn.GetFieldCount();
                JSONObject fieldObj;
                String shapeType = ogr.GeometryTypeToName(layer.GetGeomType());
                // 下面开始遍历图层中的要素
                while ((feature = layer.GetNextFeature()) != null) {
                    fieldObj = new JSONObject();
                    fieldObj.put("FID",feature.GetFID());
                    fieldObj.put("Shape",shapeType);
                    for (iField = 0; iField < fieldCount; iField++) {
                        fieldDefn = featureDefn.GetFieldDefn(iField);
                        switch (fieldDefn.GetFieldType()) {
                            case ogr.OFTString:
                                fieldObj.put(fieldDefn.GetName(),feature.GetFieldAsString(iField));
                                break;
                            case ogr.OFTReal:
                                fieldObj.put(fieldDefn.GetName(),feature.GetFieldAsDouble(iField));
                                break;
                            case ogr.OFTInteger:
                                fieldObj.put(fieldDefn.GetName(),feature.GetFieldAsInteger(iField));
                                break;
                            default:
                                fieldObj.put(fieldDefn.GetName(),feature.GetFieldAsString(iField));
                                break;
                        }
                    }
                    geometry=feature.GetGeometryRef();
                    if(withGeometry) {
                        if(isClip && clipGeometry !=null && geometry.Intersect(clipGeometry)){
                            geometry = geometry.Intersection(clipGeometry);
                        }
                        fieldObj.put("Geometry",geometry.ExportToWkt());
                    }
                    if(withArea && geometry.GetDimension()==2) {
                        if (hasProject && layer.GetSpatialRef().IsGeographic() == 1) {
                            geometry.TransformTo(GeometryUtil.getProjectedReference());
                        }
                        fieldObj.put("Area", geometry.GetArea());
                    }
                    fields.add(fieldObj);
                    index++;
                    if (index >= pageSize)
                        break;
                }
                modelMap.put("total",layer.GetFeatureCount());
                modelMap.put("rows",fields);
                modelMap.put("success",true);
                layer.delete();
                ds.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return modelMap;
    }

    public static ModelMap ShapeByFid(String filePath,String fileName,Long fid,String charset) {
        ModelMap modelMap = new ModelMap();
        modelMap.put("success",false);
        File file=new File(filePath,fileName);
        if (file.exists()) {
            try {
                // 为了使属性表字段支持中文，请添加下面这句
                gdal.SetConfigOption("SHAPE_ENCODING",charset);
                // 打开数据
                DataSource ds = ogr.Open(file.getPath(), 0);
                if (ds == null) {
                    System.err.println("打开shp失败:" + file.getPath());
                    return null;
                }
                // 获取该数据源中的图层个数，一般shp数据图层只有一个，如果是mdb、dxf等图层就会有多个
                Layer layer = ds.GetLayer(0);
                if (layer == null) {
                    System.out.println("获取图层失败:" + file.getPath());
                    return null;
                }
                String attrCon = String.format("FID = %d",fid);
                try {
                    layer.SetAttributeFilter(attrCon);
                }
                catch(Exception attrexe){
                    modelMap.put("describe","属性查询条件错误");
                    return modelMap;
                }
                String shapeType = ogr.GeometryTypeToName(layer.GetGeomType());
                Feature feature=layer.GetNextFeature();
                JSONObject itemObject = new JSONObject();
                if(feature!=null) {
                    itemObject.put("FID",feature.GetFID());
                    itemObject.put("Shape",shapeType);
                    int iField;
                    FeatureDefn featureDefn = layer.GetLayerDefn();
                    FieldDefn fieldDefn;
                    int fieldCount = featureDefn.GetFieldCount();
                    for (iField = 0; iField < fieldCount; iField++) {
                        fieldDefn = featureDefn.GetFieldDefn(iField);
                        switch (fieldDefn.GetFieldType()) {
                            case ogr.OFTString:
                                itemObject.put(fieldDefn.GetName(),feature.GetFieldAsString(iField));
                                break;
                            case ogr.OFTReal:
                                itemObject.put(fieldDefn.GetName(),feature.GetFieldAsDouble(iField));
                                break;
                            case ogr.OFTInteger:
                                itemObject.put(fieldDefn.GetName(),feature.GetFieldAsInteger(iField));
                                break;
                            default:
                                itemObject.put(fieldDefn.GetName(),feature.GetFieldAsString(iField));
                                break;
                        }
                    }
                    itemObject.put("Geometry",feature.GetGeometryRef().ExportToWkt());
                    modelMap.put("item", itemObject);
                    modelMap.put("success", true);
                }
                else {
                    modelMap.put("describe","不存在要素");
                }
                layer.delete();
                ds.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return modelMap;
    }
}
