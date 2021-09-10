package com.caiweitao.data.domain;

import java.util.concurrent.atomic.AtomicBoolean;

import com.caiweitao.data.domain.BaseEntry;

/**
 * @author 蔡伟涛
 * @Date 2021年5月24日
 * @Description 普通表（非大表）实体基类
 */
public class CommonBaseEntry extends BaseEntry {
	private AtomicBoolean mark = new AtomicBoolean(false);//持久化标记，true:表示修改过需要持久化到数据库
	private long markTrueTime;//mark标记为true时的时间戳

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
	
}
