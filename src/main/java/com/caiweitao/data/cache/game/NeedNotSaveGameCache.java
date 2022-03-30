package com.caiweitao.data.cache.game;

import java.util.Collection;

import com.caiweitao.data.cache.GameDataManager;
import com.caiweitao.data.cache.ICache;
import com.caiweitao.data.cache.local.NeedNotSaveLRUCache;
import com.caiweitao.data.config.CacheConfig;
import com.caiweitao.data.exception.CacheSelectException;

/**
 * @author caiweitao
 * @Date 2021年5月25日
 * @Description 不需要持久化的缓存基类
 */
public abstract class NeedNotSaveGameCache<K,V> extends BaseGameCache<K, V>{
	private ICache<K, V> cache;
	
	public NeedNotSaveGameCache(String name) {
		this(CacheConfig.cacheType, name,CacheConfig.maxElementsInMemory);
	}
	public NeedNotSaveGameCache(String name,int maxElementsInMemory) {
		this(CacheConfig.cacheType, name,maxElementsInMemory);
	}
	public NeedNotSaveGameCache(int cacheType,String name,int maxElementsInMemory) {
		setName(name);
		cache = new NeedNotSaveLRUCache<K, V>(maxElementsInMemory) {
			@Override
			public V loadout(K key) {
				try {
					return without(key);
				} catch (CacheSelectException e) {
					e.printStackTrace();
					return null;
				}
			}
		};
		GameDataManager.register(name, cache);
	}
	
	@Override
	public V get(K key) {
		if (key == null) {
			return null;
		}
		return cache.get(key);
	}
	
	@Override
	public void put (K k,V v) {
		cache.put(k, v);
	}

	@Override
	public Collection<K> keys() {
		return cache.keys();
	}
	
	@Override
	public boolean containKey (K k) {
		return cache.containKey(k);
	}

	public void clear() {
		cache.clear();
	}
	
	public void reload (K k) {
		V v = cache.loadout(k);
		cache.put(k, v);
	}

	@Override
	public int size() {
		return cache.size();
	}
	
	/**
	 * 加载外部数据到缓存
	 * @param playerId
	 * @return
	 */
//	public abstract V without (K playerId);
	
	
	@Override
	public String toString() {
		return cache.toString();
	}
}
