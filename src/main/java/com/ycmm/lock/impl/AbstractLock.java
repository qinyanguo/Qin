package com.ycmm.lock.impl;

import java.util.concurrent.TimeUnit;

import com.ycmm.lock.Lock;

/**
 * 分布式锁  基础类
 * @author jishubu
 *
 */
public abstract class AbstractLock implements Lock {

	/**
	 * 当前是否持有锁的状态
	 * 这里需不需要保证可见性值得讨论, 因为是分布式的锁,  
     * 1.同一个jvm的多个线程使用不同的锁对象其实也是可以的, 这种情况下不需要保证可见性  
     * 2.同一个jvm的多个线程使用同一个锁对象, 那可见性就必须要保证了. 
	 */
	protected  volatile boolean locked;
	
	@Override
	public void lock() {
		lock(false, 0, null);
	}

	@Override
	public boolean tryLock(long time, TimeUnit timeUnit) {
		
		return lock(true, time, timeUnit);
	}

	/**
	 * 具体  锁  阻塞  实现
	 * @param useTimeout   是否使用阻塞超时方式   true：使用     false：不使用
	 * @param timeout      阻塞超时时间
	 * @param unit         超时时间单位
	 * @return
	 */
	protected abstract boolean lock(boolean useTimeout, long timeout, TimeUnit unit) ;
	
}
