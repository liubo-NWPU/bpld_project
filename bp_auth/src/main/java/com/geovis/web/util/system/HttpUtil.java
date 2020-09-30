package com.geovis.web.util.system;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author:wangmeng
 * @Date:创建于2018/3/27 12:27
 * @Description:
 */
public class HttpUtil {
    public static CloseableHttpClient getHttpClient() {
        return HttpClients.createDefault();
    }

    public static String sendGet(String url, Map<String, String> param) {
        // 实例化httpclient
        CloseableHttpClient client = getHttpClient();
        // 实例化get方法

        String urlNameString = url + "?" + map2string(param);
        HttpGet httpget = new HttpGet(urlNameString);
        // 请求结果
        String content = "";
        try {
            // 执行get方法
            CloseableHttpResponse response = client.execute(httpget);
            if (response.getStatusLine().getStatusCode() == 200) {
                content = EntityUtils.toString(response.getEntity(), "utf-8");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }
    public static String sendPost(String url,Map<String, String> params){
        String body = "";
        CloseableHttpClient client = getHttpClient();
        HttpPost post = new HttpPost(url);

        // 装填参数
        List<NameValuePair> nvps = new ArrayList();
        if (params != null) {
            for (String key: params.keySet()) {
                nvps.add(new BasicNameValuePair(key, params.get(key)));
            }
        }
        post.setHeader("Content-type", "application/x-www-form-urlencoded");
        try {
            post.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
            CloseableHttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                body = EntityUtils.toString(entity,"utf-8");//,"UTF-8"
            }
            response.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return body;
    }

    public static String sendPost(String url,String params){
        String body = "";
        CloseableHttpClient client = getHttpClient();
        HttpPost post = new HttpPost(url);
        post.setHeader("Content-type", "application/json;charset=utf-8");
        try {
            post.setEntity(new StringEntity(params, "utf-8"));
            CloseableHttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                body = EntityUtils.toString(entity,"utf-8");//,"UTF-8"
            }
            response.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return body;
    }

    public static String sendPostToAuth(String url, String params, HttpServletRequest request){
        String body = "";
        CloseableHttpClient client = getHttpClient();
        HttpPost post = new HttpPost(url);
        post.setHeader("Content-type", "application/json;charset=utf-8");
        post.setHeader("Authorization", request.getHeader("Authorization"));

        //post.setHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTUzOTYxMzcwOSwibmJmIjoxNTM5NTcwNTA5fQ.ufQkwliGXWZ-MeuPTb65HuwwWq6c5vcc7hbAH8CsGQI");
        post.setHeader("username", request.getHeader("username"));
        //post.setHeader("username", "admin");
        try {
            post.setEntity(new StringEntity(params, "utf-8"));
            CloseableHttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            if (entity != null&&response.getStatusLine().getStatusCode()==200) {
                //"UTF-8"
                body = EntityUtils.toString(entity,"utf-8");
            }
            response.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }

        return body;
    }

    private static String map2string(Map<String, String> map) {
        String params = "";
        if (map == null || map.isEmpty()) {
            return "";
        }
        for (String key : map.keySet()) {
            params += key + "=" + map.get(key) + "&";
        }
        return params;
    }
}
