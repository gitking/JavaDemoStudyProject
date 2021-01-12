package com.yale.test.java.swing;

import javax.swing.JButton;
import javax.swing.JFrame;

/**
 * JFrame是个代表屏幕上window的对象。你可以把button,checkbox,text字段等接口放在window上面。
 * 标准的menu也可以加到上面,并且能够带最小化,最大化,关闭等图标。JFrame的长相会依据所处的平台而有所不同。
 * 将组件加到window上
 * 一旦创建出JFrame之后,你就可以把组件(widget)加到上面。有很多的Swing组件可以使用,他们是在javax.swing这个包中。
 * 最常用的组件包括:JButton,JRadioButton,JCheckBox,JLabel,JList,JScrollPane,JSlider,JTextArea,JTextField
 * 和JTable等。大部分都很容易使用,但像JTable这些是有点复杂的。
 * Swing的GUI组件是事件的来源,事件来源是个可以将用户操作(点击鼠标,按键,关闭窗口等)转换成事件的对象。对于java而言,事件几乎都是以对象表示。
 * 它会是某种事件类的对象,如果你查询API中的java.awt.event这个包,就会看到一组事件的类(名称中都有Event)。你会看到MouseEvent,KeyEvent
 * WindowEvent,ActionEvent等等。每个事件类型都有相对应的监听者接口。想要接受MouseEvent的话就得实现MouseListener这个接口.
 * 想要WindowEvent吗？实现WindowListener。
 * 事件监听接口让按钮有一个回调程序的方式,interface正是声明callback方法的地方。
 * @author dell
 *
 */
public class SimpleGuil {
	public static void main(String[] args) {
		JFrame frame = new JFrame();//创建JFrame
		
		/*
		 * 你还可以使用一些核心函数库"look and feel"类型,该类来控制界面的外观和操作感觉。大部分情况下都有俩种可以选：
		 * 称为Metal的java标准和平台原始界面俩种。要取得跨平台的相同外观就得使用Metal,不然就不要指定,让外观使用平台的默认值。
		 */
		JButton button = new JButton("click me,点击我");//创建JButton组件
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//退出时程序跟着退出
		frame.getContentPane().add(button);
		frame.setSize(300, 300);
		frame.setVisible(true);//显示出来
	}
}
