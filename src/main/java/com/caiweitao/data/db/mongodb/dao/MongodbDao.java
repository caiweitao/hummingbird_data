//package com.caiweitao.data.db.mongodb.dao;
//
//import java.lang.reflect.ParameterizedType;
//import java.util.List;
//import java.util.Map;
//
//import org.bson.Document;
//import org.bson.conversions.Bson;
//
//import com.caiweitao.data.db.mongodb.connection.MongodbManager;
//import com.caiweitao.data.db.mysql.dao.IDao;
//import com.google.gson.Gson;
//import com.mongodb.client.FindIterable;
//import com.mongodb.client.MongoCollection;
//
///**
// * @author caiweitao
// * @Date 2021年8月3日
// * @Description 
// */
//public class MongodbDao<K,T> implements IDao<K,T>{
//	protected MongoCollection<Document> collection;
//	protected Class<T> clazz;
//	
//	@SuppressWarnings("unchecked")
//	public MongodbDao (String collectionName) {
//		this.collection = MongodbManager.getCollection(collectionName);
//		this.clazz = (Class<T>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[1];
//	}
//
//	@Override
//	public boolean insert(T t) {
//		Document d = Document.parse(new Gson().toJson(t));
//		collection.insertOne(d);
//		return true;
//	}
//	
//	public T getOne (Bson filter) {
//		Document first = collection.find(filter).first();
//		System.out.println(first.toJson());
//		return new Gson().fromJson(first.toJson(), this.clazz);
//	}
//
//	@Override
//	public boolean update(T paramT) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public int[] batchUpdate(List<T> tList) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public int[] batchInsert(List<T> list) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public boolean delete(T paramT) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public T selectOne(String sql, Object... paramVarArgs) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public T selectByKey(Object... keys) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public List<T> selectList(String sql, Object... paramVarArgs) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public List<T> selectListByKey(Object... keys) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Map<K, T> selectMap(String sql, Object... paramVarArgs) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Map<K, T> selectMapByKey(Object... keys) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public int executeUpdate(String sql, Object... paramVarArgs) {
//		// TODO Auto-generated method stub
//		return 0;
//	}
//
//	@Override
//	public int[] batchExecuteUpdate(String sql, List<Object[]> list) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//	
//	
//}
