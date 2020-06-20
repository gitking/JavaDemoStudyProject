<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>向session添加数据</title>
</head>
<body>
	<p>获取session中的数据,浏览器不关,session中的数据就可以一直得到.如果你不关闭浏览器,但是把服务器关了,此时你刷新浏览器,你获取不到session中的数据</p>
	<p>这是很显然的,因为你tomcat服务器都关了,你肯定获取不到.但是你浏览器别关,等我把tomcat服务器启动起来,你直接刷新就能从session中获取数据。</p>
	<%
		session.setAttribute("sss", "我是一个快乐的小session");
		session.setAttribute("obj", new com.yale.test.web.listener.HttpSessionSer());
	%>
</body>
</html>