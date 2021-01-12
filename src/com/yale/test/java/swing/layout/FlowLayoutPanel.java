package com.yale.test.java.swing.layout;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class FlowLayoutPanel {
	
	public void go(){
		JFrame frame = new JFrame();
		JPanel panel = new JPanel();//注意此时面板还没有东西在上面,所以不会要求太多的区域
		panel.setBackground(Color.darkGray);//让面板变成深灰色以便观察
		frame.getContentPane().add(BorderLayout.EAST, panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(200, 200);
		frame.setVisible(true);
	}
	
	public void go1(){
		JFrame frame = new JFrame();
		JPanel panel = new JPanel();
		panel.setBackground(Color.darkGray);//让面板变成深灰色以便观察
		JButton button = new JButton("shock me");
		JButton buttonTwo = new JButton("bliss");
		panel.add(button);//把按钮加到面板上panel,会发现面板变宽了,面板默认使用的顺序布局
		panel.add(buttonTwo);//注意字少的按钮宽度也是比较小的,顺序布局会让按钮取得刚好所需的大小
		frame.getContentPane().add(BorderLayout.EAST, panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(250, 200);
		frame.setVisible(true);
	}
	
	public static void main(String[] args) {
		FlowLayoutPanel fp = new FlowLayoutPanel();
		fp.go1();
	}
}
