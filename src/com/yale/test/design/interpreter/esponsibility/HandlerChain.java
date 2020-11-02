package com.yale.test.design.interpreter.esponsibility;

import java.util.ArrayList;
import java.util.List;

public class HandlerChain {
	
	private List<Handler> handlers = new ArrayList<>();// 持有所有Handler:
	
	public void addHanlder(Handler handler) {
		this.handlers.add(handler);
	}
	
	public boolean process(Request request) {
		for(Handler handler : handlers) {// 依次调用每个Handler:
			Boolean r = handler.process(request);
			if (r != null) {// 如果返回TRUE或FALSE，处理结束:
				System.out.println(request + " " + (r ? "Approved by " : "Denied by ") + handler.getClass().getSimpleName());
				return r;
			}
		}
		throw new RuntimeException("Could not handle request: " + request);
	}
}
