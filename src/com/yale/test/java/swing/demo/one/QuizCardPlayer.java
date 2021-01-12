package com.yale.test.java.swing.demo.one;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

public class QuizCardPlayer {
	private JTextArea display;
	private JTextArea answer;
	private ArrayList<QuizCard> cardList;
	private QuizCard currentCard;
	private int currentCardIndex;
	private JFrame frame;
	private JButton nextButton;
	private boolean isShowAnswer;
	
	public void go() {
		frame = new JFrame("Quiz Card Player");
		JPanel mainPanel = new JPanel();
		Font bigFont = new Font("sanserif", Font.BOLD, 24);
		display = new JTextArea(10, 20);
		display.setFont(bigFont);
		
		display.setLineWrap(true);
		display.setEditable(false);
		
		JScrollPane qScroller = new JScrollPane(display);
		qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		nextButton = new JButton("Show Question");
		mainPanel.add(qScroller);
		mainPanel.add(nextButton);
		nextButton.addActionListener(new NextCardListener());
		
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenuItem loadMenuItem = new JMenuItem("Load card set");
		loadMenuItem.addActionListener(new OpenMenuListener());
		fileMenu.add(loadMenuItem);
		menuBar.add(fileMenu);
		frame.setJMenuBar(menuBar);
		frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(640, 500);
		frame.setVisible(true);
	}
	
	public class NextCardListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ev) {
			if(cardList != null && cardList.size() > 0) {
				if (isShowAnswer) {
					display.setText(currentCard.getAnswer());
					nextButton.setText("Next Card");
					isShowAnswer = false;
				} else {
					if (currentCardIndex < cardList.size()) {
						showNextCard();
					} else {
						display.setText("That was last card");
						nextButton.setEnabled(false);
					}
				}
			} else {
				display.setText("That was last card");
				nextButton.setEnabled(false);
			}
		}
	}
	
	public class OpenMenuListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ev) {
			JFileChooser fileOpen = new JFileChooser();
			fileOpen.showOpenDialog(frame);//打开文件的对话框让用户选择文件
			loadFile(fileOpen.getSelectedFile());
		}
	}
	
	private void loadFile(File file){
		cardList = new ArrayList<QuizCard>();
		try {
			if (file != null) {
				BufferedReader reader = new BufferedReader(new FileReader(file));
				String line = null;
				while((line = reader.readLine()) != null){
					System.out.println("读取到的内容为:" + line);
					makeCard(line);
				}
				reader.close();
				System.out.println("获得几张卡片?" + cardList.size());
				if (cardList != null && cardList.size() > 0) {
					nextButton.setText("Show Question");
					nextButton.setEnabled(true);
					currentCardIndex = 0;
				}
			} else {
				display.setText("That was last card");
				nextButton.setEnabled(false);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void makeCard(String lineToParse) {
		String[] result = lineToParse.split("/");
		QuizCard card = new QuizCard(result[0], result[1]);
		cardList.add(card);
		System.out.println("make a card");
	}
	
	private void showNextCard() {
		currentCard = cardList.get(currentCardIndex);
		currentCardIndex++;
		display.setText(currentCard.getQuestion());
		nextButton.setText("Show Answer");
		isShowAnswer = true;
	}
	
	public static void main(String[] args) {
		new QuizCardPlayer().go();
	}
}
