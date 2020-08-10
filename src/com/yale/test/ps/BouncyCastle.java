package com.yale.test.ps;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

/*
 * 我们知道，Java标准库提供了一系列常用的哈希算法。
 * 但如果我们要用的某种算法，Java标准库没有提供怎么办？
 * 方法一：自己写一个，难度很大；
 * 方法二：找一个现成的第三方库，直接使用。
 * BouncyCastle就是一个提供了很多哈希算法和加密算法的第三方库。它提供了Java标准库没有的一些算法，例如，RipeMD160哈希算法。
 * 我们来看一下如何使用BouncyCastle这个第三方提供的算法。
 * 首先，我们必须把BouncyCastle提供的jar包放到classpath中。这个jar包就是bcprov-jdk15on-xxx.jar，可以从官方网站(https://www.bouncycastle.org/latest_releases.html)下载。
 * Java标准库的java.security包提供了一种标准机制，允许第三方提供商无缝接入。我们要使用BouncyCastle提供的RipeMD160算法，需要先把BouncyCastle注册一下：
 */
public class BouncyCastle {
	public static void main(String[] args) {
		try {
			//注册BouncyCastle:注册只需要在启动时进行一次，后续就可以使用BouncyCastle提供的所有哈希算法和加密算法。
			Security.addProvider(new BouncyCastleProvider());
			//按名称正常调用:
			MessageDigest md = MessageDigest.getInstance("RipeMD160");
			md.update("HelloWorld".getBytes("UTF-8"));//ecabeaa2eb986c85e6a6ea2c22b248ab6916de35
			byte[] result = md.digest();
			System.out.println(new BigInteger(1, result).toString(16));
			/*
			 * BouncyCastle是一个开源的第三方算法提供商；
			 * BouncyCastle提供了很多Java标准库没有提供的哈希算法和加密算法；
			 * 使用第三方算法前需要通过Security.addProvider()注册。
			 */
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
