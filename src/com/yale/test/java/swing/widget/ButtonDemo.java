package com.yale.test.java.swing.widget;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ButtonDemo {
	public void go() {
		JFrame frame = new JFrame();
		JPanel panel = new JPanel();
		panel.setBackground(Color.darkGray);
		JButton button = new JButton("tesuji");
		JButton buttonTwo = new JButton("watari");
		panel.add(buttonTwo);
		frame.getContentPane().add(BorderLayout.CENTER, button);
		frame.setSize(300, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public void go1() {
		JFrame frame = new JFrame();
		JPanel panel = new JPanel();
		panel.setBackground(Color.darkGray);
		JButton button = new JButton("tesuji");
		JButton buttonTwo = new JButton("watari");
		panel.add(buttonTwo);
		frame.getContentPane().add(BorderLayout.NORTH, panel);
		frame.getContentPane().add(BorderLayout.CENTER, button);
		frame.setSize(300, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public void go2() {
		JFrame frame = new JFrame();
		JPanel panel = new JPanel();
		panel.setBackground(Color.darkGray);
		JButton button = new JButton("tesuji");
		JButton buttonTwo = new JButton("watari");
		panel.add(buttonTwo);
		frame.getContentPane().add(BorderLayout.SOUTH, panel);
		frame.getContentPane().add(BorderLayout.NORTH, button);
		frame.setSize(300, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public void go3() {
		JFrame frame = new JFrame();
		JPanel panel = new JPanel();
		panel.setBackground(Color.darkGray);
		JButton button = new JButton("tesuji");
		JButton buttonTwo = new JButton("watari");
		panel.add(button);
		frame.getContentPane().add(BorderLayout.EAST, panel);
		frame.getContentPane().add(BorderLayout.NORTH, buttonTwo);
		frame.setSize(300, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public void go4() {
		JFrame frame = new JFrame();
		JPanel panel = new JPanel();
		panel.setBackground(Color.darkGray);
		JButton button = new JButton("tesuji");
		JButton buttonTwo = new JButton("watari");
		panel.add(buttonTwo);
		frame.getContentPane().add(BorderLayout.CENTER, button);
		frame.getContentPane().add(BorderLayout.EAST, panel);
		frame.setSize(300, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public static void main(String[] args) {
		ButtonDemo bd = new ButtonDemo();
		bd.go4();
	}
}
