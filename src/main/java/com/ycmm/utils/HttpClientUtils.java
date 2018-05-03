package com.ycmm.utils;

import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;



@SuppressWarnings("all")
public class HttpClientUtils {

	private Logger logger = Logger.getLogger(HttpClientUtils.class);

	/**
	 * 指定安全套接字协议
	 */
	private final static String protocol = "TLS";

	/**
	 * 提供用于安全套接字包
	 */
	private static SSLContext sslContext;

	/**
	 * 此类是用于主机名校验的基接口    在握手期间，如果URL的主机名和服务器的标识主机名不匹配（当验证URL主机名使用的默认规则失败是使用这些回调），
	 * 则验证机制可以回调此接的实现程序来确定是否应该允许此连接    （下面实现的verify接口如果返回true则是允许连接否则不允许）
	 */
	private final static HostnameVerifier hostnameVerifier;
	
	/**
	 * http client  连接池配置
	 */
	private final static PoolingHttpClientConnectionManager connManager;

	/**
	 * 最大连接数
	 */
	private final static Integer maxTotal = 400;
	
	/**
	 * http 路由的最大连接数     比如连接 http://qq.com 和  http://sk.com时，到每个主机的并发最多只有200；即加起来最多是400（但不能超过400）
	 */
	private final static Integer defaultMaxPerRoute = 50;
	
	static {
		connManager = new PoolingHttpClientConnectionManager();
		connManager.setMaxTotal(maxTotal);
		connManager.setDefaultMaxPerRoute(defaultMaxPerRoute);
		
		//取消SSL 验证校验
		X509TrustManager x509TrustManager =  new X509TrustManager() {
			@Override
			public void checkServerTrusted(X509Certificate[] xcs, String string){
			}

			@Override
			public void checkClientTrusted(X509Certificate[] xcs, String string){
			}
			@Override
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		};
		
		try {
			sslContext = SSLContext.getInstance(protocol);
			sslContext.init(null, new TrustManager[]{ x509TrustManager },
					new SecureRandom());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		hostnameVerifier = new HostnameVerifier() {
			@Override
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};
	}
	
	/**
	 * 获取 httpClient   支持https请求
	 */
	public static CloseableHttpClient getHttpClient() {
		try {
			return HttpClients.custom().setSSLHostnameVerifier(hostnameVerifier).setSSLContext(sslContext)
					.setConnectionManager(connManager).build();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 关闭连接池   请勿轻易使用
	 */
	public static void closeHttpClient(CloseableHttpClient httpClient) {
		try {
			if (httpClient != null) {
				httpClient.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * post 请求   默认字符集  UTF-8
	 * 数据格式   new UrlEncodedFormEntity(new LinkedList<NameValuePair>())
	 * @param url
	 * @param param
	 * @return
	 */
	public static JSONObject httpPost(String url, Map<String, String> param) {
		return httpPost(url, param, null);
	}

	/**
	 * post 请求  默认字符集    UTF-8
	 * 数据格式  new UrlEncodedFormEntity(new LinkedList<NameValuePair>())
	 * @param url
	 * @param param
	 * @param object
	 * @return
	 */
	public static JSONObject httpPost(String url, Map<String, String> param, String charset) {
		CloseableHttpClient httpClient = getHttpClient();
		HttpResponse httpResponse = null;
		HttpPost httpPost = null;
		charset = StringUtils.isEmpty(charset)?"UTF-8":charset;
		String result = "";
		int status = 400;
		try {
			httpPost = new HttpPost(url);
			if(param != null){
				LinkedList<NameValuePair> linkedList = new LinkedList<NameValuePair>();
				//遍历出所有的要封装的参数
				Iterator<Entry<String, String>> iterator = param.entrySet().iterator();
				while (iterator.hasNext()) {
					Entry<String, String> entry = iterator.next();
					linkedList.add(new BasicNameValuePair(entry.getKey(), String.valueOf(entry.getValue())));
				}
				UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(linkedList, charset);
				httpPost.setEntity(urlEncodedFormEntity);
			}
			httpResponse = httpClient.execute(httpPost);
			StatusLine statusLine = httpResponse.getStatusLine();
			status = statusLine.getStatusCode();
			result = EntityUtils.toString(httpResponse.getEntity(), charset);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpPost.releaseConnection();
		}
		JSONObject json = new JSONObject();
		json.put("status", status);
		json.put("result", result);
		return json;
	}
	
	/**
	 * post 请求   数据格式  new StringEntity(json.toString)
	 * 支持数据类型   application/json/html   text/xml
	 * @param url    请求url
	 * @param param  请求参数可为null
	 * @return
	 */
	public static JSONObject httpPostJson(String url, JSONObject param) {
		return httpPostJson(url, param, null);
	}

	/**
	 * post 请求  数据格式    new StringEntity(json.toString)
	 * 支持数据格式   application/json/html   text/xml
	 * @param url
	 * @param param
	 * @param charset
	 * @return
	 */
	public static JSONObject httpPostJson(String url, JSONObject param, String charset) {
		CloseableHttpClient httpClient = getHttpClient();
		HttpResponse httpResponse = null;
		HttpPost httpPost = null;
		String result = "";
		int status = 400;
		charset = StringUtils.isEmpty(charset)?"UTF-8":charset;
		try {
			httpPost = new HttpPost(url);
			httpPost.addHeader("Content-Type", "application/json;charset="+ charset);
			httpPost.addHeader("Content-Type", "application/html;charset="+ charset);
			httpPost.addHeader("Content-Type", "text/xml;charset="+ charset);
			if(param == null) {
				param = new JSONObject();
			}
			httpPost.setEntity(new StringEntity(param.toString(), charset));
			httpResponse = httpClient.execute(httpPost);
			status = httpResponse.getStatusLine().getStatusCode();
            result = EntityUtils.toString(httpResponse.getEntity(), charset);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpPost.releaseConnection();
		}
		JSONObject data = new JSONObject();
		data.put("status", status);
		data.put("result", result);
		return data;
	}
	
	/**
	 * Http get 请求
	 * @param url
	 * @return
	 */
	public static JSONObject httpGet(String url) {
		return httpGet(url, null);
	}
	
	/**
	 * Http get 请求
	 * @param url
	 * @param charset
	 * @return
	 */
	private static JSONObject httpGet(String url, String charset) {
		JSONObject result = new JSONObject();
		CloseableHttpClient httpClient = getHttpClient();
		charset = StringUtils.isEmpty(charset)?"UTF-8":charset;
		HttpResponse httpResponse = null;
		HttpGet httpGet = null;
		try {
			httpGet = new HttpGet(url);
			httpResponse = httpClient.execute(httpGet);
			result.put("status", httpResponse.getStatusLine().getStatusCode());
			HttpEntity entity = httpResponse.getEntity();
			result.put("result", EntityUtils.toString(entity, charset));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpGet.releaseConnection();
		}
		return result;
	}
}














