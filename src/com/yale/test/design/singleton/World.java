package com.yale.test.design.singleton;

/*
 * 单例
 * 另一种实现Singleton的方式是利用Java的enum，因为Java保证枚举类的每个枚举都是单例，所以我们只需要编写一个只有一个枚举的类即可：
 * 使用枚举实现Singleton还避免了第一种方式实现Singleton的一个潜在问题：即序列化和反序列化会绕过普通类的private构造方法从而创建出多个实例，
 * 而枚举类就没有这个问题。
 * 那我们什么时候应该用Singleton呢？实际上，很多程序，尤其是Web程序，大部分服务类都应该被视作Singleton，如果全部按Singleton的写法写，
 * 会非常麻烦，所以，通常是通过约定让框架（例如Spring）来实例化这些类，保证只有一个实例，调用方自觉通过框架获取实例而不是new操作符：
 * @Component // 表示一个单例组件
	public class MyService {
	    ...
	}
 * 因此，除非确有必要，否则Singleton模式一般以“约定”为主，不会刻意实现它。
 * 小结
 * Singleton模式是为了保证一个程序的运行期间，某个类有且只有一个全局唯一实例；
 * Singleton模式既可以严格实现，也可以以约定的方式把普通类视作单例。
 */
public enum World {
	INSTANCE;//唯一枚举
	
	private String name = "world";
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	public static void main(String[] args) {
		//枚举类也完全可以像其他类那样定义自己的字段、方法，这样上面这个World类在调用方看来就可以这么用：
		String name = World.INSTANCE.getName();
	}
}
