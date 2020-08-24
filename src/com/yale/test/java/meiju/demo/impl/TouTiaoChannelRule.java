package com.yale.test.java.meiju.demo.impl;

import com.yale.test.java.meiju.demo.GeneralChannelRule;

public class TouTiaoChannelRule extends GeneralChannelRule {
	@Override
	public void process() {
		System.out.println("头条渠道处理逻辑");
	}
}
