package com.yale.test.java.classpath.rmi.demo.impl;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.yale.test.java.classpath.rmi.demo.Service;

/*
 * 输入你的生日就可以知道今天是星期几
 */
public class DayOfTheWeekService implements Service {
	JLabel outputLabel;
	JComboBox month;
	JTextField day;
	JTextField year;
	
	public JPanel getGuiPanel() {
		JPanel panel = new JPanel();
		JButton button = new JButton("Do it!");
		button.addActionListener(new DoItListener());
		outputLabel = new JLabel("date appears hers");
		DateFormatSymbols dateStuff = new DateFormatSymbols();//日期格式化
		month = new JComboBox(dateStuff.getMonths());
		day = new JTextField(8);
		year = new JTextField(8);
		JPanel inputPanel = new JPanel(new GridLayout(3, 2));
		inputPanel.add(new JLabel("Month"));
		inputPanel.add(month);
		inputPanel.add(new JLabel("Day"));
		inputPanel.add(day);
		inputPanel.add(new JLabel("Year"));
		inputPanel.add(year);
		panel.add(inputPanel);
		panel.add(button);
		panel.add(outputLabel);
		return panel;
	}
	
	public class DoItListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ev) {
			int monthNum = month.getSelectedIndex();
			int dayNum = Integer.parseInt(day.getText());
			int yearNum = Integer.parseInt(year.getText());
			Calendar c = Calendar.getInstance();
			c.set(Calendar.MONTH, monthNum);
			c.set(Calendar.DAY_OF_MONTH, dayNum);
			c.set(Calendar.YEAR, yearNum);
			Date date = c.getTime();
			String dayOfWeek = (new SimpleDateFormat("EEEE")).format(date);//日期格式化
			outputLabel.setText(dayOfWeek);
		}
	}
}
