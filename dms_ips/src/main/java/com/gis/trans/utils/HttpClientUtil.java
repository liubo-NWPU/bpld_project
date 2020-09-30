package com.gis.trans.utils;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpClientUtil {

	/**
	 * 发送 post请求访问本地应用并根据传递参数不同返回不同结果
	 */
	public static String post(String url, Map<String, Object> params) {
		// 创建默认的httpClient实例.
		CloseableHttpClient httpclient = null;

		if (url.contains("https")) {
			httpclient = getHttpsClient();
		} else {
			httpclient = HttpClients.createDefault();
		}

		// 创建httppost
		HttpPost httppost = new HttpPost(url);
		// 创建参数队列
		List<BasicNameValuePair> formparams = new ArrayList<BasicNameValuePair>();

		if (params != null) {
			for (String key : params.keySet()) {
				if(params.get(key)!=null){
				 //formparams.add(new BasicNameValuePair(key, JSON.toJSONString(params.get(key))));
				 formparams.add(new BasicNameValuePair(key, params.get(key).toString()));
				}
			}
		}

		UrlEncodedFormEntity uefEntity;
		try {
			uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
			httppost.setEntity(uefEntity);
			CloseableHttpResponse response = httpclient.execute(httppost);
			try {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					String result = EntityUtils.toString(entity, "UTF-8");
					return result;
				}
			} finally {
				response.close();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关闭连接,释放资源
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	
	/**
	 * 发送 post请求访问本地应用并根据传递参数不同返回不同结果
	 */
	public static String post(String url,String contentType, Map<String, Object> params) {
		// 创建默认的httpClient实例.
		CloseableHttpClient httpclient = null;

		if (url.contains("https")) {
			httpclient = getHttpsClient();
		} else {
			httpclient = HttpClients.createDefault();
		}

		// 创建httppost
		HttpPost httppost = new HttpPost(url);
		httppost.setHeader("Content-type", contentType);
		// 创建参数队列
		List<BasicNameValuePair> formparams = new ArrayList<BasicNameValuePair>();

		if (params != null) {
			for (String key : params.keySet()) {
				if(params.get(key)!=null){
				 formparams.add(new BasicNameValuePair(key, JSON.toJSONString(params.get(key))));
				}
			}
		}
		UrlEncodedFormEntity uefEntity;
		try {
			uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
			httppost.setEntity(uefEntity);
			CloseableHttpResponse response = httpclient.execute(httppost);
			try {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					String result = EntityUtils.toString(entity, "UTF-8");
					return result;
				}
			} finally {
				response.close();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关闭连接,释放资源
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}


	/**
	 * 发送 get请求
	 */
	public static String get(String url, Map params) {
		// 创建默认的httpClient实例.
		CloseableHttpClient httpclient = null;
		if (url.contains("https")) {
			httpclient = getHttpsClient();
		} else {
			httpclient = HttpClients.createDefault();
		}
		try {
			// 创建httpget.
			url = url + "?" + map2string(params);
			HttpGet httpget = new HttpGet(url);
			// 执行get请求.
			CloseableHttpResponse response = httpclient.execute(httpget);
			try {
				// 获取响应实体
				HttpEntity entity = response.getEntity();
				// 打印响应状态
				if (entity != null) {
					String result = EntityUtils.toString(entity);
					return result;
				}
			} finally {
				response.close();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关闭连接,释放资源
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	

	/**
	 * 发送 get请求
	 */
	public static String get(String url) {
		// 创建默认的httpClient实例.
		CloseableHttpClient httpclient = null;
		if (url.contains("https")) {
			httpclient = getHttpsClient();
		} else {
			httpclient = HttpClients.createDefault();
		}
		try {

			HttpGet httpget = new HttpGet(url);

			CloseableHttpResponse response = httpclient.execute(httpget);
			try {
				// 获取响应实体
				HttpEntity entity = response.getEntity();

				if (entity != null) {
					String result = EntityUtils.toString(entity);
					return result;
				}
			} finally {
				response.close();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关闭连接,释放资源
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	private static String map2string(Map map) {
		String params = "";
		if (map == null || map.isEmpty()) {
			return "";
		}

		for (Object key : map.keySet()) {
			params += key + "=" + map.get(key) + "&";
		}

		return params.substring(0, params.length() - 1);
	}
	
	/**
	 * 发送 post请求访问本地应用并根据传递参数不同返回不同结果
	 */
	public static String sendPost(String url, String jsonData) {
		// 创建默认的httpClient实例.
		CloseableHttpClient httpclient = null;
		if (url.contains("https")) {
			httpclient = getHttpsClient();
		} else {
			httpclient = HttpClients.createDefault();
		}

		try {
			// 创建httppost
			HttpPost httppost = new HttpPost(url);
			StringEntity postEntity = new StringEntity(jsonData,"utf-8");
			httppost.addHeader("Content-Type", "application/json");
			
			httppost.setEntity(postEntity);
			httppost.setConfig(RequestConfig.custom().setSocketTimeout(10000).
					setConnectTimeout(5000).build());
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				String result = EntityUtils.toString(entity, "UTF-8");
				return result;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭连接,释放资源
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}


	public static CloseableHttpClient getHttpsClient() {
		try {
			SSLContext sslContext = SSLContext.getInstance("SSL");
			sslContext.init(null, new TrustManager[] { new X509TrustManager() {
				@Override
				public void checkClientTrusted(X509Certificate[] x509Certificates, String s)
						throws CertificateException {

				}

				@Override
				public void checkServerTrusted(X509Certificate[] x509Certificates, String s)
						throws CertificateException {

				}

				@Override
				public X509Certificate[] getAcceptedIssuers() {
					return new X509Certificate[0];
				}
			} }, new SecureRandom());
			SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext,
					NoopHostnameVerifier.INSTANCE);

			CloseableHttpClient closeableHttpClient = HttpClientBuilder.create().setSSLSocketFactory(socketFactory)
					.build();
			return closeableHttpClient;
		} catch (Exception e) {
			e.printStackTrace();
			return HttpClientBuilder.create().build();
		}
	}
	
	public static CloseableHttpClient getHttpsClient1() {
		try {
			SSLContext sslContext = SSLContext.getInstance("SSL");
			sslContext.init(null, new TrustManager[] { new X509TrustManager() {
				@Override
				public void checkClientTrusted(X509Certificate[] x509Certificates, String s)
						throws CertificateException {

				}

				@Override
				public void checkServerTrusted(X509Certificate[] x509Certificates, String s)
						throws CertificateException {

				}

				@Override
				public X509Certificate[] getAcceptedIssuers() {
					return new X509Certificate[0];
				}
			} }, null);

			// 设置协议http和https对应的处理socket链接工厂的对象
			Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
					.register("http", PlainConnectionSocketFactory.INSTANCE)
					.register("https", new SSLConnectionSocketFactory(sslContext)).build();
			PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(
					socketFactoryRegistry);
			HttpClients.custom().setConnectionManager(connManager);

			// 创建自定义的httpclient对象
			CloseableHttpClient client = HttpClients.custom().setConnectionManager(connManager).build();
			return client;
		} catch (Exception e) {
			e.printStackTrace();
			return HttpClientBuilder.create().build();
		}
	}

}