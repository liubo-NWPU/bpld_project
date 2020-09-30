package com.gis.trans.controller;

import com.gis.trans.model.ResponseModel;
import com.gis.trans.model.Video;
import com.gis.trans.service.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(value = "VideoController", description = "视频操作")
@RestController
@RequestMapping("/videoService")
public class VideoController {
    
    @Autowired
    private VideoService videoService;

    @ApiOperation(value = "增加/修改")
    @RequestMapping(value = "/addVideo", method = RequestMethod.POST)
    public ResponseModel getVideoData(@RequestBody Video video) {
        return  videoService.addVideo(video);
    }

    @RequestMapping(value = "/saveVideo", method = RequestMethod.POST)
    public void saveVideo(HttpServletRequest request,@RequestBody MultipartFile file) {
        Map  map = new HashMap<>();
        try {
            InputStream inputStream = file.getInputStream();
            // todo 返回小视频url 和 图片url
            videoService.uploadVideo(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ;
    }

    @ApiOperation(value = "删除")
    @RequestMapping(value = "/deleteVideo", method = RequestMethod.POST)
    public ResponseModel deleteData(@RequestParam(value="VideoId[]")Long[] videoIds) {
        return videoService.deleteAllVideo(videoIds);
    }

    @ApiOperation(value = "根据视频name查询")
    @RequestMapping(value = "/searchByName", method = RequestMethod.GET)
    public List<Video> searchByName(String name) {
        return  videoService.searchByName(name);
    }
    @ApiOperation(value = "根据视频id查询")
    @RequestMapping(value = "/searchByVideoId", method = RequestMethod.GET)
    public Video searchByVideoId(@RequestParam Long videoId) {
        return videoService.searchByVideoId(videoId);

    }
}
