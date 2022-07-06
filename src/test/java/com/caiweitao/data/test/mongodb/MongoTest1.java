//package com.caiweitao.data.test.mongodb;
//
//import java.io.File;
//
//import org.bson.BsonReader;
//import org.bson.BsonWriter;
//import org.bson.Document;
//import org.bson.codecs.Codec;
//import org.bson.codecs.DecoderContext;
//import org.bson.codecs.EncoderContext;
//import org.bson.codecs.configuration.CodecRegistries;
//
//import com.caiweitao.data.db.mongodb.connection.MongodbManager;
//import com.caiweitao.data.test.cache.PlayerInfo;
//import com.google.gson.Gson;
//import com.mongodb.client.MongoCollection;
//import com.mongodb.client.model.Filters;
//
///**
// * @author caiweitao
// * @Date 2021年7月29日
// * @Description 
// */
//public class MongoTest1 {
//	static String rootPath;
//
//	public static void main(String[] args) {
//		initRootPath();
//		MongodbManager.init(rootPath + "config/game-db.properties");
//		System.out.println(MongodbManager.getMongoDatabase());
//		System.out.println(MongodbManager.getMongoDatabase().getName());
//		
////		CodecProvider pojoCodecProvider = PojoCodecProvider.builder().register(Player.class).build();
////		CodecRegistry pojoCodecRegistry = fromRegistries(fromCodecs(new StringCodec(), new ObjectIdCodec(), new BsonArrayCodec()), fromProviders(pojoCodecProvider));
////		//对Collection中的类型设置Codec
////		MongoCollection<DiagnoseDocument> collection = database.getCollection("soar", DiagnoseDocument.class).withCodecRegistry(pojoCodecRegistry);
////		collection.insertOne(diagnoseDocument);
//		
//		
////		MongoCollection<Player> collection = MongodbManager.getMongoDatabase().getCollection("player",Player.class);
////		Document document = new Document();
////		document.put("playerId", "123456");
////		document.put("age", 15);
////		document.put("name", "哈哈哈");
////		Player player = new Player();
////		player.setPlayerId(123);
////		player.setPlayerInfo(new PlayerInfo(123));
////		player.setItems(new Items(123));
////		collection.insertOne(player);
//		
////		MongoCollection<User> collection = getCollection("user");//MongodbManager.getMongoDatabase().getCollection("user",User.class);
////		User u = new User();
////		u.setPlayerId(789);
////		u.setName("张6");
////		u.setMax(true);
////		u.setTime(System.currentTimeMillis());
////		u.setPlayerInfo(new PlayerInfo(123));
////		collection.insertOne(u);
//		
//		UserDao userDao = new UserDao();
//		User one = userDao.getOne(Filters.eq("playerId", 123));
//		System.out.println(one);
//		User u = new User();
////		u.setPlayerId(123);
////		u.setName("张1");
////		u.setMax(true);
////		u.setTime(System.currentTimeMillis());
////		u.setPlayerInfo(new PlayerInfo(123));
////		userDao.insert(u);
//	}
//	
//	public static MongoCollection<User> getCollection (String name) {
//		return MongodbManager.getMongoDatabase().getCollection(name)
//				.withDocumentClass(User.class)
//				.withCodecRegistry(CodecRegistries.fromCodecs(new UserCodec()));
//	}
//	
//	public static void initRootPath() {
//		rootPath = new File(MongoTest1.class.getResource("/").getFile()).getPath().replace("%20", " ") + "/";
//
//		// 不打成jar包运行时,Main.class.getResource("/") 会有 com路径
//		String com = "classes/com/";
//		if (rootPath.endsWith(com)) {
//			rootPath = rootPath.substring(0, rootPath.length() - com.length());
//		}
//	}
//	
//}
//class UserCodec implements Codec<User> {
//
//	@Override
//	public void encode(BsonWriter writer, User value, EncoderContext encoderContext) {
////		writer.writeStartDocument();
////		writer.writedo
//////        writer.writeInt32("playerId", value.getPlayerId());  //Line 27 in the error messag
//////        writer.writeString("name", value.getName());
//////        writer.writeBoolean("max", value.isMax());
//////        writer.writeInt64("time", value.getTime());
////        writer.writeEndDocument();
//	}
//
//	@Override
//	public Class<User> getEncoderClass() {
//		return User.class;
//	}
//
//	@Override
//	public User decode(BsonReader reader, DecoderContext decoderContext) {
//		User user = new User();
//		reader.readStartDocument();
//		user.setPlayerId(reader.readInt32("playerId"));
//		user.setName(reader.readString("name"));
//		user.setMax(reader.readBoolean("max"));
//		user.setTime(reader.readInt64("time"));
//		reader.readEndDocument();
//		return user;
//	}
//	
//}
