package com.yale.test.timer;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
	public static void main(String[] args) {
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
