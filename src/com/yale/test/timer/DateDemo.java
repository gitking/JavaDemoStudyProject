package com.yale.test.timer;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/*
 * 这是日期：2019-11-20,2020-1-1
 * 这是时间： 12:30:59 2020-1-1 20:21:59,日期是指某一天，它不是连续变化的，而是应该被看成离散的。
 * 而时间有两种概念，一种是不带日期的时间，例如，12:30:59。另一种是带日期的时间，例如，2020-1-1 20:21:59，
 * 只有这种带日期的时间能唯一确定某个时刻，不带日期的时间是无法确定一个唯一时刻的。
 * 本地时间
 * 当我们说当前时刻是2019年11月20日早上8:15的时候，我们说的实际上是本地时间。在国内就是北京时间。在这个时刻，如果地球上不同地方的人们同时看一眼手表，他们各自的本地时间是不同的：
 * 所以，不同的时区，在同一时刻，本地时间是不同的。全球一共分为24个时区，伦敦所在的时区称为标准时区，其他时区按东／西偏移的小时区分，北京所在的时区是东八区。
 * 时区
 * 因为光靠本地时间还无法唯一确定一个准确的时刻，所以我们还需要给本地时间加上一个时区。时区有好几种表示方式。
 * 一种是以GMT或者UTC加时区偏移表示，例如：GMT+08:00或者UTC+08:00表示东八区。
 * GMT和UTC可以认为基本是等价的，只是UTC使用更精确的原子钟计时，每隔几年会有一个闰秒，我们在开发程序的时候可以忽略两者的误差，因为计算机的时钟在联网的时候会自动与时间服务器同步时间。
 * 另一种是缩写，例如，CST表示China Standard Time，也就是中国标准时间。但是CST也可以表示美国中部时间Central Standard Time USA，因此，缩写容易产生混淆，我们尽量不要使用缩写。
 * 最后一种是以洲／城市表示，例如，Asia/Shanghai，表示上海所在地的时区。特别注意城市名称不是任意的城市，而是由国际标准组织规定的城市。
 * 因为时区的存在，东八区的2019年11月20日早上8:15，和西五区的2019年11月19日晚上19:15，他们的时刻是相同的：
 * 时刻相同的意思就是，分别在两个时区的两个人，如果在这一刻通电话，他们各自报出自己手表上的时间，虽然本地时间是不同的，但是这两个时间表示的时刻是相同的。
 * 夏令时
 * 时区还不是最复杂的，更复杂的是夏令时。所谓夏令时，就是夏天开始的时候，把时间往后拨1小时，夏天结束的时候，再把时间往前拨1小时。我们国家实行过一段时间夏令时，1992年就废除了，但是矫情的美国人到现在还在使用，所以时间换算更加复杂。
 * 因为涉及到夏令时，相同的时区，如果表示的方式不同，转换出的时间是不同的。我们举个栗子：
 * 对于2019-11-20和2019-6-20两个日期来说，假设北京人在纽约：
 * 如果以GMT或者UTC作为时区，无论日期是多少，时间都是19:00；
 * 如果以国家／城市表示，例如America／NewYork，虽然纽约也在西五区，但是，因为夏令时的存在，在不同的日期，GMT时间和纽约时间可能是不一样的：
 * 实行夏令时的不同地区，进入和退出夏令时的时间很可能是不同的。同一个地区，根据历史上是否实行过夏令时，标准时间在不同年份换算成当地时间也是不同的。因此，计算夏令时，没有统一的公式，必须按照一组给定的规则来算，并且，该规则要定期更新。
 * 计算夏令时请使用标准库提供的相关类，不要试图自己计算夏令时。 
 * 本地化
 * 在计算机中，通常使用Locale表示一个国家或地区的日期、时间、数字、货币等格式。Locale由语言_国家的字母缩写构成，例如，zh_CN表示中文+中国，en_US表示英文+美国。语言使用小写，国家使用大写。
 * 对于日期来说，不同的Locale，例如，中国和美国的表示方式如下：
    zh_CN：2016-11-30
    en_US：11/30/2016
 * 计算机用Locale在日期、时间、货币和字符串之间进行转换。一个电商网站会根据用户所在的Locale对用户显示如下：
 * 			中国用户	         美国用户
	购买价格	12000.00	12,000.00
	购买日期	2016-11-30	11/30/2016
	小结
 * 在编写日期和时间的程序前，我们要准确理解日期、时间和时刻的概念。
 * 由于存在本地时间，我们需要理解时区的概念，并且必须牢记由于夏令时的存在，同一地区用GMT/UTC和城市表示的时区可能导致时间不同。
 * 计算机通过Locale来针对当地用户习惯格式化日期、时间、数字、货币等。
 * 在计算机中，应该如何表示日期和时间呢？
 * 我们经常看到的日期和时间表示方式如下：
    2019-11-20 0:15:00 GMT+00:00
    2019年11月20日8:15:00
    11/19/2019 19:15:00 America/New_York
 * 如果直接以字符串的形式存储，那么不同的格式，不同的语言会让表示方式非常繁琐。在理解日期和时间的表示方式之前，我们先要理解数据的存储和展示。
 */
