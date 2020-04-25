package com.yale.test.java.fanshe.action;

import java.lang.reflect.InvocationTargetException;

import com.yale.test.java.fanshe.util.BeanOperation;
import com.yale.test.java.fanshe.vo.Emp;

public class EmpAction {
	private Emp emp;
	public void setValue(String val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException, SecurityException, NoSuchMethodException, InstantiationException {
		BeanOperation.setBeanValue(this, val);
	}
	public Emp getEmp(){
		return emp;
	}
	public void setEmp(Emp emp) {
		this.emp = emp;
	}
}
