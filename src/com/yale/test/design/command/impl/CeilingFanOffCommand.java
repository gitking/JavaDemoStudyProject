package com.yale.test.design.command.impl;

import com.yale.test.design.command.Command;
import com.yale.test.design.command.vo.CeilingFan;

public class CeilingFanOffCommand implements Command {

	public CeilingFanOffCommand(CeilingFan ceilingFan){
		this.ceilingFan = ceilingFan;
	}
	
	public CeilingFan ceilingFan;
	private int speed;
	@Override
	public void execute() {
		speed = this.ceilingFan.getSpeed();
		this.ceilingFan.high();
	}

	@Override
	public void undo() {
		if(speed == CeilingFan.HIGH){
			this.ceilingFan.high();
		} else if(speed == CeilingFan.MEDIUM){
			this.ceilingFan.medium();
		} else if(speed == CeilingFan.LOW){
			this.ceilingFan.low();
		} else if(speed == CeilingFan.OFF){
			this.ceilingFan.off();
		}
	}

}
