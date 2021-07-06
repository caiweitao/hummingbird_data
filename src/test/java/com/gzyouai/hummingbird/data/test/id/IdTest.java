package com.gzyouai.hummingbird.data.test.id;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import com.gzyouai.hummingbird.data.idworker.StringIdWorker;

/**
 * @author 蔡伟涛
 * @Date 2021年6月1日
 * @Description 
 */
public class IdTest {
	StringIdWorker idworker = new StringIdWorker(1001, 1);
	public Set<String> set = new HashSet<>();
	AtomicInteger index = new AtomicInteger();

	public static void main(String[] args) {
		IdTest idTest = new IdTest();
		for (int i=0;i<200;i++) {
			new Thread(idTest.new IdRun()).start();
		}

//		try {
//			Thread.sleep(10000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		System.out.println(idTest.set.size());
	}

	class IdRun implements Runnable {
		@Override
		public void run() {
			for (int i=0;i<1000;i++) {
				String nextId = idworker.nextId();
				if (set.contains(nextId)) {
					System.out.println("***************************************:"+nextId);
				}
				set.add(nextId);
				System.out.println(nextId);
				int incrementAndGet = index.incrementAndGet();
				if (incrementAndGet == 200000) {
					System.out.println("++++++"+incrementAndGet);
				}
			}
		}
		
	}
}
