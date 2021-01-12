package com.yale.test.java.swing;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;


/*
 * 问：为什么要学动画?我又不想开发游戏。
 * 答：你也许不会去开发游戏,但可能会碰到仿真器,它也会持续地显示处理运算的结果或者你会需要创建出持续更新的图形来显示内存的耗用状况。
 * 这一类的事情都会遇到类似的处理方法。
 */
public class SimpleAnimation {
	int x = 70;
	int y = 70;
	
	public void go() throws InterruptedException {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		MyDrawPanel drawPanel = new MyDrawPanel();
		
		frame.getContentPane().add(drawPanel);
		frame.setSize(300, 300);
		frame.setVisible(true);
		
		for (int i=0; i<130; i++) {//重复循环30次改变JPanel的位置,达到动画的效果
			x++;
			y++;
			drawPanel.repaint();
			Thread.sleep(50);//加上延迟刻意放慢动画速度
		}
	}
	
	class MyDrawPanel extends JPanel {
		@Override
		public void paintComponent(Graphics g) {
			g.setColor(Color.white);//每次重绘之前先把JPanel的整个面板可用原来的背景色填满(为啥填满？就是为了覆盖之前我们画的图形,要不然就会留下痕迹)
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
			
			g.setColor(Color.green);
			g.fillOval(x, y, 40, 40);//使用外部的坐标来更新JPanel的位置
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		SimpleAnimation an = new SimpleAnimation();
		an.go();
	}
}
