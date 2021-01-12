package com.yale.test.java.swing.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventObject;

import javax.swing.JButton;
import javax.swing.JFrame;

/*
 * Swing的GUI组件是事件的来源,事件来源是个可以将用户操作(点击鼠标,按键,关闭窗口等)转换成事件的对象。
 * 如果你查询java.awt.event这个包,就会看到一组事件的类(名称中都有Event)。你会看到MouseEvent,KeyEvent,WindowEvent,ActionEvent等待。
 * 事件源(例如按钮)会在用户做出相关动作时(按下按钮)产生事件对象,你的程序在大多数的情况下是事件的接受方而不是创建方。
 * 每个事件类型都有相对应的监听者接口。想要接收MouseEvent的话就得实现MouseListener这个接口。想要WindowEvent吗？实现WindowListener.
 * ActionListener.actionPerformed(ActionEvent event)
 * ItemListener.itemStateChanged(ItemEvent event)
 * KeyListener.keyPressed(KeyEvent ev)
 * KeyListener.keyReleased(KeyEvent ev)
 * KeyListener.keyTyped(KeyEvent ev)
 * 如果类想要知道按钮的ActionEvent,就得实现ActionListener这个接口。
 * 某些接口不只有一个方法,比如MouseListener就有5个方法,因为事件本身就有不同的形态。以MouseListener为例,事件就有mousePressed,mouseReleased,
 * mouseMoved等。虽然都是MouseEvent，每个鼠标事件都在接口中有不同的方法,mousePressed()会在用户按下鼠标的时候被调用。
 * 按鼠标按键放开的时候会调用mouseReleased(),因此鼠标事件只有MouseEvent一种事件对象,却有不同的事件方法来表示不同类型的鼠标事件。
 * 下列的每个组件(widget,用户接口对象)是一或多个事件的来源。把组件可能会发出的事件连起来。有些组件会有多个事件,有些事件可能会有多个来源。
 * Widgets 			Event methods
 * check box		windowClosing()
 * text field		actionPerformed()
 * scrolling list		itemStateChanged()
 * button				mousePressed()
 * dialog box		keyTyped()
 * radio button		mouseExited()
 * menu item		focusGained()
 * 问:我看不出传给事件调用方法的事件对象有什么重要性。调用mousePressed时可能有哪些信息会被用到？
 * 答:大部分情况下你不会用到事件对象,它只不过是个携带事件数据的载体。但有时你也会需要查询事件的特定细节。例如你的mousePressed()被调用时
 * 你知道有鼠标的按钮被按下。但如果你想要知道鼠标的坐标呢？或者有时候你会想要对相同的监听注册多个对象。举例来说,计算器程序会有10个按键,且因为都做相同
 * 的事情,所以你可能不想为每个按键都制作监听。所以当你收到事件时,可以设计成用事件对象的信息来判断哪一个按钮发了事件。
 */
public class SimpleGui1B implements ActionListener {
	JButton button;
	
	@Override//实现ActionListener接口的方法,这才是真正处理事件的方法
	public void actionPerformed(ActionEvent event){
		String command = event.getActionCommand();
		int eventId = event.getID();
		Object obj = event.getSource();
		if (obj instanceof EventObject) {
			EventObject eo = (EventObject)obj;
			System.out.println(eo.getSource());
		} else if (obj instanceof JButton) {
			JButton jb = (JButton)obj;
			System.out.println(jb.getActionCommand());
			System.out.println(jb.getAlignmentX());
			System.out.println(jb.getAlignmentY());
		}
		 
		System.out.println("按钮[" + command + "]替换：我被点击了,事件ID为:" + eventId);
		button.setText("替换：我被点击了");
	}
	
	public void go() {
		JFrame frame = new JFrame();
		button = new JButton("click me");
		button.addActionListener(this);//按钮注册点击事件
		frame.getContentPane().add(button);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(300, 300);
		frame.setVisible(true);
	}
	
	public static void main(String[] args) {
		SimpleGui1B sg = new SimpleGui1B();
		sg.go();
	}
}
