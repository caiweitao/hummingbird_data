//package com.caiweitao.data.db.mongodb.connection;
//
//import org.bson.Document;
//
//import com.mongodb.MongoClient;
//import com.mongodb.MongoClientOptions;
//import com.mongodb.MongoCredential;
//import com.mongodb.ServerAddress;
//import com.mongodb.client.MongoCollection;
//import com.mongodb.client.MongoDatabase;
//
///**
// * @author caiweitao
// * @Date 2021年7月29日
// * @Description 管理类
// */
//public class MongodbManager {
//	private static MongoClient mongoClient;
//	private static MongoDatabase mongoDatabase;
//
//	public static void init (String configFile) {
//		MongodbConfig mongodbConfig = new MongodbConfig();
//		mongodbConfig.init(configFile, null);
//		init(mongodbConfig);
//	}
//
//	public static void init (MongodbConfig mongodbConfig) {
//		MongoClientOptions.Builder clientOptions = new MongoClientOptions.Builder();
//		clientOptions.connectionsPerHost(mongodbConfig.getMaxConnectionsPerHost());
//		clientOptions.minConnectionsPerHost(mongodbConfig.getMinConnectionsPerHost());
//		clientOptions.threadsAllowedToBlockForConnectionMultiplier(
//				mongodbConfig.getThreadsAllowedToBlockForConnectionMultiplier());
//		
//		ServerAddress serverAddress = new ServerAddress(mongodbConfig.getHost(), mongodbConfig.getPort());
//		if (mongodbConfig.getUsername() != null && mongodbConfig.getPassword() != null) {
//			MongoCredential credential = MongoCredential.createCredential(mongodbConfig.getUsername(), mongodbConfig.getDbName(), mongodbConfig.getPassword().toCharArray());
//			mongoClient = new MongoClient(serverAddress, credential, clientOptions.build());
//		} else {
//			mongoClient = new MongoClient(serverAddress,clientOptions.build());
//		}
//		mongoDatabase = mongoClient.getDatabase(mongodbConfig.getDbName());
//	}
//	
//	public static MongoCollection<Document> getCollection (String collectionName) {
//		return mongoDatabase.getCollection(collectionName);
//	}
//
//	public static MongoDatabase getMongoDatabase() {
//		return mongoDatabase;
//	}
//
//	public static void setMongoDatabase(MongoDatabase mongoDatabase) {
//		MongodbManager.mongoDatabase = mongoDatabase;
//	}
//	
//}
