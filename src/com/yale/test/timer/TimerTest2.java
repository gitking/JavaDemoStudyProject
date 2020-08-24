package com.yale.test.timer;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

class MyTimerTask extends TimerTask {
	public void run () {
		System.out.println("通过查看TimerTask类,可以知道TimerTask实际上是个抽象类,实现了runnable接口");
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
		
		/*
		 * 看源码注释知道
		 * 表示不变的通用唯一标识符（UUID）的类。
		 * UUID表示一个128位的值。
		 * 有关更多信息，包括用于创建的算法可以参考ttp://www.ietf.org/rfc/rfc4122.txt
		 * UUID是基于时间的算法
		 */
		UUID uuid = UUID.randomUUID();
		System.out.println("UUID类是根据你当前的地址和时间戳自动生成一个几乎不会重复的字符串,重复的几率大概是千万分之一");
		String uuidStr = uuid.toString();
		System.out.println("随机生成唯一ID:" + uuidStr);
		byte[] uuidArr = uuidStr.getBytes();
		System.out.println("uuid随机生成唯一ID的长度为:" + uuidArr.length + "16byte(字节),128bit(位)");
		
		try {
			byte[] uuidArrGbk = uuidStr.getBytes("GBK");
			System.out.println("uuid随机生成唯一ID的长度为GBK:" + uuidArrGbk.length + "byte(字节),128bit(位)");

			byte[] uuidArr8 = uuidStr.getBytes("UTF-8");
			System.out.println("uuid随机生成唯一ID的长度为UTF-8:" + uuidArr8.length + "16byte(字节),128bit(位)");
			
			String uuidReplace = uuidStr.replaceAll("-", "");
			byte[] as = uuidReplace.getBytes();
			System.out.println("替换后的" + uuidReplace);
			System.out.println("uuid随机生成唯一ID的长度为replace:" + as.length + "16byte(字节),128bit(位)");
			
			/*
			 * UUID源码里面说的是UUID是一个128bit(位)的值,但是我们打印出来的却是36个char(字符)288bit(位)：670e9a22-74dd-4d3b-b377-c370a8e063a2
			 * 即使去掉里面的四个"-"字符,还有32个char(字符)256bit(位),原因如下:
			 * API文档中并未直接说明这一串唯一标识符是16进制，但其描述这个字符串的组成时各个部分其实都是16进制：
			 * 例如描述最高有效位：
			 * 0xFFFFFFFF00000000 time_low
			 * 0x00000000FFFF0000 time_mid
			 * 0x000000000000F000 version
			 * 0x0000000000000FFF time_hi
			 * 那么既然它是表示16进制的话，我们就需要看看它等于多少位二进制：670e9a2274dd4d3bb377c370a8e063a2
			 * 2个16进制 = 8位二进制
			 * 32个16进制 = 128位二进制
			 * 所以UUID表示128位二进制或者32位字符的唯一标识符
			 * UUID由128位组成，那么组合的种类就是有限个，所以对于UUID作为唯一标识符不是永远不会重复，而是重复的概率非常的低。
			 * http://www.mamicode.com/info-detail-2195433.html
			 */
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		//https://gitee.com/yu120/sequence
		System.out.println("用雪花ID不会重复,自己百度或者美团的leaf,美团的的leaf配置太复杂，不适合中小应用。雪花ID可以看IdUtil类");
		
		/*
		 * 知乎用户andylizi 问:然而UUID并不一定要是字符串啊。“xxxxxxxx-xxxx-Mxxx-Nxxx-xxxxxxxxxxxx”只是它最常见的表现形式而已，
		 * 但跟IP地址一样，它也可以被表示为整数形式。具体来说是一个 128 位的整数或两个 64 位的整数。
		 * Java 里通过 uid.getMostSignificantBits() 和 uid.getLeastSignificantBits() 两个方法获得。
		 * 廖雪峰答:问题是没有原生128bit整数
		 * 知乎用户andylizi 问: 那就把两个 64 位整数给异或一下就得到 64 位了。总之用位运算的话要多少位都能搞出来啊。
		 * oobleck回复andylizi:异或出来和随机生成一个64bit整数安全上就没太大区别了，很容易冲突
		 * andylizi回复oobleck:SHA 512/256 就是把生成出来 512 位的 hash 给缩减到 256 位（我随手查了一下好像没查到具体的缩减方法？），也不一样没事。
		 * 以及最理想的情况下（生成的64个bit里每个bit的可能性都相等），直接生成64位跟生成128位再异或到64感觉不会有区别吧？我对这方面的数学也不了解，但光一句“很容易冲突”实在没有什么说服力。
		 * oobleck回复andylizi: 因为 256 位已经足够长了，但是 64 位就不够了。
		 * 了解下生日悖论，64 位随机数的话，只要 2^32 个数就有近 50% 的概率发生碰撞，也就几十亿的数量级，很多系统里是完全不够用的。
		 * 用时间戳就可以保证有可能发生碰撞的桶足够小，新生成的 ID 也绝不可能和很久之前的碰撞，而用异或就把时间戳给抹掉了。
		 * andylizi回复oobleck： 有道理。
		 * https://www.zhihu.com/people/liaoxuefeng/posts
		 */
		long uuidL = uuid.getMostSignificantBits();
		System.out.println("java中long类型变量占8个字节,64位,返回此UUID的128位值的最高有效64位。:" + uuidL);
		
		long uuidLl = uuid.getLeastSignificantBits();
		System.out.println("java中long类型变量占8个字节,64位,返回此UUID的128位值的最低有效64位。:" + uuidLl);
	}
}

public class TimerTest2 {
	public static void main(String[] args) {
		Timer timer = new Timer();
		//1000毫秒后开始执行,然后每隔2000毫秒执行一下
		timer.schedule(new MyTimerTask(), 1000, 2000);
	}
}
