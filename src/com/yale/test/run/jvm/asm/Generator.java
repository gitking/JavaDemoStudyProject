package com.yale.test.run.jvm.asm;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;


/**
 * https://mp.weixin.qq.com/s/CH9D-E7fxuu462Q2S3t0AA
 * 对于需要手动操纵字节码的需求，可以使用ASM，它可以直接生成.class字节码文件，也可以在类被加载入JVM之前动态修改类行为（如下图17所示）。
 * ASM的应用场景有AOP（Cglib就是基于ASM）、热部署、修改其他jar包中的类等。当然，涉及到如此底层的步骤，实现起来也比较麻烦。
 * 接下来，本文将介绍ASM的两种API，并用ASM来实现一个比较粗糙的AOP。但在此之前，为了让大家更快地理解ASM的处理流程，强烈建议读者先对访问者模式进行了解。
 * 简单来说，访问者模式主要用于修改或操作一些数据结构比较稳定的数据，而通过第一章，我们知道字节码文件的结构是由JVM固定的，所以很适合利用访问者模式对字节码文件进行修改。
 * 2.1.1 ASM API
	2.1.1.1 核心API
		ASM Core API可以类比解析XML文件中的SAX方式，不需要把这个类的整个结构读取进来，就可以用流式的方法来处理字节码文件。好处是非常节约内存，但是编程难度较大。然而出于性能考虑，一般情况下编程都使用Core API。在Core API中有以下几个关键类：
		ClassReader：用于读取已经编译好的.class文件。
		ClassWriter：用于重新构建编译后的类，如修改类名、属性以及方法，也可以生成新的类的字节码文件。
		各种Visitor类：如上所述，CoreAPI根据字节码从上到下依次处理，对于字节码文件中不同的区域有不同的Visitor，比如用于访问方法的MethodVisitor、用于访问类变量的FieldVisitor、用于访问注解的AnnotationVisitor等。为了实现AOP，重点要使用的是MethodVisitor。
	2.1.1.2 树形API
		ASM Tree API可以类比解析XML文件中的DOM方式，把整个类的结构读取到内存中，缺点是消耗内存多，但是编程比较简单。TreeApi不同于CoreAPI，TreeAPI通过各种Node类来映射字节码的各个区域，类比DOM节点，就可以很好地理解这种编程方式。
 * 利用ASM手写字节码时，需要利用一系列visitXXXXInsn()方法来写对应的助记符，所以需要先将每一行源代码转化为一个个的助记符，
 * 然后通过ASM的语法转换为visitXXXXInsn()这种写法。第一步将源码转化为助记符就已经够麻烦了，不熟悉字节码操作集合的话，
 * 需要我们将代码编译后再反编译，才能得到源代码对应的助记符。第二步利用ASM写字节码时，如何传参也很令人头疼。
 * ASM社区也知道这两个问题，所以提供了工具ASM ByteCode Outline(https://plugins.jetbrains.com/plugin/5918-asm-bytecode-outline)。
 * 安装后，右键选择“Show Bytecode Outline”，在新标签页中选择“ASMified”这个tab，如图19所示，就可以看到这个类中的代码对应的ASM写法了。
 * 图中上下两个红框分别对应AOP中的前置逻辑于后置逻辑，将这两块直接复制到Visitor中的visitMethod()以及visitInsn()方法中，就可以了。
 * @author dell
 */
public class Generator {
	public static void main(String[] args) throws IOException {
		//classReader读取字节码
		ClassReader classReader = new ClassReader("com/yale/test/run/jvm/asm/Base");
		//由ClassWriter写字节码并将旧的字节码替换掉
		ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		ClassVisitor classVisitor = new MyClassVisitor(classWriter);
		
		classReader.accept(classVisitor, ClassReader.SKIP_DEBUG);
		byte[] data = classWriter.toByteArray();
		
		//输出,new File("/")参数开头不带斜杠/获取的路径是代码当前运行的目录加上参数里面的路径
		File file = new File("WebContent/WEB-INF/classes/com/yale/test/run/jvm/asm/Base.class");
		System.out.println(file.getAbsolutePath());
		
		File fileSec = new File("/Base.class");
		System.out.println("注意观察参数开头带斜杠的路径:" + fileSec.getAbsolutePath());
		
		File fileSecs = new File("Base.class");
		System.out.println("注意观察参数开头不带斜杠的路径:" + fileSecs.getAbsolutePath());

		FileOutputStream fout = new FileOutputStream(file);
		fout.write(data);
		fout.close();
		System.out.println("利用Asm修改字节码完毕------------");
	}
}
