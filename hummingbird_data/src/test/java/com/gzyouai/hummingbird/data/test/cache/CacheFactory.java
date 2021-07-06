package com.gzyouai.hummingbird.data.test.cache;


/**
 * @author 蔡伟涛
 * @Date 2021年4月27日
 * @Description 
 */
public class CacheFactory {

	public static final PlayerCache playerCache = new PlayerCache("playerCache");
	public static final EquipCache equipCache = new EquipCache("equip");
	public static final RoomCache roomCache = new RoomCache("room");
//	public static final ItemCache itemCache = new ItemCache("itemCache");
	
	
}
