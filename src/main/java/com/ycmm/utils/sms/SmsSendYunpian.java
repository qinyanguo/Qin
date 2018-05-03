package com.ycmm.utils.sms;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.ycmm.utils.HttpClientUtils;

@SuppressWarnings("unused")
public class SmsSendYunpian {

	private static Logger logger = Logger.getLogger(SmsSendYunpian.class);
	
	/**
	 * API_KEY
	 */
	private static final String API_KEY = "c4d1cf05f4f5955f1f5045bff9a6188e";
	
	/**
	 * 服务Http地址
	 */
	private static final String BASE_URI = "https://sms.yunpian.com/";
	/**
	 * 服务版本号
	 */
	private static final String VERSION = "v2";
	/**
	 * 编码格式。发送编码格式统一用UTF-8
	 */
	private static final String ENCODING = "UTF-8";
	/**
	 * 查询账户信息的http地址
	 */
	private static final String URI_GET_USER_INFO = BASE_URI + VERSION + "/user/get.json";
	/**
	 * 单条发送   通用发送接口的http地址
	 */
	private static final String URI_SEND_SMS = BASE_URI + VERSION + "/sms/single_send.json";
	/**
	 * 批量发送
	 */
	private static final String URI_BATCH_SEND_SMS = BASE_URI + VERSION + "/sms/batch_send.json";
	/**
	 * 指定模板单发   模板发送接口的http地址
	 */
	private static final String URI_TPL_SEND_SMS = BASE_URI + VERSION + "/sms/tpl_single_send.json";
	/**
	 * 获取模板   获取账户内的全部模板
	 */
	private static final String URI_TPL_GET_SMS = BASE_URI + VERSION + "/tpl/get.json";
	
	
	public static JSONObject sendSms(String mobile, String text) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("apikey", API_KEY);
		param.put("mobile", mobile);
		param.put("text", text);
		JSONObject result = HttpClientUtils.httpPost(URI_SEND_SMS, param);
		if(result.getInt("status") == 200){
			return result.optJSONObject("result");
		}
		logger.error("【短信发送失败】------>手机号为："+ mobile +"，内容为："+ text);
		return result.optJSONObject("result");
	}

	public static JSONObject getTplSms() {
		Map<String, String> param = new HashMap<String, String>();
		param.put("apikey", API_KEY);
//		param.put("mobile", mobile);
//		param.put("text", text);
		JSONObject result = HttpClientUtils.httpPost(URI_TPL_GET_SMS, param);
		if(result.getInt("status") == 200){
			return result.optJSONObject("result");
		}
		return result.optJSONObject("result");
	}
	
	public static void main(String[] args) {
		String text = "【药材买卖网】尊敬的用户您正在执行密码找回操作，验证码为：123456，此号码30分钟内有效。如非本人操作，请忽略。";
		SmsSendYunpian.sendSms("15315437865", text);
//		SmsSendYunpian.getTplSms();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
