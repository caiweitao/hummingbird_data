package com.caiweitao.data.test.cache;

import java.io.File;

import com.caiweitao.data.cache.GameDataManager;
import com.caiweitao.data.db.mysql.connection.DruidConnectionPool;
import com.caiweitao.data.test.db.DBTest;
import com.caiweitao.data.test.db.TestDao;

/**
 * @author 蔡伟涛
 * @Date 2021年4月27日
 * @Description 
 */
public class TestMain {
	public static void main(String[] args) {
		DruidConnectionPool.init(getRootPath(), "config/game_db.properties");
		GameDataManager.init(getRootPath()+"config/cache.properties");//缓存初始化
//		TestDao playerDao = new TestDao();
		
		
		Player player = CacheFactory.playerCache.get(1);
		System.out.println(player);
//		player.getPlayerInfo().setGold(3333);
//		CacheFactory.playerCache.update(player);
		
//		player.getItems().getItemMap().put(2, new Item(1, 11, 999));
//		player.getItems().setMark(true);
//		CacheFactory.playerCache.update(player.getPlayerId(), player);
		
//		for (int i=1;i<=100;i++) {
//			Player player = CacheFactory.playerCache.get(i);
//			player.getPlayerInfo().setGold(2233);
//			CacheFactory.playerCache.update(player.getPlayerId(),player.getPlayerInfo());
//			
////			try {
////				Thread.sleep(1000);
////			} catch (InterruptedException e) {
////				e.printStackTrace();
////			}
//		}
//		CacheFactory.playerCache.get(6);
//		System.out.println(CacheFactory.playerCache.toString());
//		System.out.println(CacheFactory.itemCache.toString());
		
//		Player player = CacheFactory.playerCache.get(1);
//		Player player2 = CacheFactory.playerCache.get(2);
//		Player player3 = CacheFactory.playerCache.get(3);
//		Player player4 = CacheFactory.playerCache.get(4);
//		Player player5 = CacheFactory.playerCache.get(5);
//		Player player6 = CacheFactory.playerCache.get(6);
//		System.out.println(CacheFactory.playerCache.toString());
//		try {
//			Thread.sleep(2000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		Player player7 = CacheFactory.playerCache.get(7);
//		System.out.println(player6);
//		System.out.println(CacheFactory.playerCache.toString());
//		System.out.println(CacheFactory.playerCache.size());
		
//		player.getPlayerInfo().setGold(9999);
//		CacheFactory.playerCache.update(player.getPlayerInfo());
//		GameDataManager.shutdown();
//		System.out.println(player.getPlayerInfo());
//		System.out.println(CacheFactory.playerCache.toString());
//		while(true) {}
//			try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
////			System.out.println(CacheFactory.playerCache.toString());
////			Player player = CacheFactory.playerCache.get(11);
		
		
//		final Map<String,String> map = new ConcurrentHashMap<String, String>();
//		map.put("1", "11");
//		map.put("2", "22");
//		map.put("3", "33");
//		map.put("4", "44");
//		map.put("5", "55");
//		
//		Runnable r1 = new Runnable() {
//			@Override
//			public void run() {
//				for (Entry<String,String> en:map.entrySet()) {
//					System.out.println(en.getKey());
//					try {
//						Thread.sleep(500);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//				}
//				
//			}
//		};
//		Runnable r2 = new Runnable() {
//
//			@Override
//			public void run() {
//				for (int i=0;i<10;i++) {
//					map.put(i+"", i+""+i);
//					try {
//						Thread.sleep(500);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//				}
//				System.out.println("/////"+map);
//				
//			}
//			
//		};
//		new Thread(r1).start();
//		new Thread(r2).start();
		
	}
	public static String getRootPath() {
		String rootPath = new File(DBTest.class.getResource("/").getFile()).getPath() + "/";

		// 不打成jar包运行时,Main.class.getResource("/") 会有 com路径
		String com = "classes/com/";
		if (rootPath.endsWith(com)) {
			rootPath = rootPath.substring(0, rootPath.length() - com.length());
		}
		return rootPath;
	}
}
