package com.ycmm.base.exceptions.enums;

public enum ErrorMsgEnum {

	/**
	 * 操作处理成功
	 */
	SUCCESS("1c01", "操作成功"),
	
	/**
	 * 自定义弹窗处理
	 */
	ERROR_ALERT("0e00", "操作异常，请稍后重试"),
	
	/**
	 * 系统错误 请稍候重新尝试
	 */
	ERROR_SERVER("0e01", "系统错误，请稍后重试");
	
	private String msg;
	
	private String code;

	private ErrorMsgEnum(String msg, String code) {
		this.msg = msg;
		this.code = code;
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
	
}
