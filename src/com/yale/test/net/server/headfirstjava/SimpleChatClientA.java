package com.yale.test.net.server.headfirstjava;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * 必须先启动服务器端,再启动这个客户端
 * @author dell
 *
 */
public class SimpleChatClientA {
	JTextField outgoing;
	PrintWriter writer;
	Socket sock;
	
	public void go(){
		JFrame frame = new JFrame("Ludicrously Simple Chat Client");
		JPanel mainPanel = new JPanel();
		outgoing = new JTextField(20);
		JButton sendButton = new JButton("Send");
		sendButton.addActionListener(new SendButtonListener());
		mainPanel.add(outgoing);
		mainPanel.add(sendButton);
		frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
		setUpNetWorking();
		frame.setSize(400, 500);
		frame.setVisible(true);
	}
	
	private void setUpNetWorking() {
		try {
			sock = new Socket("127.0.0.1", 5000);
			writer = new PrintWriter(sock.getOutputStream());
			System.out.println("networking established");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public class SendButtonListener implements ActionListener {
		
		public void actionPerformed(ActionEvent ev) {
			writer.println(outgoing.getText());
			writer.flush();
			outgoing.setText("");
			outgoing.requestFocus();
		}
	}
	
	public static void main(String[] args) {
		new SimpleChatClientA().go();
	}
}
