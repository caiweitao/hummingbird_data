package com.caiweitao.data.test;

import com.caiweitao.data.annotation.PK;
import com.caiweitao.data.domain.BaseEntry;
import com.caiweitao.data.domain.CommonBaseEntry;

/**
 * @author 蔡伟涛
 * @Date 2021年5月24日
 * @Description 
 */
public class Equip extends CommonBaseEntry {
	@PK
	private String playerId;
	private int refId;
	private int num;
	
	public String getPlayerId() {
		return playerId;
	}
	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}
	public int getRefId() {
		return refId;
	}
	public void setRefId(int refId) {
		this.refId = refId;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	
}
