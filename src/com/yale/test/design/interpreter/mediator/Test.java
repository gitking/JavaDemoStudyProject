package com.yale.test.design.interpreter.mediator;

import java.awt.Container;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.yale.test.design.interpreter.mediator.ui.Mediator;

/*
 * 中介
 * 用一个中介对象来封装一系列的对象交互。中介者使各个对象不需要显式地相互引用，从而使其耦合松散，而且可以独立地改变它们之间的交互。
 * 中介模式（Mediator）又称调停者模式，它的目的是把多方会谈变成双方会谈，从而实现多方的松耦合
 * 有些童鞋听到中介立刻想到房产中介，立刻气不打一处来。这个中介模式与房产中介还真有点像，所以消消气，先看例子。
 * 考虑一个简单的点餐输入：
 * 汉堡
	鸡块
	薯条
	咖啡
 * 选择全部,取消所有,反选
 * 这个小系统有4个参与对象：
 *  多选框；
    “选择全部”按钮；
    “取消所有”按钮；
    “反选”按钮。
 * 它的复杂性在于，当多选框变化时，它会影响“选择全部”和“取消所有”按钮的状态（是否可点击），当用户点击某个按钮时，例如“反选”，除了会影响多选框的状态，它又可能影响“选择全部”和“取消所有”按钮的状态。
 * 所以这是一个多方会谈，逻辑写起来很复杂：
 *  ┌─────────────────┐     ┌─────────────────┐
	│  CheckBox List  │<───>│SelectAll Button │
	└─────────────────┘     └─────────────────┘
	         ▲ ▲                     ▲
	         │ └─────────────────────┤
	         ▼                       │
	┌─────────────────┐     ┌────────┴────────┐
	│SelectNone Button│<────│ Inverse Button  │
	└─────────────────┘     └─────────────────┘
 * 如果我们引入一个中介，把多方会谈变成多个双方会谈，虽然多了一个对象，但对象之间的关系就变简单了：
 *          ┌─────────────────┐
     ┌─────>│  CheckBox List  │
     │      └─────────────────┘
     │      ┌─────────────────┐
     │ ┌───>│SelectAll Button │
     ▼ ▼    └─────────────────┘
┌─────────┐
│Mediator │
└─────────┘
     ▲ ▲    ┌─────────────────┐
     │ └───>│SelectNone Button│
     │      └─────────────────┘
     │      ┌─────────────────┐
     └─────>│ Inverse Button  │
            └─────────────────┘
 * 下面我们用中介模式来实现各个UI组件的交互。首先把UI组件给画出来：
 * 然后，我们设计一个Mediator类，它引用4个UI组件，并负责跟它们交互：
 * 运行一下看看效果：
 * 使用Mediator模式后，我们得到了以下好处：
 * 各个UI组件互不引用，这样就减少了组件之间的耦合关系；
 * Mediator用于当一个组件发生状态变化时，根据当前所有组件的状态决定更新某些组件；
 * 如果新增一个UI组件，我们只需要修改Mediator更新状态的逻辑，现有的其他UI组件代码不变。
 * Mediator模式经常用在有众多交互组件的UI上。为了简化UI程序，MVC模式以及MVVM模式都可以看作是Mediator模式的扩展。
 * 小结
 * 中介模式是通过引入一个中介对象，把多边关系变成多个双边关系，从而简化系统组件的交互耦合度。
 */
public class Test {
	public static void main(String[] args) {
		new OrderFrame("Hanburger", "Nugget", "Chip", "Coffee");
	}
}

class OrderFrame extends JFrame {
	public OrderFrame(String...names) {
		setTitle("Order");
		setSize(460, 200);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container c = getContentPane();
		c.setLayout(new FlowLayout(FlowLayout.LEADING, 20, 20));
		c.add(new JLabel("Use Mediator Pattern"));
		
		List<JCheckBox> checkboxList = addCheckBox(names);
		JButton selectAll = addButton("Select ALL");
		JButton selectNone = addButton("Select None");
		selectNone.setEnabled(false);
		JButton selectInverse = addButton("Inverse Select");
		new Mediator(checkboxList, selectAll, selectNone, selectInverse);
		setVisible(true);
	}
	
	private List<JCheckBox> addCheckBox(String... names) {
		JPanel panel = new JPanel();
		panel.add(new JLabel("Menu:"));
		List<JCheckBox> list = new ArrayList<>();
		for(String name: names) {
			JCheckBox checkBox = new JCheckBox(name);
			list.add(checkBox);
			panel.add(checkBox);
		}
		getContentPane().add(panel);
		return list;
	}
	
	private JButton addButton(String label) {
		JButton button = new JButton(label);
		getContentPane().add(button);
		return button;
	}
}