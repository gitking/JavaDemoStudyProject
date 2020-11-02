package com.yale.test.web.servlet.lxf.mvc.framework;

import java.util.HashMap;
import java.util.Map;

public class ModelAndView {
	Map<String, Object> model;
	String view;
	
	public ModelAndView(String view) {
		this.view = view;
		this.model = new HashMap<String, Object>();
	}
	
	public ModelAndView(String view, String name, Object value) {
		this.view = view;
		this.model = new HashMap<String, Object>();
		this.model.put(name, value);
	}
	
	public ModelAndView(String view, Map<String, Object> model) {
		this.view = view;
		this.model = new HashMap<String, Object>();
	}
}
