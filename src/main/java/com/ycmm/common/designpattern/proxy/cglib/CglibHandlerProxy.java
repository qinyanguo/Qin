package com.ycmm.common.designpattern.proxy.cglib;

import com.ycmm.common.designpattern.proxy.jdk.PerformanceMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;


import java.lang.reflect.Method;



/**
 * 模拟代理（调用自己实现的代理方法）
 * @Author Oliver.qin
 * @Date 2018/8/3 09:29
 * @Description:
 */
public class CglibHandlerProxy implements MethodInterceptor {

    Logger logger = LoggerFactory.getLogger(CglibHandlerProxy.class);

    @Autowired
    private DataSourceTransactionManager transactionManager;

    private Enhancer enhancer = new Enhancer();

    /**
     * 生成代理对象
     */
    public <T> T getCglibProxy(Class<T> clazz) {
        System.err.println(clazz);
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz); //设置目标类为代理类的父类
        enhancer.setCallback(this);
        return (T)enhancer.create();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        /**
         * 也可以通过 在spring-db <aop:config>中让其切到此路径然后配置一个<tx:method即可 https://www.cnblogs.com/ruiati/p/6027277.html
         */
        //获取事务定义
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        //设置事务隔离级别，开启新事物
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        //获取事务状态
        TransactionStatus status = transactionManager.getTransaction(def);
        Object result = null;
        //权限配置
        System.err.println("权限配置");
        //性能监控
        PerformanceMonitor.begin(o.getClass().getName() + "." + method.getName());
        try {
            System.err.println("开启事务");
            System.err.println("通过代理调用真实方法");
//            result = method.invoke(target, objects);
            //调用methodProxy比method运行效率高
//            result = methodProxy.invoke(o, objects);  //调用此法会出现栈溢出  java.lang.StackOverflowError
            result = methodProxy.invokeSuper(o, objects);
            transactionManager.commit(status);
            System.err.println("提交事务");
            //日志记录
//            logger.info(result.toString()); //会对toString方法做增强
        }catch (Exception e) {
            logger.info(e.getMessage());
            System.err.println("回滚事务");
            transactionManager.rollback(status);
        }
        PerformanceMonitor.end();
        return result;
    }
}
