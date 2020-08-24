package com.yale.test.java.meiju.demo;

import com.yale.test.java.meiju.demo.impl.TencentChannelRule;
import com.yale.test.java.meiju.demo.impl.TouTiaoChannelRule;

/**
 * 渠道枚举类
 * @author dell
 */
public enum ChannelRuleEnum2 {
	/*
	 * 这里说白了就是个map,先把不同的渠道对象实例创建好,match那块根据key取出来
	 */
	TOUTIAO("头条", new TouTiaoChannelRule()),
	TENCENT("腾讯", new TencentChannelRule());
	public final String val;
	public final GeneralChannelRule gcr;
	
	private ChannelRuleEnum2(String val, GeneralChannelRule gcr) {
		this.val = val;
		this.gcr = gcr;
	}
	
	/*
	 * 以上是通过枚举来巧妙干掉if-else的方案，对于减少 if-else 还有很多有趣的解决方案（如：状态设计模式等）
	 */
	public static ChannelRuleEnum2 match(String name){
		ChannelRuleEnum2[] values = ChannelRuleEnum2.values();
		for (ChannelRuleEnum2 value: values) {
			if (value.val.equals(name)){
				return value;
			}
		}
		return null;
	}
}
