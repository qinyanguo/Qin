package com.ycmm.common.cache.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.ycmm.common.utils.KryoTranscoder;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.ycmm.common.cache.CacheService;
import com.ycmm.base.exceptions.base.ErrorMsgException;
import com.ycmm.common.constants.Constants;
import com.ycmm.common.utils.Transcoder;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.support.atomic.RedisAtomicInteger;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
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
				conn.set(key.getBytes(), KryoTranscoder.serialize(value));
//				conn.set(key.getBytes(), Transcoder.serialize(value));
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
					obj= KryoTranscoder.deserialize(in);
//					obj= Transcoder.deserialize(in);
				}
			}
			return obj;
		} catch (Exception e) {
		    e.printStackTrace();
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
		    e.printStackTrace();
//			logger.error(Constants.LOGGER_ERROR_REDIS_CACHE, e);
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
		    e.printStackTrace();
//			logger.error(Constants.LOGGER_ERROR_REDIS_CACHE, e);
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
		    e.printStackTrace();
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

    @Override
    public String getValue(String key) {
	    try {

            String s1 = String.valueOf(redisTemplate.opsForValue().get(key));
            return s1;
        }catch (Exception e) {
	        e.printStackTrace();
        }
        return null;
    }

    /**
     * 使用自增方法的一些注意事项，序列化方式不对不能进行自增运算 。查看博客   https://blog.csdn.net/tyyh08/article/details/80267261
     * 报错信息：redis.clients.jedis.exceptions.JedisDataException: ERR value is not an integer or out of range
     * @param key
     * @param value
     * @param timeToIdleSeconds
     * @param expirationTime
     * @return
     */

    @Override
    public Long setIncr(String key, Object value, int timeToIdleSeconds, int expirationTime, Integer type) {

        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericToStringSerializer<Long>(Long.class));
        redisTemplate.setExposeConnection(true);
        redisTemplate.setConnectionFactory(redisTemplate.getConnectionFactory());
        redisTemplate.afterPropertiesSet();

        Long increment = redisTemplate.opsForValue().increment(key, Integer.valueOf(value.toString()));
        if (increment <= 1 && type == 1) {
            redisTemplate.opsForValue().set(key, increment.toString(), expirationTime, TimeUnit.SECONDS);
        }
//        String s1 = String.valueOf(redisTemplate.opsForValue().get(key));
        return increment;
//	    RedisConnection conn = null;
//	    try {
//
//
//            RedisConnectionFactory connectionFactory = redisTemplate.getConnectionFactory();
//            conn = connectionFactory.getConnection();
//            if (value != null && !conn.isClosed()) {
//                System.out.println(key.getBytes());
//                conn.set(key.getBytes(), value.toString().getBytes());
//                if (expirationTime > 0) {
//                    conn.expire(key.getBytes(), expirationTime);
//                }
//            }
//        }catch (Exception e) {
//	        logger.error("原子性自增失败"+ e);
//        }

    }
}
