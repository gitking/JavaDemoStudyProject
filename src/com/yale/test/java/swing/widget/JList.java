package com.yale.test.java.swing.widget;

/*
 * JList的构造函数需要一个任意类型的数组。不一定是String,但会用String来表示项目
 * Sting[] listEntries = {"alpha","beta","gamma","delta","epsilon","zeta","eta","theta"};
 * JList list = new JList(listEntries);
 * 与JTextArea相同,要放在JScrollPane上面
 * 让他显示垂直的滚动条
 * JScrollPane scroller = new JScrollPane(list);
 * scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWARYS);
 * scroller.stHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
 * panel.add(scroller);
 * 设定显示的行数
 * list.setVisibleRowCount(4)
 * 限制用户只能选取一个项目
 * list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
 * 对选择事件做注册
 * list.addListSelectionListener(this);
 * 处理事件(判断选了哪个项目)
 * public void valueChanged(ListSelectionEvent lse){
 * 		if(!lse.getValueIsAdjusting()){//如果没有加上这个if判断你会得到俩次事件
 * 				String selection = (String)list.getSelectedValue();
 * 				System.out.println(selection);
 * 		}
 * }
 * 
 */
public class JList {

}
