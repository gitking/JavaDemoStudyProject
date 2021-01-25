package com.yale.test.java.classpath.rmi.demo.impl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.yale.test.java.classpath.rmi.demo.Service;

/*
 * 需要掷骰子吗?这个服务就会出现虚拟的骰子
 */
public class DiceService implements Service{
	JLabel label;
	JComboBox numOfDice;
	
	/*
	 * 这是很重要的方法,客户端会调用这个定义在Service中的方法来创建实际的骰子
	 * 建议:你可以这个骰子做成图形化的骰子
	 * @see com.yale.test.java.classpath.rmi.demo.Service#getGuiPanel()
	 */
	public JPanel getGuiPanel() {
		JPanel panel = new JPanel();
		JButton button = new JButton("Roll 'em!");
		String[] choices = {"1", "2", "3", "4", "5"};
		numOfDice = new JComboBox(choices);
		label = new JLabel("dice values here");
		button.addActionListener(new RollEmListener());
		panel.add(numOfDice);
		panel.add(button);
		panel.add(label);
		return panel;
	}
	
	public class RollEmListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ev) {
			String diceOutput = "";
			String selection = (String)numOfDice.getSelectedItem();
			int numOfDiceToRoll = Integer.parseInt(selection);
			for (int i=0; i < numOfDiceToRoll; i++) {
				int r = (int)((Math.random() * 6) + 1);
				diceOutput += (" " + r);
			}
			label.setText(diceOutput);
		}
	}
}
