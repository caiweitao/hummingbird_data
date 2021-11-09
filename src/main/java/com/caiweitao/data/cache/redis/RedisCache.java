package com.caiweitao.data.cache.redis;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.caiweitao.data.annotation.Except;
import com.caiweitao.data.cache.Cache;

/**
 * @author caiweitao
 * @Date 2021年4月28日
 * @Description redis缓存
 */
public abstract class RedisCache<K, V> extends Cache<K, V> {
	protected RedisMap<K,V> redisMap;
	protected List<Field> exceptFieldList;//@Except标记的属性

	public RedisCache(int maxCapacity,String cacheName,Class<V> clazz) {
		super(clazz);
		redisMap = new RedisMap<K,V>(maxCapacity,cacheName ,clazz) {
			@Override
			public boolean canDelete(V v) {
				return !isMark(v);
			}
		};
		exceptFieldList = new ArrayList<>();
		Field[] declaredFields = clazz.getDeclaredFields();
		for (Field field:declaredFields) {
			if (field.getAnnotation(Except.class) != null) {
				field.setAccessible(true);
				exceptFieldList.add(field);
			}
		}
	}

	@Override
	public V get(K key) {
		V v = redisMap.get(key);
		if (v == null) {
			Object dataLock = getDataLock(key);
			synchronized (dataLock) {
				v = redisMap.get(key);
				if (v == null) {
					v = loadout(key);
					if (v != null) {
						put(key, v);
					}
				}
			}
		}
		return v;
	}

	@Override
	public boolean put(K key, V value) {
		Set<String> remove = redisMap.put(key, value);
		if (remove != null && !remove.isEmpty()) {
			for (String rk:remove) {
				getDataLockMap.remove(rk);
			}
		}
		return true;
	}

	@Override
	public Collection<K> keys() {
		return null;
	}
	
	@Override
	public boolean containKey(K k) {
		return redisMap.containKey(k);
	}
	
	@Override
	public int size() {
		return (int)redisMap.size();
	}
	
	@Override
	public void saveToDB() {
		try {
			List<String> keyAll = new ArrayList<>(redisMap.getKeys());
			//keys太多分批处理
			int unit = 5000;
			while (keyAll.size() > 0) {
				int keySize = keyAll.size();
				keySize = keySize < unit ? keySize : unit;
				List<String> keys = keyAll.subList(0, keySize);

				List<V> insertList = new ArrayList<>();//需要insert的
				List<V> updateList = new ArrayList<>();//需要update的
				for (String key:keys) {
					V entryValue = redisMap.hget(key);
					boolean insert = insertField.getBoolean(entryValue);
					if (insert) {
						insertList.add(entryValue);
					} else {
						updateList.add(entryValue);
					}
				}
				//批量insert
				if (insertList.size() > 0) {
					dao().batchInsert(insertList);
					//标记为false
					for (V v:insertList) {
						insertField.set(v, false);
						//回写到redis
						redisMap.hset((K)pkField.get(v), v);
					}
				}
				//批量update(按字段)
				List<Object[]> updateMemberList = new ArrayList<Object[]>();//批量参数值
				for (Field field:notPkFieldList) {//按字段批量保存
					List<Object> updateFieldList = new ArrayList<Object>();//入库的属性值
					for (V entryValue:updateList) {
						Object entryFieldValue = field.get(entryValue);//这个缓存对应field属性值
						boolean mark = markField.getBoolean(entryFieldValue);
						//有变化需要入库
						if (mark) {
							Object cacheKey = pkField.get(entryValue);
							updateMemberList.add(new Object[]{entryFieldValue,cacheKey});
							updateFieldList.add(entryFieldValue);
						}
					}
					if (updateMemberList.size() > 0) {
						long saveTime = System.currentTimeMillis();
						dao().batchUpdateByFieldName(field.getName(), updateMemberList);
						//批量保存后，将对应的mark标记设置为false
						for (Object fieldObject:updateFieldList) {
							//updateFieldList中的对象如果在入库后又做了更新，这个时候不能去掉mark标记
							long markTrueTime = markTrueTimeField.getLong(fieldObject);
							if (saveTime >= markTrueTime) {
								markField.set(fieldObject, false);//更新后将mark标记为false
							}
						}
						updateMemberList.clear();
					}
				}
				//回写到redis
				for (V v:updateList) {
					redisMap.hset((K)pkField.get(v), v);
				}

				keyAll.subList(0, keySize).clear();
			}
		} catch (Exception e ) {
			System.out.println("saveToDB ERROR ::"+clazz.getName());
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void shutdown() {
		saveToDB();
	}
	
	@Override
	public void clear() {
		
	}

	public RedisMap<K, V> getRedisMap() {
		return redisMap;
	}

	public List<Field> getExceptFieldList() {
		return exceptFieldList;
	}
}
