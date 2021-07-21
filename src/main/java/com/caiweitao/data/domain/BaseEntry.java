package com.caiweitao.data.domain;

/**
 * @author 蔡伟涛
 * @Date 2021年5月8日
 * @Description 大表实体基类
 */
public abstract class BaseEntry {
	private boolean insert;//true:表示需要入库
	
	/**
	 * 将大表标记为需要插入（会执行sql insert语句）
	 */
	public void markInsert () {
		setInsert(true);
	}

	public boolean isInsert() {
		return insert;
	}

	public void setInsert(boolean insert) {
		this.insert = insert;
	}

}
