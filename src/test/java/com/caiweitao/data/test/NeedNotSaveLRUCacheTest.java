package com.caiweitao.data.test;

import java.io.File;

import com.caiweitao.data.db.connection.DruidConnectionPool;
import com.caiweitao.data.test.cache.CacheFactory;
import com.caiweitao.data.test.db.DBTest;

/**
 * @author 蔡伟涛
 * @Date 2021年5月25日
 * @Description 
 */
public class NeedNotSaveLRUCacheTest {

	public static void main(String[] args) {
		DruidConnectionPool.init(getRootPath(), "config/game_db.properties");
		
		for (int i=0;i<100;i++) {
			Room room = CacheFactory.roomCache.get(i+"");
		}
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(CacheFactory.roomCache.toString());
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
