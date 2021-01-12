package com.yale.test.java.swing;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.yale.test.java.swing.panel.MyDrawPanel01;

public class TwoButtons {
	JFrame frame;
	JLabel label;
	
	public void go() {
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JButton labelButton = new JButton("Change Label");
		labelButton.addActionListener(new LabelListener());
		JButton colorButton = new JButton("Change Circle");
		colorButton.addActionListener(new ColorListener());
		
		label = new JLabel("i'm a label");
		MyDrawPanel01 drawPanel = new MyDrawPanel01();
		frame.getContentPane().add(BorderLayout.SOUTH, colorButton);
		frame.getContentPane().add(BorderLayout.CENTER, drawPanel);
		frame.getContentPane().add(BorderLayout.EAST, labelButton);
		frame.getContentPane().add(BorderLayout.WEST, label);
		
		frame.setSize(300, 300);
		frame.setVisible(true);
	}
	
	class LabelListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			label.setText("Ocuh!");
		}
	}
	
	class ColorListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			frame.repaint();
		}
	}
	
	public static void main(String[] args) {
		TwoButtons tb  = new TwoButtons();
		tb.go();
	}
}
