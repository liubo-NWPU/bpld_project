package com.gis.trans.service;

import com.gis.trans.model.ResponseModel;
import com.gis.trans.model.Result;
import com.gis.trans.model.ShapePoint;

import java.io.FileNotFoundException;
import java.util.List;

public interface PlotInfSevice extends IBaseService<ShapePoint, String> {

    List<ShapePoint> searchContain(Long fileId, String polygons);

    List<ShapePoint> searchExclude(Long fileId, String polygons);

    Result makeShape(List<ShapePoint> list, String fileName, Long parentId);

    Result uploadFile(String filePath,Long parentId);

    ResponseModel updateShapeInf(Double x, Double y, Double angle, Long fileId);

    List<ShapePoint> findByFileId(Long fileId,String radar);

    ResponseModel getShape(String parentId,String radar,String startTime,String endTime);

    ResponseModel getShapepath(String path);

    ResponseModel getCount(String radar,String startTime,String endTime);

    ResponseModel getStrain(String radar,String startTime,String endTime);

    ResponseModel parseDiffImage(String filePath, String[] filePaths, String radarName);

    ResponseModel insertPlotList(String filePath, String radarName);

    ResponseModel dtop(String diffimagePath,String pointCloudPath,String radarName);

    ResponseModel ptostrain(String radar, String startTime, String endTime, Long parentId);

    List<ShapePoint> getPointByTime(String radarkey,String startTime,String endTime);
}
