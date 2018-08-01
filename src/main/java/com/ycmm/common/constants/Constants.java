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
     * 客服电话
     */
    public static final String SERVICE_MOBILE = PropertyUtils.getProperty("service_mobile");
	
	/**
	 * 执行日志记录形式
	 */
	public static final String LOGGER_INFO_FORMAT = "[PV]{[0]}{[1]}{[2]}{[3]}";
    /**
     * 错误日志记录
     */
    public static final String LOGGER_ERROR_FORMAT = "[ER]{[0]}{[1]}{[2]}{[3]}{[4]}{[5]}{[6]}";
    /**
	 * 短信服务平台运营商
	 */
	public static final String SMS_SERVICER_PROVIDER =  PropertyUtils.getProperty("sms_service_provider");
    /**
     * 用户注册锁
     */
    public static final String USER_REGISTER_SYN = "_USER_REGISTER_SYN_";
    /**
     * 注册 验证码
     */
    public static final String REGISTER_VERVIFY_CODE = "_REGISTER_VERVIFY_CODE";

    /**
     * 登录 验证码
     */
    public static final String LOGIN_VERVIFY_CODE = "_LOGIN_VERVIFY_CODE";
    /**
     * 重置 密码
     */
    public static final String REPWD_VERVIFY_CODE = "_REPWD_VERVIFY_CODE";
	
	
	
}
