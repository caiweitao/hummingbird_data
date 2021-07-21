package com.caiweitao.data.idworker;

import java.util.Date;

/**
 * @author 蔡伟涛
 * @Date 2021年5月14日
 * @Description String类型，全局唯一id（服务器id+时间戳+业务id+序列号 ）
 */
public class StringIdWorker {
	private String serverId;//服务器id
	private String moduleId;//业务模块id
	private int sequence = 1;//序列号
//	private final static int serverIdBits = 6;//serverId占用几个字符串个数
	private final static int moduleIdBits = 4;//moduleId占用几个字符串个数
	private final static int sequenceBits = 6;//sequence占用几个字符串个数
	private final static int timeBits = 13;//时间戳占用几个字符串个数
	private final static int maxServerId = 999999;
	private final static int maxModuleId = 9999;
	private final static int maxSequence = 999999;
	private long lastTimestamp = -1L;//记录产生时间毫秒数，判断是否是同1毫秒
	
	public StringIdWorker(int serverId, int moduleId) {
		// 检查机房id和机器id是否超过31 不能小于0
		if (moduleId > maxModuleId || moduleId < 0) {
			throw new IllegalArgumentException(
					String.format("module Id can't be greater than %d or less than 0",maxModuleId));
		}
		if (serverId > maxServerId || serverId < 0) {
			throw new IllegalArgumentException(
					String.format("server Id can't be greater than %d or less than 0",maxServerId));
		}
		//int转String,不够位数前面补0：String.format("%要补充的字符+字符串长度d", int数字对应d);
		this.serverId = String.valueOf(serverId);
		this.moduleId = String.format("%0"+moduleIdBits+"d",moduleId);
	}

	public synchronized String nextId () {
		long timestamp = timeGen();
		if (timestamp < lastTimestamp) {
			System.err.printf(
					"clock is moving backwards. Rejecting requests until %d.", lastTimestamp);
			throw new RuntimeException(
					String.format("Clock moved backwards. Refusing to generate id for %d milliseconds",
							lastTimestamp - timestamp));
		}
		// 同一个毫秒内得把seqence序号递增1
		if (lastTimestamp == timestamp) {
			sequence += 1;
			//当某一毫秒的时间，产生的id数 超过maxSequence，系统会进入等待，直到下一毫秒，系统继续产生ID
			if (sequence >= maxSequence) {
				timestamp = tilNextMillis(lastTimestamp);
				sequence = 1;
			}
		} else {
			sequence = 1;
		}
		// 记录一下最近一次生成id的时间戳
		lastTimestamp = timestamp;
		StringBuilder sb = new StringBuilder();
		sb.append(serverId).append(timestamp).append(moduleId).append(String.format("%0"+sequenceBits+"d",sequence));
		return sb.toString();
	}
	
	/**
	 * 当某一毫秒的时间，产生的id数超过序列号最大值，系统会进入等待，直到下一毫秒，系统继续产生ID
	 * @param lastTimestamp
	 * @return
	 */
	private long tilNextMillis(long lastTimestamp) {
		long timestamp = timeGen();
		while (timestamp <= lastTimestamp) {
			timestamp = timeGen();
		}
		return timestamp;
	}
	//获取当前时间戳
	private long timeGen(){
		return System.currentTimeMillis();
	}
	
	/**
	 * 解析id
	 * @param id
	 * @return
	 */
	public static IdInfo analyzeId (String id) {
		IdInfo info = new IdInfo();
		int length = id.length();
		if (length < (1 + timeBits + moduleIdBits + sequenceBits)) {
			System.err.println("id format error");
			return info;
		}
		info.setSequence(Integer.parseInt(id.substring(length-sequenceBits, length)));
		info.setModuleId(Integer.parseInt(id.substring(length-sequenceBits-moduleIdBits, length-sequenceBits)));
		long time = Long.parseLong(id.substring(length-sequenceBits-moduleIdBits-timeBits, length-sequenceBits-moduleIdBits));
		info.setDate(new Date(time));
		info.setServerId(Integer.parseInt(id.substring(0, length-sequenceBits-moduleIdBits-timeBits)));
		return info;
	}
	
	/**
	 * @author 蔡伟涛
	 * @Date 2021年5月12日
	 * @Description Id信息
	 */
	public static class IdInfo {
		private Date date;//生成id时间
		private int serverId;//服务器Id
		private int moduleId;//业务模块Id
		private int sequence;//序列号
		
		public Date getDate() {
			return date;
		}
		public void setDate(Date date) {
			this.date = date;
		}
		public int getServerId() {
			return serverId;
		}
		public void setServerId(int serverId) {
			this.serverId = serverId;
		}
		public int getModuleId() {
			return moduleId;
		}
		public void setModuleId(int moduleId) {
			this.moduleId = moduleId;
		}
		public int getSequence() {
			return sequence;
		}
		public void setSequence(int sequence) {
			this.sequence = sequence;
		}
		@Override
		public String toString() {
			return "Id [date=" + date + ", serverId=" + serverId + ", moduleId=" + moduleId + ", sequence=" + sequence
					+ ",time=]";
		}
		
	}
	
	public static void main(String[] args) {
//		10010116209767662979999000158
		StringIdWorker w = new StringIdWorker(100101,9999);
		long t = System.currentTimeMillis();
		for (int i=0;i<99999;i++) {
			System.out.println(w.nextId());
		}
		System.out.println(System.currentTimeMillis() - t);
//		System.out.println(StringIdWorker.analyzeId("16209767662979999000158"));
	}
}
