//package com.caiweitao.data.test.mongodb;
//
//import org.bson.types.ObjectId;
//
//import com.caiweitao.data.annotation.PK;
//import com.caiweitao.data.annotation.Table;
//import com.caiweitao.data.domain.BaseEntry;
//import com.caiweitao.data.test.cache.PlayerInfo;
//
///**
// * @author caiweitao
// * @Date 2021年4月27日
// * @Description 
// */
//
//@Table(tableName="player")
//public class User extends BaseEntry{
//
//	private ObjectId _id;
//	@PK
//	private int playerId;
//	private String name;
//	private boolean max;
//	private long time;
//	private PlayerInfo playerInfo;
//	
//	public User(){
//	}
//	
//	public int getPlayerId() {
//		return playerId;
//	}
//	public void setPlayerId(int playerId) {
//		this.playerId = playerId;
//	}
//	public String getName() {
//		return name;
//	}
//	public void setName(String name) {
//		this.name = name;
//	}
//	public boolean isMax() {
//		return max;
//	}
//	public void setMax(boolean max) {
//		this.max = max;
//	}
//	public long getTime() {
//		return time;
//	}
//	public void setTime(long time) {
//		this.time = time;
//	}
//	public PlayerInfo getPlayerInfo() {
//		return playerInfo;
//	}
//	public void setPlayerInfo(PlayerInfo playerInfo) {
//		this.playerInfo = playerInfo;
//	}
//
//	public ObjectId get_id() {
//		return _id;
//	}
//
//	public void set_id(ObjectId _id) {
//		this._id = _id;
//	}
//
//	@Override
//	public String toString() {
//		return "User [_id=" + _id + ", playerId=" + playerId + ", name=" + name + ", max=" + max + ", time=" + time
//				+ ", playerInfo=" + playerInfo + "]";
//	}
//
//}
