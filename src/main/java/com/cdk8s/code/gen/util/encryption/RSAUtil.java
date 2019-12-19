package com.cdk8s.code.gen.util.encryption;


import com.cdk8s.code.gen.util.code.CodecUtil;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * RSA 非对称加密，只能用来类似 MD5 进行比对，没办法把加密后内容还原
 * 公开密钥与私有密钥是一对，如果用公开密钥对数据进行加密，只有用对应的私有密钥才能解密；
 * 如果用私有密钥对数据进行加密，那么只有用对应的公开密钥才能解密。因为加密和解密使用的是两个不同的密钥，所以这种算法叫作非对称加密算法。
 * 非对称加密算法的保密性比较好，它消除了最终用户交换密钥的需要，但加密和解密花费时间长、速度慢，它不适合于对文件加密而只适用于对少量数据进行加密。
 */
public final class RSAUtil {
	public static KeyPairGenerator keyPairGenerator = null;
	public static Signature signature = null;
	public static KeyFactory keyFactory = null;

	//这两个是为了随机生成下面两个私钥和公钥，生成之后记住赋值给：rsaPrivateKeyString和rsaPublicKeyString
	//public static RSAPublicKey rsaPublicKey = null;
	//public static RSAPrivateKey rsaPrivateKey = null;

	//私钥
	public static final String rsaPrivateKeyString = "MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAywrIxENBGYjQldetOhfLokMlz3VkSPoLemozhVnwLScq7IP4nE4ePWK+uIZbeZPlSfEeQQEtBIQL1egjyRsMBQIDAQABAkAlcBOcvJngT23Gc0IpmAGgJalvG0ImgpRjOfnN+MtkR6RBgBnseE7tE9zlahM56blKRJUG7KiJTxN1fJOeXqmBAiEA9URD5wFGygdek/vH625vPT+tWxe4LQCDJgzx2sZa7l0CIQDT7Xm0S2kMEVl1XcWgg+C2MR7qUlkyZPRW/GYItx0pyQIgRO55swvtzEwD7c1eo7F2/BzQaQCil6tEMMaElrsWxVUCIGcr6CD2yVgfph52HTzsltv7g+JtXpEjBrTGQWIiyybJAiEA1P1oMu/1v3LGx06AtQ4mu+4RmgM+cHkdrMnwj5NCjEw=";
	//公钥
	public static final String rsaPublicKeyString = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAMsKyMRDQRmI0JXXrToXy6JDJc91ZEj6C3pqM4VZ8C0nKuyD+JxOHj1ivriGW3mT5UnxHkEBLQSEC9XoI8kbDAUCAwEAAQ==";

	static {
		try {
			// 1.初始化密钥
			keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			keyPairGenerator.initialize(512);
			KeyPair keyPair = keyPairGenerator.generateKeyPair();
			keyFactory = KeyFactory.getInstance("RSA");
			signature = Signature.getInstance("MD5withRSA");

			//这两个是为了随机生成下面两个私钥和公钥，生成之后记住赋值给：rsaPrivateKeyString和rsaPublicKeyString
			//rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
			//System.out.println("--------------------------------" + StringUtil.byteToBase64StringByGuava(rsaPublicKey.getEncoded()));
			//rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
			//System.out.println("--------------------------------" + StringUtil.byteToBase64StringByGuava(rsaPrivateKey.getEncoded()));

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据明文生成签名，可以把这个签名保存到数据库中
	 */
	public static String generateSignature(String plainText) {
		try {
			// 2.进行签名
			PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(CodecUtil.base64StringToSourceByteByGuava(rsaPrivateKeyString));
			PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
			signature.initSign(privateKey);
			signature.update(plainText.getBytes());
			byte[] result = signature.sign();
			return CodecUtil.byteToBase64StringByGuava(result);
		} catch (SignatureException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}

		return null;

	}

	/**
	 * 根据签名+提供的明文进行判断，判断是否该明文是对的（因为用了MD5withRSA，类似MD5，只能比对是否对的，没办法逆转知道明文）
	 */
	public static Boolean comparisonSignature(String signatureString, String password) {
		try {
			// 3.验证签名
			X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(CodecUtil.base64StringToSourceByteByGuava(rsaPublicKeyString));
			keyFactory = KeyFactory.getInstance("RSA");
			PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
			signature = Signature.getInstance("MD5withRSA");
			signature.initVerify(publicKey);
			signature.update(password.getBytes());
			return signature.verify(CodecUtil.base64StringToSourceByteByGuava(signatureString));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (SignatureException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return false;
	}


	public static void main(String[] args) {
		System.out.println("根据明文生成的签名（转成 Base64，长度在 88 左右，不固定）:" + generateSignature("123456"));
		String signatureString = "qJjMPpWp0XslrrWp2WcI85eOvHw/9H8nG9WA3DBpmfSBvIHtt53Ww7slgxaU+VrOT/lxlo2oqLR9Woiq8c/MvQ==";
		System.out.println("比对:" + comparisonSignature(signatureString, "123456"));

	}
}
