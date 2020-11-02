package com.yale.test.web.servlet.session;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/*
 * 创建一个新Cookie时，除了指定名称和值以外，通常需要设置setPath("/")，浏览器根据此前缀决定是否发送Cookie。如果一个Cookie调用了setPath("/user/")，
 * 那么浏览器只有在请求以/user/开头的路径时才会附加此Cookie。通过setMaxAge()设置Cookie的有效期，单位为秒，最后通过resp.addCookie()把它添加到响应。
 * 如果访问的是https网页，还需要调用setSecure(true)，否则浏览器不会发送该Cookie。
 * 因此，务必注意：浏览器在请求某个URL时，是否携带指定的Cookie，取决于Cookie是否满足以下所有要求：
 * 1.URL前缀是设置Cookie时的Path；
 * 2.Cookie在有效期内；
 * 3.Cookie设置了secure时必须以https访问。
 * 我们可以在浏览器看到服务器发送的Cookie：
 * 如果我们要读取Cookie，例如，在IndexServlet中，读取名为lang的Cookie以获取用户设置的语言，可以写一个方法如下：
 */
@WebServlet(urlPatterns="/pref")
public class LanguageServlet extends HttpServlet{
	private static final Set<String> LANGUAGES = new HashSet<String>();

	public LanguageServlet() {
		LANGUAGES.add("en");
		LANGUAGES.add("zh");
	}
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String lang = req.getParameter("lang");
		if (LANGUAGES.contains(lang)) {
			//创建一个新的Cookie
			Cookie cookie = new Cookie("lang", lang);
			//该Cookie的生效的路径范围
			cookie.setPath("/");
			//该Cookie的有效期,8640000秒=100天
			cookie.setMaxAge(8640000);
			//将改Cookie添加到响应
			resp.addCookie(cookie);
		}
		resp.sendRedirect("/pcis/indexServlet");
	}
}
