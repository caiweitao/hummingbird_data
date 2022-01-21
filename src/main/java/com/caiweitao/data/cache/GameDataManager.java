package com.caiweitao.data.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.caiweitao.data.cache.redis.RedisManager;
import com.caiweitao.data.cache.redis.RedisMap;
import com.caiweitao.data.common.CommonDaemonThreadFactory;
import com.caiweitao.data.common.PropertiesUtil;
import com.caiweitao.data.config.CacheConfig;

/**
 * @author caiweitao
 * @Date 2021年4月27日
 * @Description 游戏数据管理者
 */
public class GameDataManager {

	private static ScheduledExecutorService scheduledExecutorService;
	private static Map<String, Cache<?, ?>> cacheMap = new HashMap<String, Cache<?,?>>();//所有缓存对象（需要保存到数据库）
	private static Map<String,ICache<?,?>> neetNotSaveCache = new HashMap<>();//不需要保存到数据库的缓存
	
	/**
	 * 初始化缓存配置
	 * @param cacheConfigPath 缓存配置文件路径,可为null
	 * @param redisConfigPath redis配置文件路径,如果使用redis作为缓存类型，必须调用此方法初始化 
	 */
	public static void init (String cacheConfigPath) {
		if (cacheConfigPath != null) {
			Properties properties = PropertiesUtil.getProperties(cacheConfigPath);
			Object cacheType = properties.get("cacheType");
			if (cacheType != null) {
				CacheConfig.cacheType = Integer.parseInt(cacheType.toString());
				System.out.println(String.format("【CacheConfig.cacheType=%s】", CacheConfig.cacheType));
			}
			Object maxElementsInMemory = properties.get("maxElementsInMemory");
			if (maxElementsInMemory != null) {
				CacheConfig.maxElementsInMemory = Integer.parseInt(maxElementsInMemory.toString());
				System.out.println(String.format("【CacheConfig.maxElementsInMemory=%s】", CacheConfig.maxElementsInMemory));
			}
			Object savePeriod = properties.get("savePeriod");
			if (savePeriod != null) {
				CacheConfig.savePeriod = Integer.parseInt(savePeriod.toString());
				System.out.println(String.format("【CacheConfig.savePeriod=%s毫秒】", CacheConfig.savePeriod));
			}
			Object startThread = properties.get("startThread");
			if (startThread != null) {
				CacheConfig.startThread = Boolean.parseBoolean(startThread.toString());
				System.out.println(String.format("【CacheConfig.startThread=%s】", CacheConfig.startThread));
			}
			//如果缓存类型为redis
			if (CacheConfig.cacheType == CacheConfig.CACHE_TYPE_LRU_REDIS) {
				RedisManager.initRedisConfig(properties);
				RedisManager.init();
			}
		}
		//启动线程执行数据持久化操作
		if (CacheConfig.startThread) {
			startCacheToDBThread();
		}
	}
	
	private static final void startCacheToDBThread() {
		if (scheduledExecutorService == null) {
			scheduledExecutorService = Executors
					.newSingleThreadScheduledExecutor();
			//scheduleAtFixedRate:计时过去后，检测上一个任务是否执行完毕，如果上一个任务执行完毕，则当前任务立即执行，如果上一个任务没有执行完毕，则需要等上一个任务执行完毕后立即执行。
			scheduledExecutorService.scheduleAtFixedRate(()->{
				Cache<?, ?> cache = null;
				try {
					System.out.println("检查缓存数据持久化线程执行.....");
					for (Map.Entry<String, Cache<?, ?>> entry:cacheMap.entrySet()) {
						cache = entry.getValue();
						if (cache != null) {
							cache.saveToDB();
						}
					}
				} catch (Exception e) {
					if (cache != null) {
						System.out.println(String.format("GameDataManager持久化异常【%s】", cache.clazz.getName()));
					} else {
						System.out.println("GameDataManager持久化异常");
					}
					e.printStackTrace();
				}
			}, 3*60*1000L, CacheConfig.savePeriod, TimeUnit.MILLISECONDS);
		}
	}
	
	public static void register(String name, Cache<?, ?> cache) {
		cacheMap.put(name, cache);
	}
	
	public static void register (String name,ICache<?,?> cache) {
		neetNotSaveCache.put(name, cache);
	}
	
	public static void saveToDB () {
		for (Map.Entry<String, Cache<?, ?>> entry : cacheMap.entrySet()) {
			Cache<?, ?> cache = entry.getValue();
			if (cache != null) {
				cache.saveToDB();
			}
		}
	}
	
	public static void clear () {
		if (CacheConfig.cacheType == CacheConfig.CACHE_TYPE_LRU_REDIS) {
			RedisMap.clear();
		}
	}

	/**
	 * 关服时必须调用，数据入库等操作
	 */
	public static void shutdown () {
		System.out.println("GameDataManager.shutdown()..........");
		if (scheduledExecutorService != null) {
			scheduledExecutorService.shutdown();
		}
		for (Map.Entry<String, ICache<?, ?>> entry : neetNotSaveCache.entrySet()) {
			ICache<?, ?> cache = entry.getValue();
			if (cache != null) {
				cache.shutdown();
			}
		}
		for (Map.Entry<String, Cache<?, ?>> entry : cacheMap.entrySet()) {
			Cache<?, ?> cache = entry.getValue();
			if (cache != null) {
				cache.shutdown();
			}
		}
		if (CacheConfig.cacheType == CacheConfig.CACHE_TYPE_LRU_REDIS) {
			RedisManager.shutdown();
		}
	}
}
