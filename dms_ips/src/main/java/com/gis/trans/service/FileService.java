package com.gis.trans.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

public interface FileService {

    JSONObject addFolder(Long parentId, String folderName, HttpServletRequest request, Integer sort);

//     ModelMap uploadFile(MultipartFile file, String name, Long parentId, String param,String radar);

}
