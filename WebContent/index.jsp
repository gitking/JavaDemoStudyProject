<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="/WEB-INF/c.tld"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Insert title here</title>
	</head>
	<body>
		<span>首页</span>
		<%application.setAttribute("xxx", "jsp"); %>
		<h1>你就是个游客而已</h1>
		<a href="<c:url value='/index.jsp'/>">游客入口</a><br/>
		<a href="<c:url value='/users/u.jsp'/>">会员入口</a><br/>
		<a href="<c:url value='/admin/a.jsp'/>">管理员入口</a><br/>
	</body>
</html>