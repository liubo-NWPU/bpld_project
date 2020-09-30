package com.geovis.web.controller.system;

import com.geovis.web.domain.system.SysUser;
import com.geovis.web.service.system.ResourceService;
import com.geovis.web.service.system.SysLogService;
import com.geovis.web.service.system.UserService;
import com.geovis.web.util.system.HttpUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

@Api(value = "DownloadDispatherController", description = "服务请求转发")
@RestController
@SuppressWarnings("all")
@CrossOrigin
@RequestMapping("/downloadDispather")
public class DownloadDispatherController {
    private static final Logger logger = LoggerFactory.getLogger(DownloadDispatherController.class);
    @Value("${plugin.service.adress}")
    private String pluginServiceAdress;
    @Value("${dm.service.adress}")
    private String dmServiceAdress;
    @Value("${monitor.service.adress}")
    private String monitorService;
    @Value("${yw.service.ip}")
    private String ywServiceIp;


    @Autowired
    private UserService userService;
    @Autowired
    private SysLogService sysLogService;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private RestTemplate restTemplate;

    @ApiOperation(value = "数管文件下载", notes = "常规服务接口")
    @GetMapping(value = "/dmDownloadFile/{userid}/{fileid}/{filename}/{username}/{filetype}")
    public void dispatFileDownload(HttpServletRequest request, HttpServletResponse response, @PathVariable String userid, @PathVariable String fileid, @PathVariable String filename, @PathVariable String username, @PathVariable String filetype) {
        try {
            String checkUrl = request.getHeader("referer");
            if (checkUrl == "" || checkUrl == null) {
                response.getWriter().write("<script>对不起，系统检测到您通过非法方式下载数据，拒绝下载。</script>");
                return;
            }
            SysUser sysUser = this.userService.selectUserById(userid);
            if (sysUser == null) {
                response.getWriter().write("<script>对不起，系统无法验证当前用户信息，拒绝下载。</script>");
                return;
            }

            //影像
            if (filename.toUpperCase().contains("TIF") || filename.toUpperCase().contains("TIFF") || filename.toUpperCase().contains("IMG")) {
                filetype = "1";
            } else if (filename.toUpperCase().contains("SHP")) {//矢量
                filetype = "2";
            } else {
                filetype = "3";
            }

            String userSystemType = sysUser.getUserSysType();
            String[] userSystemTypes = userSystemType.split("@");
            boolean haveType = false;

            for (int i = 0; i < userSystemTypes.length; i++) {
                if (userSystemTypes[i].contains("1")) {
                    haveType = true;
                }
            }

            if (haveType == false) {
                response.getWriter().write("<script>对不起，用户信息校验不通过，当前用户无法通过此渠道下载数据，拒绝下载。</script>");
                return;
            }

            String url = dmServiceAdress + "/manager/rest/file/downloadFile?id=" + fileid + "&filename=" + filename + "&username=" + username + "&filetype=" + filetype;
            System.out.println("单个download：" + url);

            CloseableHttpClient httpClient = null;
            CloseableHttpResponse rp = null;
            // 通过址默认配置创建一个httpClient实例
            httpClient = HttpClients.createDefault();
            // 创建httpGet远程连接实例
            HttpGet httpGet = new HttpGet(url);
            // 设置请求头信息，鉴权
            httpGet.setHeader("x-forwarded-for", ywServiceIp);
            // 设置配置请求参数
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(35000)// 连接主机服务超时时间
                    .setConnectionRequestTimeout(35000)// 请求超时时间
                    .setSocketTimeout(60000)// 数据读取超时时间
                    .build();
            // 为httpGet实例设置配置
            httpGet.setConfig(requestConfig);
            // 执行get请求得到返回对象
            CloseableHttpResponse chr = httpClient.execute(httpGet);
            HttpEntity httpEntity = chr.getEntity();
            String content_disposition = chr.getFirstHeader("Content-Disposition").getValue();
            String content_length = chr.getFirstHeader("Content-Length").getValue();
            String content_type = chr.getFirstHeader("Content-Type").getValue();
            InputStream dmInputStream = httpEntity.getContent();
            int d = 0;
            byte[] b = new byte[1024];
            response.setHeader("Content-Disposition", content_disposition);
            response.setHeader("Content-Length", content_length);
            response.setHeader("Content-Type", content_type);
            OutputStream out = response.getOutputStream();
            while ((d = dmInputStream.read(b)) != -1) {
                out.write(b, 0, d);
            }
        } catch (Exception e) {
        }
    }

