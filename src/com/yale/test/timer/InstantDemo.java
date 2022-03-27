package com.yale.test.timer;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/*
 * 我们已经讲过，计算机存储的当前时间，本质上只是一个不断递增的整数。Java提供的System.currentTimeMillis()返回的就是以毫秒表示的当前时间戳。
 * 这个当前时间戳在java.time中以Instant类型表示，我们用Instant.now()获取当前时间戳，效果和System.currentTimeMillis()类似
 * 实际上，Instant内部只有两个核心字段：
 * 一个是以秒为单位的时间戳，一个是更精确的纳秒精度。它和System.currentTimeMillis()返回的long相比，只是多了更高精度的纳秒。
 * 既然Instant就是时间戳，那么，给它附加上一个时区，就可以创建出ZonedDateTime：
 * 《阿里巴巴Java开发手册嵩山版2020.pdf》
 * 3. 【强制】获取当前毫秒数：System.currentTimeMillis(); 而不是new Date().getTime()。 
 * 说明：如果想获取更加精确的纳秒级时间值，使用System.nanoTime的方式。在JDK8中，针对统计时间等场景，推荐使用Instant类。
 */
public class InstantDemo {
	public static void main(String[] args) {
		Instant now = Instant.now();
		System.out.println("秒:" + now.getEpochSecond());
		System.out.println("毫秒:" + now.toEpochMilli());
		
		Instant ins = Instant.ofEpochSecond(1568568760);
		ZonedDateTime zdt = ins.atZone(ZoneId.systemDefault());
		System.out.println("带时区的信息:" + zdt);
		/*
		 * 可见，对于某一个时间戳，给它关联上指定的ZoneId，就得到了ZonedDateTime，继而可以获得了对应时区的LocalDateTime。
		 * 所以，LocalDateTime，ZoneId，Instant，ZonedDateTime和long都可以互相转换：
		 * 转换的时候，只需要留意long类型以毫秒还是秒为单位即可。
		 * Instant表示高精度时间戳，它可以和ZonedDateTime以及long互相转换。
		 *  ┌─────────────┐
			│LocalDateTime│────┐
			└─────────────┘    │    ┌─────────────┐
			                   ├───>│ZonedDateTime│
			┌─────────────┐    │    └─────────────┘
			│   ZoneId    │────┘           ▲
			└─────────────┘      ┌─────────┴─────────┐
			                     │                   │
			                     ▼                   ▼
			              ┌─────────────┐     ┌─────────────┐
			              │   Instant   │<───>│    long     │
			              └─────────────┘     └─────────────┘
		 */
	}
}
