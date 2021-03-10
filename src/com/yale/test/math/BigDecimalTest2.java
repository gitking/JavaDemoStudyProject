package com.yale.test.math;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * 
 * @author dell
 */
public class BigDecimalTest2 {
	
	/*
	 * 在这段代码中,有一个substring(),lPad()操作,这是因为Integer.toBinaryString(b)传入的虽然是byte,但是会转型为int,
	 * 如果byte的第一个bit位是1,则代表是负数,那么转换为int的高24位将会填充1,其实我们不需要这24位,所以用了substring(),
	 * 如果是正数,那么输出的字符串会将前面的0去掉,为了在显示上使用8位二进制对齐方式,所以在代码中用了lPad()。
	 * 《Java特种兵》上册,第24页,我怀疑他书上写错了,因为代码没用到substring啊.
	 */
	private static String lPad(String now, int expectLength, char paddingChar) {
		if (now == null || now.length() >= expectLength) {
			return now;
		}
		StringBuffer buf = new StringBuffer(expectLength);
		for (int i=0, paddingLength = expectLength - now.length(); i<paddingLength; i++) {
			buf.append(paddingChar);
		}
		return buf.append(now).toString();
	}
	public static void main(String[] args) {
		/*
		 * 数字能转换为二进制数据,又能还原成数字.大家可以用一些比较小的数字做测试(2,15,16,1024等)
		 * 不过在测试过程中,有同学发现高位出现整个字节的8位全是0的情况,例如255,输出结果是俩个字节的二进制字符串:0000000011111111
		 * 大家很疑惑:既然高8位全是0,为何还要单独拿一个byte来存放呢?有一点要注意了,因为255是正数,而一个字节中的最高位变成了1,如果直接用111111111来表达
		 * 就表示它是一个负数(具体值是-1),所以需要多一个字节来表达自己是正数.
		 * 有些同学设计表的主键pk列的时候,认为数字存储不下,其实一个long类型绝对可以存得下了,用数字往往计算速度更快,占用空间也很小
		 * 用long做主键PK,即使你的主键每纳秒增加一条数据,也可以存放292年的数据,注意注意:1秒= 1_000_000_000纳秒
		 * TimeUnitDemo.java
		 */
		BigDecimal bigDecimal = new BigDecimal("12332412332412332412332412332412332412332412332412332412332412332412332412332412332");
		System.out.println("数字的原始值是:" + bigDecimal);
		//bigDecimal = bigDecimal.add(BigDecimal.TEN);
		//System.out.println("添加10以后:" + bigDecimal);
		//转换成二进制数组
		byte[] bytes = bigDecimal.toBigInteger().toByteArray();
		for (byte b : bytes) {
			String bitString = lPad(Integer.toBinaryString(b & 0xff), 8, '0');
			System.out.println(bitString);
		}
		
		//还原结果
		BigInteger bigInteger = new BigInteger(bytes);
		System.out.println("还原结果为:" + bigInteger);
	}
}
