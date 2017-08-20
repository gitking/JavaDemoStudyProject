package com.yale.design.command.vo;

public class Stereo {
	public String desc;
	public Stereo(String desc){
		this.desc = desc;
	}
	public Stereo(){
		
	}
	public void on(){
		System.out.println(this.desc + "打开音响");
	}
	
	public void off(){
		System.out.println(this.desc + "关闭音响");
	}
}
