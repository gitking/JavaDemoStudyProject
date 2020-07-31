package com.yale.test.ps;

import java.util.Base64;

/*
 * 在计算机系统中，什么是加密与安全呢？
 * 我们举个栗子：假设Bob要给Alice发一封邮件，在邮件传送的过程中，黑客可能会窃取到邮件的内容，所以需要防窃听。黑客还可能会篡改邮件的内容，Alice必须有能力识别出邮件有没有被篡改。最后，黑客可能假冒Bob给Alice发邮件，Alice必须有能力识别出伪造的邮件。
 * 所以，应对潜在的安全威胁，需要做到三防：1,防窃听2,防篡改3,防伪造
 * 计算机加密技术就是为了实现上述目标，而现代计算机密码学理论是建立在严格的数学理论基础上的，密码学已经逐渐发展成一门科学。
 * 对于绝大多数开发者来说，设计一个安全的加密算法非常困难，验证一个加密算法是否安全更加困难，当前被认为安全的加密算法仅仅是迄今为止尚未被攻破。因此，要编写安全的计算机程序，我们要做到：
 * 不要自己设计山寨的加密算法；
 * 不要自己实现已有的加密算法；
 * 不要自己修改已有的加密算法。
 */
public class Base64Test {

	public static void main(String[] args) {
		/**
		 * Base64加密最好多加密几次,多加密几次,加密后的字符长度太长了,
		 * 多次加密后最好再用MD5再加一次,MD5加密后的的密钥长度只有32位
		 */
		String password = "Base64这个类是JDK1.8加入的一个类,用于加密使用,我是密码请帮忙我加密";
		String pwStr = Base64.getEncoder().encodeToString(password.getBytes());
		System.out.println("加密后的字符串:" + pwStr);
		
		byte [] pwArr = Base64.getDecoder().decode(pwStr);
		System.out.println("解密后的字符串:" + new String(pwArr));
	}
}
