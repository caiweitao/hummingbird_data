package com.caiweitao.data.db.mysql.dao;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.rowset.serial.SerialBlob;

import com.caiweitao.data.annotation.Except;
import com.caiweitao.data.annotation.PK;
import com.caiweitao.data.annotation.Table;
import com.caiweitao.data.annotation.Tp;
import com.caiweitao.data.db.mysql.connection.ConnectionImpl;
import com.google.gson.Gson;

/**
 * @author 蔡伟涛
 * @Date 2021年5月6日
 * @Description Dao 基类
 */
public abstract class BaseDao<K,T> implements IDao<K,T>, ConnectionImpl{
	private String tableName;//数据库表名
	private List<Field> attributeList;//表所有字段
	private List<Field> pkFieldList;//主键
	private Class<?> clazz;//T的class
	//主键作为条件的基础sql
	private String insertSql;
	private String updateSql;
	private String deleteSql;
	private String selectSql;
	//实体类按字段更新的sql，<字段名，字段对应的更新sql>
	private Map<String,String> fieldUpdateSqlMap;
	
	public BaseDao () {
		//第二个泛型类型(也就是实体对象的类型)
		clazz = (Class<?>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[1];
		Table table = clazz.getAnnotation(Table.class);
		if (table != null && table.tableName() != null && !"".equals(table.tableName())) {
			tableName = table.tableName();
		} else {//默认为实体类名首字母小写做为表名
			String simpleName = clazz.getSimpleName();
			tableName = simpleName.substring(0, 1).toLowerCase() + simpleName.substring(1);
		}
		Field[] declaredFields = clazz.getDeclaredFields();
		
		attributeList = new ArrayList<Field>();
		pkFieldList = new ArrayList<Field>();
		List<Field> notPkFieldList = new ArrayList<Field>();
		for (Field field:declaredFields) {
			if (field.getAnnotation(Except.class) == null) {
				attributeList.add(field);
				if (field.getAnnotation(PK.class) == null) {//非主键
					notPkFieldList.add(field);
				} else {
					pkFieldList.add(field);
				}
			}
		}
		if (pkFieldList.isEmpty()) {
			throw new RuntimeException(clazz.getName()+"没有使用@PK标注表格主键");
		}
		
		//非主键，给每个字段创建update sql 
		fieldUpdateSqlMap = new HashMap<String, String>();
		for (Field field:notPkFieldList) {
			String createFieldUpdateSql = createFieldUpdateSql(field, pkFieldList);
			fieldUpdateSqlMap.put(field.getName(), createFieldUpdateSql);
		}
		insertSql = createInsertSqlMould();
		deleteSql = createDeleteSqlMould();
		updateSql = createUpdateSqlMould();
		selectSql = createSelectSqlMould();

	}
	
	@Override
	public boolean insert(T paramT) {
		Connection conn = getConnection();
		PreparedStatement stmt = null ;
		int executeUpdate = 0;
		try {
			stmt = conn.prepareStatement(insertSql);
			int index = 1;
			for (Field f:attributeList) {
				f.setAccessible(true);
				Object object = f.get(paramT);
				setStatementParam(stmt, index++, object, f);
			}
			executeUpdate = stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} finally {
			closeAll(null, stmt, conn);
		}
		return executeUpdate > 0;
	}
	
	@Override
	public int[] batchInsert (List<T> list) {
		Connection conn = getConnection();
		PreparedStatement stmt = null ;
		try {
			conn.setAutoCommit(false);
			stmt = conn.prepareStatement(insertSql);
			for (T paramT:list) {
				int index = 1;
				for (Field f:attributeList) {
					f.setAccessible(true);
					Object object = f.get(paramT);
					setStatementParam(stmt, index++, object, f);
				}
				stmt.addBatch();
			}
			int[] s = stmt.executeBatch();
			conn.commit();
			return s;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} finally {
			closeAll(null, stmt, conn);
		}
		return new int[0];
	}
	
	@Override
	public boolean update(T paramT) {
		Connection conn = getConnection();
		PreparedStatement stmt = null ;
		int executeUpdate = 0;
		try {
			stmt = conn.prepareStatement(updateSql);
			int index = 1;
			List<Field> pkList = new ArrayList<Field>();
			for (Field f:attributeList) {
				f.setAccessible(true);
				if (f.getAnnotation(PK.class) != null) {
					pkList.add(f);
					continue;
				}
				Object object = f.get(paramT);
				setStatementParam(stmt, index++, object, f);
			}
			//设置主键对应的值
			for (Field f:pkList) {
				Object object = f.get(paramT);
				setStatementParam(stmt, index++, object, f);
			}
			executeUpdate = stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} finally {
			closeAll(null, stmt, conn);
		}
		return executeUpdate > 0;
	}
	
	public int[] batchUpdate (List<T> tList) {
		Connection conn = getConnection();
		PreparedStatement stmt = null ;
		try {
			conn.setAutoCommit(false);
			stmt = conn.prepareStatement(updateSql);
			
			List<Field> notPkList = new ArrayList<Field>();//非主键字段
			List<Field> pkList = new ArrayList<Field>();//主键字段
			for (Field f:attributeList) {
				f.setAccessible(true);
				if (f.getAnnotation(PK.class) != null) {
					pkList.add(f);
				} else {
					notPkList.add(f);
				}
			}
			//设置参数值
			for (T paramT:tList) {
				int index = 1;
				for (Field f:notPkList) {
					Object object = f.get(paramT);
					setStatementParam(stmt, index++, object, f);
				}
				for (Field f:pkList) {
					Object object = f.get(paramT);
					setStatementParam(stmt, index++, object, f);
				}
				stmt.addBatch();
			}
			int[] s = stmt.executeBatch();
			conn.commit();
			return s;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} finally {
			closeAll(null, stmt, conn);
		}
		return new int[0];
	}
	
	@Override
	public boolean delete(T paramT) {
		Connection conn = getConnection();
		PreparedStatement stmt = null ;
		int executeUpdate = 0;
		try {
			stmt = conn.prepareStatement(deleteSql);
			int index = 1;
			for (Field f:attributeList) {
				f.setAccessible(true);
				if (f.getAnnotation(PK.class) != null) {
					Object object = f.get(paramT);
					setStatementParam(stmt, index++, object, f);
				}
			}
			executeUpdate = stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} finally {
			closeAll(null, stmt, conn);
		}
		return executeUpdate > 0;
	}
	
	@Override
	public T selectByKey(Object...k) {
		return selectOne(selectSql, k);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T selectOne(String sql, Object... paramVarArgs) {
		Connection conn = getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(sql);
			int index = 1;
			for (Object o:paramVarArgs) {
				setStatementParam(stmt, index++, o, null);
			}
			rs = stmt.executeQuery();
			if (rs.next()) {
				Object newInstance = clazz.newInstance();
				for (Field f:attributeList) {
					f.setAccessible(true);
					setModelValue(f, newInstance, rs);
				}
				return (T) newInstance;
			}
		} catch (Exception e) {
			e.printStackTrace();
			exeSqlError(sql, e);
		} finally {
			closeAll(rs, stmt, conn);
		}
		return null;
	}
	
	//paramVarArgs如果对应的数据库类型为datetime,则必须把long时间戳转成new java.sql.Timestamp(时间戳)
	@SuppressWarnings("unchecked")
	@Override
	public List<T> selectList(String sql, Object... paramVarArgs) {
		Connection conn = getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<T> list = new ArrayList<T>();
		try {
			stmt = conn.prepareStatement(sql);
			int index = 1;
			for (Object o:paramVarArgs) {
				setStatementParam(stmt, index++, o, null);
			}
			rs = stmt.executeQuery();
			while (rs.next()) {
				Object newInstance = clazz.newInstance();
				for (Field f:attributeList) {
					f.setAccessible(true);
					setModelValue(f, newInstance, rs);
				}
				list.add((T) newInstance);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			exeSqlError(sql, e);
		} finally {
			closeAll(rs, stmt, conn);
		}
		return null;
	}
	
	@Override
	public List<T> selectListByKey(Object... keys) {
		return selectList(selectSql, keys);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<K, T> selectMap(String sql, Object... paramVarArgs) {
		List<T> list = selectList(sql, paramVarArgs);
		Map<K,T> map = new ConcurrentHashMap<K, T>();
		Field pkFiled = pkFieldList.get(0);
		for (T t:list) {
			try {
				map.put((K)pkFiled.get(t), t);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return map;
	}
	
	@Override
	public Map<K, T> selectMapByKey(Object... keys) {
		return selectMap(selectSql, keys);
	}
	
	/**
	 * 执行删除、更新、插入语句
	 */
	@Override
	public int executeUpdate(String sql, Object... paramVarArgs) {
		Connection conn = getConnection();
		PreparedStatement stmt = null ;
		int executeUpdate = 0;
		try {
			stmt = conn.prepareStatement(sql);
			int index = 1;
			for (Object o:paramVarArgs) {
				setStatementParam(stmt, index++, o, null);
			}
			executeUpdate = stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeAll(null, stmt, conn);
		}
		return executeUpdate;
	}
	
	@Override
	public int[] batchExecuteUpdate(String sql, List<Object[]> list) {
		Connection conn = getConnection();
		PreparedStatement stmt = null ;
		try {
			conn.setAutoCommit(false);
			stmt = conn.prepareStatement(sql);
			if (list != null) {
				for (Object[] oList:list) {//一条记录
					int index = 1;
					for (Object o:oList) {//一条记录中所有参数
						setStatementParam(stmt, index++, o, null);
					}
					stmt.addBatch();
				}
			}
			int[] executeBatch = stmt.executeBatch();
			conn.commit();
			return executeBatch;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeAll(null, stmt, conn);
		}
		return new int[0];
	}
	
	/**
	 * 根据字段名批量更新数据
	 * @param fieldName
	 * @param list
	 * @return
	 */
	public int[] batchUpdateByFieldName (String fieldName,List<Object[]> list) {
		String sql = fieldUpdateSqlMap.get(fieldName);
		Objects.requireNonNull(sql);
		Connection conn = getConnection();
		PreparedStatement stmt = null ;
		try {
			conn.setAutoCommit(false);
			stmt = conn.prepareStatement(sql);
			for (Object[] oList:list) {//一条记录
				int index = 1;
				for (Object o:oList) {//一条记录中所有参数
					setStatementParam(stmt, index++, o, null);
				}
				stmt.addBatch();
			}
			int[] executeBatch = stmt.executeBatch();
			conn.commit();
			return executeBatch;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeAll(null, stmt, conn);
		}
		return new int[0];
	}
	
	public int[] batchSql(List<String> sqlList) {
		Connection conn = getConnection();
		Statement stmt = null ;
		try {
			conn.setAutoCommit(false);
			stmt = conn.createStatement();
			if (sqlList != null) {
				for (String sql:sqlList) {
					stmt.addBatch(sql);
				}
			}
			int[] executeBatch = stmt.executeBatch();
			conn.commit();
			return executeBatch;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeAll(null, stmt, conn);
		}
		return new int[0];
	}
	
	/**
	 * 使用ResultSet给对象某个属性设置值
	 * @param f 属性
	 * @param obj 对象
	 * @param rs 
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws SQLException
	 */
	protected void setModelValue (Field f, Object obj, ResultSet rs) throws IllegalArgumentException, IllegalAccessException, SQLException {
		Class<?> fclass = f.getType();
		if (fclass == int.class || fclass == Integer.class) {
			f.setInt(obj, rs.getInt(f.getName()));
		} else if (fclass == long.class || fclass == Long.class) {
			if (f.getAnnotation(Tp.class) != null) {
				f.setLong(obj, rs.getTimestamp(f.getName()).getTime());
			} else {
				f.setLong(obj, rs.getLong(f.getName()));
			}
		} else if (fclass == float.class || fclass == Float.class) {
			f.setFloat(obj, rs.getFloat(f.getName()));
		} else if (fclass == double.class || fclass == Double.class) {
			f.setDouble(obj, rs.getDouble(f.getName()));
		} else if (fclass == boolean.class || fclass == Boolean.class) {
			f.setBoolean(obj, rs.getBoolean(f.getName()));
		} else if (fclass == short.class || fclass == Short.class) {
			f.setShort(obj, rs.getShort(f.getName()));
		} else if (fclass == byte.class || fclass == Byte.class) {
			f.setByte(obj, rs.getByte(f.getName()));
		} else if (fclass == byte[].class) {
			Blob blob = rs.getBlob(f.getName());
			f.set(obj, blob.getBytes(1, (int) (blob.length())));
		} else if (fclass == String.class) {
			f.set(obj, rs.getString(f.getName()));
		}
		else {
			f.set(obj, new Gson().fromJson(rs.getString(f.getName()), fclass));
		}
	}
	
	/**
	 * 设置PreparedStatement参数
	 * @param stmt
	 * @param index 参数位置，1，2...
	 * @param object 参数值
	 * @param f 属性(可能为空)
	 * @throws SQLException 
	 */
	private void setStatementParam (PreparedStatement stmt, int index, Object object,Field f) throws SQLException {
		if (stmt == null)
			return;
		
		if (object instanceof Integer) {
			stmt.setInt(index, (Integer)object);
		} else if (object instanceof Long) {
			if (f != null) {
				if (f.getAnnotation(Tp.class) != null) {
					stmt.setTimestamp(index, new java.sql.Timestamp((Long)object));
				} else {
					stmt.setLong(index, (Long)object);
				}
			} else {
				stmt.setLong(index, (Long)object);
			}
		} else if (object instanceof Float) {
			stmt.setFloat(index, (Float)object);
		} else if (object instanceof Double) {
			stmt.setDouble(index, (Double)object);
		} else if (object instanceof Boolean) {
			stmt.setBoolean(index, (Boolean)object);
		} else if (object instanceof Short) {
			stmt.setShort(index, (Short)object);
		} else if (object instanceof Byte) {
			stmt.setByte(index, (Byte)object);
		}else if (object instanceof String) {
			stmt.setString(index, (String)object);
		} else if (object instanceof Timestamp) {
			stmt.setTimestamp(index, (Timestamp)object);
		} else if (object instanceof byte[]) {
			stmt.setBlob(index, new SerialBlob((byte[])object));
		} else {
			stmt.setString(index, new Gson().toJson(object));
		}
	}
	
	public void closeAll(ResultSet rs, Statement stmt, Connection conn) {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (Exception e) {
			closeError("ResultSet", e);
		}
		try {
			if (stmt != null) {
				stmt.close();
			}
		} catch (Exception e) {
			closeError("Statement", e);
		}
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (Exception e) {
			closeError("Connection", e);
		}
	}
	
	protected void closeError(String name, Throwable throwable) {
		System.out.println(String.format("关闭%s异常%s", name,throwable));
		throwable.printStackTrace();
	}
	
	protected void exeSqlError(String sql, Throwable throwable) {
		System.out.println(String.format("执行SQL [%s] 错误，%s", sql,throwable.toString()));
		throwable.printStackTrace();
	}

	/**
	 * 获得insert sql模板 :INSERT INTO (字段名1，字段名2...) VALUES (?,?...)
	 * @param tableName
	 * @param insertMap
	 * @return
	 */
	private String createInsertSqlMould () {
		StringBuilder sb = new StringBuilder();
		StringBuilder mouldTemp = new StringBuilder();
		sb.append("INSERT INTO ").append(tableName).append(" (");
		mouldTemp.append(" VALUES (");
		for (Field field:attributeList) {
			sb.append(field.getName()).append(",");
			mouldTemp.append("?,");
		}
		sb.replace(sb.length()-1, sb.length(), ")");
		mouldTemp.replace(mouldTemp.length()-1, sb.length(), ")");
		sb.append(mouldTemp);
		return sb.toString();
	}
	
	private String createDeleteSqlMould () {
		StringBuilder sb = new StringBuilder();
		sb.append("DELETE FROM ").append(tableName).append(" WHERE ");
		for (Field field:attributeList) {
			PK pk = field.getAnnotation(PK.class);
			if (pk != null) {
				sb.append(field.getName()).append("=? AND ");
			}
		}
		return sb.substring(0, sb.lastIndexOf("AND"));
	}
	
	private String createUpdateSqlMould () {
		StringBuilder sb = new StringBuilder();
		sb.append("UPDATE ").append(tableName).append(" SET ");
		StringBuilder pkSb = new StringBuilder();
		pkSb.append("WHERE ");
		for (Field field:attributeList) {
			PK pk = field.getAnnotation(PK.class);
			if (pk == null) {
				sb.append(field.getName()).append("=?,");
			} else {
				//主键
				pkSb.append(field.getName()).append("=? AND ");
			}
		}
		int lastIndexOf = sb.lastIndexOf(",");
		if (lastIndexOf >= 0) {
			sb.replace(lastIndexOf, sb.length(), " ");
		}
		sb.append(pkSb.substring(0, pkSb.lastIndexOf("AND")));
		return sb.toString();
	}
	
	private String createSelectSqlMould () {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT * FROM ").append(tableName).append(" WHERE ");
		for (Field f:attributeList) {
			if (f.getAnnotation(PK.class) != null) {
				sb.append(f.getName()).append("=? AND ");
			}
		}
		return sb.substring(0, sb.lastIndexOf("AND "));
	}
	
	private String createFieldUpdateSql (Field field,List<Field> pkList) {
		StringBuilder sb = new StringBuilder();
		sb.append("UPDATE ")
		.append(tableName)
		.append(" SET ")
		.append(field.getName())
		.append("=?")
		.append(" WHERE ");
		for (Field pkField:pkList) {
			sb.append(pkField.getName())
			.append("=?")
			.append(" AND ");
		}
		return sb.substring(0, sb.lastIndexOf("AND "));
	}

	/**
	 * 将t放入map
	 * @param map
	 * @param t
	 */
//	public abstract void modelMap(Map<K,T> map,T t);

	public List<Field> getNotPkFieldList() {
		List<Field> notPkFieldList = new ArrayList<Field>();
		for (Field f:attributeList) {
			if (f.getAnnotation(PK.class) == null) {
				notPkFieldList.add(f);
			}
		}
		return notPkFieldList;
	}
}
