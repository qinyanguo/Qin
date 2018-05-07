package com.ycmm.common.constants;

import com.ycmm.common.utils.PropertyUtils;

/**
 * 系统常量配置
 * @author jishubu
 *
 */
public class Constants {

	/**
	 * 用户登录设备标记
	 */
	public static final String USER_LOGIN_DEV = "USER_LOGIN_DEV_";
	
	public static final String LOGGER_ERROR_EH_CACHE  = "【========= EhCache 缓存异常 =========】";
	
	public static final String LOGGER_ERROR_REDIS_CACHE  = "【========= Redis 缓存异常 =========】";
	
	/**
	 * 执行日志记录形式
	 */
	public static final String LOGGER_INFO_FORMAT = "[PV]{[0]}{[1]}{[2]}{[3]}";
	/**
	 * 短信服务平台运营商
	 */
	public static final String SMS_SERVICER_PROVIDER =  PropertyUtils.getProperty("sms_service_provider");
	
	
	
}
