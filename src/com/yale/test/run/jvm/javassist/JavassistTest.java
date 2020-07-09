package com.yale.test.run.jvm.javassist;

import java.io.IOException;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

/*
 * ASM是在指令层次上操作字节码的，阅读上文后，我们的直观感受是在指令层次上操作字节码的框架实现起来比较晦涩。故除此之外，我们再简单介绍另外一类框架：强调源代码层次操作字节码的框架Javassist。
 * 利用Javassist实现字节码增强时，可以无须关注字节码刻板的结构，其优点就在于编程简单。直接使用Java编码的形式，而不需要了解虚拟机指令，就能动态改变类的结构或者动态生成类。
 * 其中最重要的是ClassPool、CtClass、CtMethod、CtField这四个类：
 * CtClass（compile-time class）：编译时类信息，它是一个Class文件在代码中的抽象表现形式，可以通过一个类的全限定名来获取一个CtClass对象，用来表示这个类文件。
 * ClassPool：从开发视角来看，ClassPool是一张保存CtClass信息的HashTable，Key为类名，Value为类名对应的CtClass对象。当我们需要对某个类进行修改时，
 * 就是通过pool.getCtClass("className")方法从pool中获取到相应的CtClass。
 * CtMethod、CtField：这两个比较好理解，对应的是类中的方法和属性。
 * 了解这四个类后，我们可以写一个小Demo来展示Javassist简单、快速的特点。我们依然是对Base中的process()方法做增强，在方法调用前后分别输出"start"和"end"，
 * 实现代码如下。我们需要做的就是从Pool中获取到相应的CtClass对象和其中的方法，然后执行method.insertBefore和insertAfter方法，
 * 参数为要插入的Java代码，再以字符串的形式传入即可，实现起来也极为简单。
 */
public class JavassistTest {
	public static void main(String[] args) throws NotFoundException, CannotCompileException, IOException, InstantiationException, IllegalAccessException {
		ClassPool cp = ClassPool.getDefault();
		CtClass cc = cp.get("com.yale.test.run.jvm.javassist.Base");
		CtMethod m = cc.getDeclaredMethod("process");
		m.insertBefore("{ System.out.println(\"我是通过Javassist修改进来的,start\");}");
		m.insertAfter("{ System.out.println(\"我是通过Javassist修改进来的,end\");}");
		
		Class c = cc.toClass();
		/*
		 * 注意这里的路径,/GitWorkSpace/JavaDemoStudyProject/WebContent/WEB-INF/classes/javassist/
		 * 生成后的路径为:\GitWorkSpace\JavaDemoStudyProject\WebContent\WEB-INF\classes\javassist\com\yale\test\run\jvm\javassist
		 */
		cc.writeFile("/GitWorkSpace/JavaDemoStudyProject/WebContent/WEB-INF/classes/javassist");
		Base h = (Base)c.newInstance();
		h.process();
	}
}
