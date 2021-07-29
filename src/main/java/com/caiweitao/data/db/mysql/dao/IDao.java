package com.caiweitao.data.db.mysql.dao;

import java.util.List;
import java.util.Map;

/**
 * @author 蔡伟涛
 * @Date 2021年5月6日
 * @Description DAO接口,K:转入map时key类型，不是主键类型，T：实体类类型
 */
public interface IDao<K,T> {
	
	public boolean update(T paramT);

	public int[] batchUpdate (List<T> tList);
	
	public boolean insert(T paramT);
	
	public int[] batchInsert (List<T> list);

	public boolean delete(T paramT);
	
	public T selectOne(String sql, Object... paramVarArgs);
	
	public T selectByKey (Object...keys);//支持多个主键的情况
	
	public List<T> selectList(String sql, Object... paramVarArgs);
	
	public List<T> selectListByKey(Object... keys);//支持多个主键的情况
	
	public Map<K, T> selectMap(String sql, Object... paramVarArgs);
	
	public Map<K, T> selectMapByKey(Object... keys);//支持多个主键的情况
	
	public int executeUpdate (String sql, Object... paramVarArgs);
	
	public int[] batchExecuteUpdate (String sql, List<Object[]> list);
}
