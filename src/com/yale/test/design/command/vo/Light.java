package com.yale.design.command.vo;

/**
 * 接收者
 * @author lenovo
 */
public class Light {
	public String desc;
	public Light(String desc){
		this.desc = desc;
	}
	
	
	public Light(){
		this.desc = "默认值";
	}
	
	public void on(){
		System.out.println(this.desc + "电灯被打开了。");
	}
	
	public void off(){
		System.out.println(this.desc + "电灯被关闭了。");
	}
}
