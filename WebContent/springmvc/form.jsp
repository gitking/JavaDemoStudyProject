<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="/WEB-INF/c.tld"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>SpringMVC获取传参</title>
	</head>
	<body>
		<form action="/pcis/helloResDataEncode.springmvc" method="post">
			姓名:<input type="text" name="name" />
			<input type="submit" value="提交Post请求">
		</form>
	</body>
</html>