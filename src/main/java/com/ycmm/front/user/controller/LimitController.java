package com.ycmm.front.user.controller;

import com.ycmm.base.bean.FrontParamBean;
import com.ycmm.base.system.RequestLimit;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 *
 */
@Controller
@RequestMapping("test")
public class LimitController {


    @ResponseBody
    @RequestMapping(value = "/limit")
    @RequestLimit(count = 2, time = 120, totalCount = 15)
    public void test(HttpServletRequest request, @RequestBody() FrontParamBean frontParamBean) {
        System.out.println(frontParamBean);
    }
}
