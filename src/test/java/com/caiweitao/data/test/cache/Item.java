package com.caiweitao.data.test.cache;

/**
 * @author 蔡伟涛
 * @Date 2021年4月27日
 * @Description 
 */
public class Item{
	private int refId; //资源id
	private int number; //数量
	
	public Item(int refId, int number) {
		super();
		this.refId = refId;
		this.number = number;
	}
	public int getRefId() {
		return refId;
	}
	public void setRefId(int refId) {
		this.refId = refId;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	@Override
	public String toString() {
		return "Item [playerId="  + ", refId=" + refId + ", number=" + number + "]";
	}
	
	
}
