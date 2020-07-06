package com.yale.test.java.exception;

import java.util.Optional;

public class NpeDemo {
	/*
	 * NullPointerException即空指针异常，俗称NPE。
	 * 写代码时应当使用使用空字符串""而不是默认的null可避免很多NullPointerException，编写业务逻辑时，用空字符串""表示未填写比null安全得多。
	 * 返回空字符串""、空数组而不是null：
	 * 这样可以使得调用方无需检查结果是否为null。
	 * 如果调用方一定要根据null判断，比如返回null表示文件不存在，那么考虑返回Optional<T>：
	 * 这样调用方必须通过Optional.isPresent()判断是否有结果。
	 */
	public Optional<String> isStr(String file) {
	    if (file == null) {
	        return Optional.empty();
	    }
	    return Optional.empty();
	}
	/*
	 * 如果产生了NullPointerException，例如，调用a.b.c.x()时产生了NullPointerException，原因可能是：
	 *  a是null；
		a.b是null；
		a.b.c是null
		从Java 14开始，如果产生了NullPointerException，JVM可以给出详细的信息告诉我们null对象到底是谁。我们来看例子：
		可以在NullPointerException的详细信息中看到类似... because "<local1>.address.city" is null，
		意思是city字段为null，这样我们就能快速定位问题所在。
		这种增强的NullPointerException详细信息是Java 14新增的功能，但默认是关闭的，
		我们可以给JVM添加一个-XX:+ShowCodeDetailsInExceptionMessages参数启用它：
		NullPointerException是Java代码常见的逻辑错误，应当早暴露，早修复；
	 */
}
