package com.caiweitao.data.cache;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

import com.caiweitao.data.annotation.Except;
import com.caiweitao.data.annotation.PK;
import com.caiweitao.data.db.mysql.dao.BaseDao;
import com.caiweitao.data.domain.BaseEntry;
import com.caiweitao.data.domain.CommonBaseEntry;
import com.caiweitao.data.domain.MarkableEntry;

/**
 * @author 蔡伟涛
 * @Date 2021年5月13日
 * @Description 定时回写入库缓存
 */
public abstract class Cache<K, V> implements ICache<K, V>{
	protected ReentrantLock reentrantLock = new ReentrantLock();
	protected ConcurrentHashMap<K, Object> getDataLockMap = new ConcurrentHashMap<K, Object>();
	
	public Class<?> clazz;
	protected int maxCapacity;
	public List<Field> notPkFieldList;//V的非主键、并且需要入库的成员(没有用@PK和@Except标注的成员)
	public Field pkField, markField,markTrueTimeField,
	insertField,commonBaseEntryField,commonBaseEntryMarkTrueField;//缓存主键field、markField

	/**
	 * V的Class
	 * @param clazz
	 */
	public Cache(Class<?> clazz) {
		this.clazz = clazz;
		try {
			boolean common = false;
			if (CommonBaseEntry.class.isAssignableFrom(clazz)) {
				common = true;
				commonBaseEntryField = CommonBaseEntry.class.getDeclaredField("mark");
				commonBaseEntryField.setAccessible(true);
				commonBaseEntryMarkTrueField = CommonBaseEntry.class.getDeclaredField("markTrueTime");
				commonBaseEntryMarkTrueField.setAccessible(true);
			} 
			notPkFieldList = new ArrayList<Field>();
			Field[] declaredFields = clazz.getDeclaredFields();
			for (Field field:declaredFields) {
				if (field.getAnnotation(Except.class) == null) {
					field.setAccessible(true);
					if (field.getAnnotation(PK.class) == null) {
						if (!common && !MarkableEntry.class.isAssignableFrom(field.getType())) {
							throw new RuntimeException(String.format("普通表（非大表模式）实体必须继承CommonBaseEntry，特殊表（大表模式）必须继承BaseEntry，【%s】成员变量【%s】没有继承MarkableEntry",
									clazz.getName(), field.getName()));
						}
						notPkFieldList.add(field);
					} else if (pkField == null) {//用第一个主键作为缓存key
						pkField = field;
					}
				}
			}
			markField = MarkableEntry.class.getDeclaredField("mark");
			markField.setAccessible(true);
			markTrueTimeField = MarkableEntry.class.getDeclaredField("markTrueTime");
			markTrueTimeField.setAccessible(true);
			insertField = BaseEntry.class.getDeclaredField("insert");
			insertField.setAccessible(true);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
	}

	/**
	 * value是否做了持久化标记（需要持久化），只要V中有一个字段修改过就返回true
	 * @param value
	 * @return true:已更新未保存到数据库
	 */
	protected boolean isMark(V value) {
		try {
			boolean insertMark = insertField.getBoolean(value);
			if (insertMark) {
				return true;
			}
			for (Field field:notPkFieldList) {
				Object fieldValue = field.get(value);
				AtomicBoolean mark = (AtomicBoolean)markField.get(fieldValue);
				if (mark.get()) {
					return true;
				}
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 获得锁
	 * @param key
	 * @return
	 */
	public Object getDataLock(K key) {
		Object value = getDataLockMap.get(key);
		if (value == null) {
			reentrantLock.lock();
			try {
				value = getDataLockMap.get(key);
				if (value == null) {
					value = new Object();
				}
				getDataLockMap.put(key, value);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				reentrantLock.unlock();
			}
		}
		return value;
	}
	
	public abstract BaseDao<K,V> dao ();
	
	/**
	 * 将缓存数据保存到数据库
	 */
	public abstract void saveToDB ();
	
}
