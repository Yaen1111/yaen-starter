package org.yaen.starter.common.util.utils;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * id util, generate none-unique id
 * 
 * @author Yaen 2016年2月1日下午8:55:32
 */
public class IdUtil {
	private static final int ATOMIC_INT_MIN = 11111111;
	private static final int ATOMIC_INT_MAX = 88888888;

	/** the atomic integer */
	private static AtomicInteger atomInt = new AtomicInteger(ATOMIC_INT_MIN);

	/** the start time as random */
	private static long startTime = System.currentTimeMillis();

	/**
	 * generate guid by java
	 * 
	 * @return
	 */
	public static String generateUUID() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	/**
	 * generate id with prefix, mostly use [Prefix]yyyyMMddHHmmssSSS, the prefix will be cut to 2 char
	 * 
	 * @param prefix
	 * @return
	 */
	public static String generateId_old(String prefix) {
		String p = StringUtil.trimToEmpty(prefix);
		if (p.length() > 2)
			p = p.substring(0, 2);

		return p + DateUtil.formatDateSeq();
	}

	/**
	 * generate id with prefix, like [Prefix2]yyyyMMddHHmmss[random3][seq3] , the prefix will be cut to 2 char
	 * 
	 * @param prefix
	 * @return
	 */
	public static String generateId(String prefix) {
		String p = StringUtil.trimToEmpty(prefix);
		if (p.length() > 2)
			p = p.substring(0, 2);

		// reset seq if larger than 88888888 to avoid concurrence risk
		int seq = atomInt.getAndIncrement();
		if (seq >= ATOMIC_INT_MAX) {
			atomInt.set(ATOMIC_INT_MIN);
		}

		// prefix + dataseq + seq
		return p + DateUtil.formatDateSeq() + StringUtil.toString(startTime, -3) + StringUtil.toString(seq, -3);
	}

	public static void main(String[] args) {
		for (int i = 0; i < 12000; i++) {
			System.out.println(generateId("AU"));
		}
	}

}
