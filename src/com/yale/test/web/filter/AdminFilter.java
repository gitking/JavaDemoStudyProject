package com.yale.test.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class AdminFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest hsp = (HttpServletRequest)request;
		HttpSession hs = hsp.getSession();
		String admName = (String)hs.getAttribute("admin");
		if (admName != null && !admName.trim().isEmpty()) {
			chain.doFilter(request, response);
			return;
		} else {
			request.setAttribute("msg", "您可能是个啥,但肯定不是管理员,不要瞎溜达");
			request.getRequestDispatcher("/login.jsp").forward(request, response);
		}
	}

	@Override
	public void destroy() {

	}

}
