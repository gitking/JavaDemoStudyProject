package com.yale.test.net.server.mail;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/*
 * 发送Email
 * Email就是电子邮件。电子邮件的应用已经有几十年的历史了，我们熟悉的邮箱地址比如abc@example.com，邮件软件比如Outlook都是用来收发邮件的。
 * 使用Java程序也可以收发电子邮件。我们先来看一下传统的邮件是如何发送的。
 * 传统的邮件是通过邮局投递，然后从一个邮局到另一个邮局，最终到达用户的邮箱：
 *             ┌──────────┐    ┌──────────┐
	           │PostOffice│    │PostOffice│     .───.
	┌─────┐    ├──────────┤    ├──────────┤    (   ( )
	│═══ ░│───>│ ┌─┐ ┌┐┌┐ │───>│ ┌─┐ ┌┐┌┐ │───> `─┬─'
	└─────┘    │ │░│ └┘└┘ │    │ │░│ └┘└┘ │       │
	           └─┴─┴──────┘    └─┴─┴──────┘       │
 * 电子邮件的发送过程也是类似的，只不过是电子邮件是从用户电脑的邮件软件，例如Outlook，发送到邮件服务器上，可能经过若干个邮件服务器的中转，最终到达对方邮件服务器上，收件方就可以用软件接收邮件：
 *  		 ┌─────────┐    ┌─────────┐    ┌─────────┐
             │░░░░░░░░░│    │░░░░░░░░░│    │░░░░░░░░░│
┌───────┐    ├─────────┤    ├─────────┤    ├─────────┤    ┌───────┐
│░░░░░░░│    │░░░░░░░░░│    │░░░░░░░░░│    │░░░░░░░░░│    │░░░░░░░│
├───────┤    ├─────────┤    ├─────────┤    ├─────────┤    ├───────┤
│       │───>│O ░░░░░░░│───>│O ░░░░░░░│───>│O ░░░░░░░│<───│       │
└───────┘    └─────────┘    └─────────┘    └─────────┘    └───────┘
   MUA           MTA            MTA            MDA           MUA
 * 我们把类似Outlook这样的邮件软件称为MUA：Mail User Agent，意思是给用户服务的邮件代理；邮件服务器则称为MTA：Mail Transfer Agent，意思是邮件中转的代理；最终到达的邮件服务器称为MDA：Mail Delivery Agent，意思是邮件到达的代理。电子邮件一旦到达MDA，就不再动了。实际上，电子邮件通常就存储在MDA服务器的硬盘上，然后等收件人通过软件或者登陆浏览器查看邮件。
 * MTA和MDA这样的服务器软件通常是现成的，我们不关心这些服务器内部是如何运行的。要发送邮件，我们关心的是如何编写一个MUA的软件，把邮件发送到MTA上。
 * MUA到MTA发送邮件的协议就是SMTP协议，它是Simple Mail Transport Protocol的缩写，使用标准端口25，也可以使用加密端口465或587。
 * SMTP协议是一个建立在TCP之上的协议，任何程序发送邮件都必须遵守SMTP协议。使用Java程序发送邮件时，我们无需关心SMTP协议的底层原理，只需要使用JavaMail这个标准API就可以直接发送邮件。
 * 准备SMTP登录信息
 * 假设我们准备使用自己的邮件地址me@example.com给小明发送邮件，已知小明的邮件地址是xiaoming@somewhere.com，发送邮件前，我们首先要确定作为MTA的邮件服务器地址和端口号。邮件服务器地址通常是smtp.example.com，端口号由邮件服务商确定使用25、465还是587。以下是一些常用邮件服务商的SMTP信息：
 * QQ邮箱：SMTP服务器是smtp.qq.com，端口是465/587；
 * 163邮箱：SMTP服务器是smtp.163.com，端口是465；
 * Gmail邮箱：SMTP服务器是smtp.gmail.com，端口是465/587。
 * 有了SMTP服务器的域名和端口号，我们还需要SMTP服务器的登录信息，通常是使用自己的邮件地址作为用户名，登录口令是用户口令或者一个独立设置的SMTP口令。
 * 我们来看看如何使用JavaMail发送邮件。
 * 首先，我们需要创建一个Maven工程，并把JavaMail相关的两个依赖加入进来：
 * <dependency>
        <groupId>javax.mail</groupId>
        <artifactId>javax.mail-api</artifactId>
        <version>1.6.2</version>
    </dependency>
    <dependency>
        <groupId>com.sun.mail</groupId>
        <artifactId>javax.mail</artifactId>
        <version>1.6.2</version>
    </dependency>
 * 然后，我们通过JavaMail API连接到SMTP服务器上：
 */
