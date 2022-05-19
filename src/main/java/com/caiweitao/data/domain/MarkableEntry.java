package com.caiweitao.data.domain;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author caiweitao
 * @Date 2021年4月27日
 * @Description 大表成员属性基类
 */
public class MarkableEntry<P> {

	private P playerId;//玩家id
	/**
	 * 持久化标记，true:表示修改过需要持久化到数据库
	 * 存在玩家线程和持久化线程同时操作mark
	 */
	private AtomicBoolean mark = new AtomicBoolean(false);
	/**
	 * mark标记为true时的时间戳
	 */
	private long markTrueTime;
	
	public MarkableEntry(P playerId) {
		this.playerId = playerId;
	}
	
	//此方法可能存在多个玩家线程同时操作所以使用synchronized
	public synchronized void markTrue() {
		mark.compareAndSet(false, true);
		setMarkTrueTime(System.currentTimeMillis());
	}

	public AtomicBoolean getMark() {
		return mark;
	}

	public void setMark(AtomicBoolean mark) {
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
