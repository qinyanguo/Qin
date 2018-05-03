package com.ycmm.front.user.controller;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ycmm.cache.CacheService;
import com.ycmm.system.Constants;

@Controller
@RequestMapping("account")
public class UserController {
	
	@Autowired
	@Qualifier("redisCache")
	private CacheService redisCache;
	
	@Autowired
	@Qualifier("ehCache")
	private CacheService ehCache;
	
	/**
	 * session id token ����
	 * @param json
	 * @param request
	 * @param dev
	 * @return
	 */
	@SuppressWarnings("unused")
	private JSONObject createToken(JSONObject json, HttpServletRequest request, String dev) {
		String uid = json.optString("uid");
		/**
		 * getSession(boolean create)��˼�Ƿ��ص�ǰreqeust�е�HttpSession �������ǰreqeust�е�HttpSession Ϊnull����createΪtrue���ʹ���һ���µ�Session�����򷵻�null�� 
		 * �����֮�� 
		 * HttpServletRequest.getSession(ture)��ͬ�� HttpServletRequest.getSession() 
		 * HttpServletRequest.getSession(false)��ͬ�� �����ǰSessionû�о�Ϊnull�� 
		 * 3. ʹ��
		 * ����Session�д�ȡ��¼��Ϣʱ��һ�㽨�飺HttpSession session =request.getSession();
		 * ����Session�л�ȡ��¼��Ϣʱ��һ�㽨�飺HttpSession session =request.getSession(false);
		 */
		HttpSession session = request.getSession(true);
		String sessionId = session.getId() + "__" +UUID.randomUUID().toString().replaceAll("-", "");
		String secretKey = json.optString("secretKey");
		JSONObject jsonSession = new JSONObject();
		jsonSession.put("SID", sessionId);
		jsonSession.put("KEY", secretKey);
		
		JSONObject jsonToken = new JSONObject();
		jsonToken.put("UID", uid);
		jsonToken.put("KEY", secretKey);
		jsonToken.put("TIME", System.currentTimeMillis());
		
		ehCache.set(sessionId, jsonToken, 1 * 24 * 60 * 60);
		redisCache.set(sessionId, jsonToken, 15 * 24 * 60 * 60);
		if(StringUtils.isNotEmpty(dev)) {
			redisCache.set(Constants.USER_LOGIN_DEV + uid, dev);
		}
		
		return jsonSession;
	}
}




















