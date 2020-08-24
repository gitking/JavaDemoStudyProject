package com.yale.test.java.meiju.demo;

/**
 * 渠道枚举类
 * @author dell
 */
public enum ChannelRuleEnum {
	TOUTIAO("头条"),
	TENCENT("腾讯");
	public final String val;
	private ChannelRuleEnum(String val) {
		this.val = val;
	}
}
