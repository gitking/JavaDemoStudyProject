package com.yale.test.springmvc.i18n;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.extension.AbstractExtension;
import com.mitchellbosecke.pebble.extension.Extension;
import com.mitchellbosecke.pebble.extension.Function;
import com.mitchellbosecke.pebble.loader.ServletLoader;
import com.mitchellbosecke.pebble.spring.extension.SpringExtension;
import com.mitchellbosecke.pebble.spring.servlet.PebbleViewResolver;
import com.mitchellbosecke.pebble.template.EvaluationContext;
import com.mitchellbosecke.pebble.template.PebbleTemplate;

/*
 * 国际化
 * 在开发应用程序的时候，经常会遇到支持多语言的需求，这种支持多语言的功能称之为国际化，英文是internationalization，缩写为i18n（因为首字母i和末字母n中间有18个字母）。
 * 还有针对特定地区的本地化功能，英文是localization，缩写为L10n，本地化是指根据地区调整类似姓名、日期的显示等。
 * 也有把上面两者合称为全球化，英文是globalization，缩写为g11n。
 * 在Java中，支持多语言和本地化是通过MessageFormat配合Locale实现的：
 * 对于Web应用程序，要实现国际化功能，主要是渲染View的时候，要把各种语言的资源文件提出来，这样，不同的用户访问同一个页面时，显示的语言就是不同的。
 * 我们来看看在Spring MVC应用程序中如何实现国际化。
 * 获取Locale
 * 实现国际化的第一步是获取到用户的Locale。在Web应用程序中，HTTP规范规定了浏览器会在请求中携带Accept-Language头，用来指示用户浏览器设定的语言顺序，如：
 * Accept-Language: zh-CN,zh;q=0.8,en;q=0.2
 * 上述HTTP请求头表示优先选择简体中文，其次选择中文，最后选择英文。q表示权重，解析后我们可获得一个根据优先级排序的语言列表，把它转换为Java的Locale，即获得了用户的Locale。大多数框架通常只返回权重最高的Locale。
 * Spring MVC通过LocaleResolver来自动从HttpServletRequest中获取Locale。有多种LocaleResolver的实现类，其中最常用的是CookieLocaleResolver：
 * CookieLocaleResolver从HttpServletRequest中获取Locale时，首先根据一个特定的Cookie判断是否指定了Locale，如果没有，就从HTTP头获取，如果还没有，就返回默认的Locale。
 * 当用户第一次访问网站时，CookieLocaleResolver只能从HTTP头获取Locale，即使用浏览器的默认语言。通常网站也允许用户自己选择语言，此时，CookieLocaleResolver就会把用户选择的语言存放到Cookie中，下一次访问时，就会返回用户上次选择的语言而不是浏览器默认语言。
 * 提取资源文件
 * 第二步是把写死在模板中的字符串以资源文件的方式存储在外部。对于多语言，主文件名如果命名为messages，那么资源文件必须按如下方式命名并放入classpath中：
 * 默认语言，文件名必须为messages.properties；
 * 简体中文，Locale是zh_CN，文件名必须为messages_zh_CN.properties；
 * 日文，Locale是ja_JP，文件名必须为messages_ja_JP.properties；
 * 其它更多语言……
 * 每个资源文件都有相同的key，例如，默认语言是英文，文件messages.properties内容如下：
 * language.select=Language
	home=Home
	signin=Sign In
	copyright=Copyright©{0,number,#}
 * 文件messages_zh_CN.properties内容如下：
 * language.select=语言
	home=首页
	signin=登录
	copyright=版权所有©{0,number,#}
 * 创建MessageSource
 * 第三步是创建一个Spring提供的MessageSource实例，它自动读取所有的.properties文件，并提供一个统一接口来实现“翻译”：
 * // code, arguments, locale:
 * String text = messageSource.getMessage("signin", null, locale);
 * 其中，signin是我们在.properties文件中定义的key，第二个参数是Object[]数组作为格式化时传入的参数，最后一个参数就是获取的用户Locale实例。
 * 创建MessageSource如下：
 * 注意到ResourceBundleMessageSource会自动根据主文件名自动把所有相关语言的资源文件都读进来。
 * 再注意到Spring容器会创建不只一个MessageSource实例，我们自己创建的这个MessageSource是专门给页面国际化使用的，因此命名为i18n，不会与其它MessageSource实例冲突。
 * 实现多语言
 * 要在View中使用MessageSource加上Locale输出多语言，我们通过编写一个MvcInterceptor，把相关资源注入到ModelAndView中：
 * 不要忘了在WebMvcConfigurer中注册MvcInterceptor。现在，就可以在View中调用MessageSource.getMessage()方法来实现多语言：
 * <a href="/signin">{{ __messageSource__.getMessage('signin', null, __locale__) }}</a>
 * 上述这种写法虽然可行，但格式太复杂了。使用View时，要根据每个特定的View引擎定制国际化函数。在Pebble中，我们可以封装一个国际化函数，名称就是下划线_，改造一下创建ViewResolver的代码：
 * 小结
 * 多语言支持需要从HTTP请求中解析用户的Locale，然后针对不同Locale显示不同的语言；
 * Spring MVC应用程序通过MessageSource和LocaleResolver，配合View实现国际化。
 */
