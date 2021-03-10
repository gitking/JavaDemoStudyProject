package com.yale.test.timer;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TimeUnitDemo {
     private TimeUnit timeUnit =TimeUnit.DAYS;

     public static void main(String[] args) {
        TimeUnitDemo demo = new TimeUnitDemo();
        demo.outInfo();
        System.out.println("------------------------------------------------------");
        
        demo.timeUnit =TimeUnit.HOURS;
        demo.outInfo();
        System.out.println("------------------------------------------------------");

        demo.timeUnit =TimeUnit.MINUTES;
        demo.outInfo();
        System.out.println("------------------------------------------------------");

        demo.timeUnit =TimeUnit.SECONDS;
        demo.outInfo();
        
        System.out.println("------------------------------------------------------");
        
		System.out.println("应该是电脑的CPU开机多长时间了,运行多少毫秒了:" + TimeUnit.NANOSECONDS.toMillis(System.nanoTime()));
		System.out.println("System.nanoTime()这个方法我试了,就是电脑开机到现在经过了多长时间,你可以把电脑关机,然后再开机,开机的同时用手机记录电脑开机到你运行这段代码的时间,"
				+ "可以发现System.nanoTime()返回的时间跟你手机上面记录的时间是大致一样的。");
        /*
         * 我们可以将nanoTime除以1_000_000_000 ，或使用TimeUnit.SECONDS.convert进行转换。
         * 注意:1秒= 1_000_000_000纳秒
         * 除了sleep的功能外，TimeUnit还提供了便捷方法用于把时间转换成不同单位，例如，如果你想把秒转换成毫秒，你可以使用下面代码：
         */
        long oneMi = TimeUnit.SECONDS.convert(1, TimeUnit.MINUTES);// 1分钟转换为秒数 
        System.out.println("1分钟转换为秒数 :[" + oneMi + "]秒");

        long convert = TimeUnit.SECONDS.convert(1_000_000_000, TimeUnit.NANOSECONDS);
        System.out.println("1_000_000_000纳秒转换为秒数 :[" + convert + "]秒");

        long milSec = TimeUnit.SECONDS.toMillis(44);//想把秒转换成毫秒
        System.out.println("想把44秒转换成毫秒:[" + milSec + "]毫秒");
        long sec = TimeUnit.SECONDS.toMinutes(60);//60秒转换为分钟数 
        System.out.println("想把60秒转换成分钟:[" + sec + "]分钟");
        
        long startTime = System.nanoTime();
		try {
			Thread.sleep(2100);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		long entTime = System.nanoTime();
		long takeTime = entTime - startTime;
		System.out.println("1秒等于多少纳秒:" + takeTime);
		System.out.println("1秒等于多少毫秒:" + TimeUnit.NANOSECONDS.toMillis(takeTime));
		System.out.println("1秒等于多少秒:" + TimeUnit.NANOSECONDS.toSeconds(takeTime));
		
		//下面这俩行代码是抄袭TimeUnit类里面的excessNanos这个方法的,TimeUnit类里面的sleep方法就是这样精确计算的
		long mi = TimeUnit.NANOSECONDS.toMillis(takeTime);
		int s12 = (int)(takeTime - (mi * 1000 * 1000));
		System.out.println(takeTime + "纳秒等于:" + mi + "毫秒" + s12 + "纳秒");
        System.out.println("*******************************************************************");
        
        /*
         * https://club.perfma.com/article/397989
         * Java里currentTimeMillis的实现
         * 我们其实可以写一个简单的例子从侧面来验证currentTimeMillis返回的到底是什么值
         * 你将看到输出结果会是两个一样的值，这说明了什么？我们上一篇文章里已经提到了new Date(0).getTime()其实是就是1970/01/01 08:00:00,
         * 而new Date().getTime()是返回的当前时间，两个日期一减，其实就是当前时间距离1970/01/01 08:00:00有多少毫秒，
         * 而System.currentTimeMillis()返回的正好是这个值，也就是说System.currentTimeMillis()就是返回的当前时间距离1970/01/01 08:00:00的毫秒数。
         * 上面的wall_to_monotonic的tv_sec以及tv_nsec都是负数，在系统启动初始化的时候设置，记录了启动的时间
         * 因此nanoTime其实算出来的是一个相对的时间，相对于系统启动的时候的时间
         * 至此应该大家也清楚了，为什么currentTimeMillis返回的值并不是nanoTime返回的值的1000000倍左右了，因为两个值的参照不一样，所以没有可比性
         * https://club.perfma.com/article/1578518 不敢相信？System.currentTimeMillis()存在性能问题
         * 
         * System.nanoTime()
         * 返回正在运行的Java虚拟机的高分辨率时间源的当前值，以纳秒为单位。
         * 此方法只能用于测量经过的时间，并且与系统或挂钟时间的任何其他概念无关。
         * System.nanoTime()是基于cpu核心的时钟周期来计时,它的开始时间是不确定的，网上有篇文章说是更加cpu核心的启动时间开始计算的，不过具体的我也不能确定，只知道是与cpu有关就是了
         * 从文档来看，System.nanoTime()这个方法一个比较显著的应用是用来提供高精度的计时，不过两次调用的间隔不能超过2^63纳秒（大概292年），目前来看，应该暂时没有人有这么长的需求...
         * 我在网上看到有人说，使用System.nanoTime()去计时会有隐患，比如在多核处理器上运行，不同的调用可能获取的是不同的核心的时间，而多核处理器不同核心的启动时间可能不完全一致，这样会造成计时错误，但是在Stack Overflow上我找到了一个问答：
         * https://stackoverflow.com/questions/510462/is-system-nanotime-completely-useless
         * 这个问答里面说明，在windows系统上这曾经是一个问题，但是现在它被修复了，因此现在使用System.nanoTime()很安全（至少在同一个虚拟机内）。
         * 还有一个值得关注的问题是，System.nanoTime()的性能不如System.currentTimeMillis()，这是因为
         * System.currentTimeMillis()是使用GetSystemTimeAsFileTime方法实现的，该方法基本上只读取Windows维护的低分辨率时间值。根据所报告的信息，读取这个全局变量自然很快 - 大约6个周期。 System.nanoTime()使用实现所述QueryPerformanceCounter/ QueryPerformanceFrequency API（如果可用的话，否则它返回currentTimeMillis*10^6)。 QueryPerformanceCounter(QPC)在这取决于它的运行在硬件上不同的方式实现。通常，其使用两可编程间隔计时器（PIT）或ACPI电源管理计时器（PMT），或CPU级别的时间戳计数器（TSC）访问PIT / PMT需要执行慢速I / O端口指令，因此QPC的执行时间大约为几微秒。 100个时钟周期的顺序（从芯片读取TSC并将其转换为基于工作频率的时间值）。
         */
        System.out.println(new Date().getTime() - new Date(0).getTime());
        System.out.println(System.currentTimeMillis());
    }

    public void outInfo() {
        System.out.println(timeUnit.name());
        System.out.println(timeUnit.toDays(1));
        System.out.println(timeUnit.toHours(1));
        System.out.println(timeUnit.toMinutes(1));
        System.out.println(timeUnit.toMicros(1));
        System.out.println(timeUnit.toMillis(1));
        System.out.println(timeUnit.toNanos(1));
        System.out.println(timeUnit.toSeconds(1));
        System.out.println("1天有"+(timeUnit.convert(1, TimeUnit.DAYS))+timeUnit.name());
        System.out.println("12小时"+(timeUnit.convert(12, TimeUnit.HOURS))+timeUnit.name());
        System.out.println("3600秒有"+(timeUnit.convert(36000, TimeUnit.MINUTES))+timeUnit.name());
        System.out.println("-------------------");
    }
}
