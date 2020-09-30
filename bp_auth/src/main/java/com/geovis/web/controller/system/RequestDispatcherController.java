package com.geovis.web.controller.system;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.geovis.core.constant.Constant;
import com.geovis.core.util.ResponseUtil;
import com.geovis.web.domain.enums.ResourceType;
import com.geovis.web.domain.enums.ServiceType;
import com.geovis.web.domain.model.ResponseDMFolderModel;
import com.geovis.web.domain.system.SysResource;
import com.geovis.web.domain.system.SysUser;
import com.geovis.web.service.system.ResourceService;
import com.geovis.web.service.system.SysLogService;
import com.geovis.web.service.system.UserService;
import com.geovis.web.util.system.SysLogUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

@Api(value = "RequestDispatcherController", description = "服务请求转发")
@RestController
@SuppressWarnings("all")
@CrossOrigin
@RequestMapping("/dispatcher")
public class RequestDispatcherController {
    private static final Logger logger = LoggerFactory.getLogger(RequestDispatcherController.class);
    @Value("${plugin.service.adress}")
    private String pluginServiceAdress;
    @Value("${dm.service.adress}")
    private String dmServiceAdress;
    @Value("${monitor.service.adress}")
    private String monitorService;


    @Autowired
    private UserService userService;
    @Autowired
    private SysLogService sysLogService;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private RestTemplate restTemplate;

