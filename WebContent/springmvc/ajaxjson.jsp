<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="/WEB-INF/c.tld"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>SpringMVC_Ajax请求</title>
		<script type="text/javascript" src="../js/jQuery/jquery.min.js"></script>
		<script type="text/javascript">
			$(function(){
				$("#btn").click(function(){
					/*
					 * 因为ajax.jsp的页面在"WebContent/springmvc/ajax.jsp"这个目录下,
					 * $.post("ajax.springmvc")发出的请求是
					 * http://localhost:8080/pcis/springmvc/ajax.springmvc
					 * 但实际我想发的请求为:http://localhost:8080/pcis/ajax.springmvc
					 * 也可以使用这种方式/pcis/ajax.springmvc
					 * 也可以使用../ajax.springmvc这种方式
					 * 以上这俩种方式可以实现发出的请求为:http://localhost:8080/pcis/ajax.springmvc
					 */
					$.post("../json.springmvc",
							function(data){
								var html="";
								for(var i=0;i<data.length;i++){
									html += "<tr><td>" + data[i].id+ "</td><td>" + data[i].name+ "</td><td>" + data[i].pwd+ "</td></tr>";
								}
								$("#content").html(html);
							}
					);
				});
			});
		</script>
	</head>
	<body>
		<input type="button" id="btn" value="获取数据"/>
		<table>
			<tr>
				<td>编号</td>
				<td>姓名</td>
				<td>性别</td>
			</tr>
			<tbody id="content">
			</tbody>
		</table>
		<p>使用SpringMVC往前台返回JSON格式的数据,都不需要自己将对象转换为JSON,SpringMVC自己就帮我们转换了,非常舒服</p>
	</body>
</html>