package com.yale.test.java.log;
/*
*(三) 日志规约
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
*  为什么需要日志门面？
	前面提到过一个重要的原因，就是为了在应用中屏蔽掉底层日志框架的具体实现。这样的话，即使有一天要更换代码的日志框架，只需要修改jar包，最多再改改日志输出相关的配置文件就可以了。这就是解除了应用和日志框架之间的耦合。
	有人或许会问了，如果我换了日志框架了，应用是不需要改了，那日志门面不还是需要改的吗？
	要回答这个问题，我们先来举一个例子，再把门面模式揉碎了重新解释一遍。
	日志门面就像饭店的服务员，而日志框架就像是后厨的厨师。对于顾客这个应用来说，我到饭店点菜，我只需要告诉服务员我要一盘番茄炒蛋即可，我不关心后厨的所有事情。因为虽然主厨从把这道菜称之为『番茄炒蛋』A厨师换成了把这道菜称之为『西红柿炒鸡蛋』的B厨师。但是，顾客不需要关心，他只要下达『番茄炒蛋』的命令给到服务员，由服务员再去翻译给厨师就可以了。
	所以，对于一个了解了”番茄炒蛋的多种叫法”的服务员来说，无论后厨如何换厨师，他都能准确的帮用户下单。
	同理，对于一个设计的全面、完善的日志门面来说，他也应该是天然就兼容了多种日志框架的。所以，底层框架的更换，日志门面几乎不需要改动。
	以上，就是日志门面的一个比较重要的好处——解耦。
 * 常用日志门面：
 * 1.SLF4J,Java简易日志门面（Simple Logging Facade for Java，缩写SLF4J），是一套包装Logging 框架的界面程式，以外观模式实现。
 * 可以在软件部署的时候决定要使用的 Logging 框架，目前主要支援的有Java Logging API、Log4j及logback等框架。以MIT 授权方式发布。
 * SLF4J 的作者就是 Log4j 的作者 Ceki Gülcü，他宣称 SLF4J 比 Log4j 更有效率，而且比 Apache 的 Jakarta Commons Logging (JCL) 简单、稳定。
 * 其实，SLF4J其实只是一个门面服务而已，他并不是真正的日志框架，真正的日志的输出相关的实现还是要依赖Log4j、logback等日志框架的。
 * 由于SLF4J比较常用，这里多用一些篇幅，再来简单分析一下SLF4J，主要和Log4J做一下对比。相比较于Log4J的API，SLF4J有以下几点优势：
 * 由于SLF4J比较常用，这里多用一些篇幅，再来简单分析一下SLF4J，主要和Log4J做一下对比。相比较于Log4J的API，SLF4J有以下几点优势：
    1.Log4j 提供 TRACE, DEBUG, INFO, WARN, ERROR 及 FATAL 六种纪录等级，但是 SLF4J 认为 ERROR 与 FATAL 并没有实质上的差别，所以拿掉了 FATAL 等级，只剩下其他五种。
    2.大部分人在程序里面会去写logger.error(exception),其实这个时候Log4j会去把这个exception tostring。真正的写法应该是logger(message.exception);而SLF4J就不会使得程序员犯这个错误。
    3.Log4j间接的在鼓励程序员使用string相加的写法（这种写法是有性能问题的），而SLF4J就不会有这个问题 ,你可以使用logger.error(“{} is+serviceid”,serviceid);
    4.使用SLF4J可以方便的使用其提供的各种集体的实现的jar。（类似commons-logger）
    5.从commons–logger和Log4j merge非常方便，SLF4J也提供了一个swing的tools来帮助大家完成这个merge。
    6.提供字串内容替换的功能，会比较有效率，说明如下：
    // 传统的字符串产生方式，如果没有要记录Debug等级的信息，就会浪费时间在产生不必要的信息上 logger.debug("There are now " + count + " user accounts: " + userAccountList); 
 *  // 为了避免上述问题，我们可以先检查是不是开启了Debug信息记录功能，只是程序的编码会比较复杂 
 *  if (logger.isDebugEnabled()) { 
 *  	//如果Debug等级没有开启，则不会产生不必要的字符串，同时也能保持程序编码的简洁 logger.debug("There are now {} user accounts: {}", count, userAccountList);
 *  	logger.debug("There are now " + count + " user accounts: " + userAccountList); 
 *  }
 *  7. SLF4J 只支持 MDC，不支持 NDC。
 * 2.commons-logging(常用的日志门面)
 * 	Apache Commons Logging是一个基于Java的日志记录实用程序，是用于日志记录和其他工具包的编程模型。它通过其他一些工具提供API，日志实现和包装器实现。
 * 	commons-logging和SLF4J的功能是类似的，主要是用来做日志 门面的。提供更加好友的API工具。
 * 所以，对于Java工程师来说，关于日志工具的使用，最佳实践就是在应用中使用如Log4j + SLF4J 这样的组合来进行日志输出。
 * 这样做的最大好处，就是业务层的开发不需要关心底层日志框架的实现及细节，在编码的时候也不需要考虑日后更换框架所带来的成本。这也是门面模式所带来的好处。
 * 综上，请不要在你的Java代码中出现任何Log4j等日志框架的API的使用，而是应该直接使用SLF4J这种日志门面。
 * https://zhuanlan.zhihu.com/p/52799066 《为什么阿里巴巴禁止工程师直接使用日志系统(Log4j、Logback)中的 API》
 */
