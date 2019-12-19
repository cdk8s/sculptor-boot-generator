package com.cdk8s.code.gen.util.randomobject;

import org.apache.commons.lang3.RandomUtils;

import java.util.Date;
import java.util.Random;

public class SelfUtils {

	private static Random random = new Random();

	private static char[] ch = {'A', 'B', 'C', 'D', 'E', 'F', 'G',
			'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b',
			'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
			'x', 'y', 'z'};

	public static String getRamdomString(int length) {
		//        char[] ch = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G',
		//                'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y',
		// 'Z', 'a', 'b',
		//                'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
		// 'u', 'v', 'w',
		//                'x', 'y', 'z', 'o', '1'};
		if (length <= 0) {
			length = ch.length;
		}
		char[] chars = new char[length];
		for (int i = 0; i < length; i++) {
			chars[i] = ch[random.nextInt(ch.length)];
		}
		return new String(chars);
	}

	public static int getIntValue() {
		return RandomUtils.nextInt();
	}

	public static byte getByteValue() {
		return (byte) (random.nextInt() & 0xEF);
	}

	public static long getLongValue() {
		return RandomUtils.nextLong(1538838377000L, 1544108777000L);
	}

	public static short getShortValue() {
		return (short) (random.nextInt() & 0xEFFF);
	}

	public static float getFloatValue() {
		return RandomUtils.nextFloat();
	}

	public static double getDoubleValue() {
		return RandomUtils.nextDouble();
	}

	public static char getCharValue() {

		return ch[random.nextInt(ch.length)];
	}

	public static boolean getBooleanValue() {

		return random.nextInt() % 2 == 0;
	}

	public static Date getDateValue() {
		return new Date();
	}
}
