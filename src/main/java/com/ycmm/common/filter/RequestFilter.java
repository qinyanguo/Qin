package com.ycmm.common.filter;


import com.ycmm.base.exceptions.base.ErrorMsgException;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author       Oliver.qin
 * @date         2018/7/31 14:32
 */

public class RequestFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!(request instanceof HttpServletRequest) || !(response  instanceof HttpServletResponse)) {
            throw new ServletException("This ServletFilter just support Http request");
        }
    }

    @Override
    public void destroy() {

    }
}
