package com.cdk8s.code.gen.util.encryption;


import com.cdk8s.code.gen.util.StringUtil;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * AES 对称加密算法
 * 来自：http://www.wenhq.com/article/view_716.html
 * <p>
 * 衍生资料:
 * AES算法有3个等级,16字节的key对应AES-128,24字节的key对应AES-192,32字节的key对应AES-256.然后有5种加密模式,
 * CBC，CFB，ECB，OFB，PCBC.然后还有无数种补码填充模式,如NoPadding，PKCS5Padding，PKCS7Padding，ISO10126Padding…
 * (from:http://www.sudochina.com/insion/blog/189/)
 * <p>
 * http://www.cnblogs.com/arix04/archive/2009/10/15/1511839.html
 * http://my.oschina.net/Jacker/blog/86383（AES加密CBC模式兼容互通四种编程语言平台【PHP、Javascript、Java、C#】）
 */


public final class AESUtil {

	//使用CBC模式，需要一个向量iv，可增加加密算法的强度(长度16)
	private static final String IV_PARAMETER = "012A34BC567E89DF";

	/**
	 * 加密
	 *
	 * @param string 被加密字符串
	 * @param key    密钥，此处使用AES-128-CBC加密模式，key需要为16位长度（字母和数字组成），最好不要用保留字符，虽然不会错
	 * @return
	 */
	public static String encrypt(String string, String key) {

		if (StringUtil.isBlank(string) || StringUtil.isBlank(key)) {
			return null;
		}

		if (key.length() != 16) {
			return null;
		}

		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");//"算法/模式/补码方式"
			byte[] raw = key.getBytes();
			SecretKeySpec secretKeySpec = new SecretKeySpec(raw, "AES");
			IvParameterSpec iv = new IvParameterSpec(IV_PARAMETER.getBytes());//使用CBC模式，需要一个向量iv，可增加加密算法的强度
			cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, iv);
			byte[] encrypted = cipher.doFinal(string.getBytes("UTF-8"));
			return new BASE64Encoder().encode(encrypted);//此处使用BASE64做转码。
			//return StringUtil.stringToBase64StringByGuava(new String(encrypted));//此处使用Guava的BASE64做转码,会报错的,base64的出来的结果比上面一种方式长很多
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解密
	 *
	 * @param string 被解密字符串
	 * @param key    密钥，此处使用AES-128-CBC加密模式，key需要为16位长度（字母和数字组成），最好不要用保留字符，虽然不会错
	 * @return
	 */
	public static String decrypt(String string, String key) {
		if (StringUtil.isBlank(string) || StringUtil.isBlank(key)) {
			return null;
		}

		if (key.length() != 16) {
			return null;
		}

		byte[] raw = new byte[0];
		try {
			raw = key.getBytes("ASCII");
			SecretKeySpec secretKeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");//"算法/模式/补码方式"
			IvParameterSpec iv = new IvParameterSpec(IV_PARAMETER.getBytes());
			cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, iv);

			byte[] encrypted1 = new BASE64Decoder().decodeBuffer(string);//先用base64解密
			//byte[] encrypted1 = StringUtil.base64StringToSourceStringByGuava(string).getBytes();//使用guava的base64解密,会报错的

			byte[] original = cipher.doFinal(encrypted1);
			String originalString = new String(original, "UTF-8");
			return originalString;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}


}