    @ApiOperation(value = "服务转发", notes = "常规服务接口")
    @CrossOrigin
    @PostMapping(value = "/trans")
    public ResponseEntity dispatCherPlugin(HttpServletRequest request, HttpServletResponse response,
                                           @RequestBody String messageInfo) {
        ResponseEntity<String> responseEntity = null;
        SysUser sysUser = null;
        String requestURI = null;
        String serviveType = null;
        HashMap<String, Object> multiValueMap = null;
        try {
            if (messageInfo != null && messageInfo.length() > 0) {
                multiValueMap = transObject(messageInfo.trim());
            } else {
                multiValueMap = new HashMap<String, Object>();
            }

            String userId = multiValueMap.get("userId").toString();

            try {
                String fileId = multiValueMap.get("fileIds%5B%5D").toString();
                if (fileId.contains("@")) {
                    String[] fileIds = fileId.split("@");
                    for (int i = 0; i < fileIds.length; i++) {
                        SysResource sr = resourceService.getResourceById(fileIds[i] + "");
                        sr.setDelFlag("DELETE");
                        resourceService.commonSave(sysUser, sr);
                    }
                } else {
                    SysResource sr = resourceService.getResourceById(fileId + "");
                    sr.setDelFlag("DELETE");
                    resourceService.commonSave(sysUser, sr);
                }
            } catch (Exception e) {

            }

            logger.info("[dispatCherPlugin] : start ,userId=" + userId);
            sysUser = userService.selectUserById(userId);
            serviveType = request.getHeader(Constant.SERVICE_TYPE);
            requestURI = request.getHeader(Constant.PLGUGIN_HEADER_ADRESSS);
            String targetURL = null;
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            String userName = request.getHeader(Constant.USER_NAME_TARGET);

            String token = request.getHeader(Constant.USER_ACCESSTOKEN_TARGET);
            headers.add(Constant.USER_NAME_TARGET, userName);
            headers.add(Constant.USER_ACCESSTOKEN_TARGET, token);
            if (serviveType.equals(ServiceType.PLS.getValue())) {
                targetURL = pluginServiceAdress + requestURI;
                restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
            } else if (serviveType.equals(ServiceType.DMS.getValue())) {
                targetURL = dmServiceAdress + requestURI;
                if (requestURI.endsWith("uploadCheck")) {
                    multiValueMap.remove("userId");
                }
                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter());
            } else if (serviveType.equals(ServiceType.MS.getValue())) {
                targetURL = monitorService + requestURI;
                restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));

            }

            logger.info("[dispatCherPlugin] : targetURL=" + targetURL);


            HttpEntity<String> requestEntity = new HttpEntity<String>(
                    messageInfo, headers);
            responseEntity = restTemplate.postForEntity(targetURL, requestEntity, String.class);

            String result = printResponseEntity(responseEntity);
            if (requestURI.contains(Constant.USER_INSERT_OPERATION)
                    || requestURI.contains(Constant.USER_UPDATE_OPERATION)
                    || requestURI.contains(Constant.USER_DELETE_OPERATION)) {
                SysLogUtil.saveLog(request, sysUser, serviveType + "  操作");
            }
           /* try {
                if (serviveType.equals(ServiceType.PLS.getValue())) {
                    boolean success =(boolean) (JSONObject.parseObject(result).get("success"));
                    if (!success){
                        responseEntity = ResponseUtil.failure("操作不规范");
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }*/
            logger.info("[dispatCherPlugin] : end");
        } catch (Exception e) {
            if (requestURI.contains(Constant.USER_INSERT_OPERATION)
                    || requestURI.contains(Constant.USER_UPDATE_OPERATION)
                    || requestURI.contains(Constant.USER_DELETE_OPERATION)) {
                SysLogUtil.saveLog(request, sysUser, sysUser.getUsername() + ": 失败," + e.getMessage(),
                        serviveType + "操作");
                responseEntity = ResponseUtil.failure(e.getMessage());
            }
            logger.error("[dispatCher Exception ]:" + e.getMessage());
        }

        return responseEntity;
    }

    @ApiOperation(value = "服务转发", notes = "数据目录权限查询接口")
    @CrossOrigin
    @GetMapping(value = "/trans/folder/{userId}/{powerTypeId}/{powerDataId}")
    public ResponseEntity dispatCherPluginFolder(HttpServletRequest request, @PathVariable String userId,
                                                 @PathVariable String powerTypeId, @PathVariable String powerDataId) {
        ResponseEntity<String> responseEntity = null;
        String targetURL = null;
        String requestURI = null;
        SysUser sysUser = null;

        try {
            logger.info("[dispatCherPluginFolder] : start, userId=" + userId + ",powerTypeId=" + powerTypeId
                    + ",powerDataId=" + powerDataId);
            sysUser = userService.selectUserById(userId);
            requestURI = request.getHeader(Constant.PLGUGIN_HEADER_ADRESSS);
            String serviveType = request.getHeader(Constant.SERVICE_TYPE);
            if (serviveType.equals(ServiceType.PLS.getValue())) {
                targetURL = pluginServiceAdress + requestURI;
            } else if (serviveType.equals(ServiceType.DMS.getValue())) {

                targetURL = dmServiceAdress + requestURI;
            } else if (serviveType.equals(ServiceType.MS.getValue())) {
                targetURL = monitorService + requestURI;
            }
            logger.info("[dispatCherPluginFolder] : targetURL=" + targetURL);
            restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
            responseEntity = restTemplate.getForEntity(targetURL, String.class);
            String bodyContent = responseEntity.getBody();
            if (bodyContent != null && bodyContent.length() > 0) {
                if ((serviveType.equals(ServiceType.DMS.getValue()))) {
                    int switchId = powerTypeId == null ? 0 : Integer.parseInt(powerTypeId);
                    String powerId = powerDataId == null ? "" : powerDataId;
                    List<SysResource> resourceList = this.resourceService.listFolderTreeByUserId(sysUser.getId());
                    String regException = getRegException(switchId);
                    List<SysResource> powerResourceList = new ArrayList<SysResource>();
                    filterFolder(powerResourceList, regException, powerId, resourceList);
                    List<ResponseDMFolderModel> responseResult = JSONArray.parseArray(bodyContent,
                            ResponseDMFolderModel.class);
                    String jsonResult = setPowerData2ResponseData(powerResourceList, responseResult);
                    responseEntity = ResponseEntity.ok().body(jsonResult);
                }
            }

            String result = printResponseEntity(responseEntity);
            logger.info("[dispatCherPluginFolder] : end");
        } catch (Exception e) {
            responseEntity = ResponseUtil.failure(e.getMessage());
            logger.error("[dispatCherPluginFolder Exception ]:" + e.getMessage());
        }

        return responseEntity;
    }

    @ApiOperation(value = "服务转发", notes = "数据服务目录权限查询接口")
    @CrossOrigin
    @GetMapping(value = "/trans/folderService/{userId}/{powerTypeId}/{powerDataId}")
    public ResponseEntity dispatCherPluginServiceFolder(HttpServletRequest request, @PathVariable String userId,
                                                        @PathVariable String powerTypeId, @PathVariable String powerDataId) {
        ResponseEntity<String> responseEntity = null;
        String targetURL = null;
        String requestURI = null;
        SysUser sysUser = null;

        try {
            logger.info("[dispatCherPluginServiceFolder] : start, userId=" + userId + ",powerTypeId=" + powerTypeId
                    + ",powerDataId=" + powerDataId);
            sysUser = userService.selectUserById(userId);
            requestURI = request.getHeader(Constant.PLGUGIN_HEADER_ADRESSS);
            String serviveType = request.getHeader(Constant.SERVICE_TYPE);
            if (serviveType.equals(ServiceType.PLS.getValue())) {
                targetURL = pluginServiceAdress + requestURI;
            } else if (serviveType.equals(ServiceType.DMS.getValue())) {

                targetURL = dmServiceAdress + requestURI;
            } else if (serviveType.equals(ServiceType.MS.getValue())) {
                targetURL = monitorService + requestURI;
            }
            logger.info("[dispatCherPluginServiceFolder] : targetURL=" + targetURL);
            restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
            responseEntity = restTemplate.getForEntity(targetURL, String.class);
            String bodyContent = responseEntity.getBody();
            if (bodyContent != null && bodyContent.length() > 0) {
                if ((serviveType.equals(ServiceType.DMS.getValue()))) {
                    int switchId = powerTypeId == null ? 0 : Integer.parseInt(powerTypeId);
                    String powerId = powerDataId == null ? "" : powerDataId;
                    List<SysResource> resourceList = this.resourceService.listServiceFolderTreeByUserId(sysUser.getId());
                    String regException = getRegException(switchId);
                    List<SysResource> powerResourceList = new ArrayList<SysResource>();
                    filterService(powerResourceList, regException, powerId, resourceList);
                    List<ResponseDMFolderModel> responseResult = JSONArray.parseArray(bodyContent,
                            ResponseDMFolderModel.class);
                    String jsonResult = setServicePowerData2ResponseData(powerResourceList, responseResult);
                    responseEntity = ResponseEntity.ok().body(jsonResult);
                }
            }

            String result = printResponseEntity(responseEntity);
            logger.info("[dispatCherPluginServiceFolder] : end");
        } catch (Exception e) {
            responseEntity = ResponseUtil.failure(e.getMessage());
            logger.error("[dispatCherPluginServiceFolder Exception ]:" + e.getMessage());
        }

        return responseEntity;
    }


    @ApiOperation(value = "服务转发", notes = "数据服务文件检索接口")
    @CrossOrigin
    @GetMapping(value = "/trans/fileSearch/{userId}/{powerTypeId}/{paramFilePath}")
    public ResponseEntity dispatCherFileSearch(HttpServletRequest request, @PathVariable String userId,
                                               @PathVariable String powerTypeId,@PathVariable String paramFilePath) {
        ResponseEntity<String> responseEntity = null;
        String targetURL = null;
        String requestURI = null;
        SysUser sysUser = null;

        String filePath="";

        try{
            filePath = URLDecoder.decode(paramFilePath,"UTF-8");
        }catch(Exception ex){

        }

        try {
            logger.info("[dispatCherFileSearch] : start, userId=" + userId + ",powerTypeId=" + powerTypeId);
            sysUser = userService.selectUserById(userId);
            requestURI = request.getHeader(Constant.PLGUGIN_HEADER_ADRESSS);
            String serviveType = request.getHeader(Constant.SERVICE_TYPE);
            if (serviveType.equals(ServiceType.PLS.getValue())) {
                targetURL = pluginServiceAdress + requestURI;
            } else if (serviveType.equals(ServiceType.DMS.getValue())) {

                targetURL = dmServiceAdress + requestURI;
            } else if (serviveType.equals(ServiceType.MS.getValue())) {
                targetURL = monitorService + requestURI;
            }
            logger.info("[dispatCherFileSearch] : targetURL=" + targetURL);
            restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
            //responseEntity = restTemplate.getForEntity(targetURL, String.class);
            responseEntity = restTemplate.getForEntity(URLDecoder.decode(targetURL,"UTF-8"), String.class);
            String bodyContent = responseEntity.getBody();
            if (bodyContent != null && bodyContent.length() > 0) {
                if ((serviveType.equals(ServiceType.DMS.getValue()))) {
                    int switchId = powerTypeId == null ? 0 : Integer.parseInt(powerTypeId);
                    List<SysResource> resourceList = this.resourceService.listSourceByType(sysUser.getId(), ResourceType.FOLDER.getValue());
                    String regException = getRegException(switchId);
                    List<SysResource> powerResourceList = new ArrayList<SysResource>();
                    filterFileService(powerResourceList, regException, resourceList);
                    List<ResponseDMFolderModel> responseResult = JSONArray.parseArray(bodyContent,
                            ResponseDMFolderModel.class);
                    String jsonResult = setPowerData2ResponseData(powerResourceList, responseResult);
                    responseEntity = ResponseEntity.ok().body(jsonResult);
                }
            }

            String result = printResponseEntity(responseEntity);
            logger.info("[dispatCherFileSearch] : end");
        } catch (Exception e) {
            responseEntity = ResponseUtil.failure(e.getMessage());
            logger.error("[dispatCherFileSearch Exception ]:" + e.getMessage());
        }

        return responseEntity;
    }

    @ApiOperation(value = "服务转发", notes = "常规服务接口")
    @CrossOrigin
    @GetMapping(value = "/trans/{userId}")
    public ResponseEntity dispatCherPlugin(HttpServletRequest request, @PathVariable String userId) {
        ResponseEntity<String> responseEntity = null;
        String targetURL = null;
        String requestURI = null;
        SysUser sysUser = null;

        try {
            logger.info("[dispatCherPlugin] : start, userId=" + userId);
            sysUser = userService.selectUserById(userId);
            requestURI = request.getHeader(Constant.PLGUGIN_HEADER_ADRESSS);
            String serviveType = request.getHeader(Constant.SERVICE_TYPE);
            if (serviveType.equals(ServiceType.PLS.getValue())) {
                targetURL = pluginServiceAdress + requestURI;
            } else if (serviveType.equals(ServiceType.DMS.getValue())) {
                targetURL = dmServiceAdress + requestURI;
            } else if (serviveType.equals(ServiceType.MS.getValue())) {
                targetURL = monitorService + requestURI;
            }
            logger.info("[dispatCherPlugin] : targetURL=" + targetURL);
            restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
            responseEntity = restTemplate.getForEntity(targetURL, String.class);

            String result = printResponseEntity(responseEntity);
            if (requestURI.contains(Constant.USER_INSERT_OPERATION)
                    || requestURI.contains(Constant.USER_UPDATE_OPERATION)
                    || requestURI.contains(Constant.USER_DELETE_OPERATION)) {
                SysLogUtil.saveLog(request, sysUser, "转发服务操作");
            }
            logger.info("[dispatCherPlugin] : end");
        } catch (Exception e) {
            if (requestURI.contains(Constant.USER_INSERT_OPERATION)
                    || requestURI.contains(Constant.USER_UPDATE_OPERATION)
                    || requestURI.contains(Constant.USER_DELETE_OPERATION)) {
                SysLogUtil.saveLog(request, sysUser,
                        sysUser.getUsername() + ":服务请求失败,tranUrl=" + requestURI + "错误信息" + e.getMessage(), "转发服务操作");
            }
            responseEntity = ResponseUtil.failure(e.getMessage());
            logger.error("[dispatCher Exception ]:" + e.getMessage());
        }

        return responseEntity;
    }

    // @ApiOperation(value="服务转发", notes="文件上传转发接口")
    // @PostMapping(value = "/uploadfile")
    // public ResponseEntity uploadFile(HttpServletRequest request,
    // MultipartHttpServletRequest multiReq){
    // ResponseEntity<String> responseEntity=null;
    // try {
    // logger.info("[uploadFile] : start");
    // String requestURI=request.getHeader(Constant.PLGUGIN_HEADER_ADRESSS);
    // String targetURL= pluginServiceAdress.concat(requestURI);
    // logger.info("[uploadFile] : targetURL="+targetURL);
    // HttpHeaders headers = new HttpHeaders();
    //
    // responseEntity =restTemplate.postForEntity(targetURL, multiReq,
    // String.class);
    // String result=printResponseEntity(responseEntity);
    // logger.info("[uploadFile] : end ");
    // } catch (Exception e) {
    // logger.error("[uploadFile Exception ]:"+e.getMessage());
    // }
    //
    // return responseEntity;
    // }

    @ApiOperation(value = "服务转发", notes = "文件上传转发接口")
    @CrossOrigin
    @RequestMapping(value = "/uploadfile", method = {RequestMethod.POST})
    public ResponseEntity uploadFile(HttpServletRequest request, MultipartHttpServletRequest multiReq,
                                     @RequestParam(value = "userId", required = true) String userId) {
        ResponseEntity<String> responseEntity = null;
        SysUser sysUser = null;
        String targetURL = null;
        try {
            logger.info("[uploadFile] : start,userId=" + userId);
            sysUser = userService.selectUserById(userId);
            String requestURI = request.getHeader(Constant.PLGUGIN_HEADER_ADRESSS);
            String serviveType = request.getHeader(Constant.SERVICE_TYPE);
            if (serviveType.equals(ServiceType.PLS.getValue())) {
                targetURL = pluginServiceAdress + requestURI;
            } else if (serviveType.equals(ServiceType.DMS.getValue())) {

                targetURL = dmServiceAdress + requestURI;
            } else if (serviveType.equals(ServiceType.MS.getValue())) {
                targetURL = monitorService + requestURI;
            }
            targetURL = pluginServiceAdress.concat(requestURI);
            logger.info("[uploadFile] : targetURL=" + targetURL);
            HttpHeaders headers = new HttpHeaders();

            headers.add("Accept", MediaType.APPLICATION_JSON.toString());
            headers.setContentType(MediaType.parseMediaType("multipart/form-data; charset=UTF-8"));

            MultipartFile sourceFile = multiReq.getFile("file");
            MultiValueMap<String, Object> form = new LinkedMultiValueMap<String, Object>();
            FileUtils.writeByteArrayToFile(new File(sourceFile.getOriginalFilename()), sourceFile.getBytes());
            FileSystemResource fileSystemResource = new FileSystemResource(new File(sourceFile.getOriginalFilename()));
            form.add("file", fileSystemResource);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(
                    form, headers);
            responseEntity = restTemplate.postForEntity(targetURL, requestEntity, String.class);
            String result = printResponseEntity(responseEntity);

            SysLogUtil.saveLog(request, sysUser, "文件上传操作");
            logger.info("[uploadFile] : end ");
        } catch (Exception e) {
            SysLogUtil.saveLog(request, sysUser, sysUser.getUsername() + ":上传文件失败," + e.getMessage(), "文件上传操作");
            responseEntity = ResponseUtil.failure(e.getMessage());
            logger.error("[uploadFile Exception ]:" + e.getMessage());
        }

        return responseEntity;
    }

    public String printResponseEntity(ResponseEntity<String> responseEntity) {
        String body = null;
        try {
            logger.info("[printResponseEntity] : start");
            body = responseEntity.getBody();
            StringBuffer result = new StringBuffer();
            HttpStatus statusCode = responseEntity.getStatusCode();
            int statusCodeValue = responseEntity.getStatusCodeValue();
            HttpHeaders headers = responseEntity.getHeaders();
            result.append("responseEntity.getBody()：").append(body).append("responseEntity.getStatusCode()：")
                    .append(statusCode).append("/n").append("responseEntity.getStatusCodeValue()：")
                    .append(statusCodeValue).append("/n").append("responseEntity.getHeaders()：").append(headers)
                    .append("/n");
            logger.info("[printResponseEntity] : end result=" + result);
        } catch (Exception e) {
            logger.info("[printResponseEntity Exception ] :" + e.getMessage());
        }
        return body;
    }

    private void filterFolder(List<SysResource> powerResourceList, String regException, String powerId,
                              List<SysResource> sysResourceList) {
        try {
            for (SysResource sysResource : sysResourceList) {
                String dataPower = sysResource.getDatapower();
                String parentID = sysResource.getParentId();
                String resourceID = sysResource.getId();

                boolean isMatch = Pattern.matches(regException, dataPower);
                if (isMatch && (resourceID.equalsIgnoreCase(powerId) || parentID.equalsIgnoreCase(powerId))) {
                    powerResourceList.add(sysResource);
                }
                List<SysResource> childSysResources = sysResource.getChilds();
                if (childSysResources != null && childSysResources.size() > 0) {
                    filterFolder(powerResourceList, regException, powerId, childSysResources);
                }
            }
        } catch (Exception e) {
            logger.error("[filterFolder Excception ]: ", e.getMessage());
        }

    }


    private void filterService(List<SysResource> powerResourceList, String regException, String powerId,
                               List<SysResource> sysResourceList) {
        try {
            for (SysResource sysResource : sysResourceList) {
                String servicePower = sysResource.getServicepower();
                String parentID = sysResource.getParentId();
                String resourceID = sysResource.getId();

                boolean isMatch = Pattern.matches(regException, servicePower);
                if (isMatch && (resourceID.equalsIgnoreCase(powerId) || parentID.equalsIgnoreCase(powerId))) {
                    powerResourceList.add(sysResource);
                }
                List<SysResource> childSysResources = sysResource.getChilds();
                if (childSysResources != null && childSysResources.size() > 0) {
                    filterService(powerResourceList, regException, powerId, childSysResources);
                }
            }
        } catch (Exception e) {
            logger.error("[filterService Excception ]: ", e.getMessage());
        }

    }


    private void filterFileService(List<SysResource> powerResourceList, String regException,
                                   List<SysResource> sysResourceList) {
        try {
            for (SysResource sysResource : sysResourceList) {
                String datapower = sysResource.getDatapower();
                boolean isMatch = Pattern.matches(regException, datapower);
                if (isMatch) {
                    powerResourceList.add(sysResource);
                }
                List<SysResource> childSysResources = sysResource.getChilds();
                if (childSysResources != null && childSysResources.size() > 0) {
                    filterFileService(powerResourceList, regException, childSysResources);
                }
            }
        } catch (Exception e) {
            logger.error("[filterFileService Excception ]: ", e.getMessage());
        }

    }

    public ResponseEntity<String> filterFindByNameFile(ResponseEntity repo) {

        return null;
    }

    /**
     * 获取正则表达式
     *
     * @param switchId
     * @return
     */
    private String getRegException(int switchId) {
        String regException = null;
        switch (switchId) {
            case 1:
                regException = "^1(\\d){4}$";
                break;
            case 2:
                regException = "^(\\d)1(\\d){3}$";
                break;
            case 3:
                regException = "^(\\d){2}1(\\d){2}$";
                break;
            case 4:
                regException = "^(\\d){3}1(\\d){1}$";
                break;
            case 5:
                regException = "^(\\d){4}1$";
                break;
            default:
                regException = "^1(\\d){4}$";
                break;
        }
        return regException;
    }

    /**
     * 根据权限目录过滤数据
     *
     * @param powerResourceList
     * @param responseResult
     * @return
     */
    public String setPowerData2ResponseDataNoFile(List<SysResource> powerResourceList,
                                                  List<ResponseDMFolderModel> responseResult) {
        List<ResponseDMFolderModel> responseDMFolderModels = new ArrayList<ResponseDMFolderModel>();
        try {
            for (ResponseDMFolderModel responseDMFolderModel : responseResult) {
                String fileName = responseDMFolderModel.getFileName();
                long dmParentID = responseDMFolderModel.getParentId();
                long dmFolderId = responseDMFolderModel.getAuth_use_catalog();
                long fileType = responseDMFolderModel.getFileType();

                for (SysResource sysResource : powerResourceList) {
                    String resourceID = sysResource.getId();
                    String powerFileName = sysResource.getName();
                    long folderId = sysResource.getFolderid();
                    if (fileType == 0) {// 数据目录根据文件名过滤，所以数据权限目录名称与数据创建目录需要保持一致
                        if (fileName.equals(powerFileName)) {
                            String id = sysResource.getId();
                            String parentId = sysResource.getParentId();

                            responseDMFolderModel.setPowerId(id);
                            responseDMFolderModel.setParaentPawerId(parentId);
                            responseDMFolderModel.setPowerData(sysResource.getDatapower());
                            responseDMFolderModels.add(responseDMFolderModel);
                            break;
                        }

                    } else if (fileType > 0) {// 数据文件按照创建的路径ID来获取父目录权限
                        responseDMFolderModels.add(responseDMFolderModel);
                        break;

                    }
                }
            }

        } catch (Exception e) {
            logger.error("[setPowerData2ResponseData Exception ] :", e.getMessage());
        }
        String jsonString = JSON.toJSONString(responseDMFolderModels);
        JSONArray array = JSONArray.parseArray(jsonString);
        return array.toJSONString();

    }

    /**
     * 根据权限目录过滤数据
     *
     * @param powerResourceList
     * @param responseResult
     * @return
     */
    public String setPowerData2ResponseData(List<SysResource> powerResourceList,
                                            List<ResponseDMFolderModel> responseResult) {
        List<ResponseDMFolderModel> responseDMFolderModels = new ArrayList<ResponseDMFolderModel>();
        try {
            for (ResponseDMFolderModel responseDMFolderModel : responseResult) {
                String fileName = responseDMFolderModel.getFileName();

                long dmParentID = responseDMFolderModel.getParentId();
                long dmFolderId = responseDMFolderModel.getAuth_use_catalog();
                long fileType = responseDMFolderModel.getFileType();

                for (SysResource sysResource : powerResourceList) {
                    String resourceID = sysResource.getId();
                    String powerFileName = sysResource.getName();
                    long folderId = sysResource.getFolderid();
                    String parentId = sysResource.getParentId();
                    String dataPower = sysResource.getDatapower();
                    if (fileType == 0) {// 数据目录根据文件名过滤，所以数据权限目录名称与数据创建目录需要保持一致
                        if (fileName.equals(powerFileName)) {
                            setDMFolderModel(responseDMFolderModel, resourceID, parentId, dataPower);
                            responseDMFolderModels.add(responseDMFolderModel);
                            break;
                        }

                    } else if (fileType > 0) {// 数据文件按照创建的路径ID来获取父目录权限
                        //  if (String.valueOf(dmParentID).equalsIgnoreCase(resourceID)) {
                        //      setDMFolderModel(responseDMFolderModel,String.valueOf(responseDMFolderModel.getId()),resourceID,dataPower);

                        ///此部分内容为参考140服务器及配合李红调试，加上的，暂不知其意义
                        ///开始
                        setDMFolderModel(responseDMFolderModel, resourceID, parentId, dataPower);
                        ///结束

                        responseDMFolderModels.add(responseDMFolderModel);
                        break;
                        //   }
                    }
                }
            }

        } catch (Exception e) {
            logger.error("[setPowerData2ResponseData Exception ] :", e.getMessage());
        }
        String jsonString = JSON.toJSONString(responseDMFolderModels);
        JSONArray array = JSONArray.parseArray(jsonString);
        return array.toJSONString();

    }


    /**
     * 根据权限目录过滤服务
     *
     * @param powerResourceList
     * @param responseResult
     * @return
     */
    public String setServicePowerData2ResponseData(List<SysResource> powerResourceList,
                                                   List<ResponseDMFolderModel> responseResult) {
        List<ResponseDMFolderModel> responseDMFolderModels = new ArrayList<ResponseDMFolderModel>();
        try {
            for (ResponseDMFolderModel responseDMFolderModel : responseResult) {
                String fileName = responseDMFolderModel.getFileName();
                long fileType = responseDMFolderModel.getFileType();
                long parentID_DM = responseDMFolderModel.getParentId();
                String id = null;
                String parentId = null;
                if (fileType == 0) {// 数据目录根据文件名过滤，所以数据权限目录名称与数据创建目录需要保持一致
                    for (SysResource sysResource : powerResourceList) {
                        String powerFileName = sysResource.getName();
                        if (fileName.equals(powerFileName)) {
                            id = sysResource.getId();
                            parentId = sysResource.getParentId();
                            responseDMFolderModel.setPowerId(id);
                            responseDMFolderModel.setParaentPawerId(parentId);
                            responseDMFolderModel.setServicePowerData(sysResource.getServicepower());
                            responseDMFolderModels.add(responseDMFolderModel);
                            break;
                        }
                    }
                } else if (fileType > 0) {// 数据文件不做判断处理
                    for (SysResource sysResource : powerResourceList) {
                        id = sysResource.getId();
                        parentId = sysResource.getParentId();
                        if (String.valueOf(parentID_DM).equals(id) || String.valueOf(parentID_DM).equals(parentId)) {
                            responseDMFolderModels.add(responseDMFolderModel);
                            break;
                        }
                    }
                }
            }

        } catch (Exception e) {
            logger.error("[setPowerData2ResponseData Exception ] :", e.getMessage());
        }
        String jsonString = JSON.toJSONString(responseDMFolderModels);
        JSONArray array = JSONArray.parseArray(jsonString);
        return array.toJSONString();

    }

    private static HashMap<String, Object> transObject(String request) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        try {

            if (request.contains("%E")) {
                request = URLDecoder.decode(request, "UTF-8");
            }

            if (request.startsWith("{") && request.endsWith("}")) {
                JSONObject json = JSON.parseObject(request);
                Set<String> keys = json.keySet();
                for (String key : keys) {
                    map.put(key.trim(),
                            (json.get(key) == null || "".equals(json.get(key))) ? null : json.get(key));
                }
                return map;
            }

            String[] pairs = request.trim().split("&");
            for (String pair : pairs) {
                String[] keyAndValue = pair.split("=");
                if (keyAndValue.length == 2) {
                    String key = keyAndValue[0];
                    String value = keyAndValue[1];
                    map.put(key.trim(), (value == null || "".equals(value)) ? null : value.trim());
                }

            }
            return map;
        } catch (Exception e) {

        }
        return null;
    }

    public void setDMFolderModel(ResponseDMFolderModel responseDMFolderModel, String id, String parentId, String dataPower) {
        responseDMFolderModel.setPowerId(id);
        responseDMFolderModel.setParaentPawerId(parentId);
        responseDMFolderModel.setPowerData(dataPower);
    }


}