    @ApiOperation(value = "数管文件下载", notes = "常规服务接口")
    @GetMapping(value = "/mhDownloadFile1/{userid}/{fileid}/{filename}/{username}/{filetype}")
    public void mhFileDownload1(HttpServletRequest request, HttpServletResponse response, @PathVariable String userid, @PathVariable String fileid, @PathVariable String filename, @PathVariable String username, @PathVariable String filetype) {
        try {
            SysUser sysUser = this.userService.selectUserById(userid);
            if (sysUser == null) {
                response.getWriter().write("<script>对不起，系统无法验证当前用户信息，拒绝下载。</script>");
                return;
            }

            //影像
            if (filename.toUpperCase().contains("TIF") || filename.toUpperCase().contains("TIFF") || filename.toUpperCase().contains("IMG")) {
                filetype = "1";
            } else if (filename.toUpperCase().contains("SHP")) {//矢量
                filetype = "2";
            } else {
                filetype = "3";
            }

            String userSystemType = sysUser.getUserSysType();
            String[] userSystemTypes = userSystemType.split("@");
            boolean haveType = false;

            for (int i = 0; i < userSystemTypes.length; i++) {
                if (userSystemTypes[i].contains("2")) {
                    haveType = true;
                }
            }

            if (haveType == false) {
                response.getWriter().write("<script>对不起，用户信息校验不通过，当前用户无法通过此渠道下载数据，拒绝下载。</script>");
                return;
            }

            String url = dmServiceAdress + "/manager/rest/file/downloadFile?id=" + fileid + "&filename=" + filename + "&username=" + username + "&filetype=" + filetype;

            System.out.println("单个download：" + url);

            CloseableHttpClient httpClient = null;
            CloseableHttpResponse rp = null;
            // 通过址默认配置创建一个httpClient实例
            httpClient = HttpClients.createDefault();
            // 创建httpGet远程连接实例
            HttpGet httpGet = new HttpGet(url);
            // 设置请求头信息，鉴权
            httpGet.setHeader("x-forwarded-for", ywServiceIp);
            // 设置配置请求参数
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(35000)// 连接主机服务超时时间
                    .setConnectionRequestTimeout(35000)// 请求超时时间
                    .setSocketTimeout(60000)// 数据读取超时时间
                    .build();
            // 为httpGet实例设置配置
            httpGet.setConfig(requestConfig);
            // 执行get请求得到返回对象
            CloseableHttpResponse chr = httpClient.execute(httpGet);
            HttpEntity httpEntity = chr.getEntity();
            String content_disposition = chr.getFirstHeader("Content-Disposition").getValue();
            String content_length = chr.getFirstHeader("Content-Length").getValue();
            String content_type = chr.getFirstHeader("Content-Type").getValue();
            InputStream dmInputStream = httpEntity.getContent();
            int d = 0;
            byte[] b = new byte[1024];
            response.setHeader("Content-Disposition", content_disposition);
            response.setHeader("Content-Length", content_length);
            response.setHeader("Content-Type", content_type);
            OutputStream out = response.getOutputStream();
            while ((d = dmInputStream.read(b)) != -1) {
                out.write(b, 0, d);
            }
        } catch (Exception e) {
        }
    }

