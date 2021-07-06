package com.gzyouai.hummingbird.data.config;

public class RedisConfig {
	
	//Redis服务器IP
    public static String ip = "127.0.0.1";
    
    //Redis的端口号
    public static int port = 6379;
    
    //访问密码
    public static String passwd;
    
    //哪个库
    public static int database = 10;
    
    //可用连接实例的最大数目，默认值为8；
    //如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
    public static int maxTotal = 8;
    
    //最小空闲连接数, 默认0
    public static int minIdle = 0;
    
    //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
    //最大空闲连接数, 默认8个
    public static int maxIdle = 8;
    
    //获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间,  默认-1
    //等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
    public static int maxWait = -1;
    
    public static int timeout = 10000;
    
    //连接耗尽时是否阻塞, false报异常,ture阻塞直到超时, 默认true
    public static boolean blockWhenExhausted = false;
    
    //设置的逐出策略类名, 默认DefaultEvictionPolicy(当连接超过最大空闲时间,或连接数超过最大空闲连接数)
    public static String evictionPolicyClassName ="org.apache.commons.pool2.impl.DefaultEvictionPolicy";
    
    //是否启用pool的jmx管理功能, 默认true
    public static boolean jmxEnabled = true;
   
    //是否启用后进先出, 默认true
    public static boolean lifo = true;
    
    //逐出连接的最小空闲时间 默认1800000毫秒(30分钟)
    public static long minEvictableIdleTimeMillis = 1800000L;
    
    //对象空闲多久后逐出, 当空闲时间>该值 且 空闲连接>最大空闲数 时直接逐出,不再根据MinEvictableIdleTimeMillis判断  (默认逐出策略)   
    public static long softMinEvictableIdleTimeMillis = 1800000L;
    
    //每次逐出检查时 逐出的最大数目 如果为负数就是 : 1/abs(n), 默认3
    public static int numTestsPerEvicyionRun = 3;
    
    //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
    //在获取连接的时候检查有效性, 默认false
    public static boolean testOnBorrow = false;
    
    //在空闲时检查有效性, 默认false
    public static boolean testWhileidle = false;
    
    //逐出扫描的时间间隔(毫秒) 如果为负数,则不运行逐出线程, 默认-1
    public static long timeBerwrrnEvictionRunsMillis = -1;
}
