package com.yale.test.web.listener;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

public class Demo {
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setSize(300, 200);//窗口宽300高200
		frame.setLocation(200, 200);//窗口出现的坐标位置200,200
		frame.setLayout(new FlowLayout());//给窗口指定一个布局管理器,FlowLayout流式布局,不指定用默认的
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//关闭窗口时也自动关闭程序
		
		JButton btn = new JButton("确定");//JButton是一个事件源
		frame.add(btn, BorderLayout.NORTH);//将按钮添加到窗口上面,并放在北边
		/**
		 * 监听器:
		 *  监听器是一个接口,内容由我们来实现
		 *  它需要注册,例如注册在按钮上
		 *  监听器中的方法,会在特殊事件发生时自动调用(观察者模式)
		 *  事件源
		 *  事件
		 *  监听器(监听器注册在事件源上,等待特定的事件发生,然后调用自己的方法)
		 *  JavaWeb被监听的事件源为:ServletContext, HttpSession,ServletRequest即三大域对象
		 */
		btn.addActionListener(new ActionListener() {//给按钮添加一个监听器
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("ActionEvent 是一个事件 ,ActionListener是一个监听器");
				System.out.println("你点了[确定]按钮," + e.getID());
			}
		});
		
		frame.setVisible(true);//显示窗口
	}
}
