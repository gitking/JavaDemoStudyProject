package com.yale.test.java.swing;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import com.yale.test.java.swing.panel.MyDrawPanel01;

public class SimpleGui3C implements ActionListener{
	JFrame jframe;
	public void go(){
		jframe = new JFrame();
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JButton button = new JButton("change colors");
		button.addActionListener(this);
		
		MyDrawPanel01 drawPanel = new MyDrawPanel01();
		
		/*
		 * jframe默认有5个区域最上面是north,左边是west,右边是east,下面是south,中间是center
		 */
		jframe.getContentPane().add(BorderLayout.SOUTH, button);
		jframe.getContentPane().add(BorderLayout.CENTER, drawPanel);
		jframe.setSize(300,300);
		jframe.setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent event){
		jframe.repaint();//要求jframe重新绘制
	}
	
	public static void main(String[] args) {
		SimpleGui3C sgc = new SimpleGui3C();
		sgc.go();
	}
}
