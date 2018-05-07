package com.ycmm.common.cache.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.ycmm.common.cache.CacheService;
import com.ycmm.base.exceptions.base.ErrorMsgException;
import com.ycmm.common.constants.Constants;
import com.ycmm.common.utils.Transcoder;
import org.springframework.stereotype.Service;

@Service
public class RedisCacheImpl implements CacheService {

	Logger logger = Logger.getLogger(RedisCacheImpl.class);
	
	private StringRedisTemplate redisTemplate;
	
	public StringRedisTemplate getRedisTemplate() {
		return redisTemplate;
	}

	public void setRedisTemplate(StringRedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@Override
	public void set(String key, Object value) {
		set(key, value, 0);
	}

	@Override
	public void set(String key, Object value, int expirationTime) {
		set(key, value, 0, expirationTime);
	}

	@Override
	public void set(String key, Object value, int timeToIdleSeconds, int expirationTime) {
		RedisConnection conn = null;
		try {
			conn = redisTemplate.getConnectionFactory().getConnection();
			if(!conn.isClosed() && value != null) {
				conn.set(key.getBytes(), Transcoder.serialize(value));
				if(expirationTime > 0) {
					conn.expire(key.getBytes(), expirationTime);
				}
			}
		} catch (Exception e) {
			logger.error(Constants.LOGGER_ERROR_REDIS_CACHE, e);
		} finally {
			if(conn != null) {
				conn.close();
			}
		}
	}

	@Override
	public Object get(String key) {
		RedisConnection conn = null;
		try {
			Object obj = null;
			conn = redisTemplate.getConnectionFactory().getConnection();
			if(StringUtils.isNotEmpty(key) && !conn.isClosed()) {
				byte[] in = conn.get(key.getBytes());
				if(in != null && in.length > 0) {
					obj= Transcoder.deserialize(in);
				}
			}
			return obj;
		} catch (Exception e) {
			logger.error(Constants.LOGGER_ERROR_REDIS_CACHE, e);
		} finally {
			if(conn != null) {
				conn.close();
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(String key, Class<T> clazz) {
		try {
			Object obj = get(key);
			if (obj == null) {
				return null;
			}
			if (obj.getClass() == clazz) {
				return (T) obj;
			} else {
				throw new ErrorMsgException(" 缓存类型不匹配  redis cache class != Class<T> clazz");
			}
		} catch (Exception e) {
			logger.error(Constants.LOGGER_ERROR_REDIS_CACHE, e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getList(String key, Class<T> clazz) {
		try {
			Object object = get(key);
			if(object == null) {
				return null;
			}
			List<?> list = (List<?>) object;
			if(list.size() == 0) {
				return (List<T>) list;
			}
			if(list.get(0).getClass() == clazz) {
				return (List<T>) list;
			} else {
				throw new ErrorMsgException(" 缓存类型不匹配  redis cache class != Class<T> clazz");
			}
		} catch (Exception e) {
			logger.error(Constants.LOGGER_ERROR_REDIS_CACHE, e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <K, T> Map<K, T> getMap(String key, Class<K> keyClazz, Class<T> valClazz) {
		try {
			Object object = get(key);
			if (object == null) {
				return null;
			}
			Map<?, ?> map = (Map<?, ?>) object;
			if (map.size() == 0) {
				return (Map<K, T>) map;
			}
			Set<?> entrySet = map.entrySet();
			Iterator<Map.Entry<?, ?>> iterator = (Iterator<Entry<?, ?>>) entrySet.iterator();
			while (iterator.hasNext()) {
				Entry<?, ?> next = iterator.next();
				if (next.getKey().getClass() == keyClazz && next.getValue().getClass() == valClazz) {
					return (Map<K, T>) map;
				} else {
					throw new ErrorMsgException(" 缓存类型不匹配  redis cache class != Class<T> clazz");
				}
			}

		} catch (Exception e) {
			logger.error(Constants.LOGGER_ERROR_REDIS_CACHE, e);
		}
		return null;
	}

	@Override
	public void remove(String key) {
		RedisConnection conn = null;
		try {
			conn = redisTemplate.getConnectionFactory().getConnection();
			byte[] in = conn.get(key.getBytes());
			if (in != null && in.length > 0) {
				conn.del(key.getBytes());
			}
		} catch (Exception e) {
			logger.error(Constants.LOGGER_ERROR_REDIS_CACHE, e);
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
	}

	@Override
	public void removeAll() {
		RedisConnection conn = null;
		try {
			conn = redisTemplate.getConnectionFactory().getConnection();
			// Redis Flushdb 命令用于清空当前数据库中的所有 key。
			conn.flushDb();
		} catch (Exception e) {
			logger.error(Constants.LOGGER_ERROR_REDIS_CACHE, e);
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
	}

}
