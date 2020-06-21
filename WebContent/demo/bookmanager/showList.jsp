<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="/WEB-INF/c.tld"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>图书详情界面</title>
	</head>
	<body>
		<h1 align="center">图书列表</h1>
		<table border="1" align="center" width="50%">
			<tr>
				<th>书名</th>
				<th>单价</th>
				<th>分类</th>
			</tr>
			<c:forEach items="${bookList}" var="book">
				<tr>
					<td>${book.bname}</td>
					<td>${book.price}</td>
					<c:choose>
						<c:when test="${book.category eq 1 }"><td style="color:red">${book.category}</td></c:when>
						<c:when test="${book.category eq 2 }"><td style="color:blue">${book.category}</td></c:when>
						<c:when test="${book.category eq 3 }"><td style="color:green">${book.category}</td></c:when>
					</c:choose>
				</tr>
			</c:forEach>
		</table>
	</body>
</html>