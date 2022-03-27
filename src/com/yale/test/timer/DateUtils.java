package com.yale.test.timer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.ImmutableTriple;

/**
 * 【强制】SimpleDateFormat 是线程不安全的类，一般不要定义为static变量，如果定义为static，必须加锁，或者使用DateUtils工具类。
	正例：注意线程安全，使用DateUtils。亦推荐如下处理：
	private static final ThreadLocal<DateFormat> df = new ThreadLocal<DateFormat>() {
		@Override
		protected DateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd");
		}
	};
	说明：如果是JDK8的应用，可以使用Instant代替Date，LocalDateTime代替Calendar，DateTimeFormatter代替SimpleDateFormat，
	官方给出的解释：simple beautiful strong immutable thread-safe。
	《阿里巴巴Java开发手册（泰山版）.pdf》
 * 4. 【强制】不允许在程序任何地方中使用：1）java.sql.Date。 2）java.sql.Time。 3）java.sql.Timestamp。 
 * 说明：第1个不记录时间，getHours()抛出异常；第2个不记录日期，getYear()抛出异常；
 * 第3个在构造方法super((time/1000)*1000)，在Timestamp 属性fastTime和nanos分别存储秒和纳秒信息。
 * 反例： java.util.Date.after(Date)进行时间比较时，当入参是java.sql.Timestamp时，会触发JDK BUG(JDK9已修复)，可能导致比较时的意外结果。
 * 《阿里巴巴Java开发手册嵩山版2020.pdf》
 * @author dell
 *
 */
