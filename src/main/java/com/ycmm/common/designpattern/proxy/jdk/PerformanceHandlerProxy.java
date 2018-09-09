package com.ycmm.common.designpattern.proxy.jdk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;


/**
 * 模拟代理（调用自己实现的代理方法）
 * @Author Oliver.qin
 * @Date 2018/8/3 09:29
 * @Description:
 */
public class PerformanceHandlerProxy implements InvocationHandler {

    Logger logger = LoggerFactory.getLogger(PerformanceHandlerProxy.class);

    @Autowired
    private DataSourceTransactionManager transactionManager;

    private Object target;

    public void setTarget(Object target) {
        this.target = target;
    }

    /**
     * 生成代理对象
     * 第一个参数：设置代码使用的类加载器，一般采用和目标类相同的类加载器
     * 第二个参数：设置代理类实现的接口，跟目标类使用相同的接口
     * 第三个参数：设置回调对象，当代理对象的方法被调用时，会调用该参数指定的 invoke 方法
     */
    public <T> T getProxyObject() {
        return (T) Proxy.newProxyInstance(this.getClass().getClassLoader(), //类加载器
                target.getClass().getInterfaces(),  //为哪些接口做代理
                this); //如何做增强  对象InvocationHandler
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
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
        PerformanceMonitor.begin(target.getClass().getName() + "." + method.getName());
        try {
            System.err.println("开启事务");
            System.err.println("通过代理调用真实方法");
            result = method.invoke(target, args);
            transactionManager.commit(status);
            System.err.println("提交事务");
            //日志记录
            logger.info(result.toString());
        }catch (Exception e) {
            logger.info(e.getMessage());
            System.err.println("回滚事务");
            transactionManager.rollback(status);
        }
        PerformanceMonitor.end();
        return result;
    }
}
