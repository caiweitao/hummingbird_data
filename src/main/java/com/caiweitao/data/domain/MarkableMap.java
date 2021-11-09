package com.caiweitao.data.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author caiweitao
 * @Date 2021年5月18日
 * @Description 大表中成员变量需要map的，继承此类
 */
public class MarkableMap<K,V,P> extends MarkableEntry<P> {
	private Map<K, V> map = new ConcurrentHashMap<K, V>();

	public MarkableMap(P playerId) {
		super(playerId);
	}

	public V get(K k) {
		return map.get(k);
	}
	
	public void put (K k,V v) {
		map.put(k, v);
	}
	
	public V remove (K k) {
		return map.remove(k);
	}
	
	public int size() {
		return map.size();
	}

	public Map<K, V> getMap() {
		return map;
	}
	
	public List<V> getList() {
		return new ArrayList<V>(map.values());
	}
	
	public boolean containsKey (K k) {
		return map.containsKey(k);
	}
	
	public boolean containsValue (V v) {
		return map.containsValue(v);
	}
	
	public void clear () {
		map.clear();
	}
	
}
