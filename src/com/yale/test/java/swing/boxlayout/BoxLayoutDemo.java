package com.yale.test.java.swing.boxlayout;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * BoxLayout布局是救星,就算够宽它还是会垂直排列,不想FlowLayout布局,就算水平宽度足以容纳组件,它还是会用新的行来排列组件
 * @author dell
 */
public class BoxLayoutDemo {
	public void go() {
		JFrame frame = new JFrame();
		JPanel panel = new JPanel();
		panel.setBackground(Color.darkGray);
		//设置面板panel的布局管理器为BoxLayout,BoxLayout的构造函数需要知道它要管理哪个组件以及使用哪个轴
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		JButton button = new JButton("shock me");
		JButton buttonTwo = new JButton("bliss");
		panel.add(button);
		panel.add(buttonTwo);
		frame.getContentPane().add(BorderLayout.EAST, panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(250, 200);
		frame.setVisible(true);
	}
	
	/*
	 * 问:框架(JFrame)为什么不能像面板(Panel)那样直接地加上组件?
	 * 答:JFrame会这么特殊是因为它是让事物显示在画面上的接点。因为Swing的组件纯粹由JAVA构成,JFrame必须要连接到底层操作系统以便来存取显示装置。
	 * 我们可以把面板想做事安置在JFrame上的100%纯java层。或者把JFrames想做事支撑面板的框架。你甚至可以用自定义的JPanel来换掉框架的面板:
	 * myFrame.setContentPane(myPanel);
	 * 问:我能够换掉框架的布局管理器吗?如果我想让框架用顺序替换边界呢？
	 * 答:最简单的方法是创建一个面板,让此面板成为框架的contentpane,使得GUI以你想要的方式运行。
	 * 问:如果想要有不同的理想大小应该怎么办？组件是否有setSize()方法？
	 * 答:是有setSize()方法,但布局管理器会把它忽略掉。组件理想大小与你想要的大小是有差距的。理想的大小是根据组件确实所需的大小来计算的(组件自行计算)。
	 * 布局管理器会调用组件的getPreferredSize()方法,而此方法并不会考虑你之前对setSize()的调用。
	 * 问:能不直接定位？能不能关掉布局管理器？
	 * 答:你可以调用setLayout(null)直接设定画面位置和大小,但使用布局管理器还是比较好的方式。
	 * pack()方法会使window的大小符合内含组件的大小。
	 */
	public static void main(String[] args) {
		BoxLayoutDemo bd = new BoxLayoutDemo();
		//注意面板panel变窄了,因为不需要水平地塞入组件,因此只需要能够放下最宽的那一个就可以了。
		bd.go();
	}
}
