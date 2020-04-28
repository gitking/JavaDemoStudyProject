package com.yale.test.regex;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式练习
 * @author lenovo
 *
 */
public class RegexTestEg {
	public static void main(String[] args) throws ParseException {
		System.out.println("[]中括号表示范围,[abc]表示在abc这个中间就可以:" + "a".matches("[abc]"));
		System.out.println("[]中括号表示范围^表示取反,[^abc]表示不在abc这个中间就可以:" + "x".matches("[^abc]"));
		System.out.println("[0-9]表示只能是数字:" + "9".matches("[0-9]"));
		System.out.println("[0-9a-zA-Z]表示只能是数字或英文字母并且不区分大小写:" + "F".matches("[0-9a-zA-Z]"));
		System.out.println("************下面展示简化表达式,即上面的简写方式************");
		System.out.println(".点这个字符表示任意一个字符:" + "/".matches("."));
		System.out.println(".点这个字符表示任意一个字符:" + ".".matches("."));
		System.out.println(".点这个字符表示任意一个字符:" + ".".matches("\\."));


		System.out.println("\\d表示任意一个数字,等价于[0-9]:" + "9".matches("\\d"));
		System.out.println("\\D表示任意一个不是数字的字符,等价取反的[^0-9]:" + ",".matches("\\D"));
		System.out.println("\\s表示是一位空格,可能是空格,\\t,\\n:" + "\n".matches("\\s"));
		System.out.println("\\S表示不是一位空格:" + "\t".matches("\\S"));
		System.out.println("\\w等价于[a-zA-Z0-9_]表示字母、数字、下划线:" + "_".matches("\\w"));
		System.out.println("\\W等价于[^a-zA-Z0-9_]就是\\w的取反操作:" + "_".matches("\\W"));
		System.out.println("************边界匹配,边界匹配JAVA用不到,js才能用到,js必须写边界字符即：^表示开始,$表示结束************");
		System.out.println("\\W等价于[^a-zA-Z0-9_]就是\\w的取反操作:" + ",".matches("^\\W$"));
		System.out.println("************下面展示数量表达************");
		System.out.println("正则?,?问号表示前面的正则匹配0次或1次都行:" + "a".matches("\\w?"));
		System.out.println("正则+,+加号表示前面的正则匹配1次或多次都行:" + "11111111111a".matches("\\w+"));
		System.out.println("正则*,*星号表示前面的正则匹配0次或1次或多次都行,其实就是随便多少次都行,爱出现不出现:" + "".matches("\\w*"));
		System.out.println("正则{n},{n}表示前面的正则匹配正好出现n次:" + "666666".matches("\\d{6}"));
		System.out.println("正则{n,},{n}表示前面的正则匹配出现至少n次以上:" + "66666677".matches("\\d{6,}"));
		System.out.println("正则{n,m},{n,m}表示前面的正则匹配出现n到m次:" + "6666667557".matches("\\d{6,9}"));
		System.out.println("************逻辑匹配************");
		System.out.println("正则A正则B,表示并且的关系,在第一个匹配之后立即匹配第二个:" + "6qweasd".matches("\\d?\\w{6}"));
		System.out.println("正则A正则B,表示并且的关系,在第一个匹配之后立即匹配第二个:" + "qweasd".matches("\\d?\\w{6}"));
		System.out.println("正则A|正则B,表示满足任意一个正则就可以:" + "a123".matches("\\d{6,9}|\\w+\\d{3}"));
		System.out.println("正则A|正则B,表示满足任意一个正则就可以:" + "1234567".matches("\\d{6,9}|\\w+\\d{3}"));
		System.out.println("匹配一个斜杠\\" + "\\".matches("\\\\"));


		System.out.println("(正则):表示按照一组正则匹配处理");
		
		System.out.println("*******************String类的对正则的支持*************************");
		System.out.println("=====================String的正则替换=============");
		String strA = "adsfzcadfa()12zxc!@#5323412zxc12zxxcsadf@#$5s12zxc";
		String regexStr = "[^a-zA-Z]";
		System.out.println("找到strA里面的所有英文字母:" +  strA.replaceAll(regexStr, ""));
		System.out.println("=====================String的正则拆分=============");
		String strNum = "a1s21zxc12zxctqwezxc11^3ez";//以数字进行拆分
		String regexNum = "\\d+";
		String [] strNumArr = strNum.split(regexNum);
		for (int i=0; i<strNumArr.length; i++) {
			System.out.println(strNumArr[i]);
		}
		System.out.println("==================字符串的验证=======");
		System.out.println("验证字符串是否为数字(整数或小数或负数)");
		String str = "-1100.09";
		String regexDou = "(-)?\\d+(\\.\\d+)?";//这里用到了正则里面的括号了,括号表示一整块正则表达
		if (str.matches(regexDou)) {
			double dou = Double.parseDouble(str);
			System.out.println(dou);
		} else {
			System.out.println("不是数字");
		}
		System.out.println("验证一个字符是否为日期或者为日期时间");
		String strDate = "2016-09-12 15:09:59";
		String regexDate = "\\d{4}-\\d{2}-\\d{2}";
		String regexDateSec = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}";
		if (strDate.matches(regexDate)) {
			System.out.println(new SimpleDateFormat("yyyy-MM-dd").parse(strDate));
		} else if (strDate.matches(regexDateSec)) {
			System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(strDate));
		}
		System.out.println("验证电话号码是否正确");
		String strPhoneNum = "51283346";
		String regPhoneNum = "\\d{7,8}";
		if (strPhoneNum.matches(regPhoneNum)) {
			System.out.println(strPhoneNum + "是合法的电话号码");
		} else {
			System.out.println(strPhoneNum + "不是合法的电话号码");
		}
		String strPhoneNum01 = "01051283346";
		String regPhoneNum01 = "(\\d{3,4})?\\d{7,8}";
		if (strPhoneNum01.matches(regPhoneNum01)) {
			System.out.println(strPhoneNum01 + "是合法的电话号码");
		} else {
			System.out.println(strPhoneNum01 + "不是合法的电话号码");
		}
		String strPhoneNum02 = "(010)-51283346";
		String regPhoneNum02 = "(\\(\\d{3,4}\\)-)?\\d{7,8}";
		if (strPhoneNum02.matches(regPhoneNum02)) {
			System.out.println(strPhoneNum02 + "是合法的电话号码");
		} else {
			System.out.println(strPhoneNum02 + "不是合法的电话号码");
		}
		System.out.println("验证字符串是否是一个合法的email地址");
		String emailStr = "amldn88-1.2@mldnjava.cn";
		String emailReg = "[a-zA-Z][a-zA-Z\\._\\-0-9]{5,14}@[a-zA-Z\\._\\-0-9]+\\.(cn|com|net|edu)";
		if (emailStr.matches(emailReg)) {
			System.out.println(emailStr + "是一个合法的email地址");
		} else {
			System.out.println(emailStr + "不是一个合法的email地址");
		}
		System.out.println("对开发者而言,正则的重大意义在于字符串的组成验证处理上");
		
		System.out.println("JDK1.4里面增加了一个开发包java.uti.regex,但是在这个包里面只有俩个类:Pattern类,Matcher类.Pattern类负责编译正则,"
				+ "而Matcher负责进行正则匹配.如果要做一般的处理,使用String类即可.");
		
		String strPat = "a|b|c";
		String regexPat = "\\|";
		Pattern pat = Pattern.compile(regexPat);
		String [] strPatArr = pat.split(strPat);
		for (int i=0; i < strPatArr.length; i++) {
			System.out.println(strPatArr[i]);
		}
		
		
		String strPatNum = "100";
		String regexPatNum = "\\d+";
		Pattern patNum = Pattern.compile(regexPatNum);
		Matcher mat = patNum.matcher(strPatNum);
		System.out.println("但是这么写还不如直接用String自带的方法:" + mat.matches());
		System.out.println("在进行一些复杂的正则操作里面,String类是完成不了的,必须通过Matcher类处理,因为这里面有一个分组的概念.");
		System.out.println("比如下面这个,String类就处理不了");
		String strMat  = "insert into table values (#{table.name},#{table.age},#{table.sex})";
		String regexMat = "#\\{[a-zA-Z\\._\\-]+\\}";
		Pattern patMat = Pattern.compile(regexMat);
		Matcher matcher = patMat.matcher(strMat);
		while (matcher.find()) {
			System.out.println(matcher.group(0));
		}
	}
}
