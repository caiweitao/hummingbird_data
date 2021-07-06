package com.gzyouai.hummingbird.data.test.cache;

import com.gzyouai.hummingbird.data.cache.GameCache;
import com.gzyouai.hummingbird.data.db.dao.BaseDao;
import com.gzyouai.hummingbird.data.test.db.DaoFactory;

/**
 * @author 蔡伟涛
 * @Date 2021年4月28日
 * @Description 
 */
public class PlayerCache extends GameCache<Integer, Player> {

	public PlayerCache(String name) {
		super(name);
	}

	@Override
	public Player without(Integer playerId) {
		return getDao().selectByKey(playerId);
	}

	@Override
	public BaseDao<Integer,Player> getDao() {
		return DaoFactory.playerDao;
	}
}
