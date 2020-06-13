package com.yale.test.timer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
		 */
		SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");
		
		System.out.println("打印JAVA所有的配置信息,打印JVM所有的配置信息,打印JVM所有的环境变量信息");
		System.getProperties().list(System.out);
		
		System.out.println("jdk6没有大写的日期Y,JDK8才有," + System.getProperty("java.class.version"));

		System.out.println("jdk6没有大写的日期Y,JDK8才有," + dateFormat.format(date));
		System.out.println("y 是Year,  Y 表示的是Week year.经过试验，得出的结果如下：Week year 意思是当天所在的周属于的年份，一周从周日开始，周六结束，只要本周跨年，那么这周就算入下一年。这周三就跨年了，所以这一整个周都跨年了。2019年12月31日11:08:16");
		/**
		 * JAVA官方API文档:https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html#patterns
		 */
	}
}
