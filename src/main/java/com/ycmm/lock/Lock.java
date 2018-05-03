package com.ycmm.lock;

import java.util.concurrent.TimeUnit;

/**
 * 分布式锁  接口类
 * @author jishubu
 *
 */
public interface Lock {

	/**
	 * 尝试获取锁   获取到锁返回，否则阻塞当前线程，直到获取到锁   方不阻塞
	 */
	void lock();
	
	/**
	 * 尝试获取锁，获取不到立即返回，不阻塞
	 * 获取成功返回true，否则  
	 */
	boolean tryLock();
	
	/**
	 * 尝试获取锁若在规定时间内获取到锁返回  true，否则返回false
	 * @param time   超时时间
	 * @param timeUnit  时间单位
	 * @return  true 获取锁成功 ，  false 规定时间内没有获取到锁
	 */
	boolean tryLock(long time, TimeUnit timeUnit);
	
	/**
	 * 释放锁
	 */
	void unLock();
}
