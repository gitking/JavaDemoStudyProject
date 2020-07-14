package com.yale.test.run.jvm.javassist;

public class Test {

	public static void main(String[] args) {
		Base b = new Base();
		b.process();
		
		com.yale.test.run.jvm.asm.Base asmb = new com.yale.test.run.jvm.asm.Base();
		asmb.process();
		
		
		System.out.println("*****可以看出来Asm是直接修改了Base.class文件,而Javassist是修改内存中的class文件,只有在运行中才生效,运行结束就没了*******");
		System.out.println("*****除非你用javassist将原来的Base.class文件覆盖掉*******");
	}
}
