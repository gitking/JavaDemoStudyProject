package com.yale.test.java.demo;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.apache.commons.lang3.StringUtils;

/**
 * https://mp.weixin.qq.com/s/CLrGJCxAczfEbnbKWrm9jw 《还在用 if(obj!=null) 做非空判断？带你快速上手 Optional 实战性理解！》
 * https://mp.weixin.qq.com/s/jp6pycU0mhFVppAsr0ftQg 《面试官：项目中你是如何利用设计模式，干掉if-else的 》
 * https://mp.weixin.qq.com/s/RVDC3Io45OsBrL2Ey8AL_w 《干掉大量 if 判断，规则执行器，太香了！ 》
 * https://mp.weixin.qq.com/s/2ct8rZiwndK1QvoAINxMKg 《Java8 中的真的 Optional 很强大，你用对了吗？》
 * https://mp.weixin.qq.com/s/R0OK1KKX9EXAh2DnHWpoHw 《一个大佬说，Java8的Optional是个鸡肋，我怒了！ 》
 * https://mp.weixin.qq.com/s/Npx4bhg765j4gRbmpC1dNA 《Java8 之 Optional 判空，简化判空操作 》
 * https://mp.weixin.qq.com/s/g8XEzjXjbSzkoXNqj9O--Q 《Optional 是个好东西，你真的会用么？ 》
 * @author issuser
 */
public class OptionalDemo {
	public static void main(String[] args) {
		String sss = "";
		 
		sss = Optional.ofNullable(sss).orElse("Str的默认值");
	
		 /**
		  * 如果字符串为空字符串或者为null就报错,否则返回String对象
		  */
		sss = Optional.ofNullable(sss).filter(new Predicate<String>() {
			 @Override
			 public boolean test(String sts) {
				 if (StringUtils.isBlank(sts)) {
					 throw new IllegalArgumentException("错误信息不能为空");
				 }
				 return true;
			 }
		 }).get();
		 
		 
		 /**
		  * 如果字符串为空字符串或者为null就报错
		  * Lambda Consumer消费者函数
		  * com.yale.test.lambda.LambdaMethod.java一起看
		  */
		 Optional.ofNullable(sss).ifPresent(new Consumer<String>() {
			 @Override
			 public void accept(String sts) {
				 if (StringUtils.isBlank(sts)) {
					 throw new IllegalArgumentException("错误信息不能为空");
				 }
			 }
		 });
	}
}
