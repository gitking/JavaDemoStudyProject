package com.yale.test.spring.schedule.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/*
 * 使用Scheduler
 * 在很多应用程序中，经常需要执行定时任务。例如，每天或每月给用户发送账户汇总报表，定期检查并发送系统状态报告，等等。
 * 定时任务我们在使用线程池一节中已经讲到了，Java标准库本身就提供了定时执行任务的功能。在Spring中，使用定时任务更简单，不需要手写线程池相关代码，只需要两个注解即可。
 * 我们还是以实际代码为例，建立工程spring-integration-schedule，无需额外的依赖，我们可以直接在AppConfig中加上@EnableScheduling就开启了定时任务的支持：
 * 接下来，我们可以直接在一个Bean中编写一个public void无参数方法，然后加上@Scheduled注解：
 * 上述注解指定了启动延迟60秒，并以60秒的间隔执行任务。现在，我们直接运行应用程序，就可以在控制台看到定时任务打印的日志：
 * 如果没有看到定时任务的日志，需要检查：
 * 1.是否忘记了在AppConfig中标注@EnableScheduling；
 * 2.是否忘记了在定时任务的方法所在的class标注@Component。
 * 除了可以使用fixedRate外，还可以使用fixedDelay，两者的区别我们已经在使用线程池一节中讲过，这里不再重复。
 * 有的童鞋在实际开发中会遇到一个问题，因为Java的注解全部是常量，写死了fixedDelay=30000，如果根据实际情况要改成60秒怎么办，只能重新编译？
 * 我们可以把定时任务的配置放到配置文件中，例如task.properties：
 * task.checkDiskSpace=30000
 * 这样就可以随时修改配置文件而无需动代码。但是在代码中，我们需要用fixedDelayString取代fixedDelay：
 */
@Component
public class TaskService {
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	/*
	 * 注解指定了启动延迟60秒，并以60秒的间隔执行任务。现在，我们直接运行应用程序，就可以在控制台看到定时任务打印的日志：
	 */
	@Scheduled(initialDelay=60_000, fixedRate=60_000)
	public void checkSystemStatusEveryMinute() {
		logger.info("Start check system status...");
	}
	
	/*
	 * 有的童鞋在实际开发中会遇到一个问题，因为Java的注解全部是常量，写死了fixedDelay=30000，如果根据实际情况要改成60秒怎么办，只能重新编译？
	 * 我们可以把定时任务的配置放到配置文件中，例如task.properties：
	 * task.checkDiskSpace=30000
	 * 这样就可以随时修改配置文件而无需动代码。但是在代码中，我们需要用fixedDelayString取代fixedDelay：
	 * 注意到上述代码的注解参数fixedDelayString是一个属性占位符，并配有默认值30000，Spring在处理@Scheduled注解时，如果遇到String，会根据占位符自动用配置项替换，这样就可以灵活地修改定时任务的配置。
	 * 此外，fixedDelayString还可以使用更易读的Duration，例如：
	 * @Scheduled(initialDelay = 30_000, fixedDelayString = "${task.checkDiskSpace:PT2M30S}")
	 * 以字符串PT2M30S表示的Duration就是2分30秒，请参考LocalDateTime一节的Duration相关部分。
	 * 多个@Scheduled方法完全可以放到一个Bean中，这样便于统一管理各类定时任务。
	 */
	@Scheduled(initialDelay=30_000, fixedDelayString = "${task.checkDiskSpace:30000}")
	public void checkDiskSpaceEveryMinute() {
		logger.info("Start check disk space...");
	}
	
	/*
	 * 使用Cron任务
	 * 还有一类定时任务，它不是简单的重复执行，而是按时间触发，我们把这类任务称为Cron任务，例如：
	 * 1.每天凌晨2:15执行报表任务；
	 * 2.每个工作日12:00执行特定任务；
	 * Cron源自Unix/Linux系统自带的crond守护进程，以一个简洁的表达式定义任务触发时间。在Spring中，也可以使用Cron表达式来执行Cron任务，在Spring中，它的格式是：
	 * 秒 分 小时 天 月份 星期 年
	 * 年是可以忽略的，通常不写。每天凌晨2:15执行的Cron表达式就是：
	 * 0 15 2 * * *
	 * 每个工作日12:00执行的Cron表达式就是：
	 * 0 0 12 * * MON-FRI
	 * 每个月1号，2号，3号和10号12:00执行的Cron表达式就是：
	 * 0 0 12 1-3,10 * *
	 * 在Spring中，我们定义一个每天凌晨2:15执行的任务：
	 */
	@Scheduled(cron= "${task.report:0 15 2 * * *}")
	public void cornDailyReport() {
		logger.info("Start daily report task...");
	}
	
	/*
	 * Cron任务同样可以使用属性占位符，这样修改起来更加方便。
	 * Cron表达式还可以表达每10分钟执行，例如：
	 * 0 *\/10 * * * *
	 * 这样，在每个小时的0:00，10:00，20:00，30:00，40:00，50:00均会执行任务，实际上它可以取代fixedRate类型的定时任务。
	 * 集成Quartz
	 * 在Spring中使用定时任务和Cron任务都十分简单，但是要注意到，这些任务的调度都是在每个JVM进程中的。如果在本机启动两个进程，或者在多台机器上启动应用，这些进程的定时任务和Cron任务都是独立运行的，互不影响。
	 * 如果一些定时任务要以集群的方式运行，例如每天23:00执行检查任务，只需要集群中的一台运行即可，这个时候，可以考虑使用Quartz(https://www.quartz-scheduler.org/)。
	 * Quartz可以配置一个JDBC数据源，以便存储所有的任务调度计划以及任务执行状态。也可以使用内存来调度任务，但这样配置就和使用Spring的调度没啥区别了，额外集成Quartz的意义就不大。
	 * Quartz的JDBC配置比较复杂，Spring对其也有一定的支持。要详细了解Quartz的集成，请参考Spring的文档(https://docs.spring.io/spring-framework/docs/current/reference/html/integration.html#scheduling-quartz)。
	 * 思考：如果不使用Quartz的JDBC配置，多个Spring应用同时运行时，如何保证某个任务只在某一台机器执行？
	 * https://blog.csdn.net/lonelymanontheway/article/details/104298922
	 * 廖老师，啥时候可以出Linux教程？
	 * 廖老师答:Linux就该这么学 刘瑞 https://book.douban.com/subject/27198046/
	 * 小结
	 * Spring内置定时任务和Cron任务的支持，编写调度任务十分方便。
	 */
	@Scheduled(cron = "${task.weekday:0 0 12 * * MON-FRI}")
	public void cronWeekdayTask() {
		logger.info("Start weekday task...");
	}
}
