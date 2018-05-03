package com.ycmm.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;

public class WebUtils {

	/**
	 * 使用Nginx做分发处理时，获取客户端IP的方法
	 * @param request
	 * @return
	 */
	public static String getNginxAddress(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if(StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)){
			ip = request.getHeader("Proxy-Client-Ip");
		}
		if(StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)){
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if(StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)){
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if(StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)){
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if(StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)){
			ip = request.getRemoteAddr();
		}
		return ip;
	}
	
	/**
	 * 获取当前访问页面response.encodeURL处理后的URL(绝对路径)
	 * @param request
	 * @param response
	 * @return
	 */
	public static String getRewritedURL(HttpServletRequest request, HttpServletResponse response) {
		/**
		 * HttpServletResponse 接口提供了重写 URL 的方法：public java.lang.String encodeURL(java.lang.String url) 
		 * 该方法的实现机制为：
		 * ● 先判断当前的 Web 组件是否启用Session，如果没有启用 Session，直接返回参数 url。
		 * ● 再判断客户端浏览器是否支持 Cookie，如果支持Cookie，直接返回参数 url；
		 *   如果不支持 Cookie，就在参数 url 中加入 Session ID 信息，然后返回修改后的url。
		 *   
		 *   这里就是先将request中的URI先制为空再加上处理后的URI
		 */
		return new StringBuffer(request.getRequestURL().toString().replace(request.getRequestURI(), ""))
		       .append(getRewritedURI(request, response)).toString();
	}

	/**
	 * 获取当前访问页面response.encodeURI处理后的URI(相对路径)
	 * @param request
	 * @param response
	 * @return
	 */
	public static Object getRewritedURI(HttpServletRequest request, HttpServletResponse response) {
		
		return response.encodeURL(getRequestURI(request).toString());
	}

	/**
	 * 获取当前访问页面的URI(相对路径)
	 * @param request
	 * @return
	 */
	public static Object getRequestURI(HttpServletRequest request) {
		
		StringBuffer uri = new StringBuffer(request.getRequestURI());
		String queryString = request.getQueryString();
		if(StringUtils.isNotEmpty(queryString)) {
			uri.append("?").append(queryString);
		}
		return uri.toString();
	}
	
	/**
	 * 获取前一个页面访问的URL
	 * @param request
	 * @return
	 */
	public static String getFromUrl(HttpServletRequest request) {
		
		return getFromUrl(request, true);
	}

	/**
	 * 获取前一个访问页面的URL
	 * @param request
	 * @param site    是否不需要获取本站URL
	 * @return
	 */
	public static String getFromUrl(HttpServletRequest request, boolean site) {
		
		String fromUrl = request.getHeader("Referer");
		
		if(StringUtils.isEmpty(fromUrl)) {
			return "";
		}
    /**
     * 在开发web程序的时候，有时我们需要得到用户是从什么页面连过来的，这就用到了referer。
     * 它是http协议，所以任何能开发web程序的语言都可以实现,比如jsp中是：request.getHeader("referer");
     * php是$_SERVER['HTTP_REFERER']。其他的我就不举例了（其实是不会其他的语言）。
     * js的话就是这样做：javascript:document.referrer
     * 那它能干什么用呢？我举两个例子：
     * 1，防止盗连，比如我是个下载软件的网站，在下载页面我先用referer来判断上一页面是不是自己网站，如果不是，说明有人盗连了你的下载地址。
     * 2，电子商务网站的安全，我在提交信用卡等重要信息的页面用referer来判断上一页是不是自己的网站，如果不是，可能是黑客用自己写的一个表单，来提交，为了能跳过你上一页里的javascript的验证等目的。
     * 使用referer的注意事项：
     * 如果我是直接在浏览器里输入有referer的页面，返回是null（jsp），也就是说referer只有从别的页面点击连接来到这页的才会有内容。
     * 我做了个实验，比如我的referer代码在a.jsp中，它的上一页面是b.htm，c.htm是一个带有iframe的页面，它把a.jsp嵌在iframe里了。
     * 我在浏览器里输入b.htm的地址，然后点击连接去c.htm，那显示的结果是b.htm，如果我在浏览器里直接输入的是c.htm那显示的是c.htm
     * (也就是说要判断是不是从b.html中跳过来的，只有先进入b.html中才通过request.getHeader("Referer")拿到上一个页面是b.html，
     * 从而可以进入c.html中，进到c.html中即可执行a.jsp)否则直接进c.html中是拿不到上一个页面是b.html的，从而可以判断出链接来源不对
     * 
     * 这里凡是从以下连个链接过来的都不允许跳转
     */	
		if(site && (PatternUtils.regex("17buy\\.com", fromUrl, false) 
				|| (PatternUtils.regex("127.0.0.1", fromUrl, false)))) {
			return "";
		}
		return fromUrl;
	}
	
	/**
	 * 获取request请求中的表单数据及参数
	 * @param request
	 * @return
	 */
	public static Map<String, String> getRequestParams(HttpServletRequest request) {
		
		Map<String, String[]> map = request.getParameterMap();
		Map<String,String> ret = new HashMap<String, String>();
		
		Iterator<String> keys = map.keySet().iterator();
		
		while(keys.hasNext()) {
			String key = keys.next();
			ret.put(key, StringKit.arrayToString(map.get(key)));
		}
		return ret;
	}
	
	/**
	 * Compatible with GET AND POST
	 * 获取get/POST请求参数     
	 * 说明：当请求方式为“Get”时，直接使用request.getQueryString()获取String
	 *     当请求方式为“Post”时，读取InputStream（request.getInputStream()）
	 * @param request
	 * @return
	 * @throws IOException 
	 */
	public static byte[] getRequestQuery(HttpServletRequest request) throws IOException {
		
		String submitMethod = request.getMethod();
		String queryString = null;
		if("GET".equalsIgnoreCase(submitMethod)){
			queryString = request.getQueryString();
			//获取编码方式  charset
			String charEncoding = request.getCharacterEncoding();
			if(charEncoding == null) {
				charEncoding = "UTF-8";
			}
			byte[] bytes = queryString.getBytes();
			return bytes;
			
		}else {  //Post
			return getRequestPostBytes(request);
		}
	}
	
	/**
	 * Get request queryString, from method post
	 * 描述:获取 post 请求的 byte[] 数组
	 * @param request
	 * @return
	 * @throws IOException 
	 */
	public static byte[] getRequestPostBytes(HttpServletRequest request) throws IOException {
		
		int contentLength = request.getContentLength();
		if(contentLength < 0) {
			return null;
		}
		
		byte buffer[] = new byte[contentLength];
		for(int i=0; i < contentLength;){
			int readlen = request.getInputStream().read(buffer, i, contentLength - i);
			if(readlen == -1) {
				break;
			}
			i += readlen;
		}
		return buffer;
	}
	
	/**
	 * 描述:获取 post 请求内容
     * 注意:POST请求读取一次后不能再次读取
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public static String getRequestPostStr(HttpServletRequest request) throws IOException {
		byte[] buffer = getRequestPostBytes(request);
		if (buffer == null) {
			return null;
		}
		String characterEncoding = request.getCharacterEncoding();
		if (characterEncoding == null) {
			characterEncoding = "UTF-8";
		}
		return new String(buffer, characterEncoding);
	}
	
	 /**
	  * 将post请求的json字符串格式的参数转为JSONObject  
	  * @param request
	  * @param response
	  * @return
	  * @throws IOException
	  * @throws JSONException
	  */
    public static JSONObject getRequestPostJson(HttpServletRequest request,HttpServletResponse response) throws IOException, JSONException{  
        StringBuffer sb = new StringBuffer() ;   
        InputStream is = request.getInputStream();   
        BufferedReader br = new BufferedReader(new InputStreamReader(is,"utf-8"));   
        String s = "";   
        while((s=br.readLine())!=null){   
            sb.append(s) ;   
        }   
        if(sb.toString().length()<=0){  
            return null;  
        }else {  
            return JSONObject.fromObject(sb.toString());  
        }  
    }
    
    /**
     * 根据所提供的URL获取网页内容
     * @param url
     * @return
     */
    public static String getHtmlContent(String url) {
    	return getHtmlContent(url, 10*1000, 15*1000, "utf-8");
    }

    /**
     * 根据指定的URL获取网页内容
     * @param url
     * @param charset
     * @return
     */
    public static String getHtmlContent(String url, String charset) {
    	return getHtmlContent(url, 10*1000, 15*1000, charset);
    }
    
    /**
     * 根据指定的URL获取网页内容
     * @param url
     * @param connectTimeout   连接超时时间
     * @param readTimeout      读取超时时间
     * @param charset          字符编码
     * @return
     */
	public static String getHtmlContent(String url, int connectTimeout, int readTimeout, String charset) {
		
		StringBuffer inputLine = new StringBuffer();
		
		try {
			HttpURLConnection urlConnection =  (HttpURLConnection) new URL(url).openConnection();
			/**
			 * HttpURLConnection.setFollowRedirects(boolean followRedirects)
			 * HttpURLConnection.setInstanceFollowRedirects(boolean followRedirects)
			 * 前者设置所有的http连接是否自动处理重定向；
			 * 后者设置本次连接是否自动处理重定向。
			 * 设置成true，系统自动处理重定向；设置成false，则需要自己从http reply中分析新的url自己重新连接。
			 */
			HttpURLConnection.setFollowRedirects(true);
			urlConnection.setConnectTimeout(connectTimeout);
			urlConnection.setReadTimeout(readTimeout);
			urlConnection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			urlConnection.setRequestProperty("Accept",
							"text/vnd.wap.wml,text/html, application/xml;q=0.9, application/xhtml+xml;q=0.9, image/png, image/jpeg, image/gif, image/x-xbitmap, */*;q=0.1");
			/**
			 * 有什么疑问的话，可以查看JDK的API文档，这个可以实时看。至于为什么要设置
			 * gzip，而又不设置deflate，原因如下，有些网站他不管你能接受什么压缩格式
			 * ，统统也会压缩网页内容传给你。当然IE，FF能处理好这些内容
			 * 。所以我们通过浏览器查看的时候完全正常。一般gzip的压缩可以将一个33K的文件压缩成7K
			 * ，这样会节约不少带宽，但服务器的负荷并没有减轻
			 * ，因为他要压缩文件呀。至于为什么不用deflate，是由于绝大多数网站的压缩方式是用gzip
			 * ，而在有些网站中，明明是用的gzip却返回deflate的压缩标识
			 * 。这有什么意义呢，所以干脆就告诉服务器，我不接受deflate，因为他太丑了
			 * ，又长，哪像gzip这么潮呀。呵呵，对于浏览量大的静态网页服务器，这样做很是必要。100M的独享服务器，他也只有100M呀。
			 */
//			urlConnection.setRequestProperty("Accept-Encoding", "gzip");// 为什么没有deflate呢
			urlConnection.setRequestProperty("Content-type", "text/html");
			urlConnection.setRequestProperty("Connection", "close"); //keep-Alive，有什么用呢，你不是在访问网站，你是在采集。嘿嘿。减轻别人的压力，也是减轻自己。
			urlConnection.setUseCaches(false);//不要用cache，用了也没有什么用，因为我们不会经常对一个链接频繁访问。（针对程序）
			urlConnection.setDoOutput(true);
			urlConnection.setDoInput(true);
			
			BufferedReader in;
			if("gzip".equalsIgnoreCase(urlConnection.getContentEncoding())) {
				in = new BufferedReader(new InputStreamReader(new GZIPInputStream(urlConnection.getInputStream()), charset));
			} else {
				in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), charset));
			}
			
			String str;
			while((str = in.readLine()) != null) {
				inputLine.append(str).append("\r\n");
			}
			
			in.close();
			
		}  catch (Exception e) {
			e.printStackTrace();
		}
		
		return inputLine.toString();
	}
	
	/**
	 * 判断URL是否包括协议名称，若无则使用HTTP协议
	 * @param Url
	 * @return
	 */
	public static String parseTargetUrl(String url) {
		//  \w:单词字符：[a-zA-Z_0-9]    . 代表任意字符， +代表匹配一个或更多字符
		if(!Pattern.matches("\\w://.+", url)){
			url =  "http://" + url;
		}
		return url;
	}
	
	/**
	 * 获取浏览器类型
	 * @param request
	 * @return
	 */
	public static String getModel(HttpServletRequest request) {
		String header = request.getHeader("User-Agent");
		if(StringUtils.isEmpty(header)) {
			return "UNKNOWN";
		}
		//微信内置浏览器
		if(header.toUpperCase().contains("MICROMESSENGER")) {
			return "WECHART";
		}
		UserAgent userAgent = UserAgent.parseUserAgentString(header);
		OperatingSystem operatingSystem = userAgent.getOperatingSystem();
		String name = operatingSystem.getName().toUpperCase();
		if(name.contains("ANDROID")) {
			return "ANDROID";
		}
		if(name.contains("IPHONE")) {
			return "IOS";
		}
		if(name.contains("WINDOWS")) {
			return "PC";
		}
		return "UNKNOWN";
	}
}
















