package com.yale.test.java.swing.widget;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

/*
 * JTextArea可以有超过一行以上的文字。它只需要少许的设定就可以制作出来,但是JTextArea本身没有滚动条或换行的功能。
 * 要让JTextArea滚动起来,就必须要把它粘在ScrollPane上。ScrollPane是个非常喜欢滚动的对象,也会考虑文本区域的滚动需求。
 */
public class TextArea1 implements ActionListener {
	JTextArea text;
	public void go() {
		JFrame frame = new JFrame();
		JPanel panel = new JPanel();
		JButton button = new JButton("Just Click It");
		button.addActionListener(this);
		text = new JTextArea(10, 20);//代表10行高,20字宽
		text.setLineWrap(true);
		
		JScrollPane scroller = new JScrollPane(text);//把JTextArea放进JScrollPane里面
		//设置只使用垂直滚动条
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		panel.add(scroller);//把JScrollPane加入到面板里面
		frame.getContentPane().add(BorderLayout.CENTER, panel);
		frame.getContentPane().add(BorderLayout.SOUTH, button);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(350, 300);
		frame.setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent ev) {
		text.append("button clicked \n");//按下按钮往JTextArea里面加入一句话
		//text.setText("");//替换掉JTextArea里面的文字内容
		//text.selectAll();//选中JTextArea里面的内容
		//text.requestFocus();//把GUI目前的焦点拉回到文本字段以便让用户进行输入操作
	}
	public static void main(String[] args) {
		TextArea1 ta = new TextArea1();
		ta.go();
	}
}
