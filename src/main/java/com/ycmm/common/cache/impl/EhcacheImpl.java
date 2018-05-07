package com.ycmm.common.cache.impl;

import java.util.List;
import java.util.Map;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.ycmm.common.cache.CacheService;
import com.ycmm.common.constants.Constants;
import org.springframework.stereotype.Service;

/**
 * Ehcache缓存实现类   ##使用方法直接在实现类中添加注解即可
 * @Autowired
 * @Qualifier("ehCache")
 *
 */
@Service
public class EhcacheImpl implements CacheService {
	
	private final Logger logger = Logger.getLogger(EhcacheImpl.class);

	/**
	 * 缓存 key 锁
	 */
	private final String EHCACHE_KEY_SYN = "EHCACHE_KEY_SYN";
	
	private Cache cache;
	
	public Cache getCache() {
		return cache;
	}

	public void setCache(Cache cache) {
		this.cache = cache;
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
		
		try {
			String syn = EHCACHE_KEY_SYN + key;
			/*
			 * intern 原理
			 * intern() 方法需要传入一个字符串对象（已存在于堆上），然后检查 StringTable 里是不是已经有一个相同的拷贝。
			 * 但在等价比较上的性能提升并不是应该使用 intern 的理由。实际上，intern 的目的在于复用字符串对象以节省内存。
			 * 在明确知道一个字符串会出现多次时才使用 intern(),并且只用它来节省内存。
			 * 使用 intern() 方法的效率，取决于重复的字符串与唯一的字符串的比值。另外，还要看在产生字符串对象的地方，代码是不是容易进行修改。
			 */
			synchronized (syn.intern()) {
				Element element = null;
				if(cache.get(key.trim()) != null) {
					cache.remove(key.trim());
				}
				if(value != null) {
					element = new Element(key.trim(), value);
					if(timeToIdleSeconds > 0) {
						//设置对象过期时间
						element.setTimeToIdle(timeToIdleSeconds);
					}
					if(expirationTime > 0) {
						element.setTimeToLive(expirationTime);
					}
					cache.put(element);
				}
			}
		} catch (Exception e) {
			logger.error(Constants.LOGGER_ERROR_EH_CACHE, e);
		}
	}

	@Override
	public Object get(String key) {
		try {
			Object result = null;
			Element element = null;
			if(StringUtils.isNotEmpty(key) && cache.get(key.trim()) != null) {
				element = cache.get(key.trim());
				if(element.getObjectValue() != null) {
					result = element.getObjectValue();
				}
			}
			return result;
		} catch (Exception e) {
			logger.error(Constants.LOGGER_ERROR_EH_CACHE,e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(String key, Class<T> clazz) {
		try {
			Object object = get(key);
			return (T)object;
		} catch (Exception e) {
			logger.error(Constants.LOGGER_ERROR_EH_CACHE,e);
		}
		return null;
	}
	
	@Override
	public void remove(String key) {
		try {
			if(cache.get(key.trim()) != null) {
				cache.remove(key.trim());
			}
		} catch (Exception e) {
			logger.error(Constants.LOGGER_ERROR_EH_CACHE,e);
		}
		
	}

	@Override
	public void removeAll() {
		try {
			cache.removeAll();
		} catch (Exception e) {
			logger.error(Constants.LOGGER_ERROR_EH_CACHE,e);
		}
		
	}

	@Override
	public <T> List<T> getList(String key, Class<T> clazz) {
		return null;
	}

	@Override
	public <K, T> Map<K, T> getMap(String key, Class<K> keyClazz, Class<T> valClazz) {
		return null;
	}

}
