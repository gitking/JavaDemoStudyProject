package com.yale.design.command;

import com.yale.design.command.impl.NoCommand;

public class RemoteControlWithUndo {
	Command [] onCommands;
	Command [] offCommands;
	Command undoCommand;
	
	public RemoteControlWithUndo(){
		onCommands = new Command[7];
		offCommands = new Command[7];
		
		Command command = new NoCommand();
		for(int i=0; i<7;i++){
			onCommands[i] = command;
			offCommands[i] = command;
		}
		undoCommand = command;
	}
	
	public void setCommand(int slot,Command onCommand,Command offCommand){
		onCommands[slot] = onCommand;
		offCommands[slot] = offCommand;
	}
	
	public void onButtonWasPushed(int slot){
		onCommands[slot].execute();
		undoCommand = onCommands[slot];
	}
	
	public void offButtonWasPushed(int slot){
		offCommands[slot].execute();
		undoCommand = offCommands[slot];
	}
	
	public void undoButtonWasPushed(){
		undoCommand.undo();
	}
	
	public String toString(){
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("\n----Remote Control ------\n");
		for(int i = 0; i < onCommands.length; i++){
			stringBuffer.append("[slot" + i + "]" + onCommands[i].getClass().getName()
					+ " " + offCommands[i].getClass().getName() + "\n");
		}
		return stringBuffer.toString();
	}
}
