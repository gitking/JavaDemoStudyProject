<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="/WEB-INF/c.tld"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>乱码处理</title>
</head>
<body>
	<a href="<c:url value='/encodeServlet?username=张三'/>">get请求乱码</a>、
	<form action="<c:url value='/encodeServlet'/>" method="post">
		用户名:<input type="text" name="username" value="李四"/>
		<input type="submit" value="提交"/>
	</form>
</body>
</html>