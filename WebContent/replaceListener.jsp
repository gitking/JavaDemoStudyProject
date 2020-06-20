<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>替换属性</title>
	</head>
	<body>
		<span>我在演示用ServletContextAttributeListener监控ServletContext里面的属性</span>
		<%application.setAttribute("xxx", "html");
			application.removeAttribute("xxx");%>
	</body>
</html>