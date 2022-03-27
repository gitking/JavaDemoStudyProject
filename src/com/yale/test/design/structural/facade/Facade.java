package com.yale.test.design.structural.facade;

import com.yale.test.design.structural.facade.organization.AdminOfIndustry;
import com.yale.test.design.structural.facade.organization.Bank;
import com.yale.test.design.structural.facade.organization.Company;
import com.yale.test.design.structural.facade.organization.Taxation;

/**
 * 在软件开发领域有这样一句话：计算机科学领域的任何问题都可以通过增加一个间接的中间层来解决。而门面模式就是对于这句话的典型实践。
 * 外观模式，门面模式（Facade Pattern），也称之为外观模式，
 * 为子系统中的一组接口提供一个一致的界面。Facade模式定义了一个高层接口，这个接口使得这一子系统更加容易使用。
 * 外观模式，即Facade，是一个比较简单的模式。它的基本思想如下：
 * 如果客户端要跟许多子系统打交道，那么客户端需要了解各个子系统的接口，比较麻烦。如果有一个统一的“中介”，让客户端只跟中介打交道，中介再去跟各个子系统打交道，对客户端来说就比较简单。所以Facade就相当于搞了一个中介。
 * 我们以注册公司为例，假设注册公司需要三步：
 * 1.向工商局申请公司营业执照；2.在银行开设账户 3.在税务局开设纳税号。
 * 如果子系统比较复杂，并且客户对流程也不熟悉，那就把这些流程全部委托给中介：
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
 * 
 * 为什么需要日志门面？
	前面提到过一个重要的原因，就是为了在应用中屏蔽掉底层日志框架的具体实现。这样的话，即使有一天要更换代码的日志框架，只需要修改jar包，最多再改改日志输出相关的配置文件就可以了。这就是解除了应用和日志框架之间的耦合。
	有人或许会问了，如果我换了日志框架了，应用是不需要改了，那日志门面不还是需要改的吗？
	要回答这个问题，我们先来举一个例子，再把门面模式揉碎了重新解释一遍。
	日志门面就像饭店的服务员，而日志框架就像是后厨的厨师。对于顾客这个应用来说，我到饭店点菜，我只需要告诉服务员我要一盘番茄炒蛋即可，我不关心后厨的所有事情。因为虽然主厨从把这道菜称之为『番茄炒蛋』A厨师换成了把这道菜称之为『西红柿炒鸡蛋』的B厨师。但是，顾客不需要关心，他只要下达『番茄炒蛋』的命令给到服务员，由服务员再去翻译给厨师就可以了。
	所以，对于一个了解了”番茄炒蛋的多种叫法”的服务员来说，无论后厨如何换厨师，他都能准确的帮用户下单。
	同理，对于一个设计的全面、完善的日志门面来说，他也应该是天然就兼容了多种日志框架的。所以，底层框架的更换，日志门面几乎不需要改动。
	以上，就是日志门面的一个比较重要的好处——解耦。
 * 常用日志门面：
 * 1.SLF4J,Java简易日志门面（Simple Logging Facade for Java，缩写SLF4J），是一套包装Logging 框架的界面程式，以外观模式实现。可以在软件部署的时候决定要使用的 Logging 框架，目前主要支援的有Java Logging API、Log4j及logback等框架。以MIT 授权方式发布。
 */
public class Facade {
	private AdminOfIndustry admin = new AdminOfIndustry();
	private Bank bank = new Bank();
	private Taxation taxation = new Taxation();
	
	public Company openCompany(String name) {
		Company c = this.admin.register(name);
		String bankAccount = this.bank.openAccount(c.getId());
		c.setBankAccount(bankAccount);
		
		String taxCode = this.taxation.applyTaxCode(c.getId());
		c.setTaxCode(taxCode);
		return c;
	}
}
