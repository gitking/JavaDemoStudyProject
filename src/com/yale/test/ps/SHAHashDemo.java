package com.yale.test.ps;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.digest.Sha2Crypt;

/*
 * SHA
 * 安全哈希算法（Secure Hash Algorithm）主要适用于数字签名标准（Digital Signature Standard DSS）里面定义的数字签名算法（Digital Signature Algorithm DSA）。
 * 对于长度小于2^64位的消息，SHA1会产生一个160位的消息摘要。该算法经过加密专家多年来的发展和改进已日益完善，并被广泛使用。该算法的思想是接收一段明文，
 * 然后以一种不可逆的方式将它转换成一段（通常更小）密文，也可以简单的理解为取一串输入码（称为预映射或信息），并把它们转化为长度较短、位数固定的输出序列即散列值（也称为信息摘要或信息认证代码）的过程。
 * 散列函数值可以说是对明文的一种“指纹”或是“摘要”所以对散列值的数字签名就可以视为对此明文的数字签名。
 * SHA(Secure Hash Algorithm，安全散列算法），数字签名等密码学应用中重要的工具，
 * 被广泛地应用于电子商务等信息安全领域。虽然，SHA与MD5通过碰撞法都被破解了， 
 * 但是SHA仍然是公认的安全加密算法，较之MD5更为安全
 * SHA-1与MD5的比较
 * 因为二者均由MD4导出，SHA-1和MD5彼此很相似。相应的，他们的强度和其他特性也是相似，但还有以下几点不同：
 * l, 对强行攻击的安全性：最显著和最重要的区别是SHA-1摘要比MD5摘要长32 位。使用强行技术，产生任何一个报文使其摘要等于给定报摘要的难度对MD5是2^128数量级的操作，而对SHA-1则是2^160数量级的操作。这样，SHA-1对强行攻击有更大的强度。
 * 2, 对密码分析的安全性：由于MD5的设计，易受密码分析的攻击，SHA-1显得不易受这样的攻击。
 * 3, 速度：在相同的硬件上，SHA-1的运行速度比MD5慢。
 * http://www.jfh.com/jfperiodical/article/818
 * https://zhuanlan.zhihu.com/p/21513964 《论计算机加密算法的原理与实践》
 * 在计算机中，经常用MD5或者SHA1作为签名算法，你看到的一串乱七八糟的数字d41d8cd9...就是MD5签名。经常用迅雷在网上乱下软件的童鞋，可以用专门的软件计算下载文件的MD5签名，再去官网对比，如果一致，说明软件没有被篡改过。
 * 但是签名算法不是加密算法，不能用来加密，它的作用是防篡改。
 */
public class SHAHashDemo {
	public static void main(String[] args) {
		String originStr = "简单加密";
		System.out.println("加密前的数据:=================" + originStr);
		byte[] inputData = originStr.getBytes();
		try {
			MessageDigest md = MessageDigest.getInstance("SHA");
			md.update(inputData);
			BigInteger sha = new BigInteger(md.digest());
			System.out.println("SHA加密后的数据:" + sha.toString(32));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		/*
		 * apache的commons-codec-1.10.jar包提供的方法
		 * commons-codec-1.3.jar没有Sha2Crypt这个类
		 * https://zhuanlan.zhihu.com/p/93860175
		 */
		String string = "我是一句话";
		String sha256crypt = Sha2Crypt.sha256Crypt(string.getBytes());
		System.out.println(sha256crypt);
	}
}
