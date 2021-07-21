package com.caiweitao.data.cache;

import java.util.Collection;

/**
 * @author 蔡伟涛
 * @Date 2021年4月27日
 * @Description 缓存接口
 */
public interface ICache<K, V> {
	
	public V get (K key);
	
	public boolean put(K key, V v);
	
	public Collection<K> keys();
	
	public boolean containKey (K k);
	
	public void clear();
	
	public int size();
	
	public V loadout(K key);//加载外部值
	
	public void shutdown ();//实现关服时该缓存的处理逻辑
	
}
