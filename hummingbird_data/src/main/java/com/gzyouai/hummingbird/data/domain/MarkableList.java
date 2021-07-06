package com.gzyouai.hummingbird.data.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 蔡伟涛
 * @Date 2021年5月19日
 * @Description 
 */
public class MarkableList<V,P> extends MarkableEntry<P> {
	private List<V> list = new ArrayList<>();

	public MarkableList(P playerId) {
		super(playerId);
	}
	
	public V get (int index) {
		return list.get(index);
	}
	
	public void add (V v) {
		list.add(v);
	}
	
	public V remove (int index) {
		return list.remove(index);
	}
	
	public int size () {
		return list.size();
	}
	
	public List<V> getList () {
		return list;
	}
	
	public boolean contains (V v) {
		return list.contains(v);
	}

}
