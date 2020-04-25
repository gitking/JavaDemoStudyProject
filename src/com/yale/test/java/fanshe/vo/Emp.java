package com.yale.test.java.fanshe.vo;

import java.util.Date;

public class Emp {
	private String ename;
	private String job;
	private Double salary;
	private Date hiredate;
	private Dept dept;
	public String getEname() {
		return ename;
	}
	public void setEname(String ename) {
		this.ename = ename;
	}
	public String getJob() {
		return job;
	}
	public void setJob(String job) {
		this.job = job;
	}
	
	public Dept getDept() {
		return dept;
	}
	public void setDept(Dept dept) {
		this.dept = dept;
	}
	
	public Double getSalary() {
		return salary;
	}
	public void setSalary(Double salary) {
		this.salary = salary;
	}
	public Date getHiredate() {
		return hiredate;
	}
	public void setHiredate(Date hiredate) {
		this.hiredate = hiredate;
	}
	@Override
	public String toString() {
		return "Emp [ename=" + ename + ", job=" + job + ", salary=" + salary + ", hiredate=" + hiredate + ", dept="
				+ dept + "]";
	}
}
