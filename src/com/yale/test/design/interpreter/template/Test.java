package com.yale.test.design.interpreter.template;

import com.yale.test.design.interpreter.template.method.AbstractSetting;
import com.yale.test.design.interpreter.template.method.LocalSetting;
import com.yale.test.design.interpreter.template.method.RedisSetting;

/*
 * 模板方法
 * 定义一个操作中的算法的骨架，而将一些步骤延迟到子类中，使得子类可以不改变一个算法的结构即可重定义该算法的某些特定步骤。
 * 模板方法（Template Method）是一个比较简单的模式。它的主要思想是，定义一个操作的一系列步骤，对于某些暂时确定不下来的步骤，就留给子类去实现好了，这样不同的子类就可以定义出不同的步骤。
 * 因此，模板方法的核心在于定义一个“骨架”。我们还是举例说明。
 * 假设我们开发了一个从数据库读取设置的类：
 * public class Setting {
	    public final String getSetting(String key) {
	        String value = readFromDatabase(key);
	        return value;
	    }
	
		private String readFromDatabase(String key) {
	        // TODO: 从数据库读取
	    }
	}
 * 由于从数据库读取数据较慢，我们可以考虑把读取的设置缓存起来，这样下一次读取同样的key就不必再访问数据库了。但是怎么实现缓存，暂时没想好，但不妨碍我们先写出使用缓存的代码：AbstractSetting
 * 整个流程没有问题，但是，AbstractSetting类中的lookupCache(key)和putIntoCache(key, value)这两个方法还根本没实现，怎么编译通过？这个不要紧，我们声明抽象方法就可以：
 * 因为声明了抽象方法，自然整个类也必须是抽象类。如何实现lookupCache(key)和putIntoCache(key, value)这两个方法就交给子类了。子类其实并不关心核心代码getSetting(key)的逻辑，它只需要关心如何完成两个小小的子任务就可以了。
 * 假设我们希望用一个Map做缓存，那么可以写一个LocalSetting：
 * 如果我们要使用Redis做缓存，那么可以再写一个RedisSetting：
 * 客户端代码使用本地缓存的代码这么写：
 * 要改成Redis缓存，只需要把LocalSetting替换为RedisSetting：
 * 可见，模板方法的核心思想是：父类定义骨架，子类实现某些细节。
 * 为了防止子类重写父类的骨架方法，可以在父类中对骨架方法使用final。对于需要子类实现的抽象方法，一般声明为protected，使得这些方法对外部客户端不可见。
 * Java标准库也有很多模板方法的应用。在集合类中，AbstractList和AbstractQueuedSynchronizer都定义了很多通用操作，子类只需要实现某些必要方法。
 * 练习
 * 使用模板方法增加一个使用Guava Cache的子类。
 * 思考：能否将readFromDatabase()作为模板方法，使得子类可以选择从数据库读取还是从文件读取。
 * 再思考如果既可以扩展缓存，又可以扩展底层存储，会不会出现子类数量爆炸的情况？如何解决？
 * 答:当然是使用桥接模式将数据读取来源从AbstractSetting里分离出来啦
 * 
 * https://www.liaoxuefeng.com/wiki/1252599548343744/1281319636041762#0
 * 小结
 * 模板方法是一种高层定义骨架，底层实现细节的设计模式，适用于流程固定，但某些步骤不确定或可替换的情况。
 * 
 * Guava依赖
 * <dependency>
	  <groupId>com.google.guava</groupId>
	  <artifactId>guava</artifactId>
	  <version>28.2-jre</version>
	</dependency>
 * 
 */
public class Test {
	public static void main(String[] args) {
		AbstractSetting setting1 = new LocalSetting();
		System.out.println("test = " + setting1.getSeting("test"));
		System.out.println("test = " + setting1.getSeting("test"));
		
		AbstractSetting setting2 = new RedisSetting();
		System.out.println("autosave = " + setting2.getSeting("autosave"));
		System.out.println("autosave = " + setting2.getSeting("autosave"));
	}
}
