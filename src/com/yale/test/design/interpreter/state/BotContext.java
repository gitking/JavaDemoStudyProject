package com.yale.test.design.interpreter.state;

import com.yale.test.design.interpreter.state.impl.ConnectedState;
import com.yale.test.design.interpreter.state.impl.DisconnectedState;

public class BotContext {
	private State state = new DisconnectedState();
	public String chat(String input) {
		if ("hello".equalsIgnoreCase(input)) {
			state = new ConnectedState();
			return state.init();
		} else if("bye".equalsIgnoreCase(input)) {
			state = new DisconnectedState();
			return state.init();
		}
		return state.reply(input);
	}
}
