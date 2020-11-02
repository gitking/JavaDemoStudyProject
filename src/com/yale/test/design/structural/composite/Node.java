package com.yale.test.design.structural.composite;

import java.util.List;

/*
 * 组合
 * 将对象组合成树形结构以表示“部分-整体”的层次结构，使得用户对单个对象和组合对象的使用具有一致性。
 * 组合模式（Composite）经常用于树形结构，为了简化代码，使用Composite可以把一个叶子节点与一个父节点统一起来处理。
 * 我们来看一个具体的例子。在XML或HTML中，从根节点开始，每个节点都可能包含任意个其他节点，这些层层嵌套的节点就构成了一颗树。
 * 要以树的结构表示XML，我们可以先抽象出节点类型Node：
 * 对于一个<abc>这样的节点，我们称之为ElementNode，它可以作为容器包含多个子节点：
 * 对于普通文本，我们把它看作TextNode，它没有子节点：
 * 此外，还可以有注释节点：
 * 通过ElementNode、TextNode和CommentNode，我们就可以构造出一颗树：
 * 可见，使用Composite模式时，需要先统一单个节点以及“容器”节点的接口：
 *   		 ┌───────────┐
             │   Node    │
             └───────────┘
                   ▲
      ┌────────────┼────────────┐
      │            │            │
┌───────────┐┌───────────┐┌───────────┐
│ElementNode││ TextNode  ││CommentNode│
└───────────┘└───────────┘└───────────┘
 * 作为容器节点的ElementNode又可以添加任意个Node，这样就可以构成层级结构。
 * 类似的，像文件夹和文件、GUI窗口的各种组件，都符合Composite模式的定义，因为它们的结构天生就是层级结构。
 * 小结
 * Composite模式使得叶子对象和容器对象具有一致性，从而形成统一的树形结构，并用一致的方式去处理它们。
 */
public interface Node {
	
	Node add(Node node);//添加一个节点为子节点
	
	List<Node> children();//读取子节点:
	
	String toXml();//输出为XML
}
