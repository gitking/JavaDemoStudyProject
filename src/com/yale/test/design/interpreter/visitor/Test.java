package com.yale.test.design.interpreter.visitor;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import com.yale.test.design.interpreter.visitor.impl.ClassFileCleanerVisitor;
import com.yale.test.design.interpreter.visitor.impl.JavaFileVisitor;

/*
 * 访问者
 * 表示一个作用于某对象结构中的各元素的操作。它使你可以在不改变各元素的类的前提下定义作用于这些元素的新操作。
 * 访问者模式（Visitor）是一种操作一组对象的操作，它的目的是不改变对象的定义，但允许新增不同的访问者，来定义新的操作。
 * 访问者模式的设计比较复杂，如果我们查看GoF原始的访问者模式，它是这么设计的：
 * ┌─────────┐       ┌───────────────────────┐
   │ Client  │─ ─ ─ >│        Visitor        │
   └─────────┘       ├───────────────────────┤
        │            │visitElementA(ElementA)│
                     │visitElementB(ElementB)│
        │            └───────────────────────┘
                                 ▲
        │                ┌───────┴───────┐
                         │               │
        │         ┌─────────────┐ ┌─────────────┐
                  │  VisitorA   │ │  VisitorB   │
        │         └─────────────┘ └─────────────┘
        ▼
┌───────────────┐        ┌───────────────┐
│ObjectStructure│─ ─ ─ ─>│    Element    │
├───────────────┤        ├───────────────┤
│handle(Visitor)│        │accept(Visitor)│
└───────────────┘        └───────────────┘
                                 ▲
                        ┌────────┴────────┐
                        │                 │
                ┌───────────────┐ ┌───────────────┐
                │   ElementA    │ │   ElementB    │
                ├───────────────┤ ├───────────────┤
                │accept(Visitor)│ │accept(Visitor)│
                │doA()          │ │doB()          │
                └───────────────┘ └───────────────┘
 * 上述模式的复杂之处在于上述访问者模式为了实现所谓的“双重分派”，设计了一个回调再回调的机制。因为Java只支持基于多态的单分派模式，这里强行模拟出“双重分派”反而加大了代码的复杂性。
 * 这里我们只介绍简化的访问者模式。假设我们要递归遍历某个文件夹的所有子文件夹和文件，然后找出.java文件，正常的做法是写个递归：
 * void scan(File dir, List<File> collector) {
	    for (File file : dir.listFiles()) {
	        if (file.isFile() && file.getName().endsWith(".java")) {
	            collector.add(file);
	        } else if (file.isDir()) {
	            // 递归调用:
	            scan(file, collector);
	        }
	    }
	}
 * 上述代码的问题在于，扫描目录的逻辑和处理.java文件的逻辑混在了一起。如果下次需要增加一个清理.class文件的功能，就必须再重复写扫描逻辑。
 * 因此，访问者模式先把数据结构（这里是文件夹和文件构成的树型结构）和对其的操作（查找文件）分离开，以后如果要新增操作（例如清理.class文件），只需要新增访问者，不需要改变现有逻辑。
 * 用访问者模式改写上述代码步骤如下：
 * 首先，我们需要定义访问者接口，即该访问者能够干的事情：Visitor 
 * 紧接着，我们要定义能持有文件夹和文件的数据结构FileStructure：
 * 然后，我们给FileStructure增加一个handle()方法，传入一个访问者：
 * 这样，我们就把访问者的行为抽象出来了。如果我们要实现一种操作，例如，查找.java文件，就传入JavaFileVisitor：
 * 类似的，如果要清理.class文件，可以再写一个ClassFileClearnerVisitor：
 * 可见，访问者模式的核心思想是为了访问比较复杂的数据结构，不去改变数据结构，而是把对数据的操作抽象出来，在“访问”的过程中以回调形式在访问者中处理操作逻辑。如果要新增一组操作，那么只需要增加一个新的访问者。
 * 实际上，Java标准库提供的Files.walkFileTree()已经实现了一个访问者模式：
 * Files.walkFileTree()允许访问者返回FileVisitResult.CONTINUE以便继续访问，或者返回FileVisitResult.TERMINATE停止访问。
 * 类似的，对XML的SAX处理也是一个访问者模式，我们需要提供一个SAX Handler作为访问者处理XML的各个节点。
 * 小结
 * 访问者模式是为了抽象出作用于一组复杂对象的操作，并且后续可以新增操作而不必对现有的对象结构做任何改动。
 */
public class Test {
	public static void main(String[] args) throws IOException {
		Files.walkFileTree(Paths.get("."), new MyFileVisitor());
		
		FileStructure sf = new FileStructure(new File("."));
		sf.handle(new JavaFileVisitor());
		sf.handle(new ClassFileCleanerVisitor());
	}
}

//实现一个FileVisitor
class MyFileVisitor extends SimpleFileVisitor<Path> {
	//处理Directory:
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
		System.out.println("pre visit dir: " + dir);
		return FileVisitResult.CONTINUE;//返回CONTINUE表示继续访问
	}
	
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException{
		System.out.println("visit file : " + file);
		return FileVisitResult.CONTINUE;//返回CONTINUE表示继续访问
	}
}
