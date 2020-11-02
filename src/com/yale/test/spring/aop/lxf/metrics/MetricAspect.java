package com.yale.test.spring.aop.lxf.metrics;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MetricAspect {
	
	/*
	 * 注意metric()方法标注了@Around("@annotation(metricTime)")，它的意思是，符合条件的目标方法是带有@MetricTime注解的方法，
	 * 因为metric()方法参数类型是MetricTime（注意参数名是metricTime不是MetricTime），我们通过它获取性能监控的名称。
	 * 有了@MetricTime注解，再配合MetricAspect，任何Bean，只要方法标注了@MetricTime注解，就可以自动实现性能监控。运行代码，输出结果如下：
	 * 问:@Around("@annotation(xxx)"),这个xxx必须改为方法签名里annotation类里的引用参数名一模一样不然会报错；而且改成不是metricTime，也照样会在有@MetricTime的方法前后执行，是不是说明MetricAspect这样的Aspect，在什么地方织入并不取决于@Around("@annotation(xxx)")括号里的xxx，而是取决于它方法第二个参数的引用类型？
	 * 其次还发现@Before("@annotation(xxx)")也可以，只要删掉方法里的ProceedingJoinPoint参数并改为void返回类型就可以了。
	 * 答:对
	 * https://www.liaoxuefeng.com/wiki/1252599548343744/1310052317134882#0
	 */
	@Around("@annotation(metricTime)")
	public Object metric(ProceedingJoinPoint joinPoint, MetricTime metricTime) throws Throwable {
		String name = metricTime.value();
		long start = System.currentTimeMillis();
		try {
			return joinPoint.proceed();
		} finally {
			long t = System.currentTimeMillis() - start;
			//写入日志或发送至JMX:
			System.err.println("[Metrics] " + name + ": " + t + "ms");
		}
	}
	
	@Before("@annotation(mm)")
	public void metric(MetricTime mm) throws Throwable {
		String name = mm.value();
		long start = System.currentTimeMillis();
		long t = System.currentTimeMillis() - start;
		//写入日志或发送至JMX:
		System.err.println("[MetricsBefore] " + name + ": " + t + "ms");
	}
}
