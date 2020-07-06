package com.yale.test.java.log;

import java.util.logging.Logger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
