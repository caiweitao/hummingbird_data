package com.caiweitao.data.common;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 蔡伟涛
 * @Date 2021年4月28日
 * @Description 守护线程工厂
 */
public class CommonDaemonThreadFactory implements ThreadFactory {

	private final AtomicInteger nextId = new AtomicInteger();
	private String prefix;

	public CommonDaemonThreadFactory(String prefix) {
		this.prefix = (prefix + " - ");
	}

	public Thread newThread(Runnable r) {
		Thread t = new Thread(r, prefix + nextId.incrementAndGet());
		t.setDaemon(true);
		return t;
	}

}
