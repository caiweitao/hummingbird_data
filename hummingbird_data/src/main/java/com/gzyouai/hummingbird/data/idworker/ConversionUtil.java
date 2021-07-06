package com.gzyouai.hummingbird.data.idworker;

import java.math.BigInteger;

/**
 * @author 蔡伟涛
 * @Date 2021年6月8日
 * @Description 进制转换
 */
public class ConversionUtil {
	/**
	 * 初始化 62 进制数据，索引位置代表字符的数值，比如 a代表10，Z代表61等
	 */
	private static String chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static BigInteger scale = new BigInteger(String.valueOf(62));

	/**
	 * 将数字字符串转为62进制（字符串必须由数字构成，不能出现字符）
	 * @param id
	 * @return 62进制字符串
	 */
	public static String encode(String id) {
		StringBuilder sb = new StringBuilder();
		BigInteger remainder = null;
		BigInteger num = new BigInteger(id);
		while (num.compareTo((scale.subtract(new BigInteger(String.valueOf(1))))) > 0) {
			//对 scale 进行求余，然后将余数追加至 sb 中，由于是从末位开始追加的，因此最后需要反转（reverse）字符串
			remainder = num.remainder(scale);
			sb.append(chars.charAt(remainder.intValue()));

			num = num.divide(scale);
		}
		sb.append(chars.charAt(num.intValue()));
		return sb.reverse().toString();
	}

	/**
	 * 62进制字符串转为数字
	 *
	 * @param str 编码后的62进制字符串
	 * @return 解码后的 10 进制字符串
	 */
	public static String decode(String str) {
		BigInteger num = new BigInteger(String.valueOf(0));
		BigInteger index = null;
		for (int i = 0; i < str.length(); i++) {
			//查找字符的索引位置
			index = new BigInteger(String.valueOf(chars.indexOf(str.charAt(i))));
			//索引位置代表字符的数值
			BigInteger multiply = index.multiply(scale.pow(str.length() - i - 1));
			num = num.add(multiply);
		}
		return num.toString();
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("62进制：" + encode("100116231215272830001000001"));
		System.out.println("10进制：" + decode("84vBqa6qEOenIf8"));
	}
}
