package com.yale.test.java.swing;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

/*
 * 如果你要在屏幕放上自己的图形,最好的方式是自己创建出有绘画功能的widget。你把widget方法jframe上。如同按钮或其他widget一样,
 * 不同之处在于它会按照你所要的方式绘制,你还可以让图形移动，表现动画效果或在点选的时候改变颜色。简单的不得了。
 * 创建JPanel的子类并覆盖掉paintComponent这个方法。
 * 所有绘图程序代码都在paintComponent()里面。把这个方法想象成会被系统告知要把要把自己画出来的方法。如果你要画的是圆圈，就写画圆圈的程序。
 * 当你的Panel所处的frame显示的时候,paintComponent()就会被调用。如果用户缩小window或者选择最小化,Java虚拟机也会知道要调用它来重新绘制。
 * 任何时候JAVA虚拟机发现有必要重绘都会这么做。
 * 还有一件事,你不会自己调用这个方法！它的参数是个跟实际屏幕有关的Graphics对象,你无法取得这个对象,它必须由系统来交给你。
 * 然而,你还是可以调用reapint()来要求系统重新绘制显示装置,然后才会产生paintComponent的调用.
 * 在widget上绘制2D图形,使用graphics对象来绘制图形。graphics.fillOval(70, 70, 100, 100);
 * 你可以画上很多的方块和圆圈,Java2D API有很多好玩,复杂的图形方法。
 * 也可以在widget上绘制JPEG图,把图形画在widget上。graphics.drawImage(myPic,10,10,this)
 * 
 * 可以对Graphics引用调用的方法:
 * drawImage()
 * drawLine()
 * drawPolygon
 * drawRect()
 * drawOval()
 * fillRect()
 * fillRoundRect()
 * setColor()
 * 转换成Graphics2D对象
 * Graphics2D g2d = (Graphics2D)g;
 * 可以对Graphics2D引用调用的方法:
 * fill3DRect()
 * draw3DRect()
 * rotate()
 * scale()
 * shear()
 * transform()
 * setRenderingHints()
 * 这不是完整的列表,查询API文件可以取得最完整的说明
 * GUI从创建window开始,通常会使用JFrame
 * JFrame与其他组件不同,不能直接加上组件,要用他的contentpane
 * 二维图形可以直接画在图形组件上。
 * .gif与.jpeg文件可以直接放在组件上。
 * 使用Image来绘制.jpg
 * Image image = new ImageIcon("pic.jpg").getImage();
 * g.drawImage(image, 3, 4, this); 
 */
public class MyDrawPanel extends JPanel {
	
	/**
	 * paintComponent是非常重要的方法,你决不能自己调用,要由系统来调用
	 * Graphics是个抽象类,传进来的参数实际上很有可能是Graphics2D的实例
	 */
	public void paintComponent(Graphics g) {
		//你可以把Graphics想象成绘图装置,告诉它要用什么颜色画出什么形状
		g.setColor(Color.orange);
		g.fillRect(20, 50, 100, 100);
		
		//显示JPEG
		//Image image = new ImageIcon("catzilla.jpg").getImage();
		//离panel的左方边缘3个像素,离顶端向下4个像素
		//g.drawImage(image, 3, 4, this);
		
		/*
		 * 以默认颜色填充
		 * fillRect前俩个参数是起点的坐标,后面俩个参数分别是宽度和高度,此处取得本身的宽高,因此会把panel填满
		 */
//		g.fillRect(0, 0, this.getWidth(), this.getHeight());
//		int red = (int)(Math.random() * 255);
//		int green = (int)(Math.random() * 255);
//		int blue = (int)(Math.random() * 255);
//		Color randomColor = new Color(red, green, blue);//传入3个参数来代表RGB的值
//		g.setColor(randomColor);
//		g.fillOval(70, 70, 100, 100);//填满参数指定的椭圆性区域
		
//		if (g instanceof Graphics2D) {渐变色
//			Graphics2D g2d = (Graphics2D)g;
//			
//			//渐变色70,70起点位置,Color.blue是开始的颜色,150,150是终点,Color.orange渐变最后的颜色
//			GradientPaint gradient = new GradientPaint(70, 70, Color.blue, 150, 150, Color.orange);
//			g2d.setPaint(gradient);//将虚拟的"笔刷"换成渐层
//			g2d.fillOval(70, 70, 100, 100);//用目前的笔刷设定来填满椭圆型的区域
//		}
//		if (g instanceof Graphics2D) {//随机渐变色
//			Graphics2D g2d = (Graphics2D)g;
//			int red = (int)(Math.random() * 255);
//			int green = (int)(Math.random() * 255);
//			int blue = (int)(Math.random() * 255);
//			Color startColor = new Color(red, green, blue);
//			
//			
//			red = (int)(Math.random() * 255);
//			green = (int)(Math.random() * 255);
//			blue = (int)(Math.random() * 255);
//			Color endColor = new Color(red, green, blue);
//			
//			//渐变色70,70起点位置,Color.blue是开始的颜色,150,150是终点,Color.orange渐变最后的颜色
//			GradientPaint gradient = new GradientPaint(70, 70, startColor, 150, 150, endColor);
//			g2d.setPaint(gradient);//将虚拟的"笔刷"换成渐层
//			g2d.fillOval(70, 70, 100, 100);//用目前的笔刷设定来填满椭圆型的区域
//		}
	}
}
