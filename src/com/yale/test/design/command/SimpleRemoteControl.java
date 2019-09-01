package com.yale.test.design.command;

public class SimpleRemoteControl {
	
	private Command slot;
	
	public SimpleRemoteControl(){
	}
	
	/**
	 * 当按钮被按下时就会调用这个方法
	 */
	public void butttonWasPressed(){
		slot.execute();
	}

	public Command getSlot() {
		return slot;
	}

	public void setSlot(Command slot) {
		this.slot = slot;
	}
	
	
}
