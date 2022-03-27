package com.yale.test.java.fanxing;

/**
 * 泛型类型只允许引用类型(类和接口,不能使用基本数据类型)
 * 编写泛型类比普通类要复杂。通常来说，泛型类一般用在集合类中，例如ArrayList<T>，我们很少需要编写泛型类。
 * T extends Number意思是所有泛型类型为Number或Number子类
 * 这种使用<? extends Number>的泛型定义称之为上界通配符（Upper Bounds Wildcards），即把泛型类型T的上界限定在Number了。
 * 
 * 12. 【强制】泛型通配符<? extends T>来接收返回的数据，此写法的泛型集合不能使用add方法，而<? super T>不能使用get方法，两者在接口调用赋值的场景中容易出错。 
 * 说明：扩展说一下PECS(Producer Extends Consumer Super)原则：第一、频繁往外读取内容的，适合用<? extends T>。第二、经常往里插入的，适合用<? super T>
 * 《阿里巴巴Java开发手册嵩山版2020.pdf》
 * @author dell
 * @param <T>
 */
public class MessageNumberImpl<T extends Number> implements IMessage<T> {
	@Override
	public void print(T t) {
		System.out.println(t);
	}
}
