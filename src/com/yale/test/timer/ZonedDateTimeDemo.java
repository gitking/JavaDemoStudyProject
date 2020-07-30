package com.yale.test.timer;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/*
 * LocalDateTime总是表示本地日期和时间，要表示一个带时区的日期和时间，我们就需要ZonedDateTime。
 * 可以简单地把ZonedDateTime理解成LocalDateTime加ZoneId。ZoneId是java.time引入的新的时区类，注意和旧的java.util.TimeZone区别。
 * 要创建一个ZonedDateTime对象，有以下几种方法，一种是通过now()方法返回当前时间：
 */
public class ZonedDateTimeDemo {
	public static void main(String[] args) {
		ZonedDateTime zdt = ZonedDateTime.now();//默认时区
		System.out.println("默认时区:" + zdt);
		ZonedDateTime zny = ZonedDateTime.now(ZoneId.of("America/New_York"));//用指定时区获取当前时间
		System.out.println("用指定时区获取当前时间:" + zny);
		
		/*
		 * 另一种方式是通过给一个LocalDateTime附加一个ZoneId，就可以变成ZonedDateTime：
		 * 以这种方式创建的ZonedDateTime，它的日期和时间与LocalDateTime相同，但附加的时区不同，因此是两个不同的时刻：
		 */
		LocalDateTime ldt = LocalDateTime.of(2019, 9, 15, 15, 16, 17);
		ZonedDateTime zbj = ldt.atZone(ZoneId.systemDefault());
		System.out.println("LocalDateTime默认时区:" + zbj);
		
		ZonedDateTime zdtJ = ldt.atZone(ZoneId.of("America/New_York"));
		System.out.println("LocalDateTime用指定时区获取当前时间:" + zdtJ);
		
		/*
		 * 时区转换
		 * 要转换时区，首先我们需要有一个ZonedDateTime对象，然后，通过withZoneSameInstant()将关联时区转换到另一个时区，转换后日期和时间都会相应调整。
		 */
		ZonedDateTime zbjSh = ZonedDateTime.now(ZoneId.of("Asia/Shanghai"));
		System.out.println("北京时间:" + zbjSh);
		//转换为纽约时间
		ZonedDateTime znyNy = zbjSh.withZoneSameInstant(ZoneId.of("America/New_York"));
		System.out.println("转换为纽约时间:" + znyNy);
		
		/*
		 * 要特别注意，时区转换的时候，由于夏令时的存在，不同的日期转换的结果很可能是不同的。这是北京时间9月15日的转换结果：
		 * 2019-09-15T21:05:50.187697+08:00[Asia/Shanghai]
		 * 2019-09-15T09:05:50.187697-04:00[America/New_York]
		 * 这是北京时间11月15日的转换结果：
		 * 2019-11-15T21:05:50.187697+08:00[Asia/Shanghai]
		 * 2019-11-15T08:05:50.187697-05:00[America/New_York]
		 * 两次转换后的纽约时间有1小时的夏令时时差。
		 * 涉及到时区时，千万不要自己计算时差，否则难以正确处理夏令时。 
		 */
		
		//有了ZonedDateTime，将其转换为本地时间就非常简单：ZonedDateTime仍然提供了plusDays()等加减操作。
		LocalDateTime ldtLocal = znyNy.toLocalDateTime();
		System.out.println("转换为LocalDateTime时，直接丢弃了时区信息。" + ldtLocal);
	}
}
