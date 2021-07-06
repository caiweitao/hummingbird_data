package com.gzyouai.hummingbird.data.test;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.concurrent.ConcurrentHashMap;

import com.gzyouai.hummingbird.data.db.connection.DruidConnectionPool;
import com.gzyouai.hummingbird.data.test.cache.CacheFactory;
import com.gzyouai.hummingbird.data.test.cache.Item;
import com.gzyouai.hummingbird.data.test.cache.Items;
import com.gzyouai.hummingbird.data.test.cache.Player;
import com.gzyouai.hummingbird.data.test.cache.PlayerInfo;
import com.gzyouai.hummingbird.data.test.db.DBTest;

/**
 * @author 蔡伟涛
 * @Date 2021年5月12日
 * @Description 
 */
public class Test2 {
	public static void main(String[] args) {
		DruidConnectionPool.init(getRootPath(), "config/game_db.properties");

		CacheFactory.playerCache.get(1);
//		CacheFactory.playerCache.get(2);
//		Player player = new Player();
//		player.setPlayerId(3);
//		player.setPlayerInfo(new PlayerInfo());
//		player.setItems(new Items());
//		CacheFactory.playerCache.insert(player);
//		try {
//			Thread.sleep(2000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		System.out.println(CacheFactory.playerCache.toString());
//		player.getItems().putItem(new Item(1, 101, 99));
//		CacheFactory.playerCache.update(player.getPlayerId(),player.getItems());
//		player.getPlayerInfo().setGold(33);

		while (true){}
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
