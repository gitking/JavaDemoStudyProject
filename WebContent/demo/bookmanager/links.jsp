<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="/WEB-INF/c.tld"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>图书管理连接页面</title>
	</head>
	<body>
		<h1>链接页面</h1>
		<a href = "<c:url value='/bookServlet?method=findAll'/>">查询所有</a><br/>
		<a href = "<c:url value='/bookServlet?method=findByCategory&category=1'/>">查询SE</a><br/>
		<a href = "<c:url value='/bookServlet?method=findByCategory&category=2'/>">查询EE</a><br/>
		<a href = "<c:url value='/bookServlet?method=findByCategory&category=3'/>">查询Framework</a><br/>
	</body>
</html>