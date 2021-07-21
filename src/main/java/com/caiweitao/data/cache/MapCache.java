package com.caiweitao.data.cache;
//package com.gzyouai.hummingbird.data.cache;
//import java.lang.reflect.Field;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//import com.gzyouai.hummingbird.data.annotation.Except;
//import com.gzyouai.hummingbird.data.annotation.PK;
//import com.gzyouai.hummingbird.data.db.dao.BaseDao;
//import com.gzyouai.hummingbird.data.domain.BaseEntry;
//import com.gzyouai.hummingbird.data.domain.CommonBaseEntry;
//import com.gzyouai.hummingbird.data.domain.MarkableEntry;
///**
// * @author 蔡伟涛
// * @Date 2021年5月29日
// * @Description Cache<cahceKey,map<mapkey,实体对象>>
// */
//public abstract class MapCache<K,V extends Map<K2,T>,K2,T extends CommonBaseEntry> implements ICache<K, V> {
//	protected Class<?> clazz;
//	protected int maxCapacity;
//	protected List<Field> notPkFieldList;//V的非主键、并且需要入库的成员(没有用@PK和@Except标注的成员)
//	protected Field pkField, markField,markTrueTimeField,
//	insertField,commonBaseEntryField,commonBaseEntryMarkTrueField;//缓存主键field、markField
//
//	/**
//	 * V的Class
//	 * @param clazz
//	 */
//	public MapCache(Class<?> clazz) {
//		this.clazz = clazz;
//		try {
//			boolean common = false;
//			if (CommonBaseEntry.class.isAssignableFrom(clazz)) {
//				common = true;
//				commonBaseEntryField = CommonBaseEntry.class.getDeclaredField("mark");
//				commonBaseEntryField.setAccessible(true);
//				commonBaseEntryMarkTrueField = CommonBaseEntry.class.getDeclaredField("markTrueTime");
//				commonBaseEntryMarkTrueField.setAccessible(true);
//			} 
//			notPkFieldList = new ArrayList<Field>();
//			Field[] declaredFields = clazz.getDeclaredFields();
//			for (Field field:declaredFields) {
//				if (field.getAnnotation(Except.class) == null) {
//					field.setAccessible(true);
//					if (field.getAnnotation(PK.class) == null) {
//						if (!common && !MarkableEntry.class.isAssignableFrom(field.getType())) {
//							throw new RuntimeException(String.format("普通表（非大表模式）实体必须继承CommonBaseEntry，特殊表（大表模式）必须继承BaseEntry，【%s】成员变量【%s】没有继承MarkableEntry",
//									clazz.getName(), field.getName()));
//						}
//						notPkFieldList.add(field);
//					} else if (pkField == null) {//用第一个主键作为缓存key
//						pkField = field;
//					}
//				}
//			}
//			markField = MarkableEntry.class.getDeclaredField("mark");
//			markField.setAccessible(true);
//			markTrueTimeField = MarkableEntry.class.getDeclaredField("markTrueTime");
//			markTrueTimeField.setAccessible(true);
//			insertField = BaseEntry.class.getDeclaredField("insert");
//			insertField.setAccessible(true);
//		} catch (NoSuchFieldException e) {
//			e.printStackTrace();
//		} catch (SecurityException e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * value是否做了持久化标记（需要持久化），只要V中有一个字段修改过就返回true
//	 * @param value
//	 * @return true:已更新未保存到数据库
//	 */
//	protected boolean isMark(V vMap) {
//		try {
//			for (T value:vMap.values()) {
//				boolean insertMark = insertField.getBoolean(value);
//				if (insertMark) {
//					return true;
//				}
//				for (Field field:notPkFieldList) {
//					Object entryFieldValue;
//					entryFieldValue = field.get(value);
//					if (markField.getBoolean(entryFieldValue)) {
//						System.out.println("updateMark>>>>>>>>>>>>>>"+field.getName());
//						return true;
//					}
//				}
//			}
//		} catch (IllegalArgumentException e) {
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			e.printStackTrace();
//		}
//		return false;
//	}
//	
//	@Override
//	public void clear() {
//		// TODO Auto-generated method stub
//		
//	}
//	
//	public abstract BaseDao<K,T> dao ();
//	
//	/**
//	 * 将缓存数据保存到数据库
//	 */
//	public abstract void saveToDB ();
//}
