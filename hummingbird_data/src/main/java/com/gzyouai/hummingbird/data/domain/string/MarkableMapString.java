package com.gzyouai.hummingbird.data.domain.string;

import com.gzyouai.hummingbird.data.domain.MarkableMap;

/**
 * @author 蔡伟涛
 * @Date 2021年5月18日
 * @Description 
 */
public class MarkableMapString<K,V> extends MarkableMap<K, V, String> {

	public MarkableMapString(String playerId) {
		super(playerId);
	}

}
