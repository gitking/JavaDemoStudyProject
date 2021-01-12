package com.yale.test.java.swing;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Animate {
	int x = 1;
	int y = 1;
	
	class MyDrawP extends JPanel {
		@Override
		public void paintComponent(Graphics g) {
			g.setColor(Color.white);
			g.fillRect(0, 0, 500, 250);
			g.setColor(Color.blue);
			g.fillRect(x, y, 500-x*2, 250-y*2);
		}
	}
	
	public void go() throws InterruptedException {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		MyDrawP drawP = new MyDrawP();
		frame.getContentPane().add(drawP);
		frame.setSize(500, 270);
		frame.setVisible(true);
		
		for (int i=0; i<124; i++,x++,y++) {
			x++;
			drawP.repaint();
			Thread.sleep(50);
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		Animate am = new Animate();
		am.go();
	}
}
