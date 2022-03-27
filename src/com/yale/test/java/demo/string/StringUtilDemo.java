package com.yale.test.java.demo.string;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;

/*
 * 关于String有很多别人的jar已经提供好的功能了
 * commons-lang-2.6.jar apache的这个jar就有很多关于String的工具类
 */
public class StringUtilDemo {
	public static void main(String[] args) {
		String smallStr = "";
		String bigStr = "";
		//比较俩个对象是否相等,如果用equals比较俩个对象是否相等,还要判断左边的对象不为null,否则可能会报空指针,可以使用java.util包下面的Objects类来比较
        if (Objects.equals(smallStr, bigStr)) {
        	
        }
        
        Objects.requireNonNull(smallStr);
        smallStr = Objects.toString(smallStr, "ssss");
        StringUtils.defaultIfBlank("15000", "15000");
        System.out.println("Objects.equals这个对象有个坑就是当俩个对象都为null的时候,返回的也是true:哈哈," + Objects.equals(null, null));
        System.out.println("Objects.equals这个对象有个坑就是当俩个对象都为null的时候,返回的也是true:哈哈," + Objects.deepEquals(null, null));
        
        
		String strLen = StringUtils.leftPad("左联通", 255, "联");
		System.out.println("strLen的字符长度" + strLen.length()); 
		System.out.println("StringUtils.leftPad的功能:" + strLen); 
		
		String randomStr = RandomStringUtils.randomNumeric(10000);
		System.out.println("randomStr的字符长度" + randomStr.length()); 
		System.out.println("生成随机数,都是由数字构成的随机数:之前在公司验车码就是用这个实现的:" + randomStr); 
		
		char[] numStr = new char[]{'0','1','2','3','4','5','6','7','8','9'};
		String randomStrLen = RandomStringUtils.random(4, numStr);

		System.out.println("randomStrLen的字符长度" + randomStrLen.length()); 
		System.out.println("从指定的字符数组里面,挑选,生成随机数:" + randomStrLen); 
		
		String leftStr = StringUtils.left("左联通", 2);
		System.out.println("leftStr的字符长度,截取字符串长度" + leftStr.length()); 
		System.out.println("从右边开始截取:" + leftStr); 
		
		/*
		 * 建议使用 apache commons工具类库，apache commons是最强大的，也是使用最广泛的工具类库，里面的子库非常多，下面介绍几个最常用的。
		 * commons-lang，java.lang的增强版,建议使用commons-lang3，优化了一些api，原来的commons-lang已停止更新.
		 * Maven依赖是：
		 * <dependency>  
	     *	<groupId>org.apache.commons</groupId>  
	     *	<artifactId>commons-lang3</artifactId>  
	     * 	<version>3.12.0</version>  
		 *	</dependency>  
		 */
		String str = "yideng";
		String capitalize = org.apache.commons.lang.StringUtils.capitalize(str);//首字母转换成大写
		System.out.println("首字母大写:" + capitalize);
		
		String repatStr = org.apache.commons.lang.StringUtils.repeat("ab", 2);
		System.out.println("重复字符串俩次:" + repatStr);
		
		List<String> strJoin = new ArrayList<String>();
		strJoin.add("A");
		strJoin.add("B");
		strJoin.add("C");
		System.out.println("将集合里面的元素以指定字符拼接在一起:" + StringUtils.join(strJoin, "++")); 
		
		System.out.println(StringUtils.deleteSpaces("5   4  0  6     2"));//去除空格
		System.out.println(StringUtils.deleteWhitespace("5   4  0  6     2"));//去除空格
		
		
		//校验字符串内容是否由数字组成
		System.out.println(StringUtils.isNumeric("54062"));
		System.out.println("这个方法不会忽略空格,所以返回false,->" + StringUtils.isNumeric("5   4  0  6     2"));
		System.out.println(".这个字符不是数字所以返回false->" + StringUtils.isNumeric("512.3"));
		System.out.println(StringUtils.isNumericSpace("54062"));
		System.out.println("忽略空格->" + StringUtils.isNumericSpace("5   4  0  6     2"));
		System.out.println(".这个字符不是数字所以返回false->" + StringUtils.isNumericSpace("512.3"));
		
		System.out.println("检查字符串是否仅包含 ASCII 可打印字符。 " + StringUtils.isAsciiPrintable("512.3"));
		System.out.println("检查字符串是否仅包含 unicode 字母、数字或空格 " + StringUtils.isAlphanumericSpace("512.3"));
		System.out.println("检查字符串是否仅包含 unicode 字母或数字。 " + StringUtils.isAlphanumeric("512.3"));
		System.out.println("检查字符串是否仅包含 unicode 字母和空格 (' ')。 " + StringUtils.isAlphaSpace("512.3"));
		System.out.println("检查字符串是否仅包含 unicode 字母。 " + StringUtils.isAlpha("512.3"));
		System.out.println("计算子字符串在较大字符串中出现的次数  " + StringUtils.countMatches("512.3","1"));
		System.out.println("将小写转换大写,将大写转化为小写" + StringUtils.swapCase("The dog has a BONE"));
	}
}
