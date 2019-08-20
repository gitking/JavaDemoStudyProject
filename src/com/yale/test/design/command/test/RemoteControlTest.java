package com.yale.test.design.command.test;

import com.yale.test.design.command.SimpleRemoteControl;
import com.yale.test.design.command.impl.LightOnCommand;
import com.yale.test.design.command.vo.Light;

/**
 * 命令模式：将"请求"封装成对象，以便使用不同的请求，队列或者日志来参数化其他对象。
 * 命令模式也支持可撤销的操作。
 * @author lenovo
 */
public class RemoteControlTest {
	public static void main(String[] args) {
		SimpleRemoteControl remote = new SimpleRemoteControl();
		Light light = new Light();
		LightOnCommand lightOn = new LightOnCommand(light);
		
		remote.setSlot(lightOn);
		remote.butttonWasPressed();
	}
}
