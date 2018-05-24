package com.ycmm.front.user.service.impl;

import com.sun.org.apache.bcel.internal.generic.RETURN;
import com.ycmm.base.bean.BizParamBean;
import com.ycmm.base.bean.ResultBean;
import com.ycmm.base.enums.Model;
import com.ycmm.base.exceptions.base.ErrorMsgException;
import com.ycmm.business.mapper.BizUserMapper;
import com.ycmm.business.service.EmployeeService;
import com.ycmm.business.service.UserService;
import com.ycmm.common.utils.DateUtils;
import com.ycmm.common.utils.SecretUtils;
import com.ycmm.front.user.service.UserSignLoginService;
import com.ycmm.model.BizUser;
import net.sf.json.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Created by jishubu on 2018/5/11.
 */
@Service
public class UserSignLoginServiceImpl implements UserSignLoginService {

    @Autowired
    EmployeeService employeeService;
    @Autowired
    UserService userService;
    @Autowired
    BizUserMapper bizUserMapper;

    @Override
    public ResultBean signLogin(BizParamBean bizParamBean) throws Exception {
        BizUser user = bizParamBean.getBiz_param(BizUser.class);
        JSONObject biz_param = bizParamBean.getBiz_param();
        String phone = biz_param.optString("phone");
        String passWord = biz_param.optString("passWord");
        BizUser bizUser = null;
        bizUser = userService.queryUserInfoByPhone(phone);
        if (bizUser == null) {
            throw new ErrorMsgException("此账号不存在请重新注册");
        }
        if (bizUser.getStatus() != 1) {
            throw new ErrorMsgException("此账号已被禁用，请联系客服处理");
        }
        if (StringUtils.isEmpty(bizUser.getPassword())) {
            throw new ErrorMsgException("当前用户密码不存在，不可登陆");
        }
        String md5Hex = DigestUtils.md5Hex(DigestUtils.sha1Hex(passWord));
        if (!bizUser.getPassword().equals(md5Hex)) {
            throw new ErrorMsgException("用户名或密码错误，请重新登录");
        }
        JSONObject json = userLogin(bizParamBean, user);
        return new ResultBean(json);

    }

    private JSONObject userLogin(BizParamBean bizParamBean, BizUser user) throws Exception {
        String dateToString = DateUtils.getDateToString();
        String secretKey = SecretUtils.secretString(user.getPhone()+"&"+user.getId()+"&"+dateToString, "SHA-1");
        BizUser bizUser = new BizUser();
        bizUser.setId(user.getId());
        bizUser.setSecretKey(secretKey);
        if (user.getSource() == null || user.getSource() <= 0) {
            bizUser.setSource(StringUtils.isEmpty(bizParamBean.getModel()) ? 0: Model.valueOf(bizParamBean.getModel()).getId());
        }
        bizUser.setLastLoginIp(bizParamBean.getIp());
        bizUser.setLastLoginTime(dateToString);
        int i = bizUserMapper.updateByPrimaryKeySelective(bizUser);
        if (i <= 0) {
            throw new ErrorMsgException("登录失败");
        }
        JSONObject json = new JSONObject();
        json.put("uid", user.getId());
        json.put("secretKey", secretKey);
        return json;
    }
}
