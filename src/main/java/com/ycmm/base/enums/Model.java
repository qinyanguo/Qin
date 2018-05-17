package com.ycmm.base.enums;

/**
 * 客户端类型  枚举
 */
public enum Model {

	UNKNOWN("未知",-1),
	
	PC("电脑",1),

	ANDROID("安卓",2),
	
	WECHART("微信",3),
	
	IOS("苹果",4);
	
	private Model(String name, int id) {
		this.name = name;
		this.id = id;
	}
	private String name;
	
	private int id;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
}
