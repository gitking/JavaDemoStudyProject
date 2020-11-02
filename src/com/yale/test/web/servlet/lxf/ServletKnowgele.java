package com.yale.test.web.servlet.lxf;

/*
 * Servlet进阶
 * 一个Web App就是由一个或多个Servlet组成的，每个Servlet通过注解说明自己能处理的路径。例如：
 * @WebServlet(urlPatterns = "/hello")
	public class HelloServlet extends HttpServlet {
	    ...
	}
 * 上述HelloServlet能处理/hello这个路径的请求。
 * 早期的Servlet需要在web.xml中配置映射路径，但最新Servlet版本只需要通过注解就可以完成映射。 
 * 因为浏览器发送请求的时候，还会有请求方法（HTTP Method）：即GET、POST、PUT等不同类型的请求。因此，要处理GET请求，我们要覆写doGet()方法：
 * @WebServlet(urlPatterns = "/hello")
	public class HelloServlet extends HttpServlet {
	    @Override
	    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	        ...
	    }
	}
 * 类似的，要处理POST请求，就需要覆写doPost()方法。
 * 如果没有覆写doPost()方法，那么HelloServlet能不能处理POST /hello请求呢？
 * 我们查看一下HttpServlet的doPost()方法就一目了然了：它会直接返回405或400错误。因此，一个Servlet如果映射到/hello，那么所有请求方法都会由这个Servlet处理，至于能不能返回200成功响应，要看有没有覆写对应的请求方法。
 * 一个Webapp完全可以有多个Servlet，分别映射不同的路径。例如：
 * @WebServlet(urlPatterns = "/hello")
	public class HelloServlet extends HttpServlet {
	    ...
	}
	@WebServlet(urlPatterns = "/signin")
	public class SignInServlet extends HttpServlet {
	    ...
	}
	
	@WebServlet(urlPatterns = "/")
	public class IndexServlet extends HttpServlet {
	    ...
	}
 * 浏览器发出的HTTP请求总是由Web Server先接收，然后，根据Servlet配置的映射，不同的路径转发到不同的Servlet：
 *                 ┌ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ┐
	
	               │            /hello    ┌───────────────┐│
	                          ┌──────────>│ HelloServlet  │
	               │          │           └───────────────┘│
	┌───────┐    ┌──────────┐ │ /signin   ┌───────────────┐
	│Browser│───>│Dispatcher│─┼──────────>│ SignInServlet ││
	└───────┘    └──────────┘ │           └───────────────┘
	               │          │ /         ┌───────────────┐│
	                          └──────────>│ IndexServlet  │
	               │                      └───────────────┘│
	                              Web Server
	               └ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ┘
 * 这种根据路径转发的功能我们一般称为Dispatch。映射到/的IndexServlet比较特殊，它实际上会接收所有未匹配的路径，相当于/*，因为Dispatcher的逻辑可以用伪代码实现如下：
 * String path = ...
	if (path.equals("/hello")) {
	    dispatchTo(helloServlet);
	} else if (path.equals("/signin")) {
	    dispatchTo(signinServlet);
	} else {
	    // 所有未匹配的路径均转发到"/"
	    dispatchTo(indexServlet);
	}
 * 所以我们在浏览器输入一个http://localhost:8080/abc也会看到IndexServlet生成的页面。
 * HttpServletRequest
 * HttpServletRequest封装了一个HTTP请求，它实际上是从ServletRequest继承而来。最早设计Servlet时，设计者希望Servlet不仅能处理HTTP，也能处理类似SMTP等其他协议，
 * 因此，单独抽出了ServletRequest接口，但实际上除了HTTP外，并没有其他协议会用Servlet处理，所以这是一个过度设计。
 * 我们通过HttpServletRequest提供的接口方法可以拿到HTTP请求的几乎全部信息，常用的方法有：
 * 1.getMethod()：返回请求方法，例如，"GET"，"POST"；
 * 2.getRequestURI()：返回请求路径，但不包括请求参数，例如，"/hello"；
 * 3.getQueryString()：返回请求参数，例如，"name=Bob&a=1&b=2"；
 * 4.getParameter(name)：返回请求参数，GET请求从URL读取参数，POST请求从Body中读取参数；
 * 5.getContentType()：获取请求Body的类型，例如，"application/x-www-form-urlencoded"；
 * 6.getContextPath()：获取当前Webapp挂载的路径，对于ROOT来说，总是返回空字符串""；
 * 7.getCookies()：返回请求携带的所有Cookie；
 * 8.getHeader(name)：获取指定的Header，对Header名称不区分大小写；
 * 9.getHeaderNames()：返回所有Header名称；
 * 10.getInputStream()：如果该请求带有HTTP Body，该方法将打开一个输入流用于读取Body；
 * 11.getReader()：和getInputStream()类似，但打开的是Reader；
 * 12.getRemoteAddr()：返回客户端的IP地址；
 * 13.getScheme()：返回协议类型，例如，"http"，"https"；
 * 此外，HttpServletRequest还有两个方法：setAttribute()和getAttribute()，可以给当前HttpServletRequest对象附加多个Key-Value，相当于把HttpServletRequest当作一个Map<String, Object>使用。
 * 调用HttpServletRequest的方法时，注意务必阅读接口方法的文档说明，因为有的方法会返回null，例如getQueryString()的文档就写了：
 * ... This method returns null if the URL does not have a query string...
 * HttpServletResponse
 * HttpServletResponse封装了一个HTTP响应。由于HTTP响应必须先发送Header，再发送Body，所以，操作HttpServletResponse对象时，必须先调用设置Header的方法，最后调用发送Body的方法。
 * 常用的设置Header的方法有：
 * setStatus(sc)：设置响应代码，默认是200；
 * setContentType(type)：设置Body的类型，例如，"text/html"；
 * setCharacterEncoding(charset)：设置字符编码，例如，"UTF-8"；
 * setHeader(name, value)：设置一个Header的值；
 * addCookie(cookie)：给响应添加一个Cookie；
 * addHeader(name, value)：给响应添加一个Header，因为HTTP协议允许有多个相同的Header；
 * 写入响应时，需要通过getOutputStream()获取写入流，或者通过getWriter()获取字符流，二者只能获取其中一个。
 * 写入响应前，无需设置setContentLength()，因为底层服务器会根据写入的字节数自动设置，如果写入的数据量很小，实际上会先写入缓冲区，如果写入的数据量很大，服务器会自动采用Chunked编码让浏览器能识别数据结束符而不需要设置Content-Length头。
 * 但是，写入完毕后调用flush()却是必须的，因为大部分Web服务器都基于HTTP/1.1协议，会复用TCP连接。如果没有调用flush()，将导致缓冲区的内容无法及时发送到客户端。此外，写入完毕后千万不要调用close()，原因同样是因为会复用TCP连接，如果关闭写入流，将关闭TCP连接，使得Web服务器无法复用此TCP连接。
 *  写入完毕后对输出流调用flush()而不是close()方法！ 
 *  有了HttpServletRequest和HttpServletResponse这两个高级接口，我们就不需要直接处理HTTP协议。注意到具体的实现类是由各服务器提供的，而我们编写的Web应用程序只关心接口方法，并不需要关心具体实现的子类。
 *  Servlet多线程模型
 *  一个Servlet类在服务器中只有一个实例，但对于每个HTTP请求，Web服务器会使用多线程执行请求。因此，一个Servlet的doGet()、doPost()等处理请求的方法是多线程并发执行的。如果Servlet中定义了字段，要注意多线程并发访问的问题：
 *  public class HelloServlet extends HttpServlet {
	    private Map<String, String> map = new ConcurrentHashMap<>();
	
	    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	        // 注意读写map字段是多线程并发的:
	        this.map.put(key, value);
	    }
	}
 *  对于每个请求，Web服务器会创建唯一的HttpServletRequest和HttpServletResponse实例，因此，HttpServletRequest和HttpServletResponse实例只有在当前处理线程中有效，它们总是局部变量，不存在多线程共享的问题。
 * 小结
 * 一个Webapp中的多个Servlet依靠路径映射来处理不同的请求；
 * 映射为/的Servlet可处理所有“未匹配”的请求；
 * 如何处理请求取决于Servlet覆写的对应方法；
 * Web服务器通过多线程处理HTTP请求，一个Servlet的处理方法可以由多线程并发执行。
 * 一个Servlet类在服务器中只有一个实例，不接受反驳。 
 * 不接受反驳，哈哈哈。我也看了您后面的文章，如果使用Spring MVC的话，其实就不用关心servlet类是不是单例了，因为我们写的类是controller。
 * dispatchservlet根据路由实例化controller，controller是dispatchservlet的局部变量，所以不存在多线程的问题
 * 那你理解还是不到位，controller是dispatcher-servlet的成员变量
 * 额，的确是这样，多谢老师纠正。dispatcher_servlet有getMappings参数，会持有所有get对应的controller的实例
 */
public class ServletKnowgele {

}
