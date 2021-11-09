package com.caiweitao.data.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.caiweitao.data.domain.BaseEntry;

/**
 * @author caiweitao
 * @Date 2021年5月29日
 * @Description Cache<K,Map<K2,T>>
 */
//public abstract class MapGameCache<K,V extends Map<K2, T>, K2, T extends CommonBaseEntry> {
//	private MapLRUCache<K, V, K2, T> cache;
//	private String name;
//	
//	public MapGameCache(String name) {
//		this(CacheConfig.cacheType, name,CacheConfig.maxElementsInMemory);
//	}
//	public MapGameCache(String name,int maxElementsInMemory) {
//		this(CacheConfig.cacheType, name,maxElementsInMemory);
//	}
//	public MapGameCache(int cacheType,String name,int maxElementsInMemory) {
//		@SuppressWarnings("unchecked")
//		Class<T> clazz = (Class<T>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[3];
//		if (!CommonBaseEntry.class.isAssignableFrom(clazz)) {
//			throw new RuntimeException(String.format("实体类【%s】没有继承CommonBaseEntry", clazz.getName()));
//		}
//		setName(name);
//		if (cacheType == CacheConfig.CACHE_TYPE_LRU) {
//			cache = new MapLRUCache<K, V, K2, T>(maxElementsInMemory,clazz) {
//				@Override
//				public V loadout(K key) {
//					return without(key);
//				}
//				@Override
//				public BaseDao<K, T> dao() {
//					return getDao();
//				}
//			};
//		} else if (cacheType == CacheConfig.CACHE_TYPE_LRU_REDIS) {
////			cache = new CommonRedisCache<K, V>(maxElementsInMemory,name,clazz) {
////				@Override
////				public V loadout(K key) {
////					return without(key);
////				}
////				@Override
////				public BaseDao<K, V> dao() {
////					return getDao();
////				}
////			};
//		}
//		GameDataManager.register(name, cache);
//	}
//	
//	//针对普通表（非大表）
//	public void update (T t) {
//		t.markTrue();
//		//本地缓存无需多余操作,非本地缓存需更新进去
////		if (CacheConfig.cacheType == CacheConfig.CACHE_TYPE_LRU_REDIS) {
////			RedisCache<K, V> redisCache = (RedisCache<K, V>)cache;
////			try {
////				redisCache.get((K2)redisCache.pkField.get(t));
////				redisCache.put(, t);
////			} catch (IllegalArgumentException e) {
////				e.printStackTrace();
////			} catch (IllegalAccessException e) {
////				e.printStackTrace();
////			}
////		}
//	}
//	
//	/**
//	 * 新创建的大表对象，需调用该方法
//	 * @param v
//	 */
//	@SuppressWarnings("unchecked")
//	public void insert (K k, K2 k2,T t) {
//		t.markInsert();
//		
////		K pk = null;
////		try {
////			pk = (K)cache.pkField.get(t);
////		} catch (IllegalArgumentException | IllegalAccessException e) {
////			e.printStackTrace();
////		}
//		get(k).put(k2, t);
//		cache.put(pk, v);
//	}
//	
//	public V get(K key) {
//		if (key == null) {
//			return null;
//		}
//		return cache.get(key);
//	}
//
////	public boolean put(K key, V val) {
////		return cache.put(key, val);
////	}
//
//	public Collection<K> keys() {
//		return cache.keys();
//	}
//	
//	public boolean containKey (K k) {
//		return cache.containKey(k);
//	}
//
//	public void clear() {
//		cache.clear();
//	}
//
//	public int size() {
//		return cache.size();
//	}
//	
//	public String getName() {
//		return name;
//	}
//	public void setName(String name) {
//		this.name = name;
//	}
//	
//	/**
//	 * 加载外部数据到缓存
//	 * @param playerId
//	 * @return
//	 */
//	public abstract V without (K playerId);
//	
//	public abstract BaseDao<K,T> getDao ();
//	
//	@Override
//	public String toString() {
//		return cache.toString();
//	}
//}
