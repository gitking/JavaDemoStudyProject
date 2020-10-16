package com.yale.test.net.server.mail;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/*
 * 发送HTML邮件
 * 发送HTML邮件和文本邮件是类似的，只需要把：message.setText(body, "UTF-8");改为：message.setText(body, "UTF-8", "html");
 * 传入的body是类似<h1>Hello</h1><p>Hi, xxx</p>这样的HTML字符串即可。HTML邮件可以在邮件客户端直接显示为网页格式：
 */
public class SendMailWithHTML {
	public static void main(String[] args) throws AddressException, MessagingException {
		final String smtp = "smtp.163.com";
		final String username = "yale268sh@163.com";
		final String password = "你自己邮箱的真实密码";
		final String from = "yale268sh@163.com";
		final String to = "937243987@qq.com";
		SendMail sender = new SendMail(smtp, username, password);
		Session session = sender.createTLSSession();
		Message message = createHtmlMessage(session, from, to, "Java HTML邮件", "<h1>Hello</h1><p>这是一封<u>javamial</u>HTML邮件</p>");
		Transport.send(message);
	}
	 static Message createHtmlMessage(Session session, String from, String to, String subject, String body) 
	 throws AddressException, MessagingException {
		 MimeMessage message = new MimeMessage(session);
		 message.setFrom(new InternetAddress(from));
		 message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
		 message.setSubject(subject, "UTF-8");
		 message.setText(body, "UTF-8", "html");
		 return message;
	 }
}
