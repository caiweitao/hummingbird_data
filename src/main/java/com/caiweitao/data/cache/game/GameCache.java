package com.caiweitao.data.cache.game;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.caiweitao.data.cache.Cache;
import com.caiweitao.data.cache.GameDataManager;
import com.caiweitao.data.cache.local.CommonLRUCache;
import com.caiweitao.data.cache.local.LRUCache;
import com.caiweitao.data.cache.redis.CommonRedisCache;
import com.caiweitao.data.cache.redis.RedisCache;
import com.caiweitao.data.config.CacheConfig;
import com.caiweitao.data.db.dao.BaseDao;
import com.caiweitao.data.domain.BaseEntry;
import com.caiweitao.data.domain.CommonBaseEntry;
import com.caiweitao.data.domain.MarkableEntry;

/**
 * @author 蔡伟涛
 * @Date 2021年4月27日
 * @Description 游戏缓存基类
 */
public abstract class GameCache<K, V extends BaseEntry> extends BaseGameCache<K, V>{
	private Cache<K, V> cache;
	
	public GameCache(String name) {
		this(CacheConfig.cacheType, name,CacheConfig.maxElementsInMemory);
	}
	public GameCache(String name,int maxElementsInMemory) {
		this(CacheConfig.cacheType, name,maxElementsInMemory);
	}
	public GameCache(int cacheType,String name,int maxElementsInMemory) {
		@SuppressWarnings("unchecked")
		Class<V> clazz = (Class<V>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[1];
		boolean commonCache = CommonBaseEntry.class.isAssignableFrom(clazz);//普通缓存（非大表）
		setName(name);
		if (cacheType == CacheConfig.CACHE_TYPE_LRU) {
			if (commonCache) {
				cache = new CommonLRUCache<K, V>(maxElementsInMemory,clazz) {
					@Override
					public V loadout(K key) {
						return without(key);
					}
					@Override
					public BaseDao<K, V> dao() {
						return getDao();
					}
				};
			} else {
				cache = new LRUCache<K, V>(maxElementsInMemory,clazz) {
					@Override
					public V loadout(K key) {
						return without(key);
					}
					
					@Override
					public BaseDao<K, V> dao() {
						return getDao();
					}
				};
			}
		} else if (cacheType == CacheConfig.CACHE_TYPE_LRU_REDIS) {
			if (commonCache) {
				cache = new CommonRedisCache<K, V>(maxElementsInMemory,name,clazz) {
					@Override
					public V loadout(K key) {
						return without(key);
					}
					@Override
					public BaseDao<K, V> dao() {
						return getDao();
					}
				};
			} else {
				cache = new RedisCache<K, V>(maxElementsInMemory,name,clazz) {
					@Override
					public V loadout(K key) {
						return without(key);
					}
					@Override
					public BaseDao<K, V> dao() {
						return getDao();
					}
				};
			}
		}
		GameDataManager.register(name, cache);
	}
	
	/**
	 * 统一处理缓存更新，在业务代码中修改完对象数据，需调用本方法更新（按字段更新）
	 * 没有调用此方法将不会被回写入库
	 * @param v
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <O extends MarkableEntry<K>> void update (O...fieldValues) {
		//把有修改的字段统一做mark标记
		for (O fieldValue:fieldValues) {
			if (fieldValue == null) {continue;}
			fieldValue.markTrue();
		}
		//本地缓存无需多余操作,非本地缓存需更新进去
		if (CacheConfig.cacheType == CacheConfig.CACHE_TYPE_LRU_REDIS) {
			//redis缓存需要手动保存 
			RedisCache<K, V> redisCache = (RedisCache<K, V>)cache;
			for (O o:fieldValues) {
				if (o == null) {continue;}
				V val = redisCache.getRedisMap().hget(o.getPlayerId().toString());
				//包括被@Except标注在内的成员都需要更新到redis上
				List<Field> allUpdateFieldList = new ArrayList<>(redisCache.notPkFieldList);
				allUpdateFieldList.addAll(redisCache.getExceptFieldList());
				for (Field f:allUpdateFieldList) {
					if (f.getType() == o.getClass()) {
						try {
							f.set(val, o);
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						}
						break;
					}
				}
				redisCache.getRedisMap().hset(o.getPlayerId(), val);
			}
		}
	}
	
	//针对普通表（非大表）
	public <V extends CommonBaseEntry> void update (V v) {
		v.markTrue();
		//本地缓存无需多余操作,非本地缓存需更新进去
		if (CacheConfig.cacheType == CacheConfig.CACHE_TYPE_LRU_REDIS) {
			RedisCache<K, V> redisCache = (RedisCache<K, V>)cache;
			try {
				redisCache.put((K)redisCache.pkField.get(v), v);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 新创建的大表对象，需调用该方法
	 * @param v
	 */
	@SuppressWarnings("unchecked")
	public void insert (V v) {
		v.markInsert();
		K pk = null;
		try {
			pk = (K)cache.pkField.get(v);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		cache.put(pk, v);
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
	
	public abstract BaseDao<K,V> getDao ();
	
	@Override
	public String toString() {
		return cache.toString();
	}
}
