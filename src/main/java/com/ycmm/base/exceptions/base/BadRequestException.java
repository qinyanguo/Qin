package com.ycmm.base.exceptions.base;

import com.ycmm.base.exceptions.BaseException;
import com.ycmm.base.exceptions.enums.ExceptionCause;

/**
 * 	访问异常  抛出 400
 *  坏的请求   参数不完整
 */
public class BadRequestException extends BaseException {

	private static final long serialVersionUID = 7425953910975678182L;

	@Override
	public String causeBy() {
		
		return ExceptionCause.badRequest.name();
	}

}
