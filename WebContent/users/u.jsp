<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="/WEB-INF/c.tld"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户页面</title>
</head>
<body>
	<h1>会员先生,您早上/晚上好!</h1>
	<a href="<c:url value='/index.jsp'/>">游客入口</a><br/>
		<a href="<c:url value='/users/u.jsp'/>">会员入口</a><br/>
		<a href="<c:url value='/admin/a.jsp'/>">管理员入口</a><br/>
</body>
</html>