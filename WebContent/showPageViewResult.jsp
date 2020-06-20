<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="/WEB-INF/c.tld"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>PageView访问数量</title>
</head>
<body>
	<h1 align="center">显示结果</h1>
	<table align="center" width="60%" border="1">
		<tr>
			<th>IP</th><th>次数</th>
		</tr>
		<c:forEach items="${applicationScope.map}" var="entry">
			<tr>
				<td>${entry.key}</td>
				<td>${entry.value}</td>
			</tr>
		</c:forEach>
	</table>
</body>
</html>