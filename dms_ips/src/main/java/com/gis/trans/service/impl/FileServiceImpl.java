package com.gis.trans.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.gis.trans.controller.PlotInfController;
import com.gis.trans.model.ResponseModel;
import com.gis.trans.service.FileService;
import com.gis.trans.utils.Crc32Util;
import com.gis.trans.utils.HttpUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Service
public class FileServiceImpl implements FileService {

    @Value("${system.file.manager}")
    private String managerUrl;

    @Autowired
    private PlotInfController plotInfController;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${auth.local_url}")
    private String authUrl;

    @Value("${system.file.diffPath}")
    private String diffPath;

    @Override
    public JSONObject addFolder(Long parentId, String folderName, HttpServletRequest request, Integer sort) {
        JSONObject result = new JSONObject();
        JSONObject params = new JSONObject();
        Map<String, String> map = new HashMap<>();
        map.put("parentId", parentId.toString());
        map.put("folderName", folderName);
        String s = HttpUtil.sendPost(managerUrl+"/manager/rest/filecatalog/addFolder", map);
        if (s != null) {
            params.put("sort", sort);
            JSONObject jsonObject = JSONObject.parseObject(s);
            // 需要调用数据权限管理的接口，并且把获取到的数据传递给权限管理服务，让另一个数据库同步创建文件夹目录
            params.put("id", jsonObject.getLongValue("id"));
            params.put("name", jsonObject.getString("fileName"));
            params.put("url", null);
            params.put("permission", null);
            params.put("icon", null);
            params.put("level", null);
            params.put("type", "FOLDER");
            params.put("parentId", jsonObject.getString("parentId"));
            params.put("createBy", "0");
            params.put("creater", "管理员");
            params.put("createDate", jsonObject.get("uploadTime"));
            params.put("updateBy", "0");
            params.put("updater", "管理员");
            params.put("updateDate", jsonObject.get("updateTime"));
            params.put("delFlag", "NORMAL");
            String authCataLogName = "";
            if (parentId != -1) {
                authCataLogName = getTotalFatherName(parentId);
            }
            params.put("folderid", Crc32Util.createID(authCataLogName + folderName));
            String isInsertSuccess = HttpUtil.sendPostToAuth(authUrl + "/system/resources", params.toJSONString(), request);
            if (isInsertSuccess == null || "".equals(isInsertSuccess)) {
                // 如果获取插入资源表时失败，则同时删除目录表中的该数据
                Map map1 = new HashMap<>();
                map1.put("folderIds[]", jsonObject.getString("id"));
                HttpUtil.sendPost(managerUrl+"/manager/rest/file/deleteFiles", map);
                result.put("success", false);
                result.put("desc", "插入资源表时失败");
                 return result;
            } else {
                //insert(item);
                result.put("success", true);
                result.put("data", jsonObject);
            }
        }
        return result;
    }

    /**
     * 获取文件目录名称字符串
     *
     * @param parentId
     * @return
     */
    public String getTotalFatherName(Long parentId) {
        String authCataLogName = "";
        JSONObject fileCatalogJsonObj = new JSONObject();
        Map map = new HashMap();
        map.put("id", parentId.toString());
        String s = HttpUtil.sendGet(managerUrl+"/manager/rest/filecatalog/getById", map);
        JSONObject jsonObject = JSONObject.parseObject(s);
        // 当该对象的父节点不为-1的时候
        if (jsonObject.getLong("parentId") != -1) {
            fileCatalogJsonObj.put("id", jsonObject.getLong("id"));
            fileCatalogJsonObj.put("filename", jsonObject.getString("fileName"));
            authCataLogName = getTotalFatherName(jsonObject.getLong("parentId"));
            authCataLogName += fileCatalogJsonObj.getString("filename");
        } else {
            authCataLogName = jsonObject.getString("fileName") + authCataLogName;
        }
        return authCataLogName;
    }

//    @Override
//    public ModelMap uploadFile(MultipartFile file, String name, Long parentId, String param,String radar) {
//        ModelMap body = new ModelMap();
//        File targetFile = new File(diffPath, file.getOriginalFilename());
//        try {
//            FileUtils.copyInputStreamToFile(file.getInputStream(), targetFile);
//        }catch (Exception e){
//            e.printStackTrace();
//            return null;
//        }
//        ResponseModel responseModel = plotInfController.insertPlotList(1.2, 1.12, 1.2, 1.2, targetFile.getAbsolutePath(),radar);
//        if (responseModel.isSuccess()){
//            return null;
//        }
//        return null;
//    }

}
