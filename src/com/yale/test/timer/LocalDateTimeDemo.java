package com.yale.test.timer;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

/*
 * 从Java 8开始，java.time包提供了新的日期和时间API，主要涉及的类型有：
 * 本地日期和时间：LocalDateTime，LocalDate，LocalTime；
 * 带时区的日期和时间：ZonedDateTime；
 * 时刻：Instant；
 * 时区：ZoneId，ZoneOffset；
 * 时间间隔：Duration。
 * 以及一套新的用于取代SimpleDateFormat的格式化类型DateTimeFormatter。
 * 和旧的API相比，新API严格区分了时刻、本地日期、本地时间和带时区的日期时间，并且，对日期和时间进行运算更加方便。
 * 此外，新API修正了旧API不合理的常量设计：Month的范围用1~12表示1月到12月；Week的范围用1~7表示周一到周日。
 * 最后，新API的类型几乎全部是不变类型（和String类似），可以放心使用不必担心被修改。
 */
public class LocalDateTimeDemo {
	public static void main(String[] args) {
		/*
		 * 我们首先来看最常用的LocalDateTime，它表示一个本地日期和时间：
		 * 本地日期和时间通过now()获取到的总是以当前默认时区返回的，和旧API不同，
		 * LocalDateTime、LocalDate和LocalTime默认严格按照ISO 8601(https://www.iso.org/iso-8601-date-and-time-format.html)规定的日期和时间格式进行打印。
		 */
		LocalDate ld = LocalDate.now();//当前日期
		System.out.println("严格按照ISO 8601格式打印：" + ld);
		
		LocalTime lt = LocalTime.now();//当前时间
		System.out.println("严格按照ISO 8601格式打印：" + lt);

		LocalDateTime ldt = LocalDateTime.now();//当前日期和时间
		System.out.println("严格按照ISO 8601格式打印：" + ldt);
		
		/*
		 * 上述代码其实有一个小问题，在获取3个类型的时候，由于执行一行代码总会消耗一点时间，因此，3个类型的日期和时间很可能对不上（时间的毫秒数基本上不同）。
		 * 为了保证获取到同一时刻的日期和时间，可以改写如下：
		 */
		LocalDateTime ldt1 = LocalDateTime.now();//当前日期和时间
		LocalDate ld1 = ldt1.toLocalDate();
		LocalTime lt1  = ldt1.toLocalTime();
		
		//反过来，通过指定的日期和时间创建LocalDateTime可以通过of()方法：
		LocalDate d2 = LocalDate.of(2019, 11, 30);//2019-11-30, 注意11=11月
		LocalTime t2 = LocalTime.of(15, 16, 17);//15:16:17
		LocalDateTime ldt3 = LocalDateTime.of(d2, t2);
		
		LocalDateTime ldt2 = LocalDateTime.of(2019, 11, 30, 15, 16, 17);
		/*
		 * 因为严格按照ISO 8601的格式，因此，将字符串转换为LocalDateTime就可以传入标准格式：
		 * 注意ISO 8601规定的日期和时间分隔符是T。标准格式如下：
		 * 日期：yyyy-MM-dd
		 * 时间：HH:mm:ss
		 * 带毫秒的时间：HH:mm:ss.SSS
		 * 日期和时间：yyyy-MM-dd'T'HH:mm:ss
		 * 带毫秒的日期和时间：yyyy-MM-dd'T'HH:mm:ss.SSS
		 */
		LocalDateTime ldtStr = LocalDateTime.parse("2019-11-19T15:16:17");
		LocalDate dStr = LocalDate.parse("2019-11-19");
		LocalTime ltStr = LocalTime.parse("15:16:17");
		
		/*
		 * 如果要自定义输出的格式，或者要把一个非ISO 8601格式的字符串解析成LocalDateTime，可以使用新的DateTimeFormatter：
		 */
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");//自定义格式化:
		System.out.println("DateTimeFormatter自定义格式化:时间转换为字符串" + dtf.format(LocalDateTime.now()));
		
		LocalDateTime ldtDtf = LocalDateTime.parse("2019/11/30 15:16:17", dtf);
		System.out.println("DateTimeFormatter自定义格式化:字符串转换为时间:" + ldtDtf);
		
		//LocalDateTime提供了对日期和时间进行加减的非常简单的链式调用：
		LocalDateTime ldtCalc = LocalDateTime.of(2019, 10, 26, 20, 30, 59);
		System.out.println("自定义时间:" + ldtCalc);
		
		LocalDateTime ldtCa = ldtCalc.plusDays(5).minusHours(3);
		System.out.println("加5天减3小时:" + ldtCa);

		LocalDateTime ldtCa3 = ldtCa.minusMonths(1);//减1个月
		System.out.println("减1个月:" + ldtCa3);
		System.out.println("注意到月份加减会自动调整日期，例如从2019-10-31减去1个月得到的结果是2019-09-30，因为9月没有31日。");
		System.out.println("对日期和时间进行调整则使用withXxx()方法，例如：withHour(15)会把10:11:12变为15:11:12：");
		/*
		 * 调整年：withYear()
		 * 调整月：withMonth()
		 * 调整日：withDayOfMonth()
		 * 调整时：withHour()
		 * 调整分：withMinute()
		 * 调整秒：withSecond()
		 */
		LocalDateTime ldtWi = LocalDateTime.of(2019, 10, 26, 20, 30, 59); 
		System.out.println("with自定义的时间:" + ldtWi);
		
		LocalDateTime dt2 = ldtWi.withDayOfMonth(31);//日期变为31日:
		System.out.println("日期变为31日:" + dt2);
		
		LocalDateTime dt3 = dt2.withMonth(9);//月份变为9:
		System.out.println("月份变为9:" + dt3);
		System.out.println("同样注意到调整月份时，会相应地调整日期，即把2019-10-31的月份调整为9时，日期也自动变为30。");
		
		System.out.println("实际上，LocalDateTime还有一个通用的with()方法允许我们做更复杂的运算。例如：");
		LocalDateTime firstDay = LocalDate.now().withDayOfMonth(1).atStartOfDay();
		System.out.println("本月第一天0:00时刻:" + firstDay);
		
		LocalDate lastDay = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());
		System.out.println("本月最后1天:" + lastDay);
		