public class Slf4jDemo {

	public static void main(String[] args) {
		/*
		 * 前面介绍了Commons Logging和Log4j这一对好基友，它们一个负责充当日志API，一个负责实现日志底层，搭配使用非常便于开发。
			有的童鞋可能还听说过SLF4J和Logback。这两个东东看上去也像日志，它们又是啥？
			其实SLF4J类似于Commons Logging，也是一个日志接口，而Logback类似于Log4j，是一个日志的实现。
			为什么有了Commons Logging和Log4j，又会蹦出来SLF4J和Logback？这是因为Java有着非常悠久的开源历史，不但OpenJDK本身是开源的，而且我们用到的第三方库，几乎全部都是开源的。开源生态丰富的一个特定就是，同一个功能，可以找到若干种互相竞争的开源库。
			因为对Commons Logging的接口不满意，有人就搞了SLF4J。因为对Log4j的性能不满意，有人就搞了Logback。
			我们先来看看SLF4J对Commons Logging的接口有何改进。在Commons Logging中，我们要打印日志，有时候得这么写：
			int score = 99;
			p.setScore(score);
			log.info("Set score " + score + " for Person " + p.getName() + " ok.");
			拼字符串是一个非常麻烦的事情，所以SLF4J的日志接口改进成这样了：
			int score = 99;
			p.setScore(score);
			logger.info("Set score {} for Person {} ok.", score, p.getName());
			我们靠猜也能猜出来，SLF4J的日志接口传入的是一个带占位符的字符串，用后面的变量自动替换占位符，所以看起来更加自然。
			如何使用SLF4J？它的接口实际上和Commons Logging几乎一模一样：
			import org.slf4j.Logger;
			import org.slf4j.LoggerFactory;
			class Main {
			    final Logger logger = LoggerFactory.getLogger(getClass());
			}
			对比一下Commons Logging和SLF4J的接口：
			Commons 						Logging	SLF4J
			org.apache.commons.logging.Log	org.slf4j.Logger
			org.apache.commons.logging.LogFactory	org.slf4j.LoggerFactory
			不同之处就是Log变成了Logger，LogFactory变成了LoggerFactory。
			使用SLF4J和Logback和前面讲到的使用Commons Logging加Log4j是类似的，先分别下载SLF4J和Logback，然后把以下jar包放到classpath下：
			    slf4j-api-1.7.x.jar
			    logback-classic-1.2.x.jar
			    logback-core-1.2.x.jar
			    然后使用SLF4J的Logger和LoggerFactory即可。和Log4j类似，我们仍然需要一个Logback的配置文件，把logback.xml放到classpath下，配置如下：
			    从目前的趋势来看，越来越多的开源项目从Commons Logging加Log4j转向了SLF4J加Logback。
			    小结
				SLF4J和Logback可以取代Commons Logging和Log4j；
				始终使用SLF4J的接口写入日志，使用Logback只需要配置，不需要修改代码。
		 */
	}
}