public class AppConfig {
	public static void main(String[] args) {
		double price = 123.5;
		int number = 10;
		Object[] arguments = {price, number};
		//在Java中，支持多语言和本地化是通过MessageFormat配合Locale实现的：
		MessageFormat mfUS = new MessageFormat("Pay, {0, number, currency} for {1} books.", Locale.US);
		System.out.println(mfUS.format(arguments));
		MessageFormat mfZH = new MessageFormat("{1}本书一共{0, number, currency}.", Locale.CHINA);
		System.out.println(mfZH.format(arguments));
	}
	
	@Bean
	LocaleResolver createLocaleResolver() {
		//CookieLocaleResolver从HttpServletRequest中获取Locale时，首先根据一个特定的Cookie判断是否指定了Locale，如果没有，就从HTTP头获取，如果还没有，就返回默认的Locale。
		//当用户第一次访问网站时，CookieLocaleResolver只能从HTTP头获取Locale，即使用浏览器的默认语言。通常网站也允许用户自己选择语言，此时，CookieLocaleResolver就会把用户选择的语言存放到Cookie中，下一次访问时，就会返回用户上次选择的语言而不是浏览器默认语言。
		CookieLocaleResolver clr = new CookieLocaleResolver();
		clr.setDefaultLocale(Locale.ENGLISH);
		clr.setDefaultTimeZone(TimeZone.getDefault());
		return clr;
	}
	
