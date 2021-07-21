package com.caiweitao.data.cache.redis;

import java.util.Properties;

import com.caiweitao.data.config.RedisConfig;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author caiweitao
 * @Date 2020年5月21日
 * @Description redis 资源管理类
 */
public class RedisManager {

	private static JedisPool jedisPool = null; 
	
	private RedisManager () {}
	
	/**
	 * @param path redis配置文件路径
	 */
	public synchronized static void init () {
		if (jedisPool != null) {
			return;
		}
		try {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setBlockWhenExhausted(RedisConfig.blockWhenExhausted);
            config.setEvictionPolicyClassName(RedisConfig.evictionPolicyClassName); 
            config.setJmxEnabled(RedisConfig.jmxEnabled);
            config.setLifo(RedisConfig.lifo);
            config.setMaxIdle(RedisConfig.maxIdle);
            config.setMaxTotal(RedisConfig.maxTotal);
            config.setMaxWaitMillis(RedisConfig.maxWait);
            config.setMinEvictableIdleTimeMillis(RedisConfig.minEvictableIdleTimeMillis);
            config.setMinIdle(RedisConfig.minIdle);
            config.setNumTestsPerEvictionRun(RedisConfig.numTestsPerEvicyionRun);
            config.setSoftMinEvictableIdleTimeMillis(RedisConfig.softMinEvictableIdleTimeMillis);
            config.setTestOnBorrow(RedisConfig.testOnBorrow);
            config.setTestWhileIdle(RedisConfig.testWhileidle);
            config.setTimeBetweenEvictionRunsMillis(RedisConfig.timeBerwrrnEvictionRunsMillis);
            
            jedisPool = new JedisPool(config, RedisConfig.ip, RedisConfig.port, RedisConfig.timeout, RedisConfig.passwd,RedisConfig.database);
        
            System.out.println("初始化JedisPool >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		} catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	/**
     * 获取Jedis实例
     * @return
     */
    public static Jedis getJedis() {
        try {
        	if (jedisPool == null) {
        		init();
        	}
        	return jedisPool.getResource();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public synchronized static void shutdown () {
    	if (jedisPool != null) {
    		jedisPool.close();
    	}
    }
    
    public static void initRedisConfig (Properties properties) {
    	Object ip = properties.get("redis.ip");
    	if (ip != null) {
    		RedisConfig.ip = ip.toString();
    	}
    	Object port = properties.get("redis.port");
    	if (port != null) {
    		RedisConfig.port = Integer.parseInt(port.toString());
    	}
    	Object passwd = properties.get("redis.passwd");
    	if (passwd != null) {
    		RedisConfig.passwd = passwd.toString();
    	}
    	Object database = properties.get("redis.database");
    	if (database != null) {
    		RedisConfig.database = Integer.parseInt(database.toString());
    	}
    	Object maxTotal = properties.get("redis.maxTotal");
    	if (maxTotal != null) {
    		RedisConfig.maxTotal = Integer.parseInt(maxTotal.toString());
    	}
    	Object minIdle = properties.get("redis.minIdle");
    	if (minIdle != null) {
    		RedisConfig.minIdle = Integer.parseInt(minIdle.toString());
    	}
    	Object maxIdle = properties.get("redis.maxIdle");
    	if (maxIdle != null) {
    		RedisConfig.maxIdle = Integer.parseInt(maxIdle.toString());
    	}
    	Object maxWait = properties.get("redis.maxWait");
    	if (maxWait != null) {
    		RedisConfig.maxWait = Integer.parseInt(maxWait.toString());
    	}
    	Object timeout = properties.get("redis.timeout");
    	if (timeout != null) {
    		RedisConfig.timeout = Integer.parseInt(timeout.toString());
    	}
    	Object blockWhenExhausted = properties.get("redis.blockWhenExhausted");
    	if (blockWhenExhausted != null) {
    		RedisConfig.blockWhenExhausted = Boolean.parseBoolean(blockWhenExhausted.toString());
    	}
    	Object evictionPolicyClassName = properties.get("redis.evictionPolicyClassName");
    	if (evictionPolicyClassName != null) {
    		RedisConfig.evictionPolicyClassName = evictionPolicyClassName.toString();
    	}
    	Object jmxEnabled = properties.get("redis.jmxEnabled");
    	if (jmxEnabled != null) {
    		RedisConfig.jmxEnabled = Boolean.parseBoolean(jmxEnabled.toString());
    	}
    	Object lifo = properties.get("redis.lifo");
    	if (lifo != null) {
    		RedisConfig.lifo = Boolean.parseBoolean(lifo.toString());
    	}
    	Object minEvictableIdleTimeMillis = properties.get("redis.minEvictableIdleTimeMillis");
    	if (minEvictableIdleTimeMillis != null) {
    		RedisConfig.minEvictableIdleTimeMillis = Long.parseLong(minEvictableIdleTimeMillis.toString());
    	}
    	Object softMinEvictableIdleTimeMillis = properties.get("redis.softMinEvictableIdleTimeMillis");
    	if (softMinEvictableIdleTimeMillis != null) {
    		RedisConfig.softMinEvictableIdleTimeMillis = Long.parseLong(softMinEvictableIdleTimeMillis.toString());
    	}
    	Object numTestsPerEvicyionRun = properties.get("redis.numTestsPerEvicyionRun");
    	if (numTestsPerEvicyionRun != null) {
    		RedisConfig.numTestsPerEvicyionRun = Integer.parseInt(numTestsPerEvicyionRun.toString());
    	}
    	Object testOnBorrow = properties.get("redis.testOnBorrow");
    	if (testOnBorrow != null) {
    		RedisConfig.testOnBorrow = Boolean.parseBoolean(testOnBorrow.toString());
    	}
    	Object testWhileidle = properties.get("redis.testWhileidle");
    	if (testWhileidle != null) {
    		RedisConfig.testWhileidle = Boolean.parseBoolean(testWhileidle.toString());
    	}
    	Object timeBerwrrnEvictionRunsMillis = properties.get("redis.timeBerwrrnEvictionRunsMillis");
    	if (timeBerwrrnEvictionRunsMillis != null) {
    		RedisConfig.timeBerwrrnEvictionRunsMillis = Long.parseLong(timeBerwrrnEvictionRunsMillis.toString());
    	}
    }
}
