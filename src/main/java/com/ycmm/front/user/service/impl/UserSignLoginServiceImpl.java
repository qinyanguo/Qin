package com.ycmm.front.user.service.impl;

import com.ycmm.base.bean.BizParamBean;
import com.ycmm.base.bean.ResultBean;
import com.ycmm.front.user.service.UserSignLoginService;
import org.springframework.stereotype.Service;


/**
 * Created by jishubu on 2018/5/11.
 */
@Service
public class UserSignLoginServiceImpl implements UserSignLoginService {

//    @Autowired
//    WmsStockService wmsStockService;
//
    @Override
    public ResultBean signLogin(BizParamBean bizParamBean) throws Exception {
//        JSONObject biz_param = bizParamBean.getBiz_param();
//        String userName = biz_param.optString("userName");
//        String passWord = biz_param.optString("passWord");
//        Stock stock = wmsStockService.queryStockById(Integer.valueOf(userName));
//        if (stock == null) {
//            throw new ErrorMsgException("此账号不存在请重新注册");
//        }
//        if(stock.getStatus() != 1) {
//            throw new ErrorMsgException("此账号已被禁用，请联系客服处理");
//        }
//        if (StringUtils.isEmpty(stock.getBatchNo())) {
//            throw new ErrorMsgException("当前用户密码不存在，不可登陆");
//        }
//        String md5Hex = DigestUtils.md5Hex(DigestUtils.sha1Hex(passWord));
//        //123456 加密后的字符串
//        if (!"d93a5def7511da3d0f2d171d9c344e91".equals(md5Hex)) {
//            throw new ErrorMsgException("用户名或密码错误，请重新登录");
//        }

        return null;
    }
}
