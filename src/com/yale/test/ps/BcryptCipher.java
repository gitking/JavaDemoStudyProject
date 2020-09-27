package com.yale.test.ps;

import java.util.HashMap;
import java.util.Map;

import com.yale.test.ps.source.BCrypt;

/*
 * 来自:芋道源码
 * https://zhuanlan.zhihu.com/p/93860175
 */
public class BcryptCipher {
	private static final int SALT_SEED = 12;
	private static final String SALT_STARTSWITH = "$2a$12";
	
	public static final String SALT_KEY = "salt";
	
	public static final String CIPHER_KEY = "cipher";
	
	public static Map<String, String> Bcrypt(final String encryptSource) {
		String salt = BCrypt.gensalt(SALT_SEED);
		System.out.println("这个每次都会变：" + salt);
		return Bcrypt(salt, encryptSource);
	}
	
	public static Map<String, String> Bcrypt(final String falt, final String encryptSource) {
		if (encryptSource == null || "".equals(encryptSource)) {
			throw new RuntimeException("Bcrypt encrypt input params cant not be empty");
		}
		if (falt == null || "".equals(falt) || falt.length() != 29) {
			throw new RuntimeException("Salt can't be empty and length must be to 29");
		}
		if (!falt.startsWith(SALT_STARTSWITH)) {
			throw new RuntimeException("Invalid salt version, salt version in $2a$12");
		}
		
		String cipher = BCrypt.hashpw(encryptSource, falt);
		Map<String, String> bcryptResult = new HashMap<String, String>();
		bcryptResult.put(SALT_KEY, falt);
		bcryptResult.put(CIPHER_KEY, cipher);
		return bcryptResult;
	}
	
	public static void main(String[] args) {
		String string = "我是一句话";
		Map<String, String> bcrypt = BcryptCipher.Bcrypt(string);
		System.out.println(bcrypt.get("cipher"));
		System.out.println(bcrypt.get("salt"));
		
		Map<String, String> bcrypt2 = BcryptCipher.Bcrypt(bcrypt.get("salt"), string);
		System.out.println(bcrypt2.get("cipher"));
		System.out.println(bcrypt2.get("salt"));
	}
}
