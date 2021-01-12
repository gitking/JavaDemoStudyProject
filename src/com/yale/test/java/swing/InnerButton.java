package com.yale.test.java.swing;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

public class InnerButton {
	JFrame frame;
	JButton button;
	
	public void go() {
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		button = new JButton("A");
		button.addActionListener(new BListener());
		frame.getContentPane().add(BorderLayout.SOUTH, button);
		frame.setSize(200,  100);
		frame.setVisible(true);
	}
	
	class BListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (button.getText().equals("A")) {
				button.setText("B");
			} else {
				button.setText("A");
			}
		}
	}
	
	public static void main(String[] args) {
		InnerButton ib = new InnerButton();
		ib.go();
	}
}
