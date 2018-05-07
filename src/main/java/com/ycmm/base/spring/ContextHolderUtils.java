package com.ycmm.base.spring;


import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class ContextHolderUtils {

    /**
     * 在service获取request和response,正常来说在service层是没有request的,然而直接从controlller传过来的话解决方法太粗暴
     */
    public static HttpServletRequest getRequest() {
        HttpServletRequest request =  ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        return request;
    }

    public static HttpSession getSession() {
        return getRequest().getSession();
    }

    public static HttpSession getSession(boolean create) {
        return getRequest().getSession(create);
    }
}
