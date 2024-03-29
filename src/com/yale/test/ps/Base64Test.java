package com.yale.test.ps;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Base64;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/*
 * 在计算机系统中，什么是加密与安全呢？
 * 我们举个栗子：假设Bob要给Alice发一封邮件，在邮件传送的过程中，黑客可能会窃取到邮件的内容，所以需要防窃听。黑客还可能会篡改邮件的内容，Alice必须有能力识别出邮件有没有被篡改。最后，黑客可能假冒Bob给Alice发邮件，Alice必须有能力识别出伪造的邮件。
 * 所以，应对潜在的安全威胁，需要做到三防：1,防窃听2,防篡改3,防伪造
 * 计算机加密技术就是为了实现上述目标，而现代计算机密码学理论是建立在严格的数学理论基础上的，密码学已经逐渐发展成一门科学。
 * 对于绝大多数开发者来说，设计一个安全的加密算法非常困难，验证一个加密算法是否安全更加困难，当前被认为安全的加密算法仅仅是迄今为止尚未被攻破。因此，要编写安全的计算机程序，我们要做到：
 * 不要自己设计山寨的加密算法；
 * 不要自己实现已有的加密算法；
 * 不要自己修改已有的加密算法。
 * 要学习编码算法，我们先来看一看什么是编码。ASCII码就是一种编码，字母A的编码是十六进制的0x41，字母B是0x42，以此类推：
 * 字母	ASCII编码
	A	0x41
	B	0x42
	C	0x43
	D	0x44
 * 因为ASCII编码最多只能有127个字符，要想对更多的文字进行编码，就需要用Unicode。而中文的中使用Unicode编码就是0x4e2d，使用UTF-8则需要3个字节编码：
 * URL编码
 * URL编码是浏览器发送数据给服务器时使用的编码，它通常附加在URL的参数部分，例如：
 * https://www.baidu.com/s?wd=%E4%B8%AD%E6%96%87
 * 之所以需要URL编码，是因为出于兼容性考虑，很多服务器只识别ASCII字符。但如果URL中包含中文、日文这些非ASCII字符怎么办？不要紧，URL编码有一套规则：
 *  如果字符是A~Z，a~z，0~9以及-、_、.、*，则保持不变；
 *  如果是其他字符，先转换为UTF-8编码，然后对每个字节以%XX表示。
 *  例如：字符中的UTF-8编码是0xe4b8ad，因此，它的URL编码是%E4%B8%AD。URL编码总是大写。
 *  ①BASE64 严格地说，属于编码格式，而非加密算法
 *  Base64是网络上最常见的用于传输8Bit字节代码的编码方式之一，大家可以查看RFC2045～RFC2049，上面有MIME的详细规范。Base64编码可用于在HTTP环境下传递较长的标识信息。
 *  例如，在Java Persistence系统Hibernate中，就采用了Base64来将一个较长的唯一标识符（一般为128-bit的UUID）编码为一个字符串，用作HTTP表单和HTTP GET URL中的参数。
 *  在其他应用程序中，也常常需要把二进制数据编码为适合放在URL（包括隐藏表单域）中的形式。此时，采用Base64编码具有不可读性，即所编码的数据不会被人用肉眼所直接看到
 *  https://www.liaoxuefeng.com/wiki/1252599548343744/1304227703947297
 *  比特币里面的区块链使用的是Base58编码，还有Bech32编码。
 *  Bech32编码实际上由两部分组成：一部分是bc这样的前缀，被称为HRP（Human Readable Part，用户可读部分），另一部分是特殊的Base32编码，使用字母表qpzry9x8gf2tvdw0s3jn54khce6mua7l，中间用1连接。对一个公钥进行Bech32编码的代码如下：
 */
