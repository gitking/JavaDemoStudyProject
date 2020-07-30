package com.yale.test.timer;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/*
 * 使用旧的Date对象时，我们用SimpleDateFormat进行格式化显示。使用新的LocalDateTime或ZonedLocalDateTime时，我们要进行格式化显示，就要使用DateTimeFormatter。
 * 和SimpleDateFormat不同的是，DateTimeFormatter不但是不变对象，它还是线程安全的。线程的概念我们会在后面涉及到。
 * 现在我们只需要记住：因为SimpleDateFormat不是线程安全的，使用的时候，只能在方法内部创建新的局部变量。而DateTimeFormatter可以只创建一个实例，到处引用。
 * 创建DateTimeFormatter时，我们仍然通过传入格式化字符串实现：
 */
public class DateTimeFormatterDemo {
	public static void main(String[] args) {
		//格式化字符串的使用方式与SimpleDateFormat完全一致。
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		//另一种创建DateTimeFormatter的方法是，传入格式化字符串时，同时指定Locale：
		DateTimeFormatter formatterLocal = DateTimeFormatter.ofPattern("E, yyyy-MMMM-dd HH:mm", Locale.US);
		
		//这种方式可以按照Locale默认习惯格式化。我们来看实际效果：
		ZonedDateTime zdt = ZonedDateTime.now();
		DateTimeFormatter form = DateTimeFormatter.ofPattern("yyyy-MM-dd'T' HH:mm ZZZZ");
		System.out.println("将日期格式化为字符串:" + form.format(zdt));
		
		DateTimeFormatter zhFormatter = DateTimeFormatter.ofPattern("yyyy MMM dd EE HH:mm", Locale.CHINA);
		System.out.println("按指定的Locale格式化:" + zhFormatter.format(zdt));
		
		DateTimeFormatter usFormatter = DateTimeFormatter.ofPattern("E, MMMM/dd/yyyy HH:mm", Locale.US);
		System.out.println("按照指定的美国格式化:" + usFormatter.format(zdt));
		/*
		 * 当我们直接调用System.out.println()对一个ZonedDateTime或者LocalDateTime实例进行打印的时候，实际上，调用的是它们的toString()方法，默认的toString()方法显示的字符串就是按照ISO 8601格式显示的，我们可以通过DateTimeFormatter预定义的几个静态变量来引用：
		 * 在格式化字符串中，如果需要输出固定字符，可以用'xxx'表示。
		 */
		LocalDateTime lt = LocalDateTime.now();
		System.out.println("静态ISO_DATE方法:" + DateTimeFormatter.ISO_DATE.format(lt));
		System.out.println("静态ISO_DATE_TIME方法:" + DateTimeFormatter.ISO_DATE_TIME.format(lt));
	}
}
