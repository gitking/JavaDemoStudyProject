<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="/WEB-INF/c.tld"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>SpringMVC_Mybatis_Demo</title>
		<script type="text/javascript" src="../js/jQuery/jquery.min.js"></script>
		<script type="text/javascript">
			
		</script>
	</head>
	<body>
		<table width="80%" align="center">
			<tr>
				<td colspan="4" align="right"><a href="addUser.jsp">添加</a></td>
			</tr>
			<tr>
				<td>编号</td>
				<td>姓名</td>
				<td>密码</td>
				<td>操作</td>
			</tr>
			<c:forEach items="${list }" var="bean">
				<tr>
					<td>${bean.id }</td>
					<td>${bean.name }</td>
					<td>${bean.pwd }</td>
					<td><a href="delUser.springmvc?id=${bean.id }">删除</a>
					<a href="delUserRes/id=${bean.id }.springmvc">Restful风格删除</a></td>
				</tr>
			</c:forEach>
		</table>
	</body>
</html>