<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="/WEB-INF/c.tld"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>SpringMVC文件上传</title>
	</head>
	<body>
		<form action="/pcis/uploadbatch.springmvc" method="post" enctype="multipart/form-data">
			请选择文件:<input type="file" name="file" /><br/>
			请选择文件2:<input type="file" name="file" /><br/>
			备注:<input type="text" name="desc" /><br/>
			<input type="submit" value="上传">
		</form>
	</body>
</html>