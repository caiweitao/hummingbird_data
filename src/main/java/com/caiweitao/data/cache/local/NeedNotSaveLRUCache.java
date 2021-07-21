package com.caiweitao.data.cache.local;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.caiweitao.data.cache.ICache;

/**
 * @author 蔡伟涛
 * @Date 2021年5月25日
 * @Description 不需要持久化的缓存
 */
public abstract class NeedNotSaveLRUCache<K, V> implements ICache<K, V> {
	protected int maxCapacity;
	protected LinkedHashMap<K, V> lruMap;//做LRU策略（非线程安全）
	protected ConcurrentHashMap<K,V> totalMap;//缓存全集（线程安全）

	public NeedNotSaveLRUCache(final int maxCapacity) {
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
			v = loadout(key);
			if (v != null) {
				put(key, v);
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
				lruMap.remove(head.getKey());
				totalMap.remove(head.getKey());
			}
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
	public void shutdown() {
		executor.shutdown();
	}

	@Override
	public void clear() {
		totalMap.clear();
		executor.submit(()->lruMap.clear());
	}
	
	@Override
	public String toString() {
		return lruMap.toString();
	}
}
