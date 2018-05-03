package com.ycmm.system;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.core.io.Resource;

public class Property {
	
	private static Logger logger = Logger.getLogger(Property.class);
	
	//spring自己封装继承于InputStreamSource
	Resource location;
	
	public Resource getLocation() {
		return location;
	}

	public void setLocation(Resource location) {
		this.location = location;
	}

	//信息键值对
	public static Properties sysConfig;
	
	public void init(){
		logger.info("----------开始资源初始化----------");
		if(loadSysConfig()){
			logger.info("----------资源初始化成功----------");
		}else {
			logger.info("———————————— 资源初始化失败     ——————————————");
		}
	}

	private boolean loadSysConfig() {
		boolean result = true;
			try {
				sysConfig = new Properties();
				sysConfig.load(location.getInputStream());
			} catch (IOException e) {
				logger.error(e);
				result = false;
			}
		return result;
	}
}
