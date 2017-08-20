package com.yale.test.ps;

import java.util.Base64;

public class Base64Test {

	public static void main(String[] args) {
		
		String password = "Base64这个类是JDK1.8加入的一个类,用于加密使用,我是密码请帮忙我加密";
		String pwStr = Base64.getEncoder().encodeToString(password.getBytes());
		System.out.println("加密后的字符串:" + pwStr);
		
		byte [] pwArr = Base64.getDecoder().decode(pwStr);
		System.out.println("解密后的字符串:" + new String(pwArr));
		
	}

}
