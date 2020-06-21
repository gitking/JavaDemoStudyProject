package com.yale.test.web.servlet.book;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yale.test.web.servlet.BaseServlet;

public class BookServlet extends BaseServlet {
	private BookDao bookDao = new BookDao();
	
	public String findAll(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
		req.setAttribute("bookList", bookDao.findAll());
		return "/demo/bookmanager/showList.jsp";
	}
	
	public String findByCategory(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
		String value = (String)req.getParameter("category");
		int category = Integer.valueOf(value);
		req.setAttribute("bookList", bookDao.findByCategory(category));
		return "/demo/bookmanager/showList.jsp";
	}
}
