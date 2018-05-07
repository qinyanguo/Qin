package com.ycmm.base.exceptions.base;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.ycmm.base.exceptions.BaseException;
import com.ycmm.base.exceptions.enums.ErrorMsgEnum;
import com.ycmm.base.exceptions.enums.ExceptionCause;

/**
 * @author jishubu
 *
 */
public class ErrorMsgException extends BaseException {

	private static final long serialVersionUID = -6499837807977390292L;

	public ErrorMsgException() {
		super();
		this.msg = ErrorMsgEnum.ERROR_ALERT.getMsg();
		this.code = ErrorMsgEnum.ERROR_ALERT.getCode();
	}

	public ErrorMsgException(String msg, String code) {
		this.msg = msg;
		this.code = code;
	}
	
	public ErrorMsgException(ErrorMsgEnum errorMsgEnum, String msg) {
		this.msg = msg;
		this.code = errorMsgEnum.getCode();
	}

	/**
	 * 错误信息提示，只用于展示
	 * 
	 * @param msg
	 */
	public ErrorMsgException(String msg) {
		this.code = ErrorMsgEnum.ERROR_ALERT.getCode();
		this.msg = msg;
		if (StringUtils.isEmpty(msg)) {
			this.msg = ErrorMsgEnum.ERROR_ALERT.getMsg();
		}
	}

	@Override
	public String causeBy() {
		return ExceptionCause.ErrorMessages.name(); // 返回即为 ErrorMessages
	}

	private String msg;

	private String code;

	@Override
	public String toString() {
		return "ErrorMsgException [msg=" + msg + ", code=" + code + "]";
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
