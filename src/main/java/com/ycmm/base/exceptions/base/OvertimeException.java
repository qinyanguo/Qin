package com.ycmm.base.exceptions.base;

import com.ycmm.base.exceptions.BaseException;
import com.ycmm.base.exceptions.enums.ExceptionCause;

/**
 * 访问异常   抛出408
 * 请求超时
 */
public class OvertimeException extends BaseException {

	private static final long serialVersionUID = 8068548853246070488L;

	@Override
	public String causeBy() {
		
		return ExceptionCause.overtime.name();
	}
}
