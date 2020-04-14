package com.yale.test.run;

import java.lang.management.ClassLoadingMXBean;
import java.lang.management.ManagementFactory;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 作者：拉莫斯之舞 链接：https://www.imooc.com/article/34605
 * @author dell
 */
public class ClassLoadingMXBeanTest {
	public static void main(String[] args) {
		
	}
	private static Map<String, Number> collectClassLoadingInfo() {
		ClassLoadingMXBean classLoadingMXBean = ManagementFactory.getClassLoadingMXBean();
		Map<String, Number> map = new LinkedHashMap<String, Number>();
		map.put("jvm.classloading.loaded.count", classLoadingMXBean.getLoadedClassCount());
		map.put("jvm.classloading.totalloaded.count", classLoadingMXBean.getTotalLoadedClassCount());
		map.put("jvm.classloading.unloaded.count", classLoadingMXBean.getUnloadedClassCount());
		return map;
	}
}
