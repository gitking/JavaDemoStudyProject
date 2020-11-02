package com.yale.test.design.interpreter.command.impl;

import com.yale.test.design.interpreter.command.Command;
import com.yale.test.design.interpreter.command.TextEditor;

public class PasteCommand implements Command {
	private TextEditor receiver;
	
	public PasteCommand(TextEditor receiver) {
		this.receiver = receiver;
	}
	
	@Override
	public void execute() {
		this.receiver.paste();
	}
}
