package com.ycmm.front.user.common;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import com.ycmm.base.FrontParamBean;
import com.ycmm.cache.impl.EhcacheImpl;
import com.ycmm.cache.impl.RedisCacheImpl;
import com.ycmm.exceptions.NoAuthException;
import com.ycmm.exceptions.OvertimeException;
import com.ycmm.exceptions.UnauthorizedExcception;
import com.ycmm.system.Constants;
import com.ycmm.system.SpringInit;
import com.ycmm.utils.SecretUtils;

/**
 * �û���¼Ȩ��У��
 *
 */
public class UserSignCheck {

	/**
	 * У���û��Ƿ��¼
	 * @param frontParam
	 * @throws Exception
	 */
	public static void isSigncheck(FrontParamBean frontParam) throws Exception {
		String sessionId = frontParam.getSID();
		if(StringUtils.isEmpty(sessionId)) {
			throw new NoAuthException();
		}
		EhcacheImpl ehcache = SpringInit.getBean(EhcacheImpl.class);
		JSONObject jsonToken = ehcache.get(sessionId, JSONObject.class);
		RedisCacheImpl redisCache = SpringInit.getBean(RedisCacheImpl.class);
		if(jsonToken == null) {
			jsonToken = redisCache.get(sessionId, JSONObject.class);
			if(jsonToken == null) {
				throw new NoAuthException();
			}
			ehcache.set(sessionId, jsonToken, 1 * 24 * 60 * 60, 0);
		}
		
		long time = System.currentTimeMillis();
		if(time - jsonToken.optLong("TIME") > 10 * 24 * 60 * 60) {
			//����û���¼ʱ�����10�죬��С��15�죨Ҳ���ǵ�¼��Ч�����ٽ�sessionId��Ч������Ϊ15��
			jsonToken.put("TIME", time);
			redisCache.set(sessionId, jsonToken, 15 * 24 * 60 * 60);
		}
		
		//�ж��ǲ���ͬһ�豸��¼���������ͬһ�豸�������л��豸�����л��豸��������ԭ�豸�ϲ�����������ʾ���������豸��¼��ǰ��֤��Ч���������ǰ�����豸��sessionId
		String uid = jsonToken.optString("UID");
		String dev = frontParam.getDev();
		if(StringUtils.isNotEmpty(dev)) {
			String old_dev = redisCache.get(Constants.USER_LOGIN_DEV + uid, String.class);
			if(old_dev != null && !dev.equals(old_dev)) {
				ehcache.remove(sessionId);
				redisCache.remove(sessionId);
				throw new UnauthorizedExcception();
			}
		}
		
		Long t = frontParam.getTime();  //��ǰ����ʱ���
		//�ж��Ƿ�����ʱ
		if(t == null || time - t > 3 * 60 * 1000L) {
			throw new OvertimeException();
		}
		
		//�ж��Ƿ���ǩ֤     sign
		String sign = frontParam.getSign();
		if(StringUtils.isEmpty(sign)) {
			throw new NoAuthException();
		}
		
		//�ж��Ƿ��� ���ܷ�ʽ   jsonToken.put("KEY", secretKey);
		if(StringUtils.isEmpty(jsonToken.getString("KEY"))) {
			throw new NoAuthException();
		}
		
		String encryptText = "biz_module=" + frontParam.getBiz_module() + "&biz_method=" + frontParam.getBiz_method()
		                   + "&time=" +frontParam.getTime();
		String tokenSign = SecretUtils.HmacSHA1Encrypt(encryptText, jsonToken.optString("KEY"));
		//�����ǰ�˴�������ǩ֤һ��
		if(!tokenSign.equals(sign)) {
			throw new NoAuthException();
		}
		frontParam.setUid(uid);
	}

	/**
	 * ��ȡ��ǰ�û���token��Ϣ
	 * @param sessionId
	 */
	public static JSONObject getAccountToken(String sessionId) {
		long time = System.currentTimeMillis();
		if(StringUtils.isBlank(sessionId)) {
			return null;
		}
		EhcacheImpl ehCache = SpringInit.getBean(EhcacheImpl.class);
		JSONObject json = ehCache.get(sessionId, JSONObject.class);
		RedisCacheImpl redisCache = SpringInit.getBean(RedisCacheImpl.class);
		if(json != null) {
			json = redisCache.get(sessionId, JSONObject.class);
			if(json == null) {
				return null;
			}
			ehCache.set(sessionId, json, 1 * 24 * 60 * 60, 0);
		}
		if((time - json.optLong("TIME")) > 10 * 24 * 60 * 60) {
			json.put("TIME", time);
			redisCache.set(sessionId, 15 * 24 * 60 * 60);
		}
		return json;
	}
	
}
