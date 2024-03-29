package com.yale.test.ps;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;

/*
 * MD5(Message Digest algorithm 5，信息摘要算法)
 * 哈希算法（Hash）又称摘要算法（Digest），它的作用是：对任意一组输入数据进行计算，得到一个固定长度的输出摘要。
 *	哈希算法最重要的特点就是：
 *	   相同的输入一定得到相同的输出；
 *   不同的输入大概率得到不同的输出。
 * 哈希算法的目的就是为了验证原始数据是否被篡改。
 * Java字符串的hashCode()就是一个哈希算法，它的输入是任意字符串，输出是固定的4字节int整数：
 * 两个相同的字符串永远会计算出相同的hashCode，否则基于hashCode定位的HashMap就无法正常工作。这也是为什么当我们自定义一个class时，
 * 覆写equals()方法时我们必须正确覆写hashCode()方法。
 * 哈希碰撞
 * 哈希碰撞是指，两个不同的输入得到了相同的输出：
 * 有童鞋会问：碰撞能不能避免？答案是不能。碰撞是一定会出现的，因为输出的字节长度是固定的，String的hashCode()输出是4字节整数，最多只有4294967296种输出，但输入的数据长度是不固定的，有无数种输入。所以，哈希算法是把一个无限的输入集合映射到一个有限的输出集合，必然会产生碰撞。
 * 碰撞不可怕，我们担心的不是碰撞，而是碰撞的概率，因为碰撞概率的高低关系到哈希算法的安全性。一个安全的哈希算法必须满足：
 * 	碰撞概率低；
 *	不能猜测输出。
 * 常用的哈希算法有：
 *  算法						输出长度（位）	输出长度（字节）
 * 	MD5						128 bits		16 bytes
 *	SHA-1(SHA1)				160 bits		20 bytes
 *	RipeMD-160(RipeMD160)	160 bits		20 bytes
 *	SHA-256(SHA256)			256 bits		32 bytes
 *	SHA-512(SHA512)			512 bits		64 bytes
 * 根据碰撞概率，哈希算法的输出长度越长，就越难产生碰撞，也就越安全。
 * Java标准库提供了常用的哈希算法，并且有一套统一的接口。我们以MD5算法为例，看看如何对输入计算哈希：
 * 哈希算法的用途
 * 因为相同的输入永远会得到相同的输出，因此，如果输入被修改了，得到的输出就会不同。
 * 我们在网站上下载软件的时候，经常看到下载页显示的哈希：
 * 哈希算法的另一个重要用途是存储用户口令。如果直接将用户的原始口令存放到数据库中，会产生极大的安全风险：
 * 	数据库管理员能够看到用户明文口令；
 * 	数据库数据一旦泄漏，黑客即可获取用户明文口令。
 * 不存储用户的原始口令，那么如何对用户进行认证？
 * 方法是存储用户口令的哈希，例如，MD5。
 * 在用户输入原始口令后，系统计算用户输入的原始口令的MD5并与数据库存储的MD5对比，如果一致，说明口令正确，否则，口令错误。
 * 因此，数据库存储用户名和口令的表内容应该像下面这样：
 * 	username	password
	bob		f30aa7a662c728b7407c54ae6bfd27d1
	alice	25d55ad283aa400af464c76d713c07ad
	tim		bed128365216c019988915ed3add75fb
	这样一来，数据库管理员看不到用户的原始口令。即使数据库泄漏，黑客也无法拿到用户的原始口令。想要拿到用户的原始口令，必须用暴力穷举的方法，一个口令一个口令地试，直到某个口令计算的MD5恰好等于指定值。
 * 使用哈希口令时，还要注意防止彩虹表攻击。
 * 什么是彩虹表呢？上面讲到了，如果只拿到MD5，从MD5反推明文口令，只能使用暴力穷举的方法。
 * 然而黑客并不笨，暴力穷举会消耗大量的算力和时间。但是，如果有一个预先计算好的常用口令和它们的MD5的对照表：
 * 		常用口令			MD5
	hello123	f30aa7a662c728b7407c54ae6bfd27d1
	12345678	25d55ad283aa400af464c76d713c07ad
	passw0rd	bed128365216c019988915ed3add75fb
	19700101	570da6d5277a646f6552b8832012f5dc
	…	…
	20201231	6879c0ae9117b50074ce0a0d4c843060
	这个表就是彩虹表。如果用户使用了常用口令，黑客从MD5一下就能反查到原始口令：
 * bob的MD5：f30aa7a662c728b7407c54ae6bfd27d1，原始口令：hello123；
 * alice的MD5：25d55ad283aa400af464c76d713c07ad，原始口令：12345678；
 * tim的MD5：bed128365216c019988915ed3add75fb，原始口令：passw0rd。
 * 这就是为什么不要使用常用密码，以及不要使用生日作为密码的原因。
 * 即使用户使用了常用口令，我们也可以采取措施来抵御彩虹表攻击，方法是对每个口令额外添加随机数，这个方法称之为加盐（salt）：
 * digest = md5(salt+inputPassword)
 * 经过加盐处理的数据库表，内容如下：
 * username		salt	password
 * bob			H1r0a	a5022319ff4c56955e22a74abcc2c210
 * alice		7$p2w	e5de688c99e961ed6e560b972dab8b6a
 * tim			z5Sk9	1eee304b92dc0d105904e7ab58fd2f64
 * 加盐的目的在于使黑客的彩虹表失效，即使用户使用常用口令，也无法从MD5反推原始口令。
 */
