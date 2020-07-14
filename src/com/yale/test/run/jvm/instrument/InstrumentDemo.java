package com.yale.test.run.jvm.instrument;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import org.apache.ibatis.javassist.NotFoundException;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

/*
 * Instrument是JVM提供的一个可以修改已加载类的类库，专门为Java语言编写的插桩服务提供支持。它需要依赖JVMTI的Attach API机制实现，
 * JVMTI这一部分，我们将在下一小节进行介绍。在JDK 1.6以前，Instrument只能在JVM刚启动开始加载类时生效，而在JDK 1.6之后，
 * Instrument支持了在运行时对类定义的修改。要使用Instrument的类修改功能，我们需要实现它提供的ClassFileTransformer接口，
 * 定义一个类文件转换器。接口中的transform()方法会在类文件被加载时调用，而在Transform方法里，
 * 我们可以利用上文中的ASM或Javassist对传入的字节码进行改写或替换，生成新的字节码数组后返回。
 * 我们定义一个实现了ClassFileTransformer接口的类TestTransformer，依然在其中利用Javassist对Base类中的process()方法进行增强，在前后分别打印“start”和“end”，代码如下：
 * https://mp.weixin.qq.com/s/CH9D-E7fxuu462Q2S3t0AA
 */
public class InstrumentDemo implements ClassFileTransformer{
	@Override
	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
			ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
		try {
			System.out.println("Transforming:" + className);
			ClassPool cp = ClassPool.getDefault();
			CtClass cc = cp.get("com.yale.test.run.jvm.instrument.Base");
			CtMethod m = cc.getDeclaredMethod("process");
			m.insertBefore("{ System.out.println(\" Start,在JVM运行时修改Base.class文件,Start \"); }");
			m.insertAfter("{ System.out.println(\" end,在JVM运行时修改Base.class文件, end\"); }");
			return cc.toBytecode();
		} catch (javassist.NotFoundException e) {
			e.printStackTrace();
		} catch (CannotCompileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
