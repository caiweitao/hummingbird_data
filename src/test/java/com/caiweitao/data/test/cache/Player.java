package com.caiweitao.data.test.cache;

import com.caiweitao.data.annotation.PK;
import com.caiweitao.data.annotation.Table;
import com.caiweitao.data.domain.BaseEntry;

/**
 * @author 蔡伟涛
 * @Date 2021年4月27日
 * @Description 
 */

@Table(tableName="player")
public class Player extends BaseEntry{

	@PK
	private int playerId;
	private PlayerInfo playerInfo;
	private Items items;
	
	public int getPlayerId() {
		return playerId;
	}
	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}
	public PlayerInfo getPlayerInfo() {
		return playerInfo;
	}
	public void setPlayerInfo(PlayerInfo playerInfo) {
		this.playerInfo = playerInfo;
	}
	public Items getItems() {
		return items;
	}
	public void setItems(Items items) {
		this.items = items;
	}
	
//	public long getTtime() {
//		return ttime;
//	}
//	public void setTtime(long ttime) {
//		this.ttime = ttime;
//	}
	
	@Override
	public String toString() {
//		return "Player [playerId=" + playerId + ", playerInfo=" + playerInfo + ", items=" + items + ", isInsert()="
//				+ isInsert() + "]";
		return playerId+"";
	}
	
	
	
	
}
