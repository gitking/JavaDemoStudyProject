package com.yale.test.java.swing.panel;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class MyDrawPanel01 extends JPanel{
	
	@Override
	public void paintComponent(Graphics g){
		System.out.println("我被调用了..............");
		if (g instanceof Graphics2D) {//随机渐变色
			Graphics2D g2d = (Graphics2D)g;
			int red = (int)(Math.random() * 255);
			int green = (int)(Math.random() * 255);
			int blue = (int)(Math.random() * 255);
			Color startColor = new Color(red, green, blue);
			
			
			red = (int)(Math.random() * 255);
			green = (int)(Math.random() * 255);
			blue = (int)(Math.random() * 255);
			Color endColor = new Color(red, green, blue);
			
			//渐变色70,70起点位置,Color.blue是开始的颜色,150,150是终点,Color.orange渐变最后的颜色
			GradientPaint gradient = new GradientPaint(70, 70, startColor, 150, 150, endColor);
			g2d.setPaint(gradient);//将虚拟的"笔刷"换成渐层
			g2d.fillOval(70, 70, 100, 100);//用目前的笔刷设定来填满椭圆型的区域
		} else {
			System.out.println(g.getClass());
		}
	}
}