public class Base64Test {
	public static void main(String[] args) {
		try {
			/*
			 * 上述代码的运行结果是%E4%B8%AD%E6%96%87%21，中的URL编码是%E4%B8%AD，文的URL编码是%E6%96%87，
			 * !虽然是ASCII字符，也要对其编码为%21。*是不会被编码的
			 * 和标准的URL编码稍有不同，URLEncoder把空格字符编码成+，而现在的URL编码标准要求空格被编码为%20，不过，服务器都可以处理这两种情况。
			 * 如果服务器收到URL编码的字符串，就可以对其进行解码，还原成原始字符串。Java标准库的URLDecoder就可以解码：
			 * 要特别注意：URL编码是编码算法，不是加密算法。URL编码的目的是把任意文本数据编码为%前缀表示的文本，编码后的文本仅包含A~Z，a~z，0~9，-，_，.，*和%，便于浏览器和服务器处理。
			 * URL编码原理:
			 * 编码原理
			 * 1、将需要转换的内容（ASCII码形式之外的内容），用十六进制表示法转换出来，并在之前加上%开头
			 * eg:  0x9c  URLEncoder --> %9c
			 * 2、内容中的空格‘ ’ ,全部用+代替
			 * 3、注：与Hex不同，Hex是将所有的字符转换为16进制表示，而URLEncoder是将ASCII码集之外的转换为%加上相应的16进制，而ASCII码集内的字符不进行处理
			 * 
			 * java怎么把中文变成16进制？这好像设计Unicode和UTF-8的关系
			 */
			String encoded = URLEncoder.encode("中文!*", "UTF-8");//Java标准库提供了一个URLEncoder类来对任意字符串进行URL编码：
			System.out.println("Java标准库提供了一个URLEncoder类来对任意字符串进行URL编码：" + encoded);
			
			//如果服务器收到URL编码的字符串，就可以对其进行解码，还原成原始字符串。Java标准库的URLDecoder就可以解码：
			String decoded = URLDecoder.decode("%E4%B8%AD%E6%96%87%21*", "UTF-8");
			/**
			 * 5. 【强制】不能使用过时的类或方法。 说明：java.net.URLDecoder 中的方法decode(String encodeStr) 这个方法已经过时，应该使用双参数decode(String source, String encode)。
			 * 接口提供方既然明确是过时接口，那么有义务同时提供新的接口；作为调用方来说，有义务去考证过时方法的新实现是什么。
			 */
		    System.out.println("Java标准库的URLDecoder就可以解码：" + decoded);
		    
		    String strHex = "中";
		    byte[] strArr = strHex.getBytes();
		    byte num = 110;
		    int i = num;
		    System.out.println(i);
		    
		    byte num0 = strArr[0];
		    int i0 = num0;
		    System.out.println(i0);
		    //将byte转换为二进制看看
		    System.out.println("转换为二进制:为什么这么长11111111111111111111111111100100:" + Integer.toBinaryString(strArr[0]));
		    System.out.println("转换为二进制:为什么这么长11111111111111111111111111100100:" + Integer.toBinaryString(-28));
		    
		    System.out.println("e4:" + Integer.toHexString(strArr[0]));
		    System.out.println("b8:" + Integer.toHexString(strArr[1]));
		    System.out.println("ad:" + Integer.toHexString(strArr[2]));
		    System.out.println("中这个字转换为字节之后,占几个字节?" + strArr.length);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		/*
		 * Base64编码
		 * URL编码是对字符进行编码，表示成%xx的形式，而Base64编码是对二进制数据进行编码，表示成文本格式。
		 * Base64编码可以把任意长度的二进制数据变为纯文本，且只包含A~Z、a~z、0~9、+、/、=这些字符。它的原理是把3字节的二进制数据按6bit一组，用4个int整数表示，
		 * 然后查表，把int整数用索引对应到字符，得到编码后的字符串。举个例子：3个byte数据分别是e4、b8、ad，按6bit分组得到39、0b、22和2d：
		 * 举个例子：3个byte数据分别是e4、b8、ad，按6bit分组得到十六进制的39(对应十进制为57,对应Base64码表为5)、0b(对应十进制为11,对应Base64码表为L)、
		 * 22(对应十进制为34,对应Base64码表为i)和2d(对应十进制为34,对应Base64码表为t)：
			┌───────────────┬───────────────┬───────────────┐
			│      e4       │      b8       │      ad       │
			└───────────────┴───────────────┴───────────────┘
			┌─┬─┬─┬─┬─┬─┬─┬─┬─┬─┬─┬─┬─┬─┬─┬─┬─┬─┬─┬─┬─┬─┬─┬─┐
			│1│1│1│0│0│1│0│0│1│0│1│1│1│0│0│0│1│0│1│0│1│1│0│1│
			└─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┘
			┌───────────┬───────────┬───────────┬───────────┐
			│    39     │    0b     │    22     │    2d     │
			└───────────┴───────────┴───────────┴───────────┘
		 * 因为6位整数的范围总是0~63，所以，能用64个字符表示：字符A~Z对应索引0~25，字符a~z对应索引26~51，字符0~9对应索引52~61，最后两个索引62、63分别用字符+和/表示。
		 * 在Java中，二进制数据就是byte[]数组。Java标准库提供了Base64来对byte[]数组进行编解码：
		 * https://mp.weixin.qq.com/s/TcSNPY1a6z8kP76usH6dCA
		 * 但因为 Base64 是每 3 个原始字符编码成 4 个字符，不够时补 =（下文会详述），因此编码后的大小是有可能会比原文件大的，所以 html 用 Base64 来展示图片而不是用具体的图片好处大概就只有少建立一条 http 连接以及少一个 http 请求（在 HTTP 1.1 以下），这种办法只有大量的小图片才有优越性了。
		 * 更多 Base64 的信息，请查看：http://zh.wikipedia.org/zh/base64
		 */
		int num16 = 0xe4;
		int num161 = 0xb8;
		int num162 = 0xad;
		System.out.println("16进制0xe4:的值是多少" + num16);
		System.out.println("16进制0xb8:的值是多少" + num161);
		System.out.println("16进制0xad:的值是多少" + num162);
		
		byte[] input = new byte[] { (byte) 0xe4, (byte) 0xb8, (byte) 0xad };//三个字节
		//在Java中，二进制数据就是byte[]数组。Java标准库提供了Base64来对byte[]数组进行编解码：
        String b64encoded = Base64.getEncoder().encodeToString(input);
        System.out.println("Base64编码把三个字节的数据用4个int整数表示:" + b64encoded);
		//编码后得到5Lit4个字符。要对Base64解码，仍然用Base64这个类：
        byte[] output = Base64.getDecoder().decode("5Lit");
        System.out.println("0xe4转换成int明明是228:输出之后为啥变成-28了?好像是把最高位舍去了" + (byte)output[0]);
        System.out.println("0xe4转换成int明明是228:输出之后为啥变成-28了?好像是把最高位舍去了" + (int)output[0]);
        byte test = 0x4e;
        System.out.println("byte的值为:78,这是为什么?" + test);
        System.out.println("byte的值为:" + String.valueOf(test));
        System.out.println("Base64解码:" + Arrays.toString(output)); // [-28, -72, -83]
        /*
         * 有的童鞋会问：如果输入的byte[]数组长度不是3的整数倍肿么办？这种情况下，需要对输入的末尾补一个或两个0x00，编码后，在结尾加一个=表示补充了1个0x00，
         * 加两个=表示补充了2个0x00，解码的时候，去掉末尾补充的一个或两个0x00即可。
         * 实际上，因为编码后的长度加上=总是4的倍数，所以即使不加=也可以计算出原始输入的byte[]。
         * Base64编码的时候可以用withoutPadding()去掉=，解码出来的结果是一样的：
         */
        byte[] input1 = new byte[] { (byte) 0xe4, (byte) 0xb8, (byte) 0xad, 0x21 };
        String b64encoded1 = Base64.getEncoder().encodeToString(input1);
        String b64encoded2 = Base64.getEncoder().withoutPadding().encodeToString(input1);
        System.out.println("" + b64encoded1);
        System.out.println("withoutPadding:" + b64encoded2);
        byte[] output1 = Base64.getDecoder().decode(b64encoded2);
        System.out.println("withoutPadding解码:" + Arrays.toString(output1));
        
        /*
         * 因为标准的Base64编码会出现+、/和=，所以不适合把Base64编码后的字符串放到URL中。一种针对URL的Base64编码可以在URL中使用的Base64编码，它仅仅是把+变成-，/变成_：
         * Base64编码的目的是把二进制数据变成文本格式，这样在很多文本中就可以处理二进制数据。例如，电子邮件协议就是文本协议，如果要在电子邮件中添加一个二进制文件，就可以用Base64编码，然后以文本的形式传送。
         * Base64编码的缺点是传输效率会降低，因为它把原始数据的长度增加了1/3。Base64编码的目的是把任意二进制数据编码为文本，但编码后数据量会增加1/3。
         * 和URL编码一样，Base64编码是一种编码算法，不是加密算法。
         * 如果把Base64的64个字符编码表换成32个、48个或者58个，就可以使用Base32编码，Base48编码和Base58编码。字符越少，编码的效率就会越低。
         */
        byte[] input3 = new byte[] { 0x01, 0x02, 0x7f, 0x00 };
        String b64encoded3 = Base64.getUrlEncoder().encodeToString(input3);
        System.out.println("针对URL的Base64编码:"+ b64encoded3);
        byte[] output3 = Base64.getUrlDecoder().decode(b64encoded3);
        System.out.println("针对URL的Base64解码:" + Arrays.toString(output3));
        
		/**
		 * Base64加密最好多加密几次,多加密几次,加密后的字符长度太长了,
		 * 多次加密后最好再用MD5再加一次,MD5加密后的的密钥长度只有32位
		 */
		String password = "Base64这个类是JDK1.8加入的一个类,用于加密使用,我是密码请帮忙我加密";
		String pwStr = Base64.getEncoder().encodeToString(password.getBytes());
		System.out.println("加密后的字符串:" + pwStr);
		
		byte [] pwArr = Base64.getDecoder().decode(pwStr);
		System.out.println("解密后的字符串:" + new String(pwArr));
		
		/*
		 * BASE64的加密解密是双向的，可以求反解.
		 * BASE64Encoder和BASE64Decoder是非官方JDK实现类。虽然可以在JDK里能找到并使用，但是在API里查不到。
		 * JRE 中 sun 和 com.sun 开头包的类都是未被文档化的，他们属于 java, javax 类库的基础，其中的实现大多数与底层平台有关，
		 * 一般来说是不推荐使用的。 BASE64 严格地说，属于编码格式，而非加密算法 .主要就是BASE64Encoder、BASE64Decoder两个类，我们只需要知道使用对应的方法即可。
		 * 另，BASE加密后产生的字节位数是8的倍数，如果不够位数以=符号填充
		 * BASE64 
		 * 按照RFC2045的定义，Base64被定义为：Base64内容传送编码被设计用来把任意序列的8位字节描述为一种不易被人直接识别的形式。
		 * （The Base64 Content-Transfer-Encoding is designed to represent arbitrary sequences of octets in a form that need not be humanly readable.） 
		 * 常见于邮件、http加密，截取http信息，你就会发现登录操作的用户名、密码字段通过BASE64加密的。
		 * http://www.jfh.com/jfperiodical/article/818
		 */
		String str = "12345678";
		//加密
		String stsPs = new BASE64Encoder().encodeBuffer(str.getBytes());
		System.out.println("sun.misc.BASE64Encoder加密后的数据:" + stsPs);
		String stsPs2 = Base64Test.encryptBASE64(str.getBytes());
		System.out.println("sun.misc.BASE64Encoder加密后的数据::静态方法" + stsPs2);
		try {
			byte[] res = new BASE64Decoder().decodeBuffer(stsPs);
			String originStr = new String(res);
			System.out.println("sun.misc.BASE64Decoder解密后的数据:" + originStr);
			
			byte[] res2 = Base64Test.decryptBASE64(stsPs2);
			String originSt2r = new String(res2);
			System.out.println("sun.misc.BASE64Decoder解密后的数据:静态方法" + originSt2r);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Base64加密
	 * @param key
	 * @return
	 */
	public static String encryptBASE64(byte[] key) {
		return new BASE64Encoder().encodeBuffer(key);
	}
	
	/**
	 * Base64解密
	 * @param key
	 * @return
	 * @throws IOException 
	 */
	public static byte[] decryptBASE64(String key) throws IOException {
		return new BASE64Decoder().decodeBuffer(key);
	}
}
