package com.yale.test.design.interpreter.memento;

/*
 * 备忘录
 * 在不破坏封装性的前提下，捕获一个对象的内部状态，并在该对象之外保存这个状态。
 * 备忘录模式（Memento），主要用于捕获一个对象的内部状态，以便在将来的某个时候恢复此状态。
 * 其实我们使用的几乎所有软件都用到了备忘录模式。最简单的备忘录模式就是保存到文件，打开文件。对于文本编辑器来说，保存就是把TextEditor类的字符串存储到文件，
 * 打开就是恢复TextEditor类的状态。对于图像编辑器来说，原理是一样的，只是保存和恢复的数据格式比较复杂而已。Java的序列化也可以看作是备忘录模式。
 * 在使用文本编辑器的时候，我们还经常使用Undo、Redo这些功能。这些其实也可以用备忘录模式实现，即不定期地把TextEditor类的字符串复制一份存起来，这样就可以Undo或Redo。
 * 标准的备忘录模式有这么几种角色：Memonto：存储的内部状态；Originator：创建一个备忘录并设置其状态；Caretaker：负责保存备忘录。
 * 实际上我们在使用备忘录模式的时候，不必设计得这么复杂，只需要对类似TextEditor的类，增加getState()和setState()就可以了。
 * 我们以一个文本编辑器TextEditor为例，它内部使用StringBuilder允许用户增删字符：
 * 为了支持这个TextEditor能保存和恢复状态，我们增加getState()和setState()两个方法：
 * 对这个简单的文本编辑器，用一个String就可以表示其状态，对于复杂的对象模型，通常我们会使用JSON、XML等复杂格式。
 * 小结
 * 备忘录模式是为了保存对象的内部状态，并在将来恢复，大多数软件提供的保存、打开，以及编辑过程中的Undo、Redo都是备忘录模式的应用。
 */
public class Test {
	public static void main(String[] args) {
		TextEditor editor = new TextEditor();
		editor.add("Hello");
		editor.add(',');
		editor.delete();
		editor.add(' ');
		editor.add("world");
		
		String state = editor.getState();//保存状态
		editor.add("!!!");//继续编辑
		editor.delete();
		editor.print();
		editor.setState(state);//恢复状态
		editor.print();
		
		
	}
}
