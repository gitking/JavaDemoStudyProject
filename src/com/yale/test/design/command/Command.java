package com.yale.test.design.command;

/**
 * CommandPattern 命令模式
 */
public interface Command {
	public void execute();
	public void undo();
}
