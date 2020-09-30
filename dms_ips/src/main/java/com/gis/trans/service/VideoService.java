package com.gis.trans.service;

import com.gis.trans.model.ResponseModel;
import com.gis.trans.model.Video;

import java.io.InputStream;
import java.util.List;

public interface VideoService extends IBaseService<Video, Long> {

    ResponseModel addVideo(Video video);

    ResponseModel deleteAllVideo(Long[] ids);


    ResponseModel deleteVideo(Long id);

    List<Video> queryAll();

    List<Video> searchByName(String name);

    Video searchByVideoId(Long videoId);

    List<Video> findByVideoKey(String videoKey);

   void uploadVideo( InputStream inputStream);

}
