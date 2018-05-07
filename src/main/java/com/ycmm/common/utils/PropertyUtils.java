package com.ycmm.common.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class PropertyUtils {

	private static Logger logger = Logger.getLogger(PropertyUtils.class);

	private static Properties prop;

	static {
		loadProps();
	}

	private synchronized static void loadProps() {
		
		logger.info("------开始加载属性文件------");
		prop = new Properties();
		InputStream in = null;
		try {
			//通过类加载器获取属性文件
			in = PropertyUtils.class.getClassLoader().getResourceAsStream("local.properties");
			//通过类获取属性文件
//			in = PropertyUtils.class.getResourceAsStream("local.properties");
			prop.load(in);
			
		} catch (FileNotFoundException e) {
			logger.error("-----未找到指定文件（local.properties）-----");
		} catch (IOException e) {
			logger.error("-----出现IOException-----");
		} finally{
			if(in != null){
				try {
					in.close();
				} catch (IOException e) {
					logger.error("-----文件流关闭出现异常-----");
				}
			}
		}
		logger.info("-----加载属性文件完成-----");
		logger.info("properties文件内容为："+ prop);
	}
	
    /**
     * 获取属性
     * @param key
     * @return
     * @throws Exception
     */
	public static String getProperty(String key) {
		if(prop == null){
			loadProps();
		}
		return prop.getProperty(key);
	}
	
	  /**
     * 获取属性
     * @param key
     * @return
     * @throws Exception
     */
	public static String getProperty(String key, String defaultValue){
		if(prop == null){
			loadProps();
		}
		return prop.getProperty(key, defaultValue);
	}
	
	/**
	 * 设置属性
	 * @param msg
	 * @throws Exception
	 */
	public static void setProperty(String msg){
		if(prop == null){
			loadProps();
		}
		prop.setProperty("新属性", msg);
		
		//    ???????????
//		prop.store(out, comments);  
	}
}












