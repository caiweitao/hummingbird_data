package com.gzyouai.hummingbird.data.test;
/**
 * @author 蔡伟涛
 * @Date 2021年5月25日
 * @Description 
 */
public class Room {

	private String id;
	private int num;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	@Override
	public String toString() {
		return "Room [id=" + id + ", num=" + num + "]";
	}
	
}
