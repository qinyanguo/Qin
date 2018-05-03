package com.ycmm.system;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;

//通过它Spring容器会自动把上下文环境对象调用ApplicationContextAware接口中的setApplicationContext方法，
//就可以通过这个上下文环境对象得到Spring容器中的Bean。
public class SpringInit implements ApplicationContextAware{
	
	private static ApplicationContext context = null;
	
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		context = applicationContext;
	}
	
	public static Object getBean(String beanName){
		return context.getBean(beanName);
	}
	
	public static <T> T getBean(Class<T> clazz) throws BeansException{
		return context.getBean(clazz);
	}
	
	//发布事件
	public static void publish(ApplicationEvent event){
		if(context != null)
			context.publishEvent(event);
	}
}
