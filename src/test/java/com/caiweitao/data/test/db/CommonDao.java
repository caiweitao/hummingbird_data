package com.caiweitao.data.test.db;

import java.sql.Connection;

import com.caiweitao.data.db.mysql.dao.BaseDao;

/**
 * @author caiweitao
 * @Date 2021年5月6日
 * @Description DAO基类
 */
public abstract class CommonDao<K, T> extends BaseDao<K, T> {
	
	@Override
	public Connection getConnection() {
		return ConnectionManager.getGameDbConnection();
	}
	
}
