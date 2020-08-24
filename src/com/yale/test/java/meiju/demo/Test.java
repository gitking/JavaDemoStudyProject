package com.yale.test.java.meiju.demo;

import com.yale.test.java.meiju.demo.impl.TencentChannelRule;
import com.yale.test.java.meiju.demo.impl.TouTiaoChannelRule;

public class Test {

	/**
	 * 这种方式有俩个缺点
	 * 1,当新增渠道的时候必须修改Test的main方法
	 * (注意,新增渠道你肯定是要修改代码的,但是我们想做到即使新增渠道了,我也不想修改Test的main方法,这就是扩展是开放的,而对修改是封闭的)
	 * 2,渠道越来越多的时候会有大量的if else
	 * @param args
	 */
	public static void main(String[] args) {
		String sign = "头条";
		GeneralChannelRule gcr = null;
		System.out.println(ChannelRuleEnum.TOUTIAO.name());
		if (ChannelRuleEnum.TOUTIAO.val.equals(sign)) {
			gcr = new TouTiaoChannelRule();
		} else if (ChannelRuleEnum.TENCENT.val.equals(sign)) {
			gcr = new TencentChannelRule();
		} 
		
		if (gcr != null) {
			gcr.process();
		} else {
			System.out.println("匹配不到");
		}
		System.out.println();
		System.out.println("*****************下面是优化后的方法*****************");
		
		main2();
	}
	
	
	/**
	 * 优化后的方法,即使新增再多的类,这里都不需要修改代码
	 * 以下是通过枚举来巧妙干掉if-else的方案，对于减少 if-else 还有很多有趣的解决方案（如：状态设计模式等）
	 * 下面这个也有不好的地方：类名，反射，getInstance不好么。用枚举的话每增加一个渠道得改枚举和改if-else有啥区别？
	 * match遍历的那里，可以改为map形式获取
	 * https://mp.weixin.qq.com/s/faQ3yWYM0swfDoEmtwa3nQ
	 * 使用策略模式很好地解决了if-else问题，但是违背了软件设计的开闭原则，还需要进一步改进！
	 * 策略加工厂就不存违反开闭原则了
	 * 这都能绝了，随便改数组或map，下标或hash一次到位，怎么都比for效率高
	 * 根据入参类名，使用spring加载多态实现类。
	 * @param args
	 */
	public static void main2() {
		String sign = "头条";
		ChannelRuleEnum2 cre = ChannelRuleEnum2.match(sign);
		if (cre != null) {
			GeneralChannelRule gcr = cre.gcr;
			System.out.println(ChannelRuleEnum.TOUTIAO.name());
			if (gcr != null) {
				gcr.process();
			} else {
				System.out.println("匹配不到");
			}
		} else {
			System.out.println("匹配不到");
		}
	}
}
