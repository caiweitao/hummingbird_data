package com.caiweitao.data.test.db;

import java.sql.Connection;

import com.caiweitao.data.db.connection.DruidConnectionPool;


/**
 * @author 蔡伟涛
 * @Date 2021年5月6日
 * @Description 连接管理
 */
public class ConnectionManager {
	public static final String GAMEDB_NAME = "game_db";
	public static final String LOGDB_NAME = "log_db";
	
	public static Connection getGameDbConnection() {
		return DruidConnectionPool.getDbConnection(GAMEDB_NAME);
	}

	public static Connection getLogDbConnection() {
		return DruidConnectionPool.getDbConnection(LOGDB_NAME);
	}
}
