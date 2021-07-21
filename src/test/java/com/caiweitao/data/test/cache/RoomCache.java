package com.caiweitao.data.test.cache;

import com.caiweitao.data.cache.game.NeedNotSaveGameCache;
import com.caiweitao.data.test.Room;

/**
 * @author 蔡伟涛
 * @Date 2021年5月25日
 * @Description 
 */
public class RoomCache extends NeedNotSaveGameCache<String, Room> {

	public RoomCache(String name) {
		super(name);
	}

	@Override
	public Room without(String playerId) {
		Room r = new Room();
		r.setId(playerId);
		r.setNum(2);
		return r;
	}

}