public class DateUtils {
	public static void main(String[] args) {
		Date date1 = new Date();
		System.out.println("直接输出日期对象:" + date1);
		
		long dateLong = System.currentTimeMillis();
		Date dateSec = new Date(dateLong);
		System.out.println("将long值转换为日期:" + dateSec);
		
		System.out.println("将日期值转换为long:" + dateSec.getTime());
		
		System.out.println("java.util.Date中的很多方法都被jdk官方标记为过期方法,不建议使用了,jdk官方建议使用Calendar.set(year + 1900, month, date)或者GregorianCalendar(year + 1900, month, date)");
		System.out.println("建议使用什么方法代替,自己看源码注释就行了");
		
		
		System.out.println("日期格式化在java.text包中,日期格式化要用到几个日期标记,日期标记在java.text.SimpleDateFormat的api上面都有,自己去看官方API就行");
		String dateStr = "yyyy-MM-dd HH:mm:ss.SSS";
		SimpleDateFormat sdf = new SimpleDateFormat(dateStr);
		String val = sdf.format(date1);
		System.out.println("将日期格式化为字符串:" + val);
		
		String dateString = "2020-06-07 10:25:00.987";
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		try {
			Date strDate = sdfDate.parse(dateString);
			System.out.println("parse方法将字符串的日期转换为日期类型的对象:" +   strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		Date date = new Date();
		/**
		 * 如果指定周年'Y' Week year 并且日历不支持任何周年，则改用日历年（'y'）。 可以通过调用getCalendar（）。isWeekDateSupported（）测试星期的支持。
		 * https://docs.oracle.com/javase/7/docs/api/index.html
		 * Character.isDigit（char）
		 * 日期时间《阿里巴巴Java开发手册嵩山版2020.pdf》
		 * 1. 【强制】日期格式化时，传入pattern中表示年份统一使用小写的y。 
		 * 说明：日期格式化时，yyyy表示当天所在的年，而大写的YYYY代表是week in which year（JDK7之后引入的概念），
		 * 意思是当天所在的周属于的年份，一周从周日开始，周六结束，只要本周跨年，返回的YYYY就是下一年。 
		 * 正例：表示日期和时间的格式如下所示： new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
		 * 2.【强制】在日期格式中分清楚大写的M和小写的m，大写的H和小写的h分别指代的意义。 
		 * 说明：日期格式中的这两对字母表意如下： 1） 表示月份是大写的M； 2） 表示分钟则是小写的m； 3） 24小时制的是大写的H； 4） 12小时制的则是小写的h。
		 */
		SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");
		
		System.out.println("打印JAVA所有的配置信息,打印JVM所有的配置信息,打印JVM所有的环境变量信息");
		System.getProperties().list(System.out);
		
		System.out.println("jdk6没有大写的日期Y,大写Y,JDK8才有," + System.getProperty("java.class.version"));

		System.out.println("jdk6没有大写的日期Y,大写的Y,JDK8才有," + dateFormat.format(date));
		System.out.println("y 是Year,  Y 表示的是Week year.经过试验，得出的结果如下：Week year 意思是当天所在的周属于的年份，一周从周日开始，周六结束，只要本周跨年，那么这周就算入下一年。这周三就跨年了，所以这一整个周都跨年了。2019年12月31日11:08:16");
		/**
		 * JAVA官方API文档:https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html#patterns
		 */
		
		/**
		 * 《Head First Java》
		 * 要取得当前的日期时间就用Date,其余功能可以从Calendar上找,Calendar这个API的设计者打算要做全球化的思考,基本的想法是当你要操作日期时
		 * 你会 要求一个Calendar,然后Java虚拟机会给你一个Calendar的子类实例,Clandar是个抽象类,有意思的是你取的是Calendar是符合所在地区(locate)特性的。
		 * 通常大部分的地区适用公历,但你也有可能处于使用农历或其他特殊格式的情况,此时你可以让java函数库来处理这一类的日期。
		 * java.util.GregorianClaendar《Head First Java》书上用的是这个类
		 * Calendar可以让你将日期转换成毫秒的表示法,或将毫秒转换成日期。更精确的说法是相对于1970年1月1日的毫秒数,因此你可以执行精确的相对时间计算。
		 */
		Calendar cal = Calendar.getInstance();
		cal.set(2004, 1, 7, 15, 40);//将时间设定为2004年1月7日15:40
		long day1 = cal.getTimeInMillis();//将时间转换为毫秒
		day1+= 1000 * 60 *60;
		cal.setTimeInMillis(day1);//将时间加上一个小时
		System.out.println("现在的hour小时为:" + cal.get(Calendar.HOUR_OF_DAY));
		cal.add(Calendar.DATE, 35);//加上35天
		System.out.println("加上35天之后" + cal.getTime());
		cal.roll(Calendar.DATE, 35);//滚动35天,注意只有日期会滚动,月份不会滚动
		System.out.println("滚动35天之后" + cal.getTime());
		cal.set(Calendar.DATE, 1);//直接设定DATE的值
		System.out.println("set date to 1:" + cal.getTime());
		
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
		String dateStr1 = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");//date类型转换String类型
		System.out.println("使用apache的工具类处理日期:" + dateStr1);
		
		///Date strToDate = org.apache.commons.lang.time.DateUtils.parseDate("2021-05-01 01:01:01", "yyyy-MM-dd HH:mm:ss");
		Date dateAdd = org.apache.commons.lang.time.DateUtils.addHours(new Date(), 1);//计算一个小时候的日期
		
		/*
		 * 包装临时对象
		 * 当一个方法需要返回两个及以上字段时，我们一般会封装成一个临时对象返回，现在有了Pair和Triple就不需要了
		 * commons-lang3-3.12.0.jar 官方文档:https://commons.apache.org/proper/commons-lang/
		 * commons-lang3-3.12.0.jar 支持jdk8和更高的版本The current release [Java 8 and up]
		 */
		ImmutablePair<Integer, String> pari = ImmutablePair.of(1, "yideng");
		System.out.println(pari.getLeft() + "," + pari.getRight());
		
		ImmutableTriple<Integer, String, Date> triple = ImmutableTriple.of(1, "yideng", new Date());//返回三个字段
		System.out.println(triple.getLeft() + "," + triple.getMiddle() + "," + triple.getRight());
		
	}
}
