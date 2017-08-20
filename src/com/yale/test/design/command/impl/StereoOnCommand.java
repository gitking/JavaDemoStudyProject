package com.yale.test.design.command.impl;

import com.yale.test.design.command.Command;
import com.yale.test.design.command.vo.Stereo;

public class StereoOnCommand implements Command {
	Stereo stereo;
	public StereoOnCommand(Stereo stereo){
		this.stereo = stereo;
	}
	@Override
	public void execute() {
		this.stereo.on();
	}
	public void undo(){
		this.stereo.off();
	}
}
