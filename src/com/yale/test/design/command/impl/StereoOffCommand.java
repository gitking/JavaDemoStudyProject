package com.yale.test.design.command.impl;

import com.yale.test.design.command.Command;
import com.yale.test.design.command.vo.Stereo;

public class StereoOffCommand implements Command{
	Stereo stereo;
	public StereoOffCommand(Stereo stereo){
		this.stereo = stereo;
	}
	
	public void execute(){
		this.stereo.off();
	}
	public void undo(){
		this.stereo.on();
	}
}
