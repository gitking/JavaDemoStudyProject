package com.yale.test.run.jvm.instrument;

import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;

/*
 * 现在有了Transformer，那么它要如何注入到正在运行的JVM呢？还需要定义一个Agent，借助Agent的能力将Instrument注入到JVM中。
 * 我们将在下一小节介绍Agent，现在要介绍的是Agent中用到的另一个类Instrumentation。
 * 在JDK 1.6之后，Instrumentation可以做启动后的Instrument、本地代码（Native Code）的Instrument，
 * 以及动态改变Classpath等等。我们可以向Instrumentation中添加上文中定义的Transformer，并指定要被重加载的类，
 * 代码如下所示。这样，当Agent被Attach到一个JVM中时，就会执行类字节码替换并重载入JVM的操作。
 * https://mp.weixin.qq.com/s/CH9D-E7fxuu462Q2S3t0AA
 */
public class TestAgent {
	public static void agentmain (String args, Instrumentation inst){
		//指定我们自定义的Transformer,在其中利用Javassist做字节码替换
		inst.addTransformer(new InstrumentDemo(), true);
		try {
			//重定义类并载入新的字节码
			inst.retransformClasses(Base.class);
			System.out.println(" Agent Load Done. ");
		} catch (UnmodifiableClassException e) {
			e.printStackTrace();
			System.out.println(" Agent Load failed. ");
		}
	}
}
