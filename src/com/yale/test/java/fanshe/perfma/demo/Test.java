package com.yale.test.java.fanshe.perfma.demo;

public class Test {

	public static void main(String[] args) {
		/*
		 * 这里模仿的是java中rt.jar包中的sun.reflect.ReflectionFactory类中的newMethodAccessor方法
		 * https://www.iteye.com/blog/rednaxelafx-548536
		 * 可以看到Method.invoke()实际上并不是自己实现的反射调用逻辑，而是委托给sun.reflect.MethodAccessor来处理。
		 * 每个实际的Java方法只有一个对应的Method对象作为root，。这个root是不会暴露给用户的，而是每次在通过反射获取Method对象时
		 * 新创建Method对象把root包装起来再给用户。在第一次调用一个实际Java方法对应得Method对象的invoke()方法之前，
		 * 实现调用逻辑的MethodAccessor对象还没创建；等第一次调用时才新创建MethodAccessor并更新给root，
		 * 然后调用MethodAccessor.invoke()真正完成反射调用。
		 * 那么MethodAccessor是啥呢？sun.reflect.MethodAccessor： 创建MethodAccessor实例的是ReflectionFactory。 
		 * 当反射调用15次之后,在NativeMethodAccessorImpl
		 * 每次NativeMethodAccessorImpl.invoke()方法被调用时，都会增加一个调用次数计数器，看超过阈值没有；一旦超过，
		 * 则调用MethodAccessorGenerator.generateMethod()来生成Java版的MethodAccessor的实现类，
		 * 并且改变DelegatingMethodAccessorImpl所引用的MethodAccessor为Java版。
		 * 后续经由DelegatingMethodAccessorImpl.invoke()调用到的就是Java版的实现了。
		 * 可以看看MethodAccessorGenerator类生成的JAVA方法调用 
		 */
		Father father = new Father();//真实对象,但是真实对象不会直接自己干活,但最终干活的还是自己
		DelegatingClass delegating = new DelegatingClass(father);
		father.setParent(delegating);//让father引用委派类
		for (int i=0;i<20;i++) {
			delegating.change();
		}
	}
}
