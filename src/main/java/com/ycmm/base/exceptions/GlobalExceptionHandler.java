package com.ycmm.base.exceptions;

import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ycmm.base.exceptions.base.*;
import com.ycmm.base.exceptions.enums.ErrorMsgEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ycmm.base.bean.FrontParamBean;
import com.ycmm.base.bean.ResultBean;
import com.ycmm.common.utils.WebUtils;

/**
 * 在Controller中抛出的异常，当没有被catch处理时，GlobalExceptionHandler中定义的处理方法可以起作用，
 * 在方法写明注解@ExceptionHandler，并注明其异常类即可。此种方法不仅可以作用于Controller，同样的在DAO层、
 * service层也可，都可以由GlobalExceptionHandler进行处理。此种写法减少代码的入侵，值得推荐。
 */
@ControllerAdvice
public class GlobalExceptionHandler {
	
	Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	/**
	 * 统一异常处理
	 * @param response
	 * @param request
	 * @param ex
	 * @return
	 */
	@ExceptionHandler
	@ResponseBody
	public ResultBean exceptionHandler(HttpServletResponse response, HttpServletRequest request, Exception ex) {
		ResultBean resultBean = null;
		String params = null;
		//获取请求的URL
		String url = request.getRequestURI();
		//获取异常IP地址
		String ip = WebUtils.getNginxAddress(request);
		//获取浏览器类型
		String model = WebUtils.getModel(request);
		//表示从request范围取得设置的属性，必须要先setAttribute设置属性，才能通过getAttribute来取得，设置与取得的为Object对象类型 。
		FrontParamBean frontParam = (FrontParamBean) request.getAttribute("frontParam");
		if (frontParam == null) {
			/**
			 * 根据Java规范：request.getParameterMap()返回的是一个Map类型的值，该返回值记录着前端（如jsp页面）
			 * 所提交请求中的请求参数和请求参数值的映射关系
			 * 。这个返回值有个特别之处——只能读。不像普通的Map类型数据一样可以修改。这是因为服务器为了实现一定的安全规范
			 * ，所作的限制。比如WebLogic，Tomcat，Resin，JBoss等服务器均实现了此规范。
			 * 如果实在有必要在取得此值以后做修改的话，要新建一个map对象，将返回值复制到此新map对象中进行修改，
			 * 用新的map对象代替使用之前的返回值。
			 */
			Map<String, String[]> parameterMap = request.getParameterMap();
			// 将参数格式化输出
			params = JSON.toJSONString(parameterMap, SerializerFeature.PrettyFormat);
		} else {
			params = JSON.toJSONString(frontParam, SerializerFeature.PrettyFormat);
		}
		if(ex instanceof InvocationTargetException) {
			Throwable targetException = ((InvocationTargetException) ex).getTargetException();
			ex = (Exception) targetException;
		}
		/**
		 * 服务中在@RequestMapping（）注解中添加了一个consumes=“application/json”的属性，
		 * 使这个方法只能处理json格式传递的参数，而我在我得服务中需要接收的参数为一个String字符串，不是通过json的方式进行传递。
		 * 所以就出现不支持的媒体类型异常信息，导致服务调用失败
		 */
		if(ex instanceof HttpMediaTypeException) {
			ex = new BadRequestException();
		}
		if(ex instanceof BadRequestException) {
			response.setStatus(400);  //参数不完整
		} else if(ex instanceof UnauthorizedExcception) {
			response.setStatus(401);  //当前设备已被登录
		} else if(ex instanceof NoAuthException) {
			response.setStatus(403);  //未登录
		} else if(ex instanceof OvertimeException) {
			response.setStatus(408);  //请求超时
		} else if(ex instanceof ErrorMsgException) {
			resultBean = new ResultBean((ErrorMsgException) ex);
		} else {
			//其他异常
			resultBean = new ResultBean(ErrorMsgEnum.ERROR_ALERT, "当前服务正在维护升级，请稍后重试");
			//通知到维护人员
			String msg = MessageFormat.format("[{0}][{1}]:{2}", ex.getMessage());
			
			
			
			
			
			if (frontParam != null) {
				msg = MessageFormat.format("[{0}][{1}][{2}/{3}/{4}/{5}]:{6}", ip, request.getMethod(), url, model,
						frontParam.getBiz_module(), frontParam.getBiz_method(), params);
			}
			logger.error(msg, ex);
		}
		return resultBean;
	}
}



















