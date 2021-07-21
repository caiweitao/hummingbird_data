package com.caiweitao.data.test.cache;

import com.caiweitao.data.cache.game.GameCache;
import com.caiweitao.data.db.dao.BaseDao;
import com.caiweitao.data.test.db.DaoFactory;

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
