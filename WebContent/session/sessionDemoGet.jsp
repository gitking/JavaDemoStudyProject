<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>获取session中的数据</title>
</head>
<body>
	<p>获取session中的数据,浏览器不关,session中的数据就可以一直得到.如果你不关闭浏览器,但是把服务器关了,此时你刷新浏览器,你获取不到session中的数据</p>
	<p>这是很显然的,因为你tomcat服务器都关了,你肯定获取不到.但是你浏览器别关,等我把tomcat服务器启动起来,你直接刷新就能从session中获取数据。</p>
	<p>这是因为HttpSession有重生的能力:你把tomcat服务器关闭的时候,在tomcat服务器的work目录下面有一个SESSIONS.ser的文件,tomcat把session序列化下来了</p>
	<p>这个SESSIONS.ser文件保存的是所有的session对象,tomcat服务器启动的时候或读取这个东西,读取完毕会把这个文件<strong>删除掉</strong></p>
	<p>如果你不想让tomcat在关闭服务的时候,序列化这个东西,怎么办呢？你可以在tomcat的conf/context.xml里面添加一个Manager pathname="" 就可以了 <br/>
		<em>注意Manager pathname=""这个配置针对tomcat服务器关闭的时候,钝化(序列化)httpSession到文件里面</em>
	</p>
	<hr/>
	<p>从session中获取的数据为:</p>
	<%
		out.println(session.getAttribute("sss"));
		out.println(session.getAttribute("obj"));
	%>
	<hr/>
	<h3>httpSession的钝化</h3>
	<p>
		<i> httpSession的钝化(序列化)配置 </i><br/>
		<em>下面的配置是,tomcat不关闭服务器,只是将那些连接到网站又长时间不活动的httpSession,在不关闭tomcat的情况下将这些httpSession序列化到文件中,用户活动了立即反序列化出来就行</em>
		<br/>
    	 &lt;Manager className="org.apache.catalina.session.PersistentManager" maxIdleSwap = "1"&gt;
    	 <br/>
    		&lt;Store className="org.apache.catalina.session.FileStore" directory="mysession"/&gt;
    		<br/>
    	 &lt;/Manager&gt;
	</p>
	<p>如果你在tomcat的/conf/context.xml这里配置,那么针对tomcat下面的所有项目都生效,maxIdleSwap最大不活动时间,单位是分钟.
    这个配置主要处理这种问题：就是当一个网站比如淘宝,连接到淘宝网站上面的用户太多了,但是其实有一部分用户连上淘宝之后大多数时间是不活动的,那么这些不活动的
    httpSession在淘宝的服务器上就太占内存了,这个时候把这些不活动的httpSession序列化到文件中保存到服务器的硬盘上面,用户活动的时候再将httpSession对象反序列化出来就行.
    	上面这个httpSession配置钝化session,是一个session钝化一个文件,文件名是sessionId文件后缀是.session,eg:0B4F1923D44E865ED91DAB7A32BEE14F.session
    	即使用户活动之后,httpSession被活化(反序列化成HTTPSession对象,这个文件也不会消失)
    </p>
    
    <p><em>注意tomcat钝化(序列化)httpSession的时候,也会将httpSession对象中保存的属性也一起序列化,所以你往httpSession里面存一个对象,这个对象没有实现序列化接口
    那么tomcat钝化(序列化)httpSession的时候就不会把你这个序列化对象保存到SESSIONS.ser文件中,下次你再访问的时候就取不到这个对象了</em>
    </p>
</body>
</html>