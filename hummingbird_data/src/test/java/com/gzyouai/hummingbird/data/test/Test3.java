package com.gzyouai.hummingbird.data.test;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.gzyouai.hummingbird.data.db.connection.DruidConnectionPool;
import com.gzyouai.hummingbird.data.test.cache.CacheFactory;
import com.gzyouai.hummingbird.data.test.db.DBTest;
import com.gzyouai.hummingbird.data.test.db.DaoFactory;

/**
 * @author 蔡伟涛
 * @Date 2021年5月10日
 * @Description 缓存数量超上限后，如果最旧的数据没有入库，不会被删除，测试put方法维持 数量的情况
 */
public class Test3 {

	public static void main(String[] args) {
//		CacheConfig.redisPropertiesPath = getRootPath() + "config/redis.properties";
		DruidConnectionPool.init(getRootPath(), "config/game_db.properties");
		
//		for (int i=1;i<=5;i++) {
//			Player player = CacheFactory.playerCache.get(i);
//			if (player == null) {
//				player = new Player();
//				player.setPlayerId(i);
//				player.setPlayerInfo(new PlayerInfo(player.getPlayerId()));
//				player.setItems(new Items(player.getPlayerId()));
//				player.getItems().put(1, new Item(1, 20));
//				player.getItems().put(2, new Item(2, 30));
//				
//				CacheFactory.playerCache.insert(player);
//			} 
////			player.getPlayerInfo().setGold(2222);
////			CacheFactory.playerCache.update(player.getPlayerInfo());
//			player.getItems().get(1).setNumber(8888);
//			CacheFactory.playerCache.update(player.getItems());
//			System.out.println(player.getItems());
//		}
//		System.out.println(CacheFactory.playerCache.toString());
//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
////		Player player = CacheFactory.playerCache.get(9);
////		player.getPlayerInfo().setGold(777);
////		CacheFactory.playerCache.update(player.getPlayerId(), player.getPlayerInfo());
//		while (true){
//			try {
//				Thread.sleep(3000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//			System.out.println(CacheFactory.playerCache.toString());
////			CacheFactory.playerCache.get(1);
////			System.out.println(CacheFactory.playerCache.toString());
////			Player player = CacheFactory.playerCache.get(11);
////			System.out.println(CacheFactory.playerCache.toString());
//		}
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
