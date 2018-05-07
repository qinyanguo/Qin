package com.ycmm.base.bean;

import java.io.Serializable;

import com.ycmm.base.exceptions.enums.ErrorMsgEnum;

import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;

/**
 *
 * @author jishubu
 *
 */
public class ResultBean implements Serializable, Cloneable{
	
	private static final long serialVersionUID = -5101284532356118881L;
	
	/**
	 * 信息
	 */
	private String msg;
	/**
	 * 编码 
	 */
	private String code;
	/**
	 * 结果ֵ
	 */
	private JSONObject biz_result;
	
	public ResultBean() {
		super();
		this.msg = ErrorMsgEnum.SUCCESS.getMsg();
		this.code=ErrorMsgEnum.SUCCESS.getCode();
		this.biz_result = new JSONObject();
	}

	public ResultBean(ErrorMsgEnum errorMsgEnum, String msg) {
		this.msg = msg;
		this.code = errorMsgEnum.getCode();
	}

	public ResultBean(Object biz_result){
		this.msg = ErrorMsgEnum.SUCCESS.getMsg();
		this.code= ErrorMsgEnum.SUCCESS.getCode();
		
		if(JSONUtils.isNull(biz_result)){
			//���Ϊ��
			this.biz_result = new JSONObject();
		}else if(JSONUtils.isArray(biz_result)){
			//����Ǽ���
			JSONObject json = new JSONObject();
			json.put("list", biz_result);
			this.biz_result = json;
		}else if(JSONUtils.isString(biz_result)){
			//������ַ���
			this.msg = String.valueOf(biz_result);
			this.biz_result = new JSONObject();
		}else if(JSONUtils.isBoolean(biz_result)){
			//����ǲ�������
			JSONObject json = new JSONObject();
			json.put("status", biz_result);
			this.biz_result = json;
		}else {
			if(biz_result.getClass() == JSONObject.class){
				this.biz_result = (JSONObject)biz_result;
			}else {
				this.biz_result = JSONObject.fromObject(biz_result);
			}
		}
	}	
	
	public String getMsg() {
		return msg;
	}


	public void setMsg(String msg) {
		this.msg = msg;
	}


	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}


	public JSONObject getBiz_result() {
		return biz_result;
	}


	public void setBiz_result(JSONObject biz_result) {
		this.biz_result = biz_result;
	}

}
