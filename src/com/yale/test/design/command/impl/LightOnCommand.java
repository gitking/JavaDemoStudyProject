package com.yale.test.design.command.impl;

import com.yale.test.design.command.Command;
import com.yale.test.design.command.vo.Light;

public class LightOnCommand implements Command {
	
	private Light light;
	
	public LightOnCommand(Light light){
		this.light = light;
	}
	
	@Override
	public void execute() {
		light.on();
	}
	public void undo(){
		this.light.off();
	}
}
