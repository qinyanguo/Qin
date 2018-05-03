package com.ycmm.cache;

import java.util.List;
import java.util.Map;

public interface CacheService {

	/**
	 * 添加    元素到cache
	 * @param key
	 * @param value
	 */
	public void set(String key, Object value);
	
	/**
	 * 添加元素到cache  并设置过期时间
	 * @param key
	 * @param value
	 * @param expirationTime 缓存过期时间（单位秒）
	 */
	public void set(String key, Object value, int expirationTime);

	/**
	 * 添加元素到cache  并设置过期时间以及缓存闲置时间
	 * @param key
	 * @param value
	 * @param timeToIdleSeconds : 对象空闲时间，指对象在多长时间没有被访问就会失效。只对eternal为false的有效。默认值0，表示一直可以访问。以秒为单位。 
	 * @param expirationTime
	 */
	public void set(String key, Object value, int timeToIdleSeconds, int expirationTime);
	
	/**
	 * 获取cache中的元素
	 * @param key
	 * @return
	 */
	public Object get(String key);
	
	/**
	 * 获取cache  中的元素
	 * @param key
	 * @param clazz
	 * @return
	 */
	public <T> T get(String key, Class<T> clazz);
	
	/**
	 * 获取list  对象
	 * @param key
	 * @param clazz  list对象内数据类型
	 * @return
	 */
	public <T> List<T> getList(String key, Class<T> clazz);
	
	/**
	 * 获取map  对象
	 * @param key
	 * @param keyClazz
	 * @param valClazz
	 * @return
	 */
	public <K,T> Map<K,T> getMap(String key, Class<K> keyClazz, Class<T> valClazz);
	
	/**
	 * 删除
	 * @param key
	 */
	public void remove(String key);
	
	/**
	 * 删除全部数据
	 */
    public void removeAll();
	
}



















