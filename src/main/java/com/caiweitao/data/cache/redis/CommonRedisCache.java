package com.caiweitao.data.cache.redis;

import java.util.ArrayList;
import java.util.List;

/**
 * @author caiweitao
 * @Date 2021年5月24日
 * @Description 
 */
public abstract class CommonRedisCache<K, V> extends RedisCache<K, V> {

	public CommonRedisCache(int maxCapacity, String cacheName, Class<V> clazz) {
		super(maxCapacity, cacheName, clazz);
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
					}
					boolean update = commonBaseEntryField.getBoolean(entryValue);
					if (update) {
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
				if (updateList.size() > 0) {
					dao().batchUpdate(updateList);
					for (V v:updateList) {
						commonBaseEntryField.set(v, false);
						//回写到redis
						redisMap.hset((K)pkField.get(v), v);
					}
				}
			}
		} catch (Exception e ) {
			System.out.println("saveToDB ERROR ::"+clazz.getName());
			e.printStackTrace();
		} 
		
	}
}
