package com.yale.test.java.fanshe.action;

import java.lang.reflect.InvocationTargetException;

import com.yale.test.java.fanshe.util.BeanOperation;
import com.yale.test.java.fanshe.vo.Dept;
import com.yale.test.java.fanshe.vo.Emp;

public class DeptAction {
	private Dept dept = new Dept();
	public void setValue(String val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException, SecurityException, NoSuchMethodException, InstantiationException {
		BeanOperation.setBeanValue(this, val);
	}
	public Dept getDept(){
		return dept;
	}
}
