package com.ycmm.base.exceptions;

import com.ycmm.base.bean.BizParamBean;
import com.ycmm.base.bean.ResultBean;
import com.ycmm.common.constants.Constants;
import net.sf.json.JSONArray;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.aop.ThrowsAdvice;

import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;

/**
 * web 项目中用  GlobalExceptionHandler，在jar包中使用 aop 配置ExceptionHandler
 * @Author Oliver.qin
 * @Date 2018/7/31 17:00
 * @Description:
 */

public class ExceptionHandler implements ThrowsAdvice{

    public static Logger logger = Logger.getLogger(ExceptionHandler.class);

    public ResultBean around(JoinPoint joinPoint) throws Throwable{

        ResultBean resultBean = null;
        BizParamBean bizParamBean = null;
        Object[] args = null;
        String className = null;
        String methodName = null;

        ProceedingJoinPoint proceedingJoinPoint = null;

        /**
         * ProceedingJoinPoint为 JoinPoint的子类
         */
        proceedingJoinPoint = (ProceedingJoinPoint)joinPoint;
        try {
            args = proceedingJoinPoint.getArgs();
            if (args != null && args.length == 1) {
                Object arg = args[0];
                bizParamBean = (arg instanceof BizParamBean) ? (BizParamBean) arg : null;
            }
            //获取原方法的返回值
            resultBean = (ResultBean)proceedingJoinPoint.proceed();

        } catch (Throwable e) {
            //代理类异常，需要转一下才可抛出
            if (e instanceof InvocationTargetException) {
                e = ((InvocationTargetException) e).getTargetException();
            }
            
            if (proceedingJoinPoint != null) {
                //获取类全限定名  com.ycmm.base.bean.BizParamBean
                Class<? extends Object> forClass = proceedingJoinPoint.getTarget().getClass();
                //获取类简易名称  BizParamBean
                className = forClass.getSimpleName();
                //获取方法名
                methodName = proceedingJoinPoint.getSignature().getName();
                String logInfo = MessageFormat.format("[{0}][{1}]:{2}", className, methodName, JSONArray.fromObject(args));
                if (bizParamBean != null) {
                    logInfo = MessageFormat.format(Constants.LOGGER_ERROR_FORMAT, bizParamBean.getIp(), className, methodName,
                            bizParamBean.getUid(), bizParamBean.getModel(), bizParamBean.getVersion(), bizParamBean.getBiz_param());
                }
                if (e instanceof BaseException) {
                    logger.warn(logInfo, e);
                }else {
                    logger.error(logInfo, e);
                }
            }
            throw e;
        }

        return resultBean;
    }
}
