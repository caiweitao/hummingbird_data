package com.gzyouai.hummingbird.data.cache.local;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.google.gson.Gson;

/**
 * @author 蔡伟涛
 * @Date 2021年5月24日
 * @Description 普通lru本地缓存
 */
public abstract class CommonLRUCache<K,V> extends LRUCache<K, V> {

	public CommonLRUCache(int maxCapacity, Class<?> clazz) {
		super(maxCapacity, clazz);
	}

	@Override
	public void saveToDB() {
		List<V> insertList = new ArrayList<>();//需要insert的
		List<V> updatetList = new ArrayList<>();//需要update的
		try {
			for (Entry<K,V> en:totalMap.entrySet()) {
				V entryValue = en.getValue();//一个缓存数据
				boolean insert = insertField.getBoolean(entryValue);
				if (insert) {
					insertList.add(entryValue);
				}
				boolean update = commonBaseEntryField.getBoolean(entryValue);
				if (update) {
					updatetList.add(entryValue);
				}
			}
			//批量insert
			if (insertList.size() > 0) {
				dao().batchInsert(insertList);
				//标记为false
				for (V v:insertList) {
					insertField.set(v, false);
				}
			}
			//批量update
			if (updatetList.size() > 0) {
				long saveTime = System.currentTimeMillis();
				dao().batchUpdate(updatetList);
				//标记为false
				for (V v:updatetList) {
					//updateFieldList中的对象如果在入库后又做了更新，这个时候不能去掉mark标记
					long markTrueTime = commonBaseEntryMarkTrueField.getLong(v);
					if (saveTime >= markTrueTime) {
						commonBaseEntryField.set(v, false);//更新后将mark标记为false
					}
				}
			}
		}catch (Exception e ) {
			System.out.println("saveToDB ERROR ::"+clazz.getName());
			e.printStackTrace();
		}
	}
	/**
	 * 清除已入库的缓存
	 */
	@Override
	public void clear() {
		try {
			for (Entry<K,V> en:totalMap.entrySet()) {
				K key = en.getKey();
				V entryValue = en.getValue();//一个缓存数据
				boolean insert = insertField.getBoolean(entryValue);
				boolean update = commonBaseEntryField.getBoolean(entryValue);
				if (!insert && !update) {
					totalMap.remove(key);
					removeLruMap(key);
					System.out.println(String.format("删除缓存:%s,【%s】", entryValue, new Gson().toJson(entryValue)));
				}
			}
		} catch (Exception e ) {
			System.out.println("CLEAR CACHE ERROR ::"+clazz.getName());
			e.printStackTrace();
		}
	}
}
