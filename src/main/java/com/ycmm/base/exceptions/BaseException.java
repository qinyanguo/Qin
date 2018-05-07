package com.ycmm.base.exceptions;

/**
 * 异常基类
 * @author jishubu
 *
 */
public abstract class BaseException extends Exception{

	private static final long serialVersionUID = 5393788176772081120L;
	
	public BaseException(){
		super();
	}
	
	public BaseException(String err){
		super(err);
	}
	
	public abstract String causeBy();
	
}
