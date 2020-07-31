package com.yale.test.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTest {
	public static void main(String[] args) {
		System.out.println("abc是数字吗? == " + isNumber("abc"));
		System.out.println("123是数字吗? == " + isNumber("123"));
		
		System.out.println("用正则表达来校验字符串是否是纯数字,一行代码就解决了,不用自己再写一个方法了。");
		System.out.println("abc".matches("\\d+"));
		System.out.println("1230".matches("\\d+"));
		
		String regex = "20\\d\\d";
		//年份是否是20##年
	    System.out.println("2019".matches(regex)); // true
	    System.out.println("2100".matches(regex)); // false
		
		System.out.println("java.util是JAVA的一个工具包,可以研究一下");
		
		/*
		 * -这个字符在正则表达式里面也是一个特殊字符,所以需要转义,但其实这里不转义也行
		 */
		Pattern p = Pattern.compile("(\\d{3,4})\\-(\\d{7,8})");
        Matcher m = p.matcher("010-12345678");
        if (m.matches()) {
            String g0 = m.group(0);
            System.out.println("整个字符串:" + g0);
            String g1 = m.group(1);
            String g2 = m.group(2);
            System.out.println(g1);
            System.out.println(g2);
        } else {
            System.out.println("匹配失败!");
        }
        
        /*
         * 在介绍非贪婪匹配前，我们先看一个简单的问题：
         * 给定一个字符串表示的数字，判断该数字末尾0的个数。例如：
         * "123000"：3个0
    	 * "10100"：2个0
    	 * "1001"：0个0
		 * 可以很容易地写出该正则表达式：(\d+)(0*)，Java代码如下：
         */
        Pattern pattern = Pattern.compile("(\\d+)(0*)");
        Matcher matcher = pattern.matcher("1230000");
        if (matcher.matches()) {
            System.out.println("group1=" + matcher.group(1)); // "1230000"
            System.out.println("group2=" + matcher.group(2)); // ""
            
            System.out.println("仔细观察上述实际匹配结果，实际上它是完全合理的，因为\\d+确实可以匹配后面任意个0。");
            System.out.println("这是因为正则表达式默认使用贪婪匹配：任何一个规则，它总是尽可能多地向后匹配，因此，\\d+总是会把后面的0包含进来。");
            System.out.println("要让\\d+尽量少匹配，让0*尽量多匹配，我们就必须让\\d+使用非贪婪匹配。在规则\\d+后面加个?即可表示非贪婪匹配。我们改写正则表达式如下：");
        }
        
        
        Pattern pattern1 = Pattern.compile("(\\d+?)(0*)");//因此，给定一个匹配规则，加上?后就变成了非贪婪匹配。
        Matcher matcher1 = pattern1.matcher("1230000");
        if (matcher1.matches()) {
            System.out.println("group1=" + matcher1.group(1)); // "1230000"
            System.out.println("group2=" + matcher1.group(2)); // ""
            
            System.out.println("仔细观察上述实际匹配结果，实际上它是完全合理的，因为\\d+确实可以匹配后面任意个0。");
            System.out.println("这是因为正则表达式默认使用贪婪匹配：任何一个规则，它总是尽可能多地向后匹配，因此，\\d+总是会把后面的0包含进来。");
            System.out.println("要让\\d+尽量少匹配，让0*尽量多匹配，我们就必须让\\d+使用非贪婪匹配。在规则\\d+后面加个?即可表示非贪婪匹配。我们改写正则表达式如下：");
        }
        
        /*
         * 我们再来看这个正则表达式(\d??)(9*)，注意\d?表示匹配0个或1个数字，后面第二个?表示非贪婪匹配，因此，给定字符串"9999"，匹配到的两个子串分别是""和"9999"，
         * 因为对于\d?来说，可以匹配1个9，也可以匹配0个9，但是因为后面的?表示非贪婪匹配，它就会尽可能少的匹配，结果是匹配了0个9。
         */
        Pattern pattern2 = Pattern.compile("(\\d??)(9*)");
        Matcher matcher2 = pattern2.matcher("9999");
        if (matcher2.matches()) {
            System.out.println("group1=" + matcher2.group(1)); // "1230000"
            System.out.println("group2=" + matcher2.group(2)); // ""
        }
        
        /*
         * 搜索和替换
         * 分割字符串
         * 使用正则表达式分割字符串可以实现更加灵活的功能。String.split()方法传入的正是正则表达式。我们来看下面的代码：
         */
        
        System.out.println("a b c".split("\\s").toString()); // { "a", "b", "c" });
        System.out.println("a b  c".split("\\s").toString()); // { "a", "b", "", "c" }
        System.out.println("a, b ;; c".split("[\\,\\;\\s]+").toString()); // { "a", "b", "c" }
        
        
        /**
         * 如果我们想让用户输入一组标签，然后把标签提取出来，因为用户的输入往往是不规范的，这时，使用合适的正则表达式，就可以消除多个空格、混合,和;这些不规范的输入，直接提取出规范的字符串。
         * 搜索字符串
         * 使用正则表达式还可以搜索字符串，我们来看例子：
         * 我们获取到Matcher对象后，不需要调用matches()方法（因为匹配整个串肯定返回false），而是反复调用find()方法，在整个串中搜索能匹配上\\wo\\w规则的子串，并打印出来。
         * 这种方式比String.indexOf()要灵活得多，因为我们搜索的规则是3个字符：中间必须是o，前后两个必须是字符[A-Za-z0-9_]。
         */
        String s = "the quick brown fox jumps over the lazy dog.";
        Pattern pp = Pattern.compile("\\wo\\w");
        Matcher mf = pp.matcher(s);
        while (mf.find()) {
            String sub = s.substring(mf.start(), mf.end());
            System.out.println(sub);
        }
        
        /*
         * 替换字符串
         * 使用正则表达式替换字符串可以直接调用String.replaceAll()，它的第一个参数是正则表达式，第二个参数是待替换的字符串。我们还是来看例子：
         * 下面的代码把不规范的连续空格分隔的句子变成了规范的句子。可见，灵活使用正则表达式可以大大降低代码量。
         */
        String sr = "The     quick\t\t brown   fox  jumps   over the  lazy dog.";
        String r = sr.replaceAll("\\s+", " ");
        System.out.println(r); // "The quick brown fox jumps over the lazy dog."
        System.out.println("#############反向引用###########");
        /*
         * 反向引用
         * 如果我们要把搜索到的指定字符串按规则替换，比如前后各加一个<b>xxxx</b>，这个时候，使用replaceAll()的时候，
         * 我们传入的第二个参数可以使用$1、$2来反向引用匹配到的子串。例如：
         */
        String ssr = "the quick brown fox jumps over the lazy dog.";
        String rss = ssr.replaceAll("\\s([a-z]{4})\\s", " <b>$1</b> ");
        System.out.println(rss);
	}
	
	public static boolean isNumber (String str) {
		System.out.println("char跟int是可以相互转换的");
		char c = '你';
		System.out.println("直接输出字符:" + c);
		int ic = c;
		System.out.println("把chari强转成int:" + ic);
		char ci = 987;
		System.out.println("整形987代表字符u:" + ci);
		
		char [] charArr = str.toCharArray();
		for (int i=0; i < charArr.length; i++) {//这个也可以判断一个字符数是否都是由数字组成的
			if (charArr[i] > '9' || charArr[i] < '0') {
				return false;
			}
		}
		return true;
	}
}
