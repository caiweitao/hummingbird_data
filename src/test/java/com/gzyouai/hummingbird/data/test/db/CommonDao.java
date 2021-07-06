package com.gzyouai.hummingbird.data.test.db;

import java.sql.Connection;

import com.gzyouai.hummingbird.data.db.dao.BaseDao;

/**
 * @author 蔡伟涛
 * @Date 2021年5月6日
 * @Description DAO基类
 */
public abstract class CommonDao<K, T> extends BaseDao<K, T> {
	
	@Override
	public Connection getConnection() {
		return ConnectionManager.getGameDbConnection();
	}
	
}
