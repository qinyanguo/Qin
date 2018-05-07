package com.ycmm.front.user.common;

import com.ycmm.base.bean.FrontParamBean;
import com.ycmm.base.exceptions.base.NoAuthException;
import com.ycmm.base.exceptions.base.OvertimeException;
import com.ycmm.base.exceptions.base.UnauthorizedExcception;
import com.ycmm.base.spring.SpringInit;
import com.ycmm.common.cache.impl.EhcacheImpl;
import com.ycmm.common.cache.impl.RedisCacheImpl;
import com.ycmm.common.constants.Constants;
import com.ycmm.common.utils.SecretUtils;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;


/**
 * 用户登录权限校验
 *
 */
public class UserSignCheck {

    /**
     * 校验用户是否登录
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
            //如果用户登录时间大于10天，且小于15天（也就是登录有效），再将sessionId有效期重置为15天
            jsonToken.put("TIME", time);
            redisCache.set(sessionId, jsonToken, 15 * 24 * 60 * 60);
        }

        //判断是不是同一设备登录，如果不是同一设备（就是切换设备），切换设备后如仍在原设备上操作则马上提示已在其他设备登录当前认证无效，并清除当前操作设备的sessionId
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

        Long t = frontParam.getTime();  //当前请求时间戳
        //判断是否请求超时
        if(t == null || time - t > 3 * 60 * 1000L) {
            throw new OvertimeException();
        }

        //判断是否有签证     sign
        String sign = frontParam.getSign();
        if(StringUtils.isEmpty(sign)) {
            throw new NoAuthException();
        }

        //判断是否有 加密方式   jsonToken.put("KEY", secretKey);
        if(StringUtils.isEmpty(jsonToken.getString("KEY"))) {
            throw new NoAuthException();
        }

        String encryptText = "biz_module=" + frontParam.getBiz_module() + "&biz_method=" + frontParam.getBiz_method()
                + "&time=" +frontParam.getTime();
        String tokenSign = SecretUtils.HmacSHA1Encrypt(encryptText, jsonToken.optString("KEY"));
        //如果和前端传过来的签证一样
        if(!tokenSign.equals(sign)) {
            throw new NoAuthException();
        }
        frontParam.setUid(uid);
    }

    /**
     * 获取当前用户的token信息
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
