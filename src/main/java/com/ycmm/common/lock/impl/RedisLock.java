package com.ycmm.common.lock.impl;

import java.util.concurrent.TimeUnit;

import org.jboss.resteasy.logging.Logger;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.ycmm.base.spring.SpringInit;

public class RedisLock extends AbstractLock{

	private static Logger logger = Logger.getLogger(RedisLock.class);
	
	private static final String REDIS_LOCK_KEY = "LOCK.";
	
	private static final String DEFAULT_LOCK_KEY = "DEFAULT_LOCK";
	
	private static final long DEFAULT_LOCK_EXPIRES = 3000L;
	 
	/**
	 * 锁key
	 */
	protected String lockKey; 
	
	/**
	 * 锁的有效时长（毫秒）
	 */
	protected Long lockExpires;
	
	/**
	 * key 锁  过期时间戳
	 */
	protected Long lockExpireTime;
	
	/**
	 * redis  连接
	 */
	private RedisConnection redisConnection = null;
	
	public RedisLock() {
		this(DEFAULT_LOCK_KEY, DEFAULT_LOCK_EXPIRES);
	}

	/**
	 * 创建锁对象
	 * @param lockKey      锁的KEY
	 * @param lockExpires  锁的有效期（毫秒）
	 */
	public RedisLock(String lockKey, long lockExpires) {
		this.lockKey = REDIS_LOCK_KEY + lockKey;
		this.lockExpires = lockExpires;
		StringRedisTemplate stringRedisTemplate = SpringInit.getBean(StringRedisTemplate.class);
		RedisConnectionFactory connectionFactory = stringRedisTemplate.getConnectionFactory();
		try {
			if(connectionFactory != null) {
				this.redisConnection = connectionFactory.getConnection();
			}
		} catch (Exception e) {
			logger.error("【=====Redis分布式锁异常=====】", e);
		}
	}
	
	@Override
	protected boolean lock(boolean useTimeout, long time, TimeUnit timeUnit) {
		try {
			locked = false;
			byte[] synKey = lockKey.getBytes();
			if(useTimeout) { //默认为true  故使用阻塞超时
				long timeout = timeUnit.toMillis(time);
				long startTime = localTimeMillis();
				while(isTimeout(startTime, timeout) && !locked) {
					//阻塞时间为  正数   且当前没有现成获取到锁
					locked = lockImpl(synKey);
				}
				return locked;
			}
			//非阻塞
			while(!locked) {
				locked = lockImpl(synKey);
			}
			return locked;
		} catch (Exception e) {
			logger.error("【==== Redis分布式锁异常 ====】--->", e);
			return locked;
		}
	}
	
	@Override
	public boolean tryLock() {
		try {
			byte[] synKey = lockKey.getBytes();
			locked = lockImpl(synKey);
			return locked;
		} catch (Exception e) {
			logger.error("【=====Redis分布式锁异常=====】----->", e);
			locked = false;
			return false;
		}
	}

	/**
	 *  锁实现   
	 * @param synKey   锁KEY
	 * @return
	 */
	private boolean lockImpl(byte[] synKey) {
		if(this.redisConnection == null) {
			return false;
		}
		
		lockExpireTime = redisTimeMillis() + lockExpires + 1L;
		byte[] expireMillis = String.valueOf(lockExpireTime).getBytes();
		if(redisConnection.setNX(synKey, expireMillis)) {
			//如果获取锁成功   设置锁  key 的有效时长
			redisConnection.pExpire(synKey, lockExpires);
		}
		//获取当前锁的过期时间
		byte[] currentLock = redisConnection.get(synKey);
		if(currentLock != null) {
			String currentLockExpires = new String(currentLock);
			if(isTimeExpired(Long.valueOf(currentLockExpires))) {  
				//锁过期    获取锁   该方法是原子的,设置指定  key 的值，并返回  key 的旧值
				byte[] oldValue = redisConnection.getSet(synKey, expireMillis);
				if(oldValue != null && new String(oldValue).equals(currentLockExpires)) {
					//锁获取成功
					redisConnection.pExpire(synKey, lockExpires);
					return true;
				}
				
			}
		}
		
		return false;
	}
	
	@Override
	public void unLock() {
		try {
			if (locked) {
				// 如果当前线程还持有锁
				byte[] synKey = lockKey.getBytes();
				byte[] value = redisConnection.get(synKey);
				if (value != null) {
					if (new String(value).equals(lockExpireTime.toString())) {
						//确保是自己的锁
						redisConnection.del(synKey);
					}
				}
			}
		} catch (Exception e) {
			logger.error("【==== Redis分布式锁异常 ====】--->", e);
		} finally {
			// 释放锁 释放redis连接
			if (redisConnection != null) {
				redisConnection.close();
			}
		}
	}

	/**
	 * 校验锁是否过期
	 * @param
	 * @return
	 */
	private boolean isTimeExpired(Long currentLockExpires) {
		//如果当前锁过期时间   < redis服务器时间    说明过期
		return currentLockExpires <= redisTimeMillis();
	}

	/**
	 * 校验是否超时     防止设置的阻塞时间为    负数
	 * @param startTime  本地时间
	 * @param timeout    阻塞时间    
	 * @return
	 */
	private boolean isTimeout(long startTime, long timeout) {
		
		return (startTime + timeout) > System.currentTimeMillis();
	}

	/**
	 * 获取redis服务器时间
	 * @return
	 */
	private Long redisTimeMillis() {
		try {
			return redisConnection.time();
		} catch (Exception e) {
			return System.currentTimeMillis();
		}
		
	}
	
	/**
	 * 本地时间
	 * @return
	 */
	private Long localTimeMillis() {
		return System.currentTimeMillis();
	}
}
