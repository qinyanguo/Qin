package com.ycmm.exceptions;

/**
 * 用户未登录  抛出  403
 *
 */
public class NoAuthException extends BaseException{

	private static final long serialVersionUID = 1301514136131015720L;

	@Override
	public String causeBy() {
		return ExceptionCause.noAuth.name();
	}

}