public class HashDemo {
	public static void main(String[] args) {
		//Java字符串的hashCode()就是一个哈希算法，它的输入是任意字符串，输出是固定的4字节int整数：
		System.out.println("hello".hashCode());// 0x5e918d2
		System.out.println("hello, java".hashCode());// 0x7a9d88e8
		System.out.println("hello, bob".hashCode());// 0xa0dbae2f
		
		System.out.println("hash碰撞,，两个不同的输入得到了相同的输出：");
		System.out.println("AaAaAa".hashCode());// 0x7460e8c0
		System.out.println("BBAaBB".hashCode());// 0x7460e8c0
		System.out.println("data-123456".hashCode());
		System.out.println("data-ABCDEF".hashCode());
		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("AaAaAa", "取不到了");
		hashMap.put("BBAaBB", "取到了");
		System.out.println("能不能取到正确的值:" + hashMap.get("BBAaBB"));
		System.out.println("能不能取到正确的值:" + hashMap.get("AaAaAa"));
		try {
			/*
			 * Java标准库提供了常用的哈希算法，并且有一套统一的接口。我们以MD5算法为例，看看如何对输入计算哈希：
			 * 使用MessageDigest时，我们首先根据哈希算法获取一个MessageDigest实例，然后，反复调用update(byte[])输入数据。
			 * 当输入结束后，调用digest()方法获得byte[]数组表示的摘要，最后，把它转换为十六进制的字符串。
			 * 运行下述代码，可以得到输入HelloWorld的MD5是68e109f0f40ca72a15e05cc22786f8e6。
			 */
			// 创建一个MessageDigest实例:
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update("Hello".getBytes("UTF-8"));// 反复调用update输入数据:
			md.update("World".getBytes("UTF-8"));// 反复调用update输入数据:
			byte[] result = md.digest();//16bytes
			System.out.println(Arrays.toString(result));
			//new BigInteger(1, result1).toString(16)有个大坑：0开头的，0最终会被省略,使用String.format代替
			System.out.println("MD5:" + new BigInteger(1, result).toString(16));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		/*
		 * SHA-1
		 * SHA-1也是一种哈希算法，它的输出是160 bits，即20字节。SHA-1是由美国国家安全局开发的，SHA算法实际上是一个系列，包括SHA-0（已废弃）、SHA-1、SHA-256、SHA-512等。
		 * 在Java中使用SHA-1，和MD5完全一样，只需要把算法名称改为"SHA-1"：
		 * 类似的，计算SHA-256，我们需要传入名称"SHA-256"，计算SHA-512，我们需要传入名称"SHA-512"。
		 * Java标准库支持的所有哈希算法可以在这里(https://docs.oracle.com/en/java/javase/14/docs/specs/security/standard-names.html#messagedigest-algorithms)查到。
		 * 注意：MD5因为输出长度较短，短时间内破解是可能的，目前已经不推荐使用。 
		 */
		try {
			MessageDigest mdSha = MessageDigest.getInstance("SHA-1");
			mdSha.update("Hello".getBytes("UTF-8"));
			mdSha.update("World".getBytes("UTF-8"));
			byte[] result = mdSha.digest();//20bytes: db8ac1c259eb89d4a131b253bacfca5f319d54f2
			//new BigInteger(1, result1).toString(16)有个大坑：0开头的，0最终会被省略,使用String.format代替
			System.out.println("SHA-1:" + new BigInteger(1, result).toString(16));
			
			int num = new BigInteger("0004e473f59ab5bd4639f848dd8ed27f1b3f6b0d", 16).intValue();
			System.out.println("0004e473f59ab5bd4639f848dd8ed27f1b3f6b0d 的值整数值为:" + num);
			//String.format的用法见类com.yale.test.java.demo.string.StringTest.java
			//结合com.yale.test.math.FloatDemo一起看.
			System.out.println(String.format("%040x", new BigInteger("0004e473f59ab5bd4639f848dd8ed27f1b3f6b0d", 16)));// 0004e473f59ab5bd4639f848dd8ed27f1b3f6b0d
			
			 System.out.println("注意看前面的三个0没有了:" + new BigInteger("0004e473f59ab5bd4639f848dd8ed27f1b3f6b0d", 16).toString(16));// 0004e473f59ab5bd4639f848dd8ed27f1b3f6b0d
			 System.out.println("注意看前面的三个0没有了:" + String.format("%x", new BigInteger("0004e473f59ab5bd4639f848dd8ed27f1b3f6b0d", 16)));// 0004e473f59ab5bd4639f848dd8ed27f1b3f6b0d
			 //%040x的意思是,如果后面的参数不足40位就在开头用0补足40位.注意"0004e473f59ab5bd4639f848dd8ed27f1b3f6b0d"的长度刚好就是40位。
			 System.out.println("注意看前面的三个0有了:" + String.format("%040x", new BigInteger("0004e473f59ab5bd4639f848dd8ed27f1b3f6b0d", 16)));// 0004e473f59ab5bd4639f848dd8ed27f1b3f6b0d
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		/*
		 * MD5即Message-Digest Algorithm 5（信息-摘要算法5），用于确保信息传输完整一致。是计算机广泛使用的杂凑算法之一（又译摘要算法、哈希算法），主流编程语言普遍已有MD5实现。
		 * 将数据（如汉字）运算为另一固定长度值，是杂凑算法的基础原理，MD5的前身有MD2、MD3和MD4。广泛用于加密和解密技术，常用于文件校验。校验？不管文件多大，经过MD5后都能生成唯一的MD5值。
		 * 好比现在的ISO校验，都是MD5校验。怎么用？当然是把ISO经过MD5后产生MD5的值。一般下载linux-ISO的朋友都见过下载链接旁边放着MD5的串。就是用来验证文件是否一致的。
		 * 通常我们不直接使用上述MD5加密。通常将MD5产生的字节数组交给BASE64再加密一把，得到相应的字符串Digest:汇编
		 * MD5算法具有以下特点：
		 *	1、压缩性：任意长度的数据，算出的MD5值长度都是固定的。
		 *	2、容易计算：从原数据计算出MD5值很容易。
		 *  3、抗修改性：对原数据进行任何改动，哪怕只修改1个字节，所得到的MD5值都有很大区别。
		 *	4、弱抗碰撞：已知原数据和其MD5值，想找到一个具有相同MD5值的数据（即伪造数据）是非常困难的。
		 *	5、强抗碰撞：想找到两个不同的数据，使它们具有相同的MD5值，是非常困难的。
		 * MD5的作用是让大容量信息在用数字签名软件签署私人密钥前被”压缩”成一种保密的格式（就是把一个任意长度的字节串变换成一定长的十六进制数字串）。除了MD5以外，其中比较有名的还有sha-1、RIPEMD以及Haval等。
		 * http://www.jfh.com/jfperiodical/article/818
		 */
		String originStr = "简单加密8888888888888888888";
		System.out.println("=======加密前的数据:" + originStr);
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] inputData = originStr.getBytes();
			md.update(inputData);
			BigInteger bigInteger = new BigInteger(md.digest());
			System.out.println("加密后的数据:" + bigInteger.toString(16));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
	}
}
