<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="/WEB-INF/c.tld"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>SpringMVC_Mybatis_添加用户</title>
		<script type="text/javascript" src="../js/jQuery/jquery.min.js"></script>
		<script type="text/javascript">
			
		</script>
	</head>
	<body>
		<form action="user/addUser.springmvc" method="post">
			姓名:<input type="text" name="name"/><br/>
			密码:<input type="password" name="pwd"/><br/>
			<input type="submit" value="提交"/>
		</form>
	</body>
</html>