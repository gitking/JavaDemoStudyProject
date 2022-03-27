package com.yale.test.java.log;

import java.util.logging.Logger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * java.util.logging(jul,j.u.l)j.u.l是java.util.logging包的简称，是JDK在1.4版本中引入的Java原生日志框架。
 * Java Logging API提供了七个日志级别用来控制输出。这七个级别分别是：SEVERE、WARNING、INFO、CONFIG、FINE、FINER、FINEST。
 * Log4j是Apache的一个开源项目，通过使用Log4j，我们可以控制日志信息输送的目的地是控制台、文件、GUI组件，甚至是套接口服务器、NT的事件记录器、UNIX Syslog守护进程等；
 * 我们也可以控制每一条日志的输出格式；通过定义每一条日志信息的级别，我们能够更加细致地控制日志的生成过程。最令人感兴趣的就是，这些可以通过一个配置文件来灵活地进行配置，而不需要修改应用的代码。
 * Log4也有七种日志级别：OFF、FATAL、ERROR、WARN、INFO、DEBUG和TRACE。
 * LogBack
 * LogBack也是一个很成熟的日志框架，其实LogBack和Log4j出自一个人之手，这个人就是Ceki Gülcü。
 * logback当前分成三个模块：logback-core,logback- classic和logback-access。logback-core是其它两个模块的基础模块。
 * logback-classic是Log4j的一个改良版本。此外logback-classic完整实现SLF4J API使你可以很方便地更换成其它日记系统如Log4j或j.u.l。
 * logback-access访问模块与Servlet容器集成提供通过Http来访问日记的功能。
 * Log4j2
 * 前面介绍过Log4j，这里要单独介绍一下Log4j2，之所以要单独拿出来说，而没有和Log4j放在一起介绍，是因为作者认为，Log4j2已经不仅仅是Log4j的一个升级版本了，而是从头到尾被重写的，这可以认为这其实就是完全不同的两个框架。
 * 关于Log4j2解决了Log4j的哪些问题，Log4j2相比较于Log4j、j.u.l和logback有哪些优势，我们在后续的文章中介绍。
 * 前面介绍了四种日志框架，也就是说，我们想要在应用中打印日志的时候，可以使用以上四种类库中的任意一种。比如想要使用Log4j，那么只要依赖Log4j的jar包，配置好配置文件并且在代码中使用其API打印日志就可以了。
 * https://zhuanlan.zhihu.com/p/52799066 《为什么阿里巴巴禁止工程师直接使用日志系统(Log4j、Logback)中的 API》
 * (三) 日志规约
 *	1. 【强制】应用中不可直接使用日志系统（Log4j、Logback）中的API，而应依赖使用日志框架 （SLF4J、JCL--Jakarta Commons Logging）中的API，使用门面模式的日志框架，有利于维护和各个类的日志处理方式统一。
 *	说明：日志框架（SLF4J、JCL--Jakarta Commons Logging）的使用方式（推荐使用SLF4J）
 *	使用SLF4J：
 *		import org.slf4j.Logger;
 *		import org.slf4j.LoggerFactory;
 *  	private static final Logger logger = LoggerFactory.getLogger(Test.class);
 *	使用JCL：
 *		import org.apache.commons.logging.Log;
 *		import org.apache.commons.logging.LogFactory;
 *		private static final Log log = LogFactory.getLog(Test.class);
 *	2. 【强制】所有日志文件至少保存15天，因为有些异常具备以“周”为频次发生的特点。对于当天日志，以“应用名.log”来保存，保存在/home/admin/应用名/logs/目录下，过往日志格式为: {logname}.log.{保存日期}，日期格式：yyyy-MM-dd
 * 		正例：以aap应用为例，日志保存在/home/admin/aapserver/logs/aap.log，历史日志名称为aap.log.2016-08-01
 *  3. 【强制】根据国家法律，网络运行状态、网络安全事件、个人敏感信息操作等相关记录，留存的日志不少于六个月，并且进行网络多机备份。
 *  5. 【强制】在日志输出时，字符串变量之间的拼接使用占位符的方式。 说明：因为String字符串的拼接会使用StringBuilder的append()方式，有一定的性能损耗。
 *  使用占位符仅是替换动作，可以有效提升性能。 正例：logger.debug("Processing trade with id: {} and symbol: {}", id, symbol);
 *  6. 【强制】对于trace/debug/info级别的日志输出，必须进行日志级别的开关判断。 说明：虽然在debug(参数)的方法体内第一行代码isDisabled(Level.DEBUG_INT)为真时（Slf4j的常见实现Log4j和Logback），就直接return，但是参数可能会进行字符串拼接运算。此外，如果debug(getName())这种参数内有getName()方法调用，无谓浪费方法调用的开销。 
 *  正例：
		// 如果判断为真，那么可以输出trace和debug级别的日志
		if (logger.isDebugEnabled()) {
		logger.debug("Current ID is: {} and name is: {}", id, getName());
		}
 * 7. 【强制】避免重复打印日志，浪费磁盘空间，务必在日志配置文件中设置additivity=false。 
 * 正例：<logger name="com.taobao.dubbo.config" additivity="false">
 * 8. 【强制】生产环境禁止直接使用System.out 或System.err 输出日志或使用e.printStackTrace()打印异常堆栈。 
 * 说明：标准日志输出与标准错误输出文件每次Jboss重启时才滚动，如果大量输出送往这两个文件，容易造成文件大小超过操作系统大小限制。
 * 9. 【强制】异常信息应该包括两类信息：案发现场信息和异常堆栈信息。如果不处理，那么通过关键字throws往上抛出。 
 * 正例：logger.error("inputParams:{} and errorMessage:{}", 各类参数或者对象toString(), e.getMessage(), e);
 * 《阿里巴巴Java开发手册嵩山版2020.pdf》
 * @author issuser
 */
public class LoggingDemo {
	//使用Commons Logging时，如果在静态方法中引用Log，通常直接定义一个静态类型变量：
    static final Log log = LogFactory.getLog(LoggingDemo.class);
    /*
     * 在实例方法中引用Log，通常定义一个实例变量：
     * 注意到实例变量log的获取方式是LogFactory.getLog(getClass())，
     * 虽然也可以用LogFactory.getLog(Person.class)，但是前一种方式有个非常大的好处，就是子类可以直接使用该log实例。例如：
     */
    protected final Log logObj = LogFactory.getLog(getClass());

	public static void main(String[] args) {
		
		System.out.println("打印日志..");
		/*
		 * 因为Java标准库内置了日志包java.util.logging，我们可以直接用。先看一个简单的例子：
		 * 对比可见，使用日志最大的好处是，它自动打印了时间、调用类、调用方法等很多有用的信息。
		 * 再仔细观察发现，4条日志，只打印了3条，logger.fine()没有打印。这是因为，日志的输出可以设定级别。
		 * JDK的Logging定义了7个日志级别，从严重到普通：
		    SEVERE
		    WARNING
		    INFO
		    CONFIG
		    FINE
		    FINER
		    FINEST
		 * 因为默认级别是INFO，因此，INFO级别以下的日志，不会被打印出来。使用日志级别的好处在于，调整级别，就可以屏蔽掉很多调试相关的日志输出。
		 * 使用Java标准库内置的Logging有以下局限：
			Logging系统在JVM启动时读取配置文件并完成初始化，一旦开始运行main()方法，就无法修改配置；
			配置不太方便，需要在JVM启动时传递参数-Djava.util.logging.config.file=<config-file-name>。
			因此，Java标准库内置的Logging使用并不是非常广泛。更方便的日志系统我们稍后介绍。
		 */
		Logger logger = Logger.getGlobal();
        logger.info("start process...");
        logger.warning("memory is running out...");
        logger.fine("ignored.");
        logger.severe("process will be terminated...");
        
        /*
         * Commons Logging的特色是，它可以挂接不同的日志系统，并通过配置文件指定挂接的日志系统。
         * 默认情况下，Commons Loggin自动搜索并使用Log4j（Log4j是另一个流行的日志系统），
         * 如果没有找到Log4j，再使用JDK Logging。
         * 使用Commons Logging只需要和两个类打交道，并且只有两步：
		 * 第一步，通过LogFactory获取Log类的实例； 第二步，使用Log实例的方法打日志。
		 * 示例代码如下：
		 * 下载commons-logging-1.2.jar包:https://commons.apache.org/proper/commons-logging/download_logging.cgi
		 * 然后用javac编译Main.java，编译的时候要指定classpath，不然编译器找不到我们引用的org.apache.commons.logging包。编译命令如下：
		 * javac -cp commons-logging-1.2.jar Main.java
		 * 现在可以执行这个Main.class，使用java命令，也必须指定classpath，命令如下：
		 * java -cp .;commons-logging-1.2.jar LoggingDemo
		 * 注意到传入的classpath有两部分：一个是.，一个是commons-logging-1.2.jar，用;分割。.表示当前目录，如果没有这个.，JVM不会在当前目录搜索LoggingDemo.class，就会报错。
		 * 如果在Linux或macOS下运行，注意classpath的分隔符不是;，而是:：
		 * java -cp .:commons-logging-1.2.jar LoggingDemo
		 * Commons Logging定义了6个日志级别：
		    FATAL
		    ERROR
		    WARNING
		    INFO
		    DEBUG
		    TRACE
			默认级别是INFO。
			使用Commons Logging时，如果在静态方法中引用Log，通常直接定义一个静态类型变量：
		 * static final Log log = LogFactory.getLog(Main.class);
         */
        Log log = LogFactory.getLog(LoggingDemo.class);
        log.info("start...");
        log.warn("end.");
        //此外，Commons Logging的日志方法，例如info()，除了标准的info(String)外，
        //还提供了一个非常有用的重载方法：info(String, Throwable)，这使得记录异常更加简单：
        try {
        	String s = "";
        	s.equals("");
        } catch(Exception e) {
        	//使用log.error(String, Throwable)打印异常。
            log.error("got exception!", e);
        }
	}
}
