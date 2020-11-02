package com.yale.test.design.interpreter.command.impl;

import com.yale.test.design.interpreter.command.Command;
import com.yale.test.design.interpreter.command.TextEditor;

public class CopyCommand implements Command{
	private TextEditor receiver;
	
	public CopyCommand(TextEditor receiver) {
		this.receiver = receiver;
	}
	
	@Override
	public void execute() {
		this.receiver.copy();
	}
}
