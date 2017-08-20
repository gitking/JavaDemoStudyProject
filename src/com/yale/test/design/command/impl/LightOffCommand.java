package com.yale.test.design.command.impl;

import com.yale.test.design.command.Command;
import com.yale.test.design.command.vo.Light;

public class LightOffCommand implements Command {

	Light light;
	
	public LightOffCommand(Light light){
		this.light = light;
	}
	
	public void execute() {
		light.off();
	}
	
	public void undo(){
		this.light.on();
	}
}
