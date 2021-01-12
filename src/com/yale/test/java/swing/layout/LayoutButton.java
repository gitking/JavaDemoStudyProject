package com.yale.test.java.swing.layout;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;

public class LayoutButton {
	public void go() {
		JFrame frame = new JFrame();
		/*
		 * 按钮在东边(右边),所以JFrame会尊重他的宽度,但高度必须跟JFrame一样宽
		 */
		JButton button = new JButton("click like you mean it");
		frame.getContentPane().add(BorderLayout.EAST, button);
		frame.setSize(200, 200);
		frame.setVisible(true);
	}
	
	public void go1() {
		JFrame frame = new JFrame();
		//按钮在北边(上方),所以宽度会跟JFrame一样宽,高度是默认的高度
		JButton button = new JButton("There is no spoossssssssssssssssssssssssn...");
		frame.getContentPane().add(BorderLayout.NORTH, button);
		frame.setSize(200, 200);
		frame.setVisible(true);
	}
	
	public void go2() {
		JFrame frame = new JFrame();
		//按钮在北边(上方),所以宽度会跟JFrame一样宽,高度是默认的高度,不过可以通过更大的字体来让他的高度变高
		JButton button = new JButton("There is no spoossssssssssssssssssssssssn...");
		//28是字体的大小,更大的字体会强迫JFrame留更高的高度给按钮
		Font bigFont = new Font("serif", Font.BOLD, 28);
		button.setFont(bigFont);
		frame.getContentPane().add(BorderLayout.NORTH, button);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(200, 200);
		frame.setVisible(true);
	}
	
	public void goCenter() {
		JFrame frame = new JFrame();
		//按钮在北边(上方),所以宽度会跟JFrame一样宽,高度是默认的高度,不过可以通过更大的字体来让他的高度变高
		JButton east = new JButton("East");
		JButton west = new JButton("West");
		JButton north = new JButton("North");
		JButton south = new JButton("South");
		JButton center = new JButton("Center");
		frame.getContentPane().add(BorderLayout.NORTH, north);
		frame.getContentPane().add(BorderLayout.EAST, east);
		frame.getContentPane().add(BorderLayout.WEST, west);
		frame.getContentPane().add(BorderLayout.SOUTH, south);
		frame.getContentPane().add(BorderLayout.CENTER, center);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(300, 300);
		frame.setVisible(true);
	}
	
	/**
	 * 按钮如果在西区(左边)或者东区(右边),宽度可以自己决定,但高度是默认的高度,高度会受布局管理器的控制
	 * 按钮如果在北边(上方)或者南边(下边),高度可以通过字体来调整,宽度是默认的,宽度会受布局管理器的控制
	 * 南北会先占位,所以东西的高度还要扣除南北的高度
	 * 东西会取得预设的宽度南北会取得预设的高度
	 * 在中间呢？中间只能捡剩下的区域。中间的组件大小要看扣除周围之后还剩下多少
	 * @param args
	 */
	public static void main(String[] args) {
		LayoutButton lb = new LayoutButton();
		lb.goCenter();
	}
}