    @ApiOperation(value = "数管文件下载", notes = "常规服务接口")
    @GetMapping(value = "/mhDownloadFile/{message}")
    public void mhFileDownload(HttpServletRequest request, HttpServletResponse response, @PathVariable String message) {
        Base64.Decoder decoder = Base64.getDecoder();
        String s2 = "";
        try {
            s2 = new String(decoder.decode(message.getBytes("UTF-8")));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String[] split = s2.split("/");
        String userid = "";
        String fileid = "";
        String filename ="";
        String username = "";
        String filetype = "";
        if (new Date().getTime()-Long.valueOf(split[5])<=30000){
            userid = split[0];
            fileid = split[1];
            filename = split[2];
            username = split[3];
            filetype = split[4];
        }
        Map map = new HashMap<>();
        map.put("userid",userid);
        map.put("fileId",fileid);
        map.put("filename",filename);
        String s = HttpUtil.sendPost(pluginServiceAdress + "/orderService/checkUpload", map);
        Boolean checkRe = Boolean.valueOf(s);
        if (!checkRe){
            return;
        }
        try {
            SysUser sysUser = this.userService.selectUserById(userid);
            if (sysUser == null) {
                response.getWriter().write("<script>对不起，系统无法验证当前用户信息，拒绝下载。</script>");
                return;
            }

            //影像
            if (filename.toUpperCase().contains("TIF") || filename.toUpperCase().contains("TIFF") || filename.toUpperCase().contains("IMG")) {
                filetype = "1";
            } else if (filename.toUpperCase().contains("SHP")) {//矢量
                filetype = "2";
            } else {
                filetype = "3";
            }

            String userSystemType = sysUser.getUserSysType();
            String[] userSystemTypes = userSystemType.split("@");
            boolean haveType = false;

            for (int i = 0; i < userSystemTypes.length; i++) {
                if (userSystemTypes[i].contains("2")) {
                    haveType = true;
                }
            }

            if (haveType == false) {
                response.getWriter().write("<script>对不起，用户信息校验不通过，当前用户无法通过此渠道下载数据，拒绝下载。</script>");
                return;
            }

            String url = dmServiceAdress + "/manager/rest/file/downloadFile?id=" + fileid + "&filename=" + filename + "&username=" + username + "&filetype=" + filetype;

            System.out.println("单个download：" + url);

            CloseableHttpClient httpClient = null;
            CloseableHttpResponse rp = null;
            // 通过址默认配置创建一个httpClient实例
            httpClient = HttpClients.createDefault();
            // 创建httpGet远程连接实例
            HttpGet httpGet = new HttpGet(url);
            // 设置请求头信息，鉴权
            httpGet.setHeader("x-forwarded-for", ywServiceIp);
            // 设置配置请求参数
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(35000)// 连接主机服务超时时间
                    .setConnectionRequestTimeout(35000)// 请求超时时间
                    .setSocketTimeout(60000)// 数据读取超时时间
                    .build();
            // 为httpGet实例设置配置
            httpGet.setConfig(requestConfig);
            // 执行get请求得到返回对象
            CloseableHttpResponse chr = httpClient.execute(httpGet);
            HttpEntity httpEntity = chr.getEntity();
            String content_disposition = chr.getFirstHeader("Content-Disposition").getValue();
            String content_length = chr.getFirstHeader("Content-Length").getValue();
            String content_type = chr.getFirstHeader("Content-Type").getValue();
            InputStream dmInputStream = httpEntity.getContent();
            int d = 0;
            byte[] b = new byte[1024];
            response.setHeader("Content-Disposition", content_disposition);
            response.setHeader("Content-Length", content_length);
            response.setHeader("Content-Type", content_type);
            OutputStream out = response.getOutputStream();
            while ((d = dmInputStream.read(b)) != -1) {
                out.write(b, 0, d);
            }
        } catch (Exception e) {
        }
    }

}
