package com.yale.test.java.fanshe.apache;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;

/*
 * common-beanutils 操作对象
 * Maven依赖：
 * <dependency>  
    <groupId>commons-beanutils</groupId>  
    <artifactId>commons-beanutils</artifactId>  
    <version>1.9.4</version>  
</dependency>  
 * https://blog.csdn.net/u011039332/article/details/104500471  关于BeanUtils.copyProperties复制不生效
 * https://club.perfma.com/article/1717246?from=timeline 一次艰难的内存泄露排查，BeanUtils的锅
 * https://mp.weixin.qq.com/s/fO-snQbhvu41o2fLBP48rA 如何优雅的转换 Bean 对象！
 * https://mp.weixin.qq.com/s/hrpoYHAN7xLcd1JbVtPXEw 还在用 BeanUtils 来做对象转换吗？快试试 MapStruct 吧
 * https://club.perfma.com/article/2233744 Java常用的几种属性拷贝工具类使用总结
 * https://mp.weixin.qq.com/s/w0sd_g-UqmS0BXDyzcq5tA 为什么不推荐使用BeanUtils属性转换工具
 * https://mp.weixin.qq.com/s/NTxli-oQDhCSgyXTBbVUSQ 最近，我在Spring的BeanUtils踩了不少坑~
 */
public class BeanutilsDemo {
	public static void main(String[] args) {
		User user = new User();
		try {
			BeanUtils.setProperty(user, "id", 1);
			BeanUtils.setProperty(user, "name", "yideng");
			System.out.println(BeanUtils.getProperty(user, "name"));//必须有get方法
			System.out.println(user);
			
			//对象和map互转
			Map<String, String> map = BeanUtils.describe(user);
			System.out.println("将对象转换成Map->" + map);
			
			User mapUser = new User();
			BeanUtils.populate(mapUser, map);
			System.out.println("将map转换为对象->" + mapUser);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		
		
		try {
			//PropertyUtils.copyProperties(modDetail, origPlyFeeVO);
			PropertyUtils.copyProperties(null, null);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}
}
