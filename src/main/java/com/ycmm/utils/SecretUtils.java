package com.ycmm.utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Encoder;

public class SecretUtils {

	/**
	 * 对字符串按照指定算法进行加密，并返回加密后的BASE64编码转换
	 * @param source
	 * @param algorithm
	 *                  support such as -- MD2,MD5,SHA-1,SHA-256,SHA-384,SHA-512
	 * @return
	 */
	public static String secretString(String source, String algorithm) {
		
		//判断加密source
		if(source == null || source.length() < 1) {
			throw new IllegalArgumentException("Incorrect string source: empty input!");
		}
		
		try {
			MessageDigest alga = MessageDigest.getInstance(algorithm);
			
			//使用指定的 byte 数组更新摘要
			alga.update(source.getBytes());
			
			//通过执行诸如填充之类的最终操作完成哈希计算。在调用此方法之后，方法被重置           返回：存放哈希值结果的 byte 数组。
			byte[] hash = alga.digest();
			
			return base64Encode(hash);
			
		} catch (NoSuchAlgorithmException e) {
			//当请求特定的加密算法而它在该环境中不可用时抛出此异常
			System.err.println(e.getMessage());
			return "";
		}
		
	}

	/**
	 * 对字符串按照给定密钥进行HmacSHA1加密，并返回加密后的BASE64编码转换
	 * @param encryptText
	 * @param encryptKey
	 * @return 
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeyException 
	 * @throws UnsupportedEncodingException 
	 */
	public static String HmacSHA1Encrypt(String encryptText, String encryptKey) throws NoSuchAlgorithmException, 
	                                                            InvalidKeyException, UnsupportedEncodingException {
		
		String MAC_NAME = "HmacSHA1";  //加密方式
		String ENCODING = "UTF-8";     //编码格式
		
		byte[] data = encryptKey.getBytes();
		
		//根据给定的字节数组构造一个密钥，第二个参数指定一个密钥算法的名称
		SecretKeySpec secretKey = new SecretKeySpec(data, MAC_NAME);
		
		//返回实现指定   MAC 算法的  Mac对象
		Mac mac = Mac.getInstance(MAC_NAME);
		
		//用给定的密钥初始化此   Mac 对象
		mac.init(secretKey);
		
		//用指定编码格式获取  字符串   字节码
		byte[] text = encryptText.getBytes(ENCODING);
		
		//处理自定的    byte 数组并完成  MAC操作
		byte[] bs = mac.doFinal(text);
		
		return base64Encode(bs);
	}
	
	/**
	 * 将字符串转换成BASE64编码
	 * @param b
	 * @return
	 */
	public static String base64Encode(byte[] b) {
		
		return (b == null || b.length < 1) ? "" : new BASE64Encoder().encode(b);
	}

}
