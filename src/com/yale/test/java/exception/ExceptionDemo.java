package com.yale.test.java.exception;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/*
 * 因为Java的异常是class，它的继承关系如下：

                     ┌───────────┐
                     │  Object   │
                     └───────────┘
                           ▲
                           │
                     ┌───────────┐
                     │ Throwable │
                     └───────────┘
                           ▲
                 ┌─────────┴─────────┐
                 │                   │
           ┌───────────┐       ┌───────────┐
           │   Error   │       │ Exception │
           └───────────┘       └───────────┘
                 ▲                   ▲
         ┌───────┘              ┌────┴──────────┐
         │                      │               │
┌─────────────────┐    ┌─────────────────┐┌───────────┐
│OutOfMemoryError │... │RuntimeException ││IOException│...
└─────────────────┘    └─────────────────┘└───────────┘
                                ▲
                    ┌───────────┴─────────────┐
                    │                         │
         ┌─────────────────────┐ ┌─────────────────────────┐
         │NullPointerException │ │IllegalArgumentException │...
         └─────────────────────┘ └─────────────────────────┘
 * 从继承关系可知：Throwable是异常体系的根，它继承自Object。Throwable有两个体系：Error和Exception，
 * Error表示严重的错误，程序对此一般无能为力，例如：
    OutOfMemoryError：内存耗尽
    NoClassDefFoundError：无法加载某个Class
    StackOverflowError：栈溢出
 * Java规定：
 * 必须捕获的异常，包括Exception及其子类，但不包括RuntimeException及其子类，这种类型的异常称为Checked Exception。
 * 不需要捕获的异常，包括Error及其子类，RuntimeException及其子类。
 * 所有异常都可以调用printStackTrace()方法打印异常栈，这是一个简单有用的快速打印异常的方法。
 * 简单地说就是：多个catch语句只有一个能被执行。例如：
 * 存在多个catch的时候，catch的顺序非常重要：子类必须写在前面。例如：
 * 某些情况下，可以没有catch，只使用try ... finally结构。例如：
 */
public class ExceptionDemo {
	public static void main(String[] args) {
		try {
			
			File file = new File("");
			InputStream is = new FileInputStream(file);
			int sd = Integer.parseInt("ss");
			process1();
		} catch(IOException | NumberFormatException e) {
			//因为处理IOException和NumberFormatException的代码是相同的，所以我们可以把它两用|合并到一起：
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			Throwable ta = e.getCause();
			if (ta == null) {
				System.out.println("在代码中获取原始异常可以使用Throwable.getCause()方法。如果返回null，说明已经是“根异常”了。");
			}
		}
	}
	
	
	static void process1() {
        try {
            process2();
        } catch (NullPointerException e) {
        	/*
        	 * throw new IllegalArgumentException();
        	 * 这样搞新的异常丢失了原始异常信息，我们已经看不到原始异常NullPointerException的信息了。
			 * 为了能追踪到完整的异常栈，在构造异常的时候，把原始的Exception实例传进去，
			 * 新的Exception就可以持有原始Exception信息。对上述代码改进如下：
			 * 警告: 捕获到异常并再次抛出时，一定要留住原始异常，否则很难定位第一案发现场！ 
        	 */
            throw new IllegalArgumentException(e);//把原始异常放进去
        }
    }

    static void process2() {
        throw new NullPointerException();
    }
    
    static void process3() {
    	try {
            Integer.parseInt("abc");
        } catch (Exception e) {
            System.out.println("catched");
            throw new RuntimeException(e);
        } finally {
            System.out.println("finally");
            /*
             * finally抛出异常后，原来在catch中准备抛出的异常就“消失”了，因为只能抛出一个异常。
             * 没有被抛出的异常称为“被屏蔽”的异常（Suppressed Exception）。 
             * 在极少数的情况下，我们需要获知所有的异常。如何保存所有的异常信息？方法是先用origin变量保存原始异常，
             * 然后调用Throwable.addSuppressed()，把原始异常添加进来，最后在finally抛出：
             */
            throw new IllegalArgumentException();
        }
    }
    
    static void process4() throws Exception{
        Exception origin = null;
    	try {
    		Integer.parseInt("abc");
    	} catch (Exception e) {
    		System.out.println("catched");
    		throw new RuntimeException(e);
    	} finally {
    		System.out.println("finally");
    		/*
    		 * finally抛出异常后，原来在catch中准备抛出的异常就“消失”了，因为只能抛出一个异常。
    		 * 没有被抛出的异常称为“被屏蔽”的异常（Suppressed Exception）。 
    		 * 在极少数的情况下，我们需要获知所有的异常。如何保存所有的异常信息？方法是先用origin变量保存原始异常，
    		 * 然后调用Throwable.addSuppressed()，把原始异常添加进来，最后在finally抛出：
    		 * 当catch和finally都抛出了异常时，虽然catch的异常被屏蔽了，但是，finally抛出的异常仍然包含了它：
    		 * 通过Throwable.getSuppressed()可以获取所有的Suppressed Exception。
			 * 绝大多数情况下，在finally中不要抛出异常。因此，我们通常不需要关心Suppressed Exception。
    		 */
    		 Exception e = new IllegalArgumentException();
             if (origin != null) {
                 e.addSuppressed(origin);
             }
             Throwable[] ss = e.getSuppressed();//获取素有的被屏蔽的异常
             throw e;
    	}
    }
    
    /*
     * 在一个大型项目中，可以自定义新的异常类型，但是，保持一个合理的异常继承体系是非常重要的。
		一个常见的做法是自定义一个BaseException作为“根异常”，然后，派生出各种业务类型的异常。
		BaseException需要从一个适合的Exception派生，通常建议从RuntimeException派生：在一个大型项目中，可以自定义新的异常类型，但是，保持一个合理的异常继承体系是非常重要的。
		一个常见的做法是自定义一个BaseException作为“根异常”，然后，派生出各种业务类型的异常。
		BaseException需要从一个适合的Exception派生，通常建议从RuntimeException派生：
		public class BaseException extends RuntimeException {
		}
		其他业务类型的异常就可以从BaseException派生：
		public class UserNotFoundException extends BaseException {
		}
		public class LoginFailedException extends BaseException {
		}
		自定义的BaseException应该提供多个构造方法：
		public class BaseException extends RuntimeException {
		    public BaseException() {
		        super();
		    }
		
		    public BaseException(String message, Throwable cause) {
		        super(message, cause);
		    }
		
		    public BaseException(String message) {
		        super(message);
		    }
		
		    public BaseException(Throwable cause) {
		        super(cause);
		    }
		}
		上述构造方法实际上都是原样照抄RuntimeException。这样，抛出异常的时候，就可以选择合适的构造方法。通过IDE可以根据父类快速生成子类的构造方法。
     */
}
