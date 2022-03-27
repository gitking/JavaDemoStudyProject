package com.yale.test.java.fanxing;

/**
 * 泛型接口
 * 16. 【推荐】集合泛型定义时，在JDK7及以上，使用diamond语法或全省略。
 * 说明：菱形泛型，即diamond，直接使用<>来指代前边已经指定的类型。
	正例：
	// diamond方式，即<>
	HashMap<String, String> userCache = new HashMap<>(16);
	// 全省略方式
	ArrayList<User> users = new ArrayList(10);
 * 《阿里巴巴Java开发手册嵩山版2020.pdf》
 * @author dell
 * @param <T>
 */
public interface IMessage<T> {
	public void print(T t);
}
