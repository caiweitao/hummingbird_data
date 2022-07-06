//package com.caiweitao.data.db.mongodb.connection;
//
//import java.util.Objects;
//import java.util.Properties;
//
//import com.caiweitao.data.common.PropertiesUtil;
//
///**
// * @author caiweitao
// * @Date 2021年7月29日
// * @Description mongodb配置
// */
//public class MongodbConfig {
//	private String host;
//	private int port;
//	private String dbName;
//	private String username;
//	private String password;
//	private int minConnectionsPerHost;
//	private int maxConnectionsPerHost = 10;
//	private int threadsAllowedToBlockForConnectionMultiplier = 5;
//	private int connectTimeout = 10000;
//	private int maxWaitTime = 120000;
//	
//	public void init(String file, String prefix) {
//		Properties properties = PropertiesUtil.getProperties(file);
//		Object hostObj = properties.get("host");
//		Objects.requireNonNull(hostObj);
//		this.host = hostObj.toString();
//		Object port = properties.get("port");
//		Objects.requireNonNull(port);
//		this.port = Integer.parseInt(String.valueOf(port));
//		Object dbName = properties.get("dbName");
//		Objects.requireNonNull(dbName);
//		this.dbName = dbName.toString();
//		Object usernameObj = properties.get("username");
//		if (usernameObj != null) {
//			this.username = usernameObj.toString();
//		}
//		Object passwordObj = properties.get("password");
//		if (passwordObj != null) {
//			this.password = passwordObj.toString();
//		}
//		Object minConnectionsPerHostObj = properties.get("minConnectionsPerHost");
//		if (minConnectionsPerHostObj != null) {
//			this.minConnectionsPerHost = Integer.parseInt(String.valueOf(minConnectionsPerHostObj));
//		}
//		Object maxConnectionsPerHostObj = properties.get("maxConnectionsPerHost");
//		if (maxConnectionsPerHostObj != null) {
//			this.maxConnectionsPerHost = Integer.parseInt(String.valueOf(maxConnectionsPerHostObj));
//		}
//		Object threadsAllowedToBlockForConnectionMultiplierObj = properties.get("threadsAllowedToBlockForConnectionMultiplier");
//		if (threadsAllowedToBlockForConnectionMultiplierObj != null) {
//			this.threadsAllowedToBlockForConnectionMultiplier = Integer.parseInt(String.valueOf(threadsAllowedToBlockForConnectionMultiplierObj));
//		}
//		Object connectTimeoutObj = properties.get("connectTimeout");
//		if (connectTimeoutObj != null) {
//			this.connectTimeout = Integer.parseInt(String.valueOf(connectTimeout));
//		}
//		Object maxWaitTimeObj = properties.get("maxWaitTime");
//		if (maxWaitTimeObj != null) {
//			this.maxWaitTime = Integer.parseInt(String.valueOf(maxWaitTimeObj));
//		}
//	}
//	
//	public String getHost() {
//		return host;
//	}
//	public void setHost(String host) {
//		this.host = host;
//	}
//	public int getPort() {
//		return port;
//	}
//	public void setPort(int port) {
//		this.port = port;
//	}
//	public String getDbName() {
//		return dbName;
//	}
//	public void setDbName(String dbName) {
//		this.dbName = dbName;
//	}
//	public String getUsername() {
//		return username;
//	}
//	public void setUsername(String username) {
//		this.username = username;
//	}
//	public String getPassword() {
//		return password;
//	}
//	public void setPassword(String password) {
//		this.password = password;
//	}
//	public int getMinConnectionsPerHost() {
//		return minConnectionsPerHost;
//	}
//	public void setMinConnectionsPerHost(int minConnectionsPerHost) {
//		this.minConnectionsPerHost = minConnectionsPerHost;
//	}
//	public int getMaxConnectionsPerHost() {
//		return maxConnectionsPerHost;
//	}
//	public void setMaxConnectionsPerHost(int maxConnectionsPerHost) {
//		this.maxConnectionsPerHost = maxConnectionsPerHost;
//	}
//	public int getThreadsAllowedToBlockForConnectionMultiplier() {
//		return threadsAllowedToBlockForConnectionMultiplier;
//	}
//	public void setThreadsAllowedToBlockForConnectionMultiplier(int threadsAllowedToBlockForConnectionMultiplier) {
//		this.threadsAllowedToBlockForConnectionMultiplier = threadsAllowedToBlockForConnectionMultiplier;
//	}
//	public int getConnectTimeout() {
//		return connectTimeout;
//	}
//	public void setConnectTimeout(int connectTimeout) {
//		this.connectTimeout = connectTimeout;
//	}
//	public int getMaxWaitTime() {
//		return maxWaitTime;
//	}
//	public void setMaxWaitTime(int maxWaitTime) {
//		this.maxWaitTime = maxWaitTime;
//	}
//	
//}
