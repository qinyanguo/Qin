package com.ycmm.front.user.controller;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.alibaba.dubbo.rpc.RpcException;
import com.ycmm.base.bean.BizParamBean;
import com.ycmm.base.bean.FrontParamBean;
import com.ycmm.base.bean.ResultBean;
import com.ycmm.base.exceptions.base.ErrorMsgException;
import com.ycmm.base.exceptions.enums.ErrorMsgEnum;
import com.ycmm.common.cache.CacheService;
import com.ycmm.common.constants.Constants;
import com.ycmm.common.tools.CommonUtils;
import com.ycmm.common.utils.WebUtils;
import com.ycmm.front.user.service.UserSignLoginService;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("account")
public class UserController {

    private static Logger logger = Logger.getLogger(UserController.class);

    @Autowired
    UserSignLoginService userSignLoginService;

    @Autowired
    @Qualifier("redisCache")
    private CacheService redisCache;

    @Autowired
    @Qualifier("ehCache")
    private CacheService ehCache;

    @ResponseBody
    @RequestMapping("/login")
    public ResultBean signLogin(HttpServletRequest request, HttpServletResponse response, @RequestBody FrontParamBean
            frontParamBean) {
        ResultBean resultBean = null;
        JSONObject biz_param = frontParamBean.getBiz_param();
        String phone = biz_param.optString("phone");
        String passWord = biz_param.optString("passWord");
        try {
            if (StringUtils.isEmpty(phone)) {
                throw new ErrorMsgException("请输入用户名");
            }
            if (StringUtils.isEmpty(passWord)) {
                throw new ErrorMsgException("请输入登录密码");
            }
            BizParamBean bizParamBean = new BizParamBean(frontParamBean);
            //利用Nginx做分发处理时，获取客户端IP地址
            bizParamBean.setIp(WebUtils.getNginxAddress(request));
            bizParamBean.setModel(CommonUtils.getModel(request));
            resultBean = userSignLoginService.signLogin(bizParamBean);
            if(!"1c01".equals(resultBean.getCode())){
                return resultBean;
            }
//            JSONObject token = createToken(resultBean.getBiz_result(), request, frontParamBean.getDev());
            JSONObject jsonSession = createToken(resultBean.getBiz_result(), request, frontParamBean.getDev());
            return new ResultBean(jsonSession);
        }catch (RpcException e){

        }catch (Exception e) {
            logger.error("【===== 用户登录异常 =====】--->phone:" + biz_param.optString("phone"), e);
            resultBean = new ResultBean(ErrorMsgEnum.ERROR_ALERT, "用户登录异常，请稍候重新尝试");
        }


        return resultBean;
    }



    /**
     * session id token 保存
     */
    @SuppressWarnings("unused")
    private JSONObject createToken(JSONObject json, HttpServletRequest request, String dev) {
        String uid = json.optString("uid");
        /**
         * getSession(boolean create)意思是返回当前reqeust中的HttpSession ，
         * 如果当前reqeust中的HttpSession 为null，当create为true，就创建一个新的Session，否则返回null；
         * 简而言之：
         * HttpServletRequest.getSession(ture)等同于 HttpServletRequest.getSession()
         * HttpServletRequest.getSession(false)等同于 如果当前Session没有就为null；
         * 3. 使用
         * 当向Session中存取登录信息时，一般建议：HttpSession session =request.getSession();
         * 当从Session中获取登录信息时，一般建议：HttpSession session =request.getSession(false);
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




















