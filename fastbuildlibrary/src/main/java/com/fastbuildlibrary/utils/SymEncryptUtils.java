package com.fastbuildlibrary.utils;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * 加密工具类（DES,AES,DESEDE）
 */
public class SymEncryptUtils {
	private SymEncryptUtils() {
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	/**
	 * 生成秘钥
	 * @param arrBTmp
	 * @param alg
     * @return
     */
	public static Key getKey(byte[] arrBTmp, String alg){
		if(!(alg.equals("DES")||alg.equals("DESede")||alg.equals("AES"))){
			System.out.println("alg type not find: "+alg);
			return null;
		}
		byte[] arrB;
		if(alg.equals("DES")){
			arrB = new byte[8];
		}
		else if(alg.equals("DESede")){
			arrB = new byte[24];
		}
		else{
			arrB = new byte[16];
		}
		int i=0;
		int j=0;
		while(i < arrB.length){
			if(j>arrBTmp.length-1){
				j=0;
			}
			arrB[i] = arrBTmp[j];
			i++;
			j++;
		}
		Key key = new javax.crypto.spec.SecretKeySpec(arrB, alg);
		return key;
	}

	/**
	 * 加密
	 * @param s 需要加密的字符串
	 * @param strKey 秘钥
	 * @param alg 加密方式
     * @return
     */
	public static byte[] encrypt(String s,String strKey,String alg){
		if(!(alg.equals("DES")||alg.equals("DESede")||alg.equals("AES"))){
			System.out.println("alg type not find: "+alg);
			return null;
		}
		byte[] r=null;
		try {
			Key key = getKey(strKey.getBytes(),alg);
			Cipher c;
			c = Cipher.getInstance(alg);
			c.init(Cipher.ENCRYPT_MODE, key);
			r = c.doFinal(s.getBytes());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}		
		return r;
	}

	/**
	 * 解密
	 * @param code 需要解密的数据
	 * @param strKey 秘钥
	 * @param alg 解密方式
     * @return
     */
	public static String decrypt(byte[] code,String strKey,String alg){
		if(!(alg.equals("DES")||alg.equals("DESede")||alg.equals("AES"))){
			System.out.println("alg type not find: "+alg);
			return null;
		}
		String r=null;
		try {
			Key key = getKey(strKey.getBytes(),alg);
			Cipher c;
			c = Cipher.getInstance(alg);
			c.init(Cipher.DECRYPT_MODE,key); 
			byte[] clearByte=c.doFinal(code); 
			r=new String(clearByte);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			System.out.println("not padding");
			r=null;
		} 
		return r;
	}
	
}
