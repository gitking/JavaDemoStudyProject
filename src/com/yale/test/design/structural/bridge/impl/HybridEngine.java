package com.yale.test.design.structural.bridge.impl;

import com.yale.test.design.structural.bridge.Engine;

/*
 * 而针对每一种引擎，继承自Engine，例如HybridEngine：
 * 客户端通过自己选择一个品牌，再配合一种引擎，得到最终的Car：
 */
public class HybridEngine implements Engine {
	public void start() {
		System.out.println("Start Hybrid Engine...");
	}
}
