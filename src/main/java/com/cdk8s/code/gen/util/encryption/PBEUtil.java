package com.cdk8s.code.gen.util.encryption;


import com.cdk8s.code.gen.util.code.CodecUtil;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.security.Key;
import java.security.SecureRandom;

/**
 * PBE 比 AES 和 DES 更好一点
 */
public final class PBEUtil {


	//PBE 支持的其他算法具体看网络资料，这个是最常用的
	public static final String ALGORITHM = "PBEWithMD5AndDES";//加密算法
	public static final String SALT = "59866524";//盐（需要8位数）

	/**
	 * 定义迭代次数为1000次
	 */
	private static final int ITERATION_COUNT = 1000;

	/**
	 * 获取加密算法中使用的盐值,解密中使用的盐值必须与加密中使用的相同才能完成操作. 盐长度必须为8字节
	 *
	 * @return byte[] 盐值
	 */
	public static byte[] getSalt() throws Exception {
		// 实例化安全随机数
		SecureRandom random = new SecureRandom();
		// 产出盐
		return random.generateSeed(8);
	}

	/**
	 * 自己设置的常量值的盐，如果按上面随机生成的话，在解密的时候如果没有记下来，就解密不了
	 *
	 * @return
	 */
	public static byte[] getConstantSalt() {
		// 产出盐
		return SALT.getBytes();
	}

	/**
	 * 根据PBE密码生成一把密钥
	 *
	 * @param password 生成密钥时所使用的密码（可以把它理解为是一个秘钥KEY）
	 * @return Key PBE算法密钥
	 */
	private static Key getPBEKey(String password) {
		// 实例化使用的算法
		SecretKeyFactory secretKeyFactory;
		Key secretKey = null;
		try {
			secretKeyFactory = SecretKeyFactory.getInstance(ALGORITHM);
			// 设置PBE密钥参数
			PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray());
			// 生成密钥
			secretKey = secretKeyFactory.generateSecret(pbeKeySpec);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return secretKey;
	}

	/**
	 * 加密明文字符串
	 *
	 * @param plainText 待加密的明文字符串
	 * @param password  生成密钥时所使用的密码
	 * @param salt      盐值
	 * @return 加密后的密文字符串（被base64转换过）
	 * @throws Exception
	 */
	public static String encrypt(String plainText, String password, byte[] salt) {

		if (null == salt) {
			salt = getConstantSalt();
		}

		Key key = getPBEKey(password);
		byte[] encipheredData = null;
		PBEParameterSpec parameterSpec = new PBEParameterSpec(salt, ITERATION_COUNT);
		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM);

			cipher.init(Cipher.ENCRYPT_MODE, key, parameterSpec);

			encipheredData = cipher.doFinal(plainText.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return CodecUtil.byteToBase64StringByGuava(encipheredData);
	}

	/**
	 * 解密密文字符串
	 *
	 * @param cipherText 待解密的密文字符串（被base64转换过）
	 * @param password   生成密钥时所使用的密码(如需解密,该参数需要与加密时使用的一致)
	 * @param salt       盐值(如需解密,该参数需要与加密时使用的一致)
	 * @return 解密后的明文字符串
	 * @throws Exception
	 */
	public static String decrypt(String cipherText, String password, byte[] salt) {

		if (null == salt) {
			salt = getConstantSalt();
		}

		Key key = getPBEKey(password);
		byte[] passDec = null;
		PBEParameterSpec parameterSpec = new PBEParameterSpec(getConstantSalt(), ITERATION_COUNT);
		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, key, parameterSpec);
			passDec = cipher.doFinal(CodecUtil.base64StringToSourceByteByGuava(cipherText));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new String(passDec);
	}


	public static void main(String[] args) {
		try {
			System.out.println("--------------------------------" + encrypt("123456", "aaa1", null));
			System.out.println("--------------------------------" + decrypt("7m4jyZ6+bGA=", "aaa1", null));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
