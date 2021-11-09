package com.caiweitao.data.domain;

import java.util.HashSet;
import java.util.Set;

/**
 * @author caiweitao
 * @Date 2021年5月19日
 * @Description 
 */
public class MarkableSet<V,P> extends MarkableEntry<P> {
	private Set<V> set = new HashSet<>();

	public MarkableSet(P playerId) {
		super(playerId);
	}
	
	public void add (V v) {
		set.add(v);
	}
	
	public boolean remove (V v) {
		return set.remove(v);
	}
	
	public int size () {
		return set.size();
	}
	
	public Set<V> getSet () {
		return set;
	}
	
	public boolean contains (V v) {
		return set.contains(v);
	}

}
