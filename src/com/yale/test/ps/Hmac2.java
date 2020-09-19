package com.yale.test.ps;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/*
 * Hmac算法
 * HMAC(Hash Message Authentication Code，散列消息鉴别码)，基于密钥的Hash算法的认证协议。消息鉴别码实现鉴别的原理是，用公开函数和密钥产生一个固定长度的值作为认证标识，
 * 用这个标识鉴别消息的完整性。使用一个密钥生成一个固定大小的小数据块，即MAC，并将其加入到消息中，然后传输。接收方利用与发送方共享的密钥进行鉴别认证等。
 * http://www.jfh.com/jfperiodical/article/818
 */
public class Hmac2 {
	public static final String KEY_MAC = "HmacMD5";
	
	public static void main(String[] args) {
		try {
			String str = "简单加密";
			//使用同一密钥:对数据进行加密:查看俩次加密的结果是否一样
			getResult1(str);
			getResult2(str);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * 初始化HMAC密钥
	 */
	public static String initMacKey() throws NoSuchAlgorithmException {
		KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_MAC);
		SecretKey secretKey = keyGenerator.generateKey();
		return Base64Test.encryptBASE64(secretKey.getEncoded());
	}
	
	/*
	 * HMAC加密:主要方法
	 */
	public static String encryptHMAC(byte[] data, String key) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
		SecretKey secretKey = new SecretKeySpec(Base64Test.decryptBASE64(key), KEY_MAC);
		Mac mac = Mac.getInstance(secretKey.getAlgorithm());
		mac.init(secretKey);
		return new String(mac.doFinal(data));
	}
	
	public static String getResult1(String inputStr) throws NoSuchAlgorithmException, InvalidKeyException, IOException {
		String path = "";//Tools.getClassPath()
		String fileSource = path + "/file/HMAC_key.txt";
		System.out.println("==================加密前的数据:" + inputStr);
		byte[] inputdata = inputStr.getBytes();
		String key = Hmac2.initMacKey();//产生密钥
		System.out.println("Mac密钥:===" + key);
		//将密钥写入文件Tools.WriteMyFile(fileSource, key);
		
		String result = Hmac2.encryptHMAC(inputdata, key);//加密
		System.out.println("HMAC加密后:======" + result);
		return result;
	}
	
	public static String getResult2(String inputStr) throws InvalidKeyException, NoSuchAlgorithmException, IOException {
		System.out.println("加密前的数据==========" + inputStr);
		String path = "";//Tools.getClassPath();
		String fileSource = path + "/file/HMAC_key.txt";
		String key = null;
		//将密钥从文件中读取
		key = ""; //Tools.ReadMyFile(fileSource)
		System.out.println("读取的密钥为:=================" + key);
		byte[] inputData = inputStr.getBytes();
		//对数据进行加密
		String result = Hmac2.encryptHMAC(inputData, key);
		System.out.println("HMAC加密后:===" + result);
		return result;
	}
}