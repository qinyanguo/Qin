package com.ycmm.front.user.service;

import com.ycmm.base.bean.BizParamBean;
import com.ycmm.base.bean.ResultBean;

/**
 * Created by jishubu on 2018/5/11.
 */
public interface UserSignLoginService {

    /**
     * 账户登录处理
     * @param bizParamBean
     * @return
     */
    public ResultBean signLogin(BizParamBean bizParamBean) throws Exception;
}
