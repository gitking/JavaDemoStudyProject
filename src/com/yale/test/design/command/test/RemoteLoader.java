package com.yale.design.command.test;

import com.yale.design.command.Command;
import com.yale.design.command.RemoteControl;
import com.yale.design.command.RemoteControlWithUndo;
import com.yale.design.command.impl.CeilingFanHighCommand;
import com.yale.design.command.impl.CeilingFanLowCommand;
import com.yale.design.command.impl.CeilingFanMediumCommand;
import com.yale.design.command.impl.CeilingFanOffCommand;
import com.yale.design.command.impl.LightOffCommand;
import com.yale.design.command.impl.LightOnCommand;
import com.yale.design.command.impl.StereoOffCommand;
import com.yale.design.command.impl.StereoOnCommand;
import com.yale.design.command.vo.CeilingFan;
import com.yale.design.command.vo.Light;
import com.yale.design.command.vo.MacroCommand;
import com.yale.design.command.vo.Stereo;

public class RemoteLoader {
	public static void main(String[] args) {
		RemoteControl remoteControl = new RemoteControl();
		
		Light livingRoomLight = new Light("Living Room");
		Light kitchen = new Light("Kitchen");
		
		LightOnCommand lightOnCommand = new LightOnCommand(livingRoomLight);
		LightOffCommand lightOffCommand = new LightOffCommand(livingRoomLight);
		
		LightOnCommand kitchenLightOnCommand = new LightOnCommand(kitchen);
		LightOffCommand kitchenLightOffCommand = new LightOffCommand(kitchen);
		
		Stereo stereo = new Stereo("锤子音响");
		
		StereoOnCommand stereoOnCommand = new StereoOnCommand(stereo);
		StereoOffCommand stereoOffCommand = new StereoOffCommand(stereo);
		remoteControl.setCommand(0, kitchenLightOnCommand, kitchenLightOffCommand);
		remoteControl.setCommand(1, lightOnCommand, lightOffCommand);
		remoteControl.setCommand(2, stereoOnCommand, stereoOffCommand);
		
		System.out.println("数组:" + remoteControl);
		
		remoteControl.onButtonWasPushed(0);
		remoteControl.offButtonWasPushed(0);
		remoteControl.onButtonWasPushed(1);
		remoteControl.offButtonWasPushed(1);
		remoteControl.onButtonWasPushed(2);
		remoteControl.offButtonWasPushed(2);
		
		
		RemoteControlWithUndo remoteControlWihtUndo = new RemoteControlWithUndo();
		
		Light livingRoomLightSec = new Light("Living Room");
		Light kitchenSec = new Light("Kitchen");
		
		LightOnCommand lightOnCommandSec = new LightOnCommand(livingRoomLightSec);
		LightOffCommand lightOffCommandSec = new LightOffCommand(livingRoomLightSec);
		
		LightOnCommand kitchenLightOnCommandSec = new LightOnCommand(kitchenSec);
		LightOffCommand kitchenLightOffCommandSec = new LightOffCommand(kitchenSec);
		
		Stereo stereoSec = new Stereo("锤子音响");
		
		StereoOnCommand stereoOnCommandSec = new StereoOnCommand(stereoSec);
		StereoOffCommand stereoOffCommandSec = new StereoOffCommand(stereoSec);
		remoteControlWihtUndo.setCommand(0, kitchenLightOnCommandSec, kitchenLightOffCommandSec);
		remoteControlWihtUndo.setCommand(1, lightOnCommandSec, lightOffCommandSec);
		remoteControlWihtUndo.setCommand(2, stereoOnCommandSec, stereoOffCommandSec);
		
		System.out.println("数组:" + remoteControlWihtUndo);
		
		remoteControlWihtUndo.onButtonWasPushed(0);
		remoteControlWihtUndo.offButtonWasPushed(0);
		
		remoteControlWihtUndo.undoButtonWasPushed();
		
		remoteControlWihtUndo.onButtonWasPushed(1);
		remoteControlWihtUndo.offButtonWasPushed(1);
		
		remoteControlWihtUndo.undoButtonWasPushed();
		
		remoteControlWihtUndo.onButtonWasPushed(2);
		remoteControlWihtUndo.offButtonWasPushed(2);
		
		remoteControlWihtUndo.undoButtonWasPushed();
		
		CeilingFan ceilingFan = new CeilingFan("高级风扇");
		CeilingFanHighCommand ceilingFanHighCommand = new CeilingFanHighCommand(ceilingFan);
		CeilingFanLowCommand ceilingFanLowCommand = new CeilingFanLowCommand(ceilingFan);
		CeilingFanMediumCommand ceilingFanMediumCommand = new CeilingFanMediumCommand(ceilingFan);
		
		CeilingFanOffCommand ceilingFanOffCommand = new CeilingFanOffCommand(ceilingFan);

		remoteControlWihtUndo.setCommand(0, ceilingFanHighCommand, ceilingFanOffCommand);
		remoteControlWihtUndo.setCommand(2, ceilingFanLowCommand, ceilingFanOffCommand);
		remoteControlWihtUndo.setCommand(1, ceilingFanMediumCommand, ceilingFanOffCommand);

		remoteControlWihtUndo.onButtonWasPushed(0);
		remoteControlWihtUndo.offButtonWasPushed(0);
		
		remoteControlWihtUndo.undoButtonWasPushed();
		
		remoteControlWihtUndo.onButtonWasPushed(1);
		remoteControlWihtUndo.offButtonWasPushed(1);
		
		remoteControlWihtUndo.undoButtonWasPushed();
		
		remoteControlWihtUndo.onButtonWasPushed(2);
		remoteControlWihtUndo.offButtonWasPushed(2);
		remoteControlWihtUndo.undoButtonWasPushed();
		
		
		/**
		 * 宏命令
		 */
		
		LightOnCommand lightOnCommandThird = new LightOnCommand(livingRoomLight);
		LightOffCommand lightOffCommandThird = new LightOffCommand(livingRoomLight);
		
		LightOnCommand kitchenLightOnCommandThird = new LightOnCommand(kitchen);
		LightOffCommand kitchenLightOffCommandThird = new LightOffCommand(kitchen);
		
		StereoOnCommand stereoOnCommandThird = new StereoOnCommand(stereo);
		StereoOffCommand stereoOffCommandThird = new StereoOffCommand(stereo);
		
		Command [] onCommandArr = new Command []{lightOnCommandThird,kitchenLightOnCommandThird,stereoOnCommandThird};
		Command [] offCommandArr = new Command []{lightOffCommandThird,kitchenLightOffCommandThird,stereoOffCommandThird};
		
		/**
		 * 宏命令对象
		 */
		MacroCommand macroOnCommmand = new MacroCommand(onCommandArr);
		
		MacroCommand macroOffCommmand = new MacroCommand(offCommandArr);
		
		remoteControlWihtUndo.setCommand(0, macroOnCommmand, macroOffCommmand);

		remoteControlWihtUndo.onButtonWasPushed(0);
		remoteControlWihtUndo.offButtonWasPushed(0);
		remoteControlWihtUndo.undoButtonWasPushed();

	}
}
