package com.ycmm.base.bean;

import java.io.Serializable;

import net.sf.json.JSONObject;

public class FrontParamBean implements Serializable, Cloneable{

	private static final long serialVersionUID = -6880873660121195084L;

	/**
	 * 请求业务模块
	 */
	private String biz_module;
	/**
	 * 请求业务  方法
	 */
	private String biz_method;
	/**
	 * 版本号
	 */
	private String version;
	/**
	 * 用户 session id
	 */
	private String SID;
	/**
	 * 请求客户端唯一标志
	 */
	private String dev;
	/**
	 * 用户uid
	 */
	private String uid;
	/**
	 * 请求时间戳
	 */
	private Long time;
	/**
	 * 请求签名   校验
	 */
	private String sign;
	
	/**
	 * 业务请求参数
	 */
	private JSONObject biz_param;

	public String getBiz_module() {
		return biz_module;
	}

	public void setBiz_module(String biz_module) {
		this.biz_module = biz_module;
	}

	public String getBiz_method() {
		return biz_method;
	}

	public void setBiz_method(String biz_method) {
		this.biz_method = biz_method;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getSID() {
		return SID;
	}

	public void setSID(String sID) {
		SID = sID;
	}

	public String getDev() {
		return dev;
	}

	public void setDev(String dev) {
		this.dev = dev;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	@SuppressWarnings("unchecked")
	public <T>T getBiz_param(Class<T> clazz){
		return (T)JSONObject.toBean(biz_param, clazz);
	}
	
	public JSONObject getBiz_param() {
		return biz_param;
	}

	public void setBiz_param(JSONObject biz_param) {
		this.biz_param = biz_param;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("{\"dev\":\"").append(dev);
		sb.append("\",\"biz_module\":\"").append(biz_module);
		sb.append("\",\"biz_method\":\"").append(biz_method);
		sb.append("\",\"time\":").append(time);
		sb.append("}");
		return sb.toString();
	}

	@Override
	public FrontParamBean clone(){
		try {
			FrontParamBean bean = (FrontParamBean)super.clone();
			if(biz_param != null){
				JSONObject json = new JSONObject();
				json.putAll(biz_param);
				bean.setBiz_param(json);
			}
			return bean;
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
}














