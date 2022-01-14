package com.caiweitao.data.test.cache;
//
//import java.util.HashMap;
//import java.util.Map;
//
//
///**
// * @author caiweitao
// * @Date 2021年4月27日
// * @Description 
// */
//public class ItemCache extends GameCache<Integer, Map<Integer,Item>> {
//
//	public ItemCache(String name) {
//		super(name);
//	}
//
//	@Override
//	public boolean mark(Map<Integer,Item> itemMap) {
//		for (Item item:itemMap.values()) {
//			if (item.isMark()) {
//				return true;
//			}
//		}
//		return false;
//	}
//
//	@Override
//	public Map<Integer,Item> without(Integer playerId) {
//		Map<Integer,Item> map = new HashMap<Integer, Item>();
//		map.put(1, new Item(playerId, 1, 1));
//		map.put(2, new Item(playerId, 2, 2));
//		return map;
//	}
//
//	@Override
//	public void asyncSaveChangeData(Map<Integer, Item> value) {
//		CacheFactory.itemCache.toString();
//		System.out.println("33333");
//		get(6);
//		System.out.println("xx "+value);
//	}
//
//
//}