public class SendMail {
	final String smtpHost;
	final String username;
	final String password;
	final boolean debug;
	public SendMail(String smtpHost, String username, String password) {
		this.smtpHost = smtpHost;
		this.username = username;
		this.password = password;
		this.debug = true;
	}
	
	/*
	 * QQ邮箱填写的口令不是QQ密码，而是授权码。可以在QQ邮箱->设置->账户生成授权码。
	 * 另外，QQ邮箱好像只支持SSL连接，因为我测试TLS的时候失败了。。
	 */
	public static void main(String[] args) throws AddressException, MessagingException {
		final String smtp = "smtp.163.com";
		final String username = "yale268sh@163.com";
		final String password = "你自己邮箱的真实密码";
		final String from = "yale268sh@163.com";
		final String to = "937243987@qq.com";
		
		SendMail sender = new SendMail(smtp, username, password);
		Session session = sender.createTLSSession();
		Message message = createTextMessage(session, from, to, "JavaMail邮件", "Hello,这是一封来自javamail的邮件");
		Transport.send(message);
	}
	
	Session createSSLSession() {
		Properties props = new Properties();
		props.put("mail.smtp.host", this.smtpHost);//SMTP主机名
		props.put("mail.smtp.prot", "465");//主机端口号
		props.put("mail.smtp.auth", "true");//是否需要用户认证
		//启动SSL
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.socketFactory.port", "465");
		Session session = Session.getInstance(props, new Authenticator(){
			//用户名+口令认证
			@Override
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(SendMail.this.username, SendMail.this.password);
			}
		});
		session.setDebug(this.debug);//显示调试信息
		return session;
	}
	
	/*
	 * 安全传输层协议（TLS）
	 * SSL(Secure Sockets Layer 安全套接字协议),及其继任者传输层安全（Transport Layer Security，TLS）是为网络通信提供安全及数据完整性的一种安全协议。
	 * TLS与SSL在传输层与应用层之间对网络连接进行加密。
	 * 廖老师，我好像懂了。createTLSSession()方法里面应该在加一行代码,props.put("mail.smtp.ssl.enable", "true");这样163邮箱使用465端口就不会报错了。虽然把端口改成25一样不会报错,但是这样通过25端口发送邮件时网络就不会加密了。加密是必须的,这句话的意思是发送邮件时最好通过465端口进行加密。
	 * 为啥我们用465端口代码会报错呢？我想很可能是因为我自己用的JDK版本时1.8的原因,如果跟廖老师一样JDK用15的,应该不加props.put("mail.smtp.ssl.enable", "true");这行代码也不会报错。
	 * https://www.liaoxuefeng.com/wiki/1252599548343744/1319099923693601
	 */
	Session createTLSSession() {
		Properties props = new Properties();
		props.put("mail.smtp.host", this.smtpHost);//SMTP主机名
		props.put("mail.smtp.port", "465");//主机端口号
		props.put("mail.smtp.auth", "true");//是否需要用户认证
		props.put("mail.smtp.starttls.enable", "true");//启用TLS加密
		/**
		 * 如果不加这行代码,props.put("mail.smtp.ssl.enable", "true");代码会报下面的错误
		 * Exception in thread "main" javax.mail.MessagingException: Could not connect to SMTP host: smtp.163.com, port: 465, response: -1
		 * 也可以把端口改成25,props.put("mail.smtp.port", "25");上面的错误就不会报了,但是端口改成25了,发送邮件就不会加密了。
		 */
		props.put("mail.smtp.ssl.enable", "true");
		Session session = Session.getInstance(props, new Authenticator(){
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(SendMail.this.username, SendMail.this.password);
			}
		});
		session.setDebug(this.debug);//显示调试信息
		return session;
	}
	
	Session createInsecureSession(String host, String username, String password) {
		Properties props = new Properties();
		props.put("mail.smtp.host", this.smtpHost);
		props.put("mail.smtp.port", "25");//主机端口号
		props.put("mail.smtp.auth", "true");//是否需要用户认证
		Session session = Session.getInstance(props, new Authenticator(){
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(SendMail.this.username, SendMail.this.password);
			}
		});
		session.setDebug(this.debug);//显示调试信息
		return session;
	}
	
	static Message createTextMessage(Session session, String from, String to, String subject, String body)
	throws AddressException, MessagingException {
		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress(from));
		message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
		message.setSubject(subject, "UTF-8");
		message.setText(body, "UTF-8");
		return message;
	}
}