public class DateDemo {
	public static void main(String[] args) {
		/*
		 * 当我们定义一个整型变量并赋值时：int n = 123400;
		 * 编译器会把上述字符串（程序源码就是一个字符串）编译成字节码。在程序的运行期，变量n指向的内存实际上是一个4字节区域：
		 * ┌──┬──┬──┬──┐
		   │00│01│e2│08│
		   └──┴──┴──┴──┘
		 * 注意到计算机内存除了二进制的0/1外没有其他任何格式。上述十六机制是为了简化表示。
		 * 当我们用System.out.println(n)打印这个整数的时候，实际上println()这个方法在内部把int类型转换成String类型，然后打印出字符串123400。
		 * 类似的，我们也可以以十六进制的形式打印这个整数，或者，如果n表示一个价格，我们就以$123,400.00的形式来打印它：
		 */
		int n = 123400;
        // 123400
        System.out.println(n);
        // 1e208
        System.out.println("十六进制:" + Integer.toHexString(n));
        // $123,400.00
        System.out.println(NumberFormat.getCurrencyInstance(Locale.US).format(n));
        
        /*
         * 可见，整数123400是数据的存储格式，它的存储格式非常简单。而我们打印的各种各样的字符串，则是数据的展示格式。展示格式有多种形式，但本质上它就是一个转换方法：
         * 理解了数据的存储和展示，我们回头看看以下几种日期和时间：
	     * 2019-11-20 0:15:01 GMT+00:00
	     * 2019年11月20日8:15:01
	     * 11/19/2019 19:15:01 America/New_York
	     * 它们实际上是数据的展示格式，分别按英国时区、中国时区、纽约时区对同一个时刻进行展示。而这个“同一个时刻”在计算机中存储的本质上只是一个整数，我们称它为Epoch Time。
	     * Epoch Time是计算从1970年1月1日零点（格林威治时区／GMT+00:00）到现在所经历的秒数，例如：
	     * 1574208900表示从从1970年1月1日零点GMT时区到该时刻一共经历了1574208900秒，换算成伦敦、北京和纽约时间分别是：
	     * 1574208900 = 北京时间2019-11-20 8:15:00
          			  = 伦敦时间2019-11-20 0:15:00
           			  = 纽约时间2019-11-19 19:15:00
         * 因此，在计算机中，只需要存储一个整数1574208900表示某一时刻。当需要显示为某一地区的当地时间时，我们就把它格式化为一个字符串：
         * Epoch Time又称为时间戳，在不同的编程语言中，会有几种存储方式：
		 * 以秒为单位的整数：1574208900，缺点是精度只能到秒；
		 * 以毫秒为单位的整数：1574208900123，最后3位表示毫秒数；
		 * 以秒为单位的浮点数：1574208900.123，小数点后面表示零点几秒。
		 * 它们之间转换非常简单。而在Java程序中，时间戳通常是用long表示的毫秒数，即：
		 * 转换成北京时间就是2019-11-20T8:15:00.123。要获取当前时间戳，可以使用System.currentTimeMillis()，这是Java程序获取时间戳最常用的方法。
         */
        long t = 1574208900123L;
        long curTime = System.currentTimeMillis();
        
        /*
         * 标准库API
         * 我们再来看一下Java标准库提供的API。Java标准库有两套处理日期和时间的API：
         * 一套定义在java.util这个包里面，主要包括Date、Calendar和TimeZone这几个类；
    	 * 一套新的API是在Java 8引入的，定义在java.time这个包里面，主要包括LocalDateTime、ZonedDateTime、ZoneId等。
    	 * 为什么会有新旧两套API呢？因为历史遗留原因，旧的API存在很多问题，所以引入了新的API。
		 * 那么我们能不能跳过旧的API直接用新的API呢？如果涉及到遗留代码就不行，因为很多遗留代码仍然使用旧的API，所以目前仍然需要对旧的API有一定了解，很多时候还需要在新旧两种对象之间进行转换。
		 * 本节我们快速讲解旧API的常用类型和方法。
		 * Date
		 * java.util.Date是用于表示一个日期和时间的对象，注意与java.sql.Date区分，后者用在数据库中。
		 * 如果观察Date的源码，可以发现它实际上存储了一个long类型的以毫秒表示的时间戳：
         */
        System.out.println("Date的用法----------------------");
        Date date = new Date(); // 获取当前时间:
        System.out.println("获取年份:必须加上1900:" + (date.getYear() + 1900)); 
        System.out.println("月份从0开始必须加上1:" + (date.getMonth() + 1)); // 0~11，必须加上1
        System.out.println("天数不用加1:" + date.getDate()); // 1~31，不能加1
        System.out.println("转换为String:" + date.toString());
        System.out.println("转换为GMT时区:" + date.toGMTString());
        System.out.println("转换为本地时区:" + date.toLocaleString());
        
        /*
         * 打印本地时区表示的日期和时间时，不同的计算机可能会有不同的结果。如果我们想要针对用户的偏好精确地控制日期和时间的格式，就可以使用SimpleDateFormat对一个Date进行转换。
         * 它用预定义的字符串表示格式化：
         * yyyy：年,MM：月,dd: 日,HH: 小时,mm: 分钟,ss: 秒
         */
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("SimpleDateFormat格式时间:" + sdf.format(date));
        
        /*
         * Java的格式化预定义了许多不同的格式，我们以MMM和E为例：
         * 上述代码在不同的语言环境会打印出类似Sun Sep 15, 2019这样的日期。可以从JDK文档(https://docs.oracle.com/en/java/javase/12/docs/api/java.base/java/text/SimpleDateFormat.html)查看详细的格式说明。
         * 一般来说，字母越长，输出越长。以M为例，假设当前月份是9月：
         * M：输出9,MM：输出09,MMM：输出Sep,MMMM：输出September
         * Date对象有几个严重的问题：它不能转换时区，除了toGMTString()可以按GMT+0:00输出外，
         * Date总是以当前计算机系统的默认时区为基础进行输出。此外，我们也很难对日期和时间进行加减，计算两个日期相差多少天，计算某个月第一个星期一的日期等。
         */
        SimpleDateFormat sdff = new SimpleDateFormat("E MMM dd, yyyy");
        System.out.println("SimpleDateFormat格式时间:" + sdff.format(date));
        
        /*
         * Calendar
	     * Calendar可以用于获取并设置年、月、日、时、分、秒，它和Date比，主要多了一个可以做简单的日期和时间运算的功能。
	     * Calendar只有一种方式获取，即Calendar.getInstance()，而且一获取到就是当前时间。如果我们想给它设置成特定的一个日期和时间，就必须先清除所有字段：
         */
        System.out.println("Calendar的用法----------------------");
        Calendar c = Calendar.getInstance();//获取当前时间:
        int y = c.get(Calendar.YEAR);//年份不必再加1900了,直接用就行
        int m = 1 + c.get(Calendar.MONTH);//月份仍然要加1
        int d = c.get(Calendar.DAY_OF_MONTH);
        int w = c.get(Calendar.DAY_OF_WEEK);//返回的星期要特别注意，1~7分别表示周日，周一，……，周六。
        int hh = c.get(Calendar.HOUR_OF_DAY);
        int mm = c.get(Calendar.MINUTE);
        int ss = c.get(Calendar.SECOND);
        int ms = c.get(Calendar.MILLISECOND);
        System.out.println(y + "-" + m + "-" + d + " " + w + " " + hh + ":" + mm + ":" + ss + "." + ms);
        
        /*
         * Calendar只有一种方式获取，即Calendar.getInstance()，而且一获取到就是当前时间。如果我们想给它设置成特定的一个日期和时间，就必须先清除所有字段：
         * 利用Calendar.getTime()可以将一个Calendar对象转换成Date对象，然后就可以用SimpleDateFormat进行格式化了。
         */
        Calendar cc = Calendar.getInstance();//当前时间:
        cc.clear();// 清除所有:
        cc.set(Calendar.YEAR, 2019);// 设置2019年:
        cc.set(Calendar.MONTH, 8);// 设置9月:注意8表示9月:
        cc.set(Calendar.DATE, 2);// 设置2日:
        cc.set(Calendar.HOUR_OF_DAY, 21);// 设置时间:
        cc.set(Calendar.MINUTE, 22);
        cc.set(Calendar.SECOND, 23);
        System.out.println("自定义时间:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cc.getTime()));
        
        /*
         * TimeZone,Calendar和Date相比，它提供了时区转换的功能。时区用TimeZone对象表示：
         * 时区的唯一标识是以字符串表示的ID，我们获取指定TimeZone对象也是以这个ID为参数获取，GMT+09:00、Asia/Shanghai都是有效的时区ID。
         * 要列出系统支持的所有ID，请使用TimeZone.getAvailableIDs()。
         * 有了时区，我们就可以对指定时间进行转换。例如，下面的例子演示了如何将北京时间2019-11-20 8:15:00转换为纽约时间：
         */
        System.out.println("TimeZone的用法----------------------");
        TimeZone tzDefault = TimeZone.getDefault(); // 当前时区
        TimeZone tzGMT9 = TimeZone.getTimeZone("GMT+09:00"); // GMT+9:00时区
        TimeZone tzNY = TimeZone.getTimeZone("America/New_York"); // 纽约时区
        System.out.println("时区的唯一标识是以字符串表示的ID:" + tzDefault.getID()); // Asia/Shanghai
        System.out.println("时区的唯一标识是以字符串表示的ID:" + tzGMT9.getID()); // GMT+09:00
        System.out.println("时区的唯一标识是以字符串表示的ID:" +  tzNY.getID()); // America/New_York
        String[] allIds = TimeZone.getAvailableIDs();
//        for (String ids : allIds) {
//        	System.out.println("系统支持的所有时区的唯一标识id" +  ids);
//        }
        
        
        System.out.println("利用SimpleDateFormat进行时区转换的用法----------------------");
        //有了时区，我们就可以对指定时间进行转换。例如，下面的例子演示了如何将北京时间2019-11-20 8:15:00转换为纽约时间：
        Calendar ccc = Calendar.getInstance();// 当前时间:
        ccc.clear();// 清除所有:
        ccc.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));//设置为北京时区:
        // 设置年月日时分秒:
        ccc.set(2019, 10, 20, 8, 15, 0);
        // 显示时间:格式化获取的Date对象（注意Date对象无时区信息，时区信息存储在SimpleDateFormat中）。因此，本质上时区转换只能通过SimpleDateFormat在显示的时候完成。
        SimpleDateFormat sdfff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdfff.setTimeZone(TimeZone.getTimeZone("America/New_York"));
        System.out.println("转换为纽约的时间:" + sdfff.format(ccc.getTime()));// 2019-11-19 19:15:00
        
        System.out.println("利用Calendar对时间进行加减----------------------");
        Calendar c1 = Calendar.getInstance();// 当前时间:
        c1.clear();// 清除所有:
        c1.set(2019, 10 /* 11月 */, 20, 8, 15, 0);// 设置年月日时分秒:
       
        c1.add(Calendar.DAY_OF_MONTH, 5); // 加5天
        c1.add(Calendar.HOUR_OF_DAY, -2);//并减去2小时:
        // 显示时间:
        SimpleDateFormat sdf11 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d1 = c1.getTime();
        System.out.println(sdf11.format(d1));// 2019-11-25 6:15:00
        System.out.println("计算机表示的时间是以整数表示的时间戳存储的，即Epoch Time，Java使用long型来表示以毫秒为单位的时间戳，通过System.currentTimeMillis()获取当前时间戳。");
        
        
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
		System.out.println("1秒等于多少纳秒:" + (entTime - startTime));
		System.out.println("1秒等于多少毫秒:" + TimeUnit.NANOSECONDS.toMillis((entTime - startTime)));
		System.out.println("1秒等于多少秒:" + TimeUnit.NANOSECONDS.toSeconds((entTime - startTime)));
	}
}
