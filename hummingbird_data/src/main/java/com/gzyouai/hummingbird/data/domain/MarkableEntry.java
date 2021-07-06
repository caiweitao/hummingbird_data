package com.gzyouai.hummingbird.data.domain;
/**
 * @author 蔡伟涛
 * @Date 2021年4月27日
 * @Description 
 */
public class MarkableEntry<P> {

	private P playerId;//玩家id
	private boolean mark;//持久化标记，true:表示修改过需要持久化到数据库
	private long markTrueTime;//mark标记为true时的时间戳
	
	public MarkableEntry(P playerId) {
		this.playerId = playerId;
	}
	
	public void markTrue() {
		setMark(true);
		setMarkTrueTime(System.currentTimeMillis());
	}

	public boolean isMark() {
		return mark;
	}

	public void setMark(boolean mark) {
		this.mark = mark;
	}

	public long getMarkTrueTime() {
		return markTrueTime;
	}

	public void setMarkTrueTime(long markTrueTime) {
		this.markTrueTime = markTrueTime;
	}

	public P getPlayerId() {
		return playerId;
	}

	public void setPlayerId(P playerId) {
		this.playerId = playerId;
	}

}
