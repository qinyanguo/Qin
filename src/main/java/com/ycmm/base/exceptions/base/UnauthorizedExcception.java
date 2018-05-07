package com.ycmm.base.exceptions.base;

import com.ycmm.base.exceptions.BaseException;
import com.ycmm.base.exceptions.enums.ExceptionCause;

/**
 * 用户已在其他设备登录    当前认证无效               抛出401
 *
 */
public class UnauthorizedExcception  extends BaseException {

	private static final long serialVersionUID = -6264683378639240004L;

	@Override
	public String causeBy() {
		return ExceptionCause.unauthorized.name();
	}

}
