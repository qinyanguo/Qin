package com.ycmm.front.handle.controller;

import com.ycmm.base.bean.BizParamBean;
import com.ycmm.base.bean.ResultBean;
import com.ycmm.common.constants.Constants;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 获取系统时间
 */
@Controller
@RequestMapping("system")
public class SystemHandleController {

    @RequestMapping("/date")
    @ResponseBody
    public ResultBean getSystemDate(BizParamBean bizParamBean) throws Exception {
        JSONObject json = new JSONObject();
        json.put("time", System.currentTimeMillis());
        json.put("serviceMobile", Constants.SERVICE_MOBILE);
        return new ResultBean(json);
    }
}
