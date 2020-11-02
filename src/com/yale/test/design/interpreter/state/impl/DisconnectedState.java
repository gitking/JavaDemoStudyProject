package com.yale.test.design.interpreter.state.impl;

import com.yale.test.design.interpreter.state.State;

public class DisconnectedState implements State {

	@Override
	public String init() {
		return "Bye!";
	}

	@Override
	public String reply(String input) {
		return "";
	}
}
