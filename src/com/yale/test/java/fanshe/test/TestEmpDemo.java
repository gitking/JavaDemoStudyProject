package com.yale.test.java.fanshe.test;

import java.lang.reflect.InvocationTargetException;

import com.yale.test.java.fanshe.action.EmpAction;

public class TestEmpDemo {
	public static void main(String[] args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException, SecurityException, NoSuchMethodException, InstantiationException {
		String value = "emp.ename:smith|emp.job:clerk|emp.dept.dname:财务部|"+
					   "emp.dept.company.name:MLDN|emp.dept.company.address:北京天安门|" + 
					   "emp.salary:1999.12|emp.hiredate:1999-10-10|" + 
					   "emp.dept.count:65465465465|emp.dept.company.cid:10|emp.dept.company.create:1990-09-15 10:10:10";
		EmpAction action = new EmpAction();
		action.setValue(value);
		System.out.println(action.getEmp());
	}
}
