package com.caiweitao.data.cache.redis;

import java.util.Set;

import com.google.gson.Gson;

import redis.clients.jedis.Jedis;

public abstract class RedisMap<K,V> {
	private long maxCapacity;
	private Class<V> vClass;
	private static final String redisMapKeyPrefix = "redis-cache-map-key-";
	private static final String redisZsetKeyPrefix = "redis-cache-zset-key-";
	private String redis_map_key;
	private String redis_zset_key;
	
	public RedisMap (long maxCapacity, String cacheName, Class<V> vClass) {
		this.maxCapacity = maxCapacity;
		this.vClass = vClass;
		redis_map_key = redisMapKeyPrefix+cacheName;
		redis_zset_key = redisZsetKeyPrefix+cacheName;
	}
	
	public Set<String> put (K key,V v) {
		String keyStr = key.toString();
		Jedis jedis = null;
		try {
			jedis = RedisManager.getJedis();
			jedis.zadd(redis_zset_key, System.currentTimeMillis(), keyStr);
			jedis.hset(redis_map_key, keyStr, new Gson().toJson(v));
			Long hlen = jedis.hlen(redis_map_key);
			//缓存数量超上限处理
			Set<String> remove = null;
			if (hlen > maxCapacity) {
				remove = jedis.zrange(redis_zset_key, 0, hlen-maxCapacity-1);
				for (String oldKey:remove) {
					String oldValStr = jedis.hget(redis_map_key, oldKey);
					V oldVal = new Gson().fromJson(oldValStr, vClass);
					if (canDelete(oldVal)) {
						jedis.zrem(redis_zset_key, oldKey);
						jedis.hdel(redis_map_key, oldKey);
					}
				}
			}
			return remove;
		} finally {
			jedis.close();
		}
	}
	
	public V get(K key) {
		String keyStr = key.toString();
		Jedis jedis = null;
		try {
			jedis = RedisManager.getJedis();
			String val = jedis.hget(redis_map_key, keyStr);
			if (val != null && val.length() > 0) {
				jedis.zadd(redis_zset_key, System.currentTimeMillis(), keyStr);
				return new Gson().fromJson(val, vClass);
			} else {
				return null;
			}
		} finally {
			jedis.close();
		}
	}
	
	public V hget (String key) {
		Jedis jedis = null;
		try {
			jedis = RedisManager.getJedis();
			String hget = jedis.hget(redis_map_key, key);
			return new Gson().fromJson(hget, vClass);
		} finally {
			jedis.close();
		}
	}
	
	public void hset(K key,V v) {
		String keyStr = key.toString();
		Jedis jedis = null;
		try {
			jedis = RedisManager.getJedis();
			jedis.hset(redis_map_key, keyStr, new Gson().toJson(v));
		} finally {
			jedis.close();
		}
	}
	
	public long size () {
		Jedis jedis = null;
		try {
			jedis = RedisManager.getJedis();
			return jedis.hlen(redis_map_key);
		} finally {
			jedis.close();
		}
	}
	
	public Set<String> getKeys () {
		Jedis jedis = null;
		try {
			jedis = RedisManager.getJedis();
			return jedis.hkeys(redis_map_key);
		} finally {
			jedis.close();
		}
	}
	
	public boolean containKey (K k) {
		Jedis jedis = null;
		try {
			jedis = RedisManager.getJedis();
			return jedis.hexists(redis_map_key, k.toString());
		} finally {
			jedis.close();
		}
	}
	
	public static void clear () {
		Jedis jedis = null;
		try {
			jedis = RedisManager.getJedis();
			Set<String> mapKeys = jedis.keys(redisMapKeyPrefix+"*");
			long delMapNum = jedis.del(mapKeys.toArray(new String[mapKeys.size()]));
			Set<String> setKeys = jedis.keys(redisZsetKeyPrefix+"*");
			long delSetNum = jedis.del(setKeys.toArray(new String[setKeys.size()]));
			System.out.println(String.format("del redis cache mapkey:%s,setKey:%s", delMapNum,delSetNum));
		} finally {
			jedis.close();
		}
	}
	
//	public List<V> getallValueList (String redisKey) {
//		Jedis jedis = null;
//		List<V> list = new ArrayList<V>();
//		try {
//			jedis = RedisManager.getJedis();
//			Map<String, String> allMap = jedis.hgetAll(redisKey);
//			Gson gson = new Gson();
//			for (String strVal:allMap.values()) {
//				list.add(gson.fromJson(strVal, vClass));
//			}
//		} finally {
//			jedis.close();
//		}
//		return list;
//	}
	
	protected abstract boolean canDelete (V v);
	
}
