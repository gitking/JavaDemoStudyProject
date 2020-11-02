package com.yale.test.design.interpreter.esponsibility;

import java.math.BigDecimal;

/*
 * 责任链
 * 使多个对象都有机会处理请求，从而避免请求的发送者和接收者之间的耦合关系。将这些对象连成一条链，并沿着这条链传递该请求，直到有一个对象处理它为止。
 * 责任链模式（Chain of Responsibility）是一种处理请求的模式，它让多个处理器都有机会处理该请求，直到其中某个处理成功为止。责任链模式把多个处理器串成链，然后让请求在链上传递：
 * 
 * 		 ┌─────────┐
	     │ Request │
	     └─────────┘
	          │
	┌ ─ ─ ─ ─ ┼ ─ ─ ─ ─ ┐
	          ▼
	│  ┌─────────────┐  │
	   │ ProcessorA  │
	│  └─────────────┘  │
	          │
	│         ▼         │
	   ┌─────────────┐
	│  │ ProcessorB  │  │
	   └─────────────┘
	│         │         │
	          ▼
	│  ┌─────────────┐  │
	   │ ProcessorC  │
	│  └─────────────┘  │
	          │
	└ ─ ─ ─ ─ ┼ ─ ─ ─ ─ ┘
	          │
	          ▼
 * 在实际场景中，财务审批就是一个责任链模式。假设某个员工需要报销一笔费用，审核者可以分为：
 * Manager：只能审核1000元以下的报销；
 * Director：只能审核10000元以下的报销；
 * CEO：可以审核任意额度。
 * 用责任链模式设计此报销流程时，每个审核者只关心自己责任范围内的请求，并且处理它。对于超出自己责任范围的，扔给下一个审核者处理，这样，将来继续添加审核者的时候，不用改动现有逻辑。
 * 我们来看看如何实现责任链模式。
 * 首先，我们要抽象出请求对象，它将在责任链上传递：
 * 并且做好约定：如果返回Boolean.TRUE，表示处理成功，如果返回Boolean.FALSE，表示处理失败（请求被拒绝），如果返回null，则交由下一个Handler处理。
 * 然后，依次编写ManagerHandler、DirectorHandler和CEOHandler。以ManagerHandler为例：
 * 有了不同的Handler后，我们还要把这些Handler组合起来，变成一个链，并通过一个统一入口处理：
 * 现在，我们就可以在客户端组装出责任链，然后用责任链来处理请求：
 */
public class Test {
	public static void main(String[] args) {
		/*
		 * 责任链模式本身很容易理解，需要注意的是，Handler添加的顺序很重要，如果顺序不对，处理的结果可能就不是符合要求的。
		 * 此外，责任链模式有很多变种。有些责任链的实现方式是通过某个Handler手动调用下一个Handler来传递Request，例如：
		 * public class AHandler implements Handler {
			    private Handler next;
			    public void process(Request request) {
			        if (!canProcess(request)) {
			            // 手动交给下一个Handler处理:
			            next.process(request);
			        } else {
			            ...
			        }
			    }
			}
		 * 还有一些责任链模式，每个Handler都有机会处理Request，通常这种责任链被称为拦截器（Interceptor）或者过滤器（Filter），
		 * 它的目的不是找到某个Handler处理掉Request，而是每个Handler都做一些工作，比如：记录日志；检查权限；准备相关资源；...
		 * 例如，JavaEE的Servlet规范定义的Filter就是一种责任链模式，它不但允许每个Filter都有机会处理请求，还允许每个Filter决定是否将请求“放行”给下一个Filter：
		 * public class AuditFilter implements Filter {
			    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
			        log(req);
			        if (check(req)) {
			            // 放行:
			            chain.doFilter(req, resp);
			        } else {
			            // 拒绝:
			            sendError(resp);
			        }
			    }
			}
		 * 这种模式不但允许一个Filter自行决定处理ServletRequest和ServletResponse，还可以“伪造”ServletRequest和ServletResponse以便让下一个Filter处理，能实现非常复杂的功能。
		 * 小结
		 * 责任链模式是一种把多个处理器组合在一起，依次处理请求的模式；
		 * 责任链模式的好处是添加新的处理器或者重新排列处理器非常容易；
		 * 责任链模式经常用在拦截、预处理请求等。
		 */
		HandlerChain chain = new HandlerChain();// 构造责任链:
		chain.addHanlder(new ManagerHandler());
		chain.addHanlder(new DirectorHandler());
		chain.addHanlder(new CEOHandler());
		// 处理请求:
		chain.process(new Request("Bob", new BigDecimal("123.45")));
		chain.process(new Request("Alice", new BigDecimal("1234.56")));
		chain.process(new Request("Bill", new BigDecimal("12345.67")));
		chain.process(new Request("John", new BigDecimal("123456.78")));
	}
}
