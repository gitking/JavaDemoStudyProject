package com.yale.test.java.swing.demo.one;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

public class QuizCardBuilder {
	private JTextArea question;
	private JTextArea answer;
	private ArrayList<QuizCard> cardList;
	private JFrame frame;
	
	public void go() {
		frame = new JFrame("Quiz Card Builder");
		JPanel mainPanel = new JPanel();
		Font bigFont = new Font("sanserif", Font.BOLD, 24);
		question = new JTextArea(6, 20);
		question.setLineWrap(true);
		question.setWrapStyleWord(true);
		question.setFont(bigFont);
		
		JScrollPane qScroller = new JScrollPane(question);
		qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		answer = new JTextArea(6, 20);
		answer.setLineWrap(true);
		answer.setWrapStyleWord(true);
		answer.setFont(bigFont);
		
		JScrollPane aScroller = new JScrollPane(answer);
		aScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		aScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		JButton nextButton = new JButton("Next Card");
		cardList = new ArrayList<QuizCard>();
		
		JLabel qLabel = new JLabel("Question:");
		JLabel aLabel = new JLabel("Answer:");
		
		mainPanel.add(qLabel);
		mainPanel.add(qScroller);
		mainPanel.add(aLabel);
		mainPanel.add(aScroller);
		mainPanel.add(nextButton);
		
		nextButton.addActionListener(new NextCardListener());
		
		/*
		 * 创建菜单,把new与save项目加到File下,然后指定frame使用这个菜单,菜单项目会触发ActionEvent
		 */
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenuItem newMenuItem = new JMenuItem("NEW");
		JMenuItem saveMenuItem = new JMenuItem("save");
		newMenuItem.addActionListener(new NewMenuListener());
		
		saveMenuItem.addActionListener(new SaveMenuListener());
		fileMenu.add(newMenuItem);
		fileMenu.add(saveMenuItem);
		menuBar.add(fileMenu);
		frame.setJMenuBar(menuBar);
		frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500, 600);
		frame.setVisible(true);
	}
	
	/*
	 * 按下Next Card时会被触发,代表用户完成卡片并继续下一张卡片
	 */
	public class NextCardListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ev) {
			//向列表增加当前卡片并清除文本域
			QuizCard card = new QuizCard(question.getText(), answer.getText());
			cardList.add(card);
			clearCard();
		}
	}
	
	/*
	 * 会被菜单上的Save触发,代表用户想要存储目前这一组卡片
	 */
	public class SaveMenuListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ev) {
			QuizCard card  = new QuizCard(question.getText(), answer.getText());
			cardList.add(card);
			/**
			 * 调出存盘对话框(dialog)等待用户决定,这都是靠JFileChooser完成的。
			 */
			JFileChooser fileSave = new JFileChooser();
			fileSave.showSaveDialog(frame);
			try {
				saveFile(fileSave.getSelectedFile());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/*
	 * 会被菜单上的NEW按钮触发,这会打开新的一组卡片(还要清空文字块)
	 */
	public class NewMenuListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ev) {
			cardList.clear();
			clearCard();
		}
	}
	
	private void clearCard(){
		question.setText("");
		answer.setText("");
		question.requestFocus();
	}
	
	/**
	 * 实际编写文件的方法由SaveMenuListener的事件处理程序所调用
	 * @param file
	 * @throws IOException 
	 */
	private void saveFile(File file) throws IOException{//把列表输出到一个文本文件
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		for(QuizCard card: cardList){
			writer.write(card.getQuestion() + "/");
			writer.write(card.getAnswer() + "\n");
		}
		//如果你想要强制缓冲区立即写入,只要调用writer.flush()这个方法就可以要求缓冲区马上把内容写下去。
		writer.close();
	}
	
	public static void main(String[] args) {
		QuizCardBuilder qcb = new QuizCardBuilder();
		qcb.go();
	}
}
