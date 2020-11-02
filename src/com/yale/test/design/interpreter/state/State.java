package com.yale.test.design.interpreter.state;

public interface State {
	String init();
	String reply(String input);
}
