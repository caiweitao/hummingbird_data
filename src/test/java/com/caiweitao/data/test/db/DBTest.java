package com.caiweitao.data.test.db;

import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.caiweitao.data.db.mysql.connection.DruidConnectionPool;
import com.caiweitao.data.test.cache.CacheFactory;
import com.caiweitao.data.test.cache.Items;
import com.caiweitao.data.test.cache.Player;

/**
 * @author caiweitao
 * @Date 2021年5月6日
 * @Description 
 */
public class DBTest {

	public static void main(String[] args) {
		DruidConnectionPool.init(getRootPath(), "config/game_db.properties");
		Connection gameDbConnection = ConnectionManager.getGameDbConnection();
		System.out.println(gameDbConnection);

		TestDao testDao = new TestDao();
		
		long t = System.currentTimeMillis();
		
//		List<Player> selectList = testDao.selectList("select * from player where ttime = ?", new java.sql.Timestamp(1620369690000l));
//		List<Player> selectList = testDao.selectListByKey(2);
//		Map<Integer, Player> selectMap = testDao.selectMap("select * from player where ttime = ?", new java.sql.Timestamp(1620369690000l));
//		Map<Integer, Player> selectMap = testDao.selectMapByKey(2);
//		for (Player p:selectMap.values()) {
//			System.out.println(p);
//		}
//		List<Object[]> list = new ArrayList<Object[]>();
//		list.add(new Object[]{new java.sql.Timestamp(1620369691000l),2});
//		list.add(new Object[]{new java.sql.Timestamp(1620369691000l),3});
//		list.add(new Object[]{new java.sql.Timestamp(1620369691000l),4});
//		System.out.println(testDao.batchExecuteUpdate("update player set ttime=? where playerId=?", list));
		List<Player> list = new ArrayList<Player>();
		for (int i=1;i<=100;i++) {
			Player player = CacheFactory.playerCache.get(i);
//			if (player == null) {
//				player = new Player();
//				player.setPlayerId(i);
//				player.setPlayerInfo(new PlayerInfo());
//				player.getPlayerInfo().setGold(999);
//				list.add(player);
//			}
			player.setItems(new Items(player.getPlayerId()));
			list.add(player);
//			testDao.delete(player);
//			player.getPlayerInfo().setMark(true);
//			player.getItems().getItemMap().put(1, new Item(1, 11, 111));
//			player.setTtime(System.currentTimeMillis());
//					testDao.insert(player);
//			testDao.update(player);
		}
		testDao.batchUpdate(list);
//		testDao.batchInsert(list);

		
		System.out.println(System.currentTimeMillis() - t);
		DruidConnectionPool.shutdown();
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
