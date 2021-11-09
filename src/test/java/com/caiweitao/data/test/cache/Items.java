package com.caiweitao.data.test.cache;

import com.caiweitao.data.domain.MarkableMap;

/**
 * @author caiweitao
 * @Date 2021年4月30日
 * @Description 
 */
public class Items extends MarkableMap<Integer, Item,Integer>{

	public Items(Integer playerId) {
		super(playerId);
	}

	@Override
	public String toString() {
		return "Items [getMap()=" + getMap() + "]";
	}

	
}
