package com.yale.test.design.command.vo;

import com.yale.test.design.command.Command;

/**
 * 宏命令,一次执行一组命令
 * @author lenovo
 */
public class MacroCommand implements Command {

	Command [] commands;
	
	public MacroCommand(Command [] commands){
		this.commands = commands;
	}
	
	@Override
	public void execute() {
		for(Command command : commands){
			command.execute();
		}
	}

	@Override
	public void undo() {
		for(Command command : commands){
			command.undo();
		}
	}
}
