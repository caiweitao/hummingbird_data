package com.caiweitao.data.test;

import java.io.File;

import com.caiweitao.data.db.connection.DruidConnectionPool;
import com.caiweitao.data.test.cache.CacheFactory;
import com.caiweitao.data.test.db.DBTest;

/**
 * @author 蔡伟涛
 * @Date 2021年5月24日
 * @Description 
 */
public class CommonCacheTest {

	public static void main(String[] args) {
		DruidConnectionPool.init(getRootPath(), "config/game_db.properties");
		
		Equip equip = CacheFactory.equipCache.get("2");
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (equip == null) {
			equip = new Equip();
			equip.setPlayerId("2");
			equip.setRefId(1);
			equip.setNum(22);
			CacheFactory.equipCache.insert(equip);
		}
		equip.setNum(5657);
//		CacheFactory.equipCache.update(equip);
		
		while (true){
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
//			System.out.println("insert:"+equip.isInsert()+"  update:"+equip.isMark());
		}

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
