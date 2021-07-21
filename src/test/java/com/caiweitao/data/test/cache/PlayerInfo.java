package com.caiweitao.data.test.cache;

import com.caiweitao.data.domain.MarkableEntry;

/**
 * @author 蔡伟涛
 * @Date 2021年4月30日
 * @Description 玩家基本信息
 */
public class PlayerInfo extends MarkableEntry<Integer> {

	public PlayerInfo(Integer playerId) {
		super(playerId);
	}
	private int id;
	private String name;
	private int gold;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getGold() {
		return gold;
	}
	public void setGold(int gold) {
		this.gold = gold;
	}
	@Override
	public String toString() {
		return "PlayerInfo [id=" + id + ", name=" + name + ", gold=" + gold + ", isMark()=" + isMark() + "]";
	}
	
	
}
