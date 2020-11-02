package com.yale.test.design.interpreter.command;

import com.yale.test.design.interpreter.command.impl.CopyCommand;
import com.yale.test.design.interpreter.command.impl.PasteCommand;

/*
 * 命令
 * 将一个请求封装为一个对象，从而使你可用不同的请求对客户进行参数化，对请求排队或记录请求日志，以及支持可撤销的操作。
 * 命令模式（Command）是指，把请求封装成一个命令，然后执行该命令。
 * 在使用命令模式前，我们先以一个编辑器为例子，看看如何实现简单的编辑操作：TextEditor
 * 我们用一个StringBuilder模拟一个文本编辑器，它支持copy()、paste()、add()、delete()等方法。
 * 正常情况，我们像这样调用TextEditor：
 * TextEditor editor = new TextEditor();
	editor.add("Command pattern in text editor.\n");
	editor.copy();
	editor.paste();
	System.out.println(editor.getState());
 * 这是直接调用方法，调用方需要了解TextEditor的所有接口信息。
 * 如果改用命令模式，我们就要把调用方发送命令和执行方执行命令分开。怎么分？
 * 解决方案是引入一个Command接口：
 * 调用方创建一个对应的Command，然后执行，并不关心内部是如何具体执行的。
 * 为了支持CopyCommand和PasteCommand这两个命令，我们从Command接口派生：
 * 最后我们把Command和TextEditor组装一下，客户端这么写：
 * 这就是命令模式的结构：
 *  ┌──────┐      ┌───────┐
	│Client│─ ─ ─>│Command│
	└──────┘      └───────┘
	                  │  ┌──────────────┐
	                  ├─>│ CopyCommand  │
	                  │  ├──────────────┤
	                  │  │editor.copy() │─ ┐
	                  │  └──────────────┘
	                  │                    │  ┌────────────┐
	                  │  ┌──────────────┐   ─>│ TextEditor │
	                  └─>│ PasteCommand │  │  └────────────┘
	                     ├──────────────┤
	                     │editor.paste()│─ ┘
	                     └──────────────┘
 * 有的童鞋会有疑问：搞了一大堆Command，多了好几个类，还不如直接这么写简单：
 * TextEditor editor = new TextEditor();
	editor.add("Command pattern in text editor.\n");
	editor.copy();
	editor.paste();
 * 实际上，使用命令模式，确实增加了系统的复杂度。如果需求很简单，那么直接调用显然更直观而且更简单。
 * 那么我们还需要命令模式吗？
 * 答案是视需求而定。如果TextEditor复杂到一定程度，并且需要支持Undo、Redo的功能时，就需要使用命令模式，因为我们可以给每个命令增加undo()：
 * public interface Command {
	    void execute();
	    void undo();
	}
 * 然后把执行的一系列命令用List保存起来，就既能支持Undo，又能支持Redo。这个时候，我们又需要一个Invoker对象，负责执行命令并保存历史命令：
 *  ┌─────────────┐
	│   Client    │
	└─────────────┘
	       │
	
	       │
	       ▼
	┌─────────────┐
	│   Invoker   │
	├─────────────┤    ┌───────┐
	│List commands│─ ─>│Command│
	│invoke(c)    │    └───────┘
	│undo()       │        │  ┌──────────────┐
	└─────────────┘        ├─>│ CopyCommand  │
	                       │  ├──────────────┤
	                       │  │editor.copy() │─ ┐
	                       │  └──────────────┘
	                       │                    │  ┌────────────┐
	                       │  ┌──────────────┐   ─>│ TextEditor │
	                       └─>│ PasteCommand │  │  └────────────┘
	                          ├──────────────┤
	                          │editor.paste()│─ ┘
	                          └──────────────┘
 * 可见，模式带来的设计复杂度的增加是随着需求而增加的，它减少的是系统各组件的耦合度。
 * 练习
 * 给命令模式新增Add和Delete命令并支持Undo、Redo操作。
 * 小结
 * 命令模式的设计思想是把命令的创建和执行分离，使得调用者无需关心具体的执行过程。
 * 通过封装Command对象，命令模式可以保存已执行的命令，从而支持撤销、重做等操作。
 * 问：java怎么像C一样实现动态内存分配？
 * 答：所有新对象都由JVM自动分配内存，你不用关心
 * 如果你想手动申请系统内存，必须升级到JDK15，使用相关API：
 * try (MemorySegment segment = MemorySegment.allocateNative(1024)) {
 */
public class Test {
	public static void main(String[] args) {
		TextEditor editor = new TextEditor();
		editor.add("Command pattern in text editor1. \n");
		
		Command copy = new CopyCommand(editor);
		copy.execute();
		
		editor.add("---\n");
		
		Command paste  = new PasteCommand(editor);
		paste.execute();
		System.out.println(editor.getState());
	}
}
