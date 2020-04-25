package com.yale.test.java.fanshe.test;

import java.lang.reflect.InvocationTargetException;
import com.yale.test.java.fanshe.action.DeptAction;

public class TestDeptDemo {
	public static void main(String[] args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException, SecurityException, NoSuchMethodException, InstantiationException {
		String value = "dept.dname:技术部|dept.loc:北京";
		DeptAction action = new DeptAction();
		action.setValue(value);
		System.out.println(action.getDept());
	}
}
