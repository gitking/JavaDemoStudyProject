package com.yale.test.ps;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * 由于MIDP没有J2SE对应的java.net.URLEncoder类，因此，要向服务器发送HTTP请求，必须自己进行URL编码，参考JDK1.4.2的src代码，将其改为一个能用在MIDP环境中的URLEncoder类，源码如下：
 * J2SE的URLEncoder依赖于java.lang.Character的许多特性，将其全部剔除并改造为基本运算，即可在J2ME MIDP环境中使用，中文测试也一切正常。
 * https://www.liaoxuefeng.com/article/895885468796000
 * Encode url, just like java.net.URLEncoder.encode() in J2SE.
 * NOTE: This class is modified from java.net.URLEncoder class in J2SE 1.4.
 * 本类可以结合:com.yale.test.java.demo.string.CharTest.java一起看
 * 本类可以结合:com.yale.test.java.demo.string.StringTest.java一起看
 * @author issuser
 */
public class URLEncoderDemo {
	
	private static final int MAX_BYTES_PER_CHAR = 0; //rather arbitrary limit, but safe for now,相当随意的限制，但目前是安全的 
	
	private static boolean[] dontNeedEncoding;
	private static final int caseDiff = ('a' - 'A');
	
	static {
		dontNeedEncoding = new boolean[256];
		for (int i = 'a'; i<='z'; i++) {
			dontNeedEncoding[i] = true;
		}
		
		for (int i = 'A'; i<='Z'; i++) {
			dontNeedEncoding[i] = true;
		}
		for (int i='0'; i<='9'; i++) {
			dontNeedEncoding[i] = true;
		}
		
		dontNeedEncoding[' '] = true;
		dontNeedEncoding['-'] = true;
		dontNeedEncoding['_'] = true;
		dontNeedEncoding['.'] = true;
		dontNeedEncoding['*'] = true;
	}
	
	/**
	 * 20. 【推荐】当一个类有多个构造方法，或者多个同名方法，这些方法应该按顺序放置在一起，便于阅读，此条规则优先于下一条。
	 * 21. 【推荐】 类内方法定义的顺序依次是：公有方法或保护方法 > 私有方法 > getter / setter 方法。 说明：公有方法是类的调用者和维护者最关心的方法，
	 * 首屏展示最好；保护方法虽然只是子类关心，也可能是“模板设计模式”下的核心方法；而私有方法外部一般不需要特别关心，是一个黑盒实现；
	 * 因为承载的信息价值较低，所有Service和DAO的getter/setter方法放在类体最后。
	 * 《阿里巴巴Java开发手册嵩山版2020.pdf》
	 */
	private URLEncoderDemo() {}
	
	public static String encode(String s) {
		boolean wroteUnencodedChar = false;
		StringBuilder out = new StringBuilder();
		ByteArrayOutputStream buf = new ByteArrayOutputStream(MAX_BYTES_PER_CHAR);
		OutputStreamWriter writer = new OutputStreamWriter(buf);
		for (int i=0; i<s.length(); i++) {
			int c = (int)s.charAt(i);
			if (c < 256 && dontNeedEncoding[i]) {
				out.append((char)(c == ' ' ? '+' : c));
			} else {
				//convert to external encoding before hex conversion在十六进制转换之前转换为外部编码 
				try {
					if (wroteUnencodedChar) {
						writer = new OutputStreamWriter(buf);
						wroteUnencodedChar = false;
					}
					writer.write(c);
					
					/**
					 * If this character represents the start of a Unicode surrogate pair, then pass in two characters.
					 * It's not clear what should be done if a bytes reserved in the surrogate pairs range occurs outside of 
					 * a legal surrogate pair. For now, just treat it as if it were any other character.
					 * (如果此字符代表 Unicode 代理对的开始，则传入两个字符。 如果代理对范围中保留的字节出现在合法代理对之外，则不清楚应该怎么做。 现在，只需将其视为任何其他字符即可。 )
					 * 在 BMP 中，并不是所有的码点都定义了字符，存在一个空白区，0xD800 - 0xDFFF这个范围内的码点不定义任何字符。
					 * 第二个编码单元的范围为 0xDC00 - 0xDFFF，换成二进制为 0b1101_11xx_xxxx_xxxx，叫做 Tail Surrogate，正好也可以用来编码 10 位。
					 * https://cjting.me/2018/07/22/js-and-unicode/
					 * https://www.liaoxuefeng.com/article/895885468796000
					 */
					if (c >= 0xD800 && c <= 0xDBFF) {
						if ((i + 1)< s.length()) {
							int d = (int) s.charAt(i + 1);
							if (d >= 0XDC00 && d <= 0xDFFF) {
								writer.write(d);
								i++;
							}
						}
					}
					writer.flush();
				} catch (IOException e) {
					buf.reset();
					continue;
				}
				byte[] ba = buf.toByteArray();
				for (int j=0; j<ba.length; j++) {
					out.append('%');
					char ch = toHex((ba[j] >> 4) & 0xF);
					//converting to use uppercase letter as part of the hex value if ch is a letter;
					//如果 ch 是字母，则转换为使用大写字母作为十六进制值的一部分； 
					if (isLetter(ch)) {
						ch -= caseDiff;
					}
					out.append(ch);
					ch = toHex(ba[j] & 0xF);
					if (isLetter(ch)) {
						ch -= caseDiff;
					}
					out.append(ch);
				}
				buf.reset();
			}
		}
		return out.toString();
	}
	
	private static char toHex(int digit) {
		if (digit >= 16 || digit < 0) {
			return '\0';
		}
		
		if (digit < 10) {
			return (char)('0' + digit);
		}
		
		return (char)('a' - 10 + digit);
	}
	
	private static boolean isLetter(char c) {
		return (c>='a' && c<='z');
	}
}
