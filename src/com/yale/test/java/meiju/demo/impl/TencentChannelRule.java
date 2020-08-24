package com.yale.test.java.meiju.demo.impl;

import com.yale.test.java.meiju.demo.GeneralChannelRule;

public class TencentChannelRule extends GeneralChannelRule {
	@Override
	public void process() {
		System.out.println("腾讯渠道处理逻辑");
	}
}
