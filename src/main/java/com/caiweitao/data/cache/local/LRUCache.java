package com.caiweitao.data.cache.local;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import com.caiweitao.data.cache.Cache;
import com.google.gson.Gson;

/**
 * @author 蔡伟涛
 * @Date 2021年4月27日
 * @Description 特殊本地LRU缓存（按字段批量持久化）V的成员变量相当于一个小表
 */
public abstract class LRUCache<K, V> extends Cache<K, V> {
	protected LinkedHashMap<K, V> lruMap;//做LRU策略（非线程安全）
	protected ConcurrentHashMap<K,V> totalMap;//缓存全集（线程安全）

	public LRUCache(int maxCapacity,Class<?> clazz) {
		super(clazz);
		this.maxCapacity = maxCapacity;
		lruMap = new LinkedHashMap<K, V>(16, 0.75f, true);//true:表是按访问顺序排序
		totalMap = new ConcurrentHashMap<K, V>();
	}

	//LinkedHashMap非线程安全，由executor单线程负责操作，避免多线程问题
	ExecutorService executor = Executors.newSingleThreadExecutor();
	@Override
	public V get(K key) {
		V v = totalMap.get(key);
		if (v == null) {
			Object dataLock = getDataLock(key);
			synchronized (dataLock) {
				v = totalMap.get(key);
				if (v == null) {
					v = loadout(key);
					if (v != null) {
						put(key, v);
					}
				}
			}
		} else {
			//做LRU策略，访问的key会刷新为最新缓存
			executor.submit(()->lruMap.get(key));
		}
		return v;
	}

	@Override
	public boolean put(K key, V value) {
		totalMap.put(key, value);
		executor.submit(()->{
			lruMap.put(key, value);
			//缓存数量达上限后，会判断最旧的缓存是否入库，如果未入库则不会删除，
			//removeEldestEntry只会在put的时候删掉一个，加下面逻辑为了数据入库后，缓存数量维持在上限值以内
			while (size() > maxCapacity) {
				Entry<K, V> head = getHead();
				if (isMark(head.getValue())) {
					break;
				} else {
					lruMap.remove(head.getKey());
					totalMap.remove(head.getKey());
					getDataLockMap.remove(head.getKey());
				}
			}
			System.out.println(clazz.getSimpleName()+"sssss::"+size());
		});
		return true;
	}
	
	/**获得cacheMap首个元素（也是最旧的元素）*/
	private Entry<K, V> getHead() {
	    return lruMap.entrySet().iterator().next();
	} 

	@Override
	public Collection<K> keys() {
		return totalMap.keySet();
	}

	@Override
	public int size() {
		return totalMap.size();
	}
	
	@Override
	public boolean containKey(K k) {
		return totalMap.containsKey(k);
	}
	
	@Override
	public void saveToDB() {
		List<V> insertList = new ArrayList<>();//需要insert的
		try {
			for (Entry<K,V> en:totalMap.entrySet()) {
				V entryValue = en.getValue();//一个缓存数据
				boolean insert = insertField.getBoolean(entryValue);
				if (insert) {
					insertList.add(entryValue);
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
			//批量update(按字段)
			List<Object[]> updateMemberList = new ArrayList<Object[]>();//批量参数值
			for (Field field:notPkFieldList) {//按字段批量保存
				List<Object> updateFieldList = new ArrayList<Object>();//入库的属性值
				for (Entry<K,V> en:totalMap.entrySet()) {
					V entryValue = en.getValue();
					Object entryFieldValue = field.get(entryValue);//这个缓存对应field属性值
					if (entryFieldValue == null) {//属性值如果为空，不做持久化
//						System.out.println(String.format("数据【%s】类-属性【%s】反射获取值为null,对象【%s】", 
//								clazz.getName(), field.getName(), new Gson().toJson(entryValue)));
						continue;
					}
//					boolean mark = markField.getBoolean(entryFieldValue);
					AtomicBoolean mark = (AtomicBoolean)markField.get(entryFieldValue);
					//有变化需要入库
					if (mark.get()) {
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
//							markField.set(fieldObject, false);//更新后将mark标记为false
							AtomicBoolean mark = (AtomicBoolean)markField.get(fieldObject);
							mark.compareAndSet(true, false);
						}
					}
					updateMemberList.clear();
				}
			}
		} catch (Exception e ) {
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
				V value = en.getValue();
				boolean insert = insertField.getBoolean(value);
				if (insert) {//需要insert，不能清除
					continue;
				}
				boolean remove = true;
				for (Field field:notPkFieldList) {
					Object entryFieldValue = field.get(value);//这个缓存对应field属性值
					if (entryFieldValue == null) {//属性值如果为空，不做持久化
						continue;
					}
					boolean mark = markField.getBoolean(entryFieldValue);
					if (mark) {
						remove = false;
						break;
					}
				}
				if (remove) {
					totalMap.remove(key);
					removeLruMap(key);
					System.out.println(String.format("删除缓存:%s,【%s】", value, new Gson().toJson(value)));
				}
			}
		} catch (Exception e ) {
			System.out.println("CLEAR CACHE ERROR ::"+clazz.getName());
			e.printStackTrace();
		}
	}
	protected void removeLruMap (K k) {
		executor.submit(()->lruMap.remove(k));
	}
	
	@Override
	public void shutdown() {
		executor.shutdown();
		saveToDB();
	}
	
//	public void shutdown(Runnable r) {
//		executor.shutdown();
//		saveToDB();
//		r.run();
////		closeexecutor
////		ruku
////		run()
//	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
//		for (Map.Entry<K, V> entry : cacheMap.entrySet()) {
//			sb.append(String.format("【%s】 ", entry.getValue()));
//		}
		sb.append(lruMap.values());
		return "toString():"+sb.toString();
	}
}
