package com.caiweitao.data.idworker;

import java.util.Date;

/**
 * @author caiweitao
 * @Date 2021年5月11日
 * @Description String类型全局唯一ID生成器（游戏服ID+时间差值+序列号，服务器id字符串+时间差值和序列号拼接成一个long）
 * 相应内容参考https://segmentfault.com/a/1190000011282426
 */
public class GameIdWorker {

	private long serverId;//游戏服ID
	private final long serverIdBase = 10000;
	/**起始时间戳，用当前时间戳减去这个时间戳，算出偏移量（一但确定不能再修改）按这个日期算：2021-01-01 00:00:00 1609430400000L*/
	private final static long twepoch = 1609430400000L;
	private long sequence = 0;//序列号，同一毫秒内生成多个id,由序列化递增区分
	private long sequenceBits;//序列号占用的位数
	private long maxSequence;//序列号最大值
	private long lastTimestamp = -1L;//记录产生时间毫秒数，判断是否是同1毫秒
	
	/**
	 * @param serverId 服务器Id
	 */
	public GameIdWorker(long serverId) {
		this(serverId, 15L);
	}
	
	/**
	 * @param serverId 服务器Id
	 * @param sequenceBits 序列号占用位数,1ms内最多可产生(2^sequenceBits-1)个序列号
	 */
	public GameIdWorker(long serverId,long sequenceBits) {
		if (sequenceBits > 20 || sequenceBits <= 0) {
			throw new IllegalArgumentException(
					String.format("sequence bits can't be greater than %d or less than 0",20));
		}
		if (serverId < 0) {
			throw new IllegalArgumentException(
					String.format("server Id can't be less than 0"));
		}
		this.serverId = serverIdBase+serverId;
		this.sequenceBits = sequenceBits;
		//-1L ^ (-1L << n)用位运算计算n个bit能表示的最大数值
		this.maxSequence = -1L ^ (-1L << sequenceBits);
	}

	/**
	 * 这个是核心方法，通过调用nextId()方法，生成一个全局唯一的id
	 * @return
	 */
	public synchronized String nextId() {
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
			//通过位与运算保证计算的结果范围始终是 0~maxSequence,防止溢出,溢出后归0
			sequence = (sequence + 1) & maxSequence;
			//当某一毫秒的时间，产生的id数 超过maxSequence，系统会进入等待，直到下一毫秒，系统继续产生ID
			if (sequence == 0) {
				timestamp = tilNextMillis(lastTimestamp);
			}
		} else {
			sequence = 0;
		}
		// 记录一下最近一次生成id的时间戳
		lastTimestamp = timestamp;
		// 将对应值移位到对应的位置，拼接起来成一个64 bit的二进制数字，转换成10进制就是个long型
		long timeAndSequence = ((timestamp - twepoch) << sequenceBits) | sequence;
		return String.valueOf(serverId)+timeAndSequence;
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
	public IdInfo analyzeId (String id) {
		int length = String.valueOf(this.serverIdBase).length();
		if (id == null || id.length() < length) {
			return null;
		}
		IdInfo info = new IdInfo();
		String serverIdTemp = id.substring(0,length);
		info.setServerId(Long.parseLong(serverIdTemp)-this.serverIdBase);
		long timeAndSequence = Long.parseLong(id.substring(length));
		//需要取低几位，就先构建一个低几位全是1，其它位全是0的数据，再与原数据做&运算
		long maxBits = 63;//位数
		//序列号
		long sequenceTemp = Long.MAX_VALUE >> (maxBits-sequenceBits);
		info.setSequence(sequenceTemp & timeAndSequence);
		//时间戳
		long notSeqenceModuleServerId = timeAndSequence >> sequenceBits;
		long timeTemp = Long.MAX_VALUE >> sequenceBits;
		long time = timeTemp & notSeqenceModuleServerId;
		info.setDate(new Date(time+twepoch));
		return info;
	}
	
	 //==============================Test=============================================
    /** 测试 */
    public static void main(String[] args) {
    	GameIdWorker idWorker = new GameIdWorker(1001,9);
    	for (int i=0;i<10;i++) {
    		String nextId = idWorker.nextId();
    		System.out.println(nextId);
    		System.out.println(idWorker.analyzeId(nextId));
    	}
    }
    
    /**
	 * @author caiweitao
	 * @Date 2021年5月12日
	 * @Description Id信息
	 */
	public static class IdInfo {
		private Date date;//生成id时间
		private long serverId;//服务器Id
		private long sequence;//序列号
		
		public long getServerId() {
			return serverId;
		}
		public void setServerId(long serverId) {
			this.serverId = serverId;
		}
		public long getSequence() {
			return sequence;
		}
		public void setSequence(long sequence) {
			this.sequence = sequence;
		}
		public Date getDate() {
			return date;
		}
		public void setDate(Date date) {
			this.date = date;
		}
		@Override
		public String toString() {
			return "IdInfo [date=" + date + ", timestmap=" + date.getTime() +  ", serverId=" + serverId + ", sequence=" + sequence + "]";
		}
	}
}