		LocalDate nextMonthFirstDay = LocalDate.now().with(TemporalAdjusters.firstDayOfNextMonth());
		System.out.println("下个月第一天:" + nextMonthFirstDay);
		
		LocalDate firstWeekday = LocalDate.now().with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY));
		System.out.println("本月第一个周一:" + firstWeekday);
		
		/*
		 * 对于计算某个月第1个周日这样的问题，新的API可以轻松完成。
		 * 要判断两个LocalDateTime的先后，可以使用isBefore()、isAfter()方法，对于LocalDate和LocalTime类似：
		 * 注意到LocalDateTime无法与时间戳进行转换，因为LocalDateTime没有时区，无法确定某一时刻。后面我们要介绍的ZonedDateTime相当于LocalDateTime加时区的组合，它具有时区，可以与long表示的时间戳进行转换。
		 */
		System.out.println("判断日期的大小---------------------");
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime target = LocalDateTime.of(2019, 11, 19, 8, 15, 0);
		System.out.println("now是否在target之前:" + now.isBefore(target));
		System.out.println(LocalDate.now().isBefore(LocalDate.of(2019, 11, 19)));
		System.out.println("是否在指定时间之后:" +LocalTime.now().isAfter(LocalTime.parse("08:15:00")));
		
		
		System.out.println("判断日期间隔---------------------");
		/*
		 * Duration表示两个时刻之间的时间间隔。另一个类似的Period表示两个日期之间的天数：
		 * 注意到两个LocalDateTime之间的差值使用Duration表示，类似PT1235H10M30S，表示1235小时10分钟30秒。
		 * 而两个LocalDate之间的差值用Period表示，类似P1M21D，表示1个月21天。
		 * Duration和Period的表示方法也符合ISO 8601的格式，它以P...T...的形式表示，P...T之间表示日期间隔，T后面表示时间间隔。
		 * 如果是PT...的格式表示仅有时间间隔。利用ofXxx()或者parse()方法也可以直接创建Duration：
		 */
		LocalDateTime start = LocalDateTime.of(2019, 11, 19, 8, 15, 0);
		LocalDateTime end = LocalDateTime.of(2020, 1, 9, 19, 25, 30);
		Duration du = Duration.between(start, end);
		System.out.println("PT1235H10M30S:表示1235小时10分钟30秒" + du);
		
		Period p = LocalDate.of(2019, 11, 19).until(LocalDate.of(2020, 1, 9));
		System.out.println("P1M21D:表示1个月21天" + p);
		
		Duration d1 = Duration.ofHours(10);//
		Duration dd2 = Duration.parse("P1DT2H3M");//1 day, 2 hours, 3 minutes
		/*
		 * 有的童鞋可能发现Java 8引入的java.timeAPI。怎么和一个开源的Joda Time(https://www.joda.org/)很像？难道JDK也开始抄袭开源了？
		 * 其实正是因为开源的Joda Time设计很好，应用广泛，所以JDK团队邀请Joda Time的作者Stephen Colebourne共同设计了java.timeAPI。
		 */
	}
}