	//创建MessageSource如下：
	@Bean("i18n")
	MessageSource createMessageSource() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		//指定文件是UTF-8编码
		messageSource.setDefaultEncoding("UTF-8");
		/*
		 * 指定主文件名:
		 * 注意到ResourceBundleMessageSource会自动根据主文件名自动把所有相关语言的资源文件都读进来。
		 * 再注意到Spring容器会创建不只一个MessageSource实例，我们自己创建的这个MessageSource是专门给页面国际化使用的，因此命名为i18n，不会与其它MessageSource实例冲突。
		 */
		messageSource.setBasename("messages");
		return messageSource;
	}
	
	/*
	 * <a href="/signin">{{ __messageSource__.getMessage('signin', null, __locale__) }}</a>
	 * 上述这种写法虽然可行，但格式太复杂了。使用View时，要根据每个特定的View引擎定制国际化函数。在Pebble中，我们可以封装一个国际化函数，名称就是下划线_，改造一下创建ViewResolver的代码：
	 * 这样，我们可以把多语言页面改写为：
	 * <a href="/signin">{{ _('signin') }}</a>
	 * 如果是带参数的多语言，需要把参数传进去：
	 * <h5>{{ _('copyright', 2020) }}</h5>
	 * 使用其它View引擎时，也应当根据引擎接口实现更方便的语法。
	 * 切换Locale
	 * 最后，我们需要允许用户手动切换Locale，编写一个LocaleController来实现该功能：
	 * 在页面设计中，通常在右上角给用户提供一个语言选择列表，来看看效果：
	 */
	@Bean
	ViewResolver createViewResolver(@Autowired ServletContext servletContext, @Autowired @Qualifier("i18n") MessageSource messageSource) {
		PebbleEngine engine = new PebbleEngine.Builder()
				.autoEscaping(true)
				.cacheActive(false)
				.loader(new ServletLoader(servletContext))
				.extension(createExtension(messageSource))// 添加扩展:
				//.extension(new SpringExtension())
				.build();
		PebbleViewResolver viewResolver = new PebbleViewResolver();
		viewResolver.setPrefix("/WEB-INF/templates/");
		viewResolver.setSuffix("");
		viewResolver.setPebbleEngine(engine);
		return viewResolver;
	}
	
	/*
	 * 老大写的createExtension好像就是pebbletemplate已实现的功能SpringExtension，代码高度相似
	 * 我们可以直接把pebble-spring已经实现的SpringExtension拿来用：
	 *  @Bean
	    public SpringExtension springExtension() {
	        return new SpringExtension();
	    }
	 * 但是有一个问题是怎么给这个springExtension注入我们的MessageSource，这个问题我折腾了一天，发现springExtension有一个private成员字段MessageSource，
	 * 没有setter方法没有带这个参数的构造方法（下个版本就会有了），只能field注入，折腾了半天发现其实只要给我们的MessageSource bean添加一个@Primary注解就可以了：
	 * 这样Spring给其他组件注入MessageSource时除非注入时指定别名，都会优先注入我们的MessageSource。
	 * 然后在模板里，我们可以直接使用MessageSourceFunction定义的函数message(...)，用法和廖大的_(...)函数基本一致：
	 * <h5>{{ message('copyright', 2019) }}</h5>
	 * （参见https://pebbletemplates.io/wiki/guide/spring-integration/）
	 * 还有就是spring还自带了一个通过GET请求参数传递并修改locale的LocaleChangeInterceptor：
	 *  @Override
        public void addInterceptors(InterceptorRegistry registry) {
            LocaleChangeInterceptor l = new LocaleChangeInterceptor();
            l.setParamName("lang");
            registry.addInterceptor(l);
        }
     * 可以代替MvcInterceptor和LocaleController组合，直接通过类似request_url/?lang=en方式修改locale，他的要点则是需要注入我们自己的CookieLocaleResolver作为他的LocaleResolver，办法也很简单，给我们的bean添加一个别名：
     * @Bean("localeResolver")
	    LocaleResolver createLocaleResolver() {
	        var clr = new CookieLocaleResolver();
	        clr.setDefaultLocale(Locale.ENGLISH);
	        clr.setDefaultTimeZone(TimeZone.getDefault());
	        return clr;
	    }
	 * 就行了（参见https://zhuanlan.zhihu.com/p/138375291）
	 * 另外其实pebble内置了一个i18n函数，和message也大致相同，不同的是没有用到ResourceBundleMessageSource，需要我们在用的时候给出bundle名，如：
	 * <h5>{{ i18n("messages", "copyright", 2020) }}</h5>
	 * 最后我发现默认语言资源文件不等于en资源文件，需要启用英语还需要添加一个messages_en.properties（messages_en_US.properties )也不行
	 * 详细修改见https://github.com/tzwjkl/spring-web-mvc-i18n
	 */
	private Extension createExtension(MessageSource messageSource) {
		return new AbstractExtension(){
			@Override
			public Map<String, Function> getFunctions() {
				Map<String, Function> res = new HashMap<String, Function>();
				res.put("_", new Function(){
					public Object execute(Map<String, Object> args, PebbleTemplate self, EvaluationContext context, int lineNumber) {
						String key = (String)args.get("0");
						List<Object> arguments = this.extractArgements(args);
						Locale locale = (Locale)context.getVariable("__locale__");
						return messageSource.getMessage(key, arguments.toArray(), "???" + key + "???", locale);
					}
					
					private List<Object> extractArgements(Map<String, Object> args) {
						int i = 1;
						List<Object> arguments = new ArrayList<>();
						while(args.containsKey(String.valueOf(i))) {
							Object param = args.get(String.valueOf(i));
							arguments.add(param);
							i++;
						}
						return arguments;
					}
					
					@Override
					public List<String> getArgumentNames() {
						return null;
					}
				});
				
				return res;
			}
		};
	}
}
