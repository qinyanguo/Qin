package com.ycmm.base.exceptions.base;

import com.ycmm.base.exceptions.BaseException;
import com.ycmm.base.exceptions.enums.ExceptionCause;

/**
 * 用户未登录  抛出  403
 *
 */
public class NoAuthException extends BaseException {

	private static final long serialVersionUID = 1301514136131015720L;

	@Override
	public String causeBy() {
		return ExceptionCause.noAuth.name();
	}

}
