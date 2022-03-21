package com.seekey.commons;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import cn.hutool.core.codec.Base64;

/**
 * AES 256 CBC 加密及解密
 * 注意：pom.xml必需引入bcprov-jdk15on包
 * 
 * @author Seekey
 * @version 1.0
 **/
public class Crypto {
	
	/**
	 * 加密
	 * @param data		明文
	 * @param key		加密键值（32位）
	 * @param secret	密码（16位）
	 * @return 密文
	 **/
	public static String encrypt(String data, String key, String secret) {
		return Base64.encode(encrypt(data.getBytes(), key.getBytes(), secret.getBytes()));
	}
	
	/**
	 * 解密
	 * @param encstr	密文
	 * @param key		加密键值（32位）
	 * @param secret	密码（16位）
	 * @return 明文
	 **/
	public static String decrypt(String encstr, String key, String secret) {
		return decrypt(Base64.decode(encstr), key.getBytes(), secret.getBytes());
	}
	
	private static String decrypt(byte[] data, byte[] key, byte[] secret) {
		SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
		Security.addProvider(new BouncyCastleProvider());
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
			IvParameterSpec iv = new IvParameterSpec(secret);
			cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);
			byte[] decbbdt = cipher.doFinal(data);
			return new String(decbbdt);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException 
				| NoSuchProviderException | InvalidKeyException 
				| InvalidAlgorithmParameterException | IllegalBlockSizeException 
				| BadPaddingException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private static byte[] encrypt(byte[] srcData, byte[] key, byte[] iv) {
	    SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
	    Security.addProvider(new BouncyCastleProvider());
	    
	    try {
	    	Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding","BC");
	    	IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

			cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParameterSpec);
		    return cipher.doFinal(srcData);
		} catch (InvalidKeyException | InvalidAlgorithmParameterException 
				| NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException 
				| IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
			return new byte[0];
		}
	}
	
	public static void main(String[] args) {
		String key		= "bw0kgpsze0abtgxg0nmxryd6p9zcms60";	//32位
		String secret	= "enps6otl8yu6ymzr";					//16位
		String data		= "{\"db\":\"testmssql\",\"sql\":\"SELECT 1 AS VALUE FROM DUMMY";
		
		String str = encrypt(data, key, secret); //
		String ret = decrypt(str, key, secret);
		
		System.out.println("data:" + data);
		System.out.println("密文:" + str);
		System.out.println("明文:" + ret);
		
		if(data.equals(ret)) {
			System.out.println("TEST PASS");
		}else {
			System.out.println("TEST NO PASS");
		}
	}
}
