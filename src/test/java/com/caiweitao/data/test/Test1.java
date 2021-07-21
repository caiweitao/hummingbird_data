package com.caiweitao.data.test;


import java.io.File;

import com.caiweitao.data.db.connection.DruidConnectionPool;
import com.caiweitao.data.test.cache.CacheFactory;
import com.caiweitao.data.test.cache.Player;
import com.caiweitao.data.test.db.DBTest;

/**
 * @author 蔡伟涛
 * @Date 2021年5月10日
 * @Description 测试取出数据后  在缓存中被删除的情况
 */
public class Test1 {

	public static void main(String[] args) {
		DruidConnectionPool.init(getRootPath(), "config/game_db.properties");
		
		Player player = CacheFactory.playerCache.get(1);
		Player player2 = CacheFactory.playerCache.get(2);
		Player player3 = CacheFactory.playerCache.get(3);
		Player player4 = CacheFactory.playerCache.get(4);
		Player player5 = CacheFactory.playerCache.get(5);
		Player player6 = CacheFactory.playerCache.get(6);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		player.getPlayerInfo().setGold(33);
		CacheFactory.playerCache.update(player.getPlayerInfo());
		
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
