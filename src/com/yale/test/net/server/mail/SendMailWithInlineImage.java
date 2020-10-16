package com.yale.test.net.server.mail;

import java.io.IOException;
import java.io.InputStream;

import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

/*
 * 发送内嵌图片的HTML邮件
 * 有些童鞋可能注意到，HTML邮件中可以内嵌图片，这是怎么做到的？
 * 如果给一个<img src="http://example.com/test.jpg">，这样的外部图片链接通常会被邮件客户端过滤，并提示用户显示图片并不安全。只有内嵌的图片才能正常在邮件中显示。
 * 内嵌图片实际上也是一个附件，即邮件本身也是Multipart，但需要做一点额外的处理：
 * 在HTML邮件中引用图片时，需要设定一个ID，用类似<img src=\"cid:img01\">引用，然后，在添加图片作为BodyPart时，除了要正确设置MIME类型（根据图片类型使用image/jpeg或image/png），还需要设置一个Header：
 * imagepart.setHeader("Content-ID", "<img01>");
 * 这个ID和HTML中引用的ID对应起来，邮件客户端就可以正常显示内嵌图片：
 * 常见问题
 * 如果用户名或口令错误，会导致535登录失败：
 * DEBUG SMTP: AUTH LOGIN failed
 * Exception in thread "main" javax.mail.AuthenticationFailedException: 535 5.7.3 Authentication unsuccessful [HK0PR03CA0105.apcprd03.prod.outlook.com]
 * 如果登录用户和发件人不一致，会导致554拒绝发送错误：
 * DEBUG SMTP: MessagingException while sending, THROW: 
 * com.sun.mail.smtp.SMTPSendFailedException: 554 5.2.0 STOREDRV.Submission.Exception:SendAsDeniedException.MapiExceptionSendAsDenied;
 * 有些时候，如果邮件主题和正文过于简单，会导致554被识别为垃圾邮件的错误：
 * DEBUG SMTP: MessagingException while sending, THROW: 
 * com.sun.mail.smtp.SMTPSendFailedException: 554 DT:SPM
 * 小结
 * 使用JavaMail API发送邮件本质上是一个MUA软件通过SMTP协议发送邮件至MTA服务器；
 * 打开调试模式可以看到详细的SMTP交互信息；
 * 某些邮件服务商需要开启SMTP，并需要独立的SMTP登录密码。
 */
public class SendMailWithInlineImage {
	public static void main(String[] args) throws IOException, AddressException, MessagingException {
		final String smtp = "smtp.163.com";
		final String username = "yale268sh@163.com";
		final String password = "";
		final String from = "yale268sh@163.com";
		final String to = "937243987@qq.com";
		SendMail sender = new SendMail(smtp, username, password);
		Session session = sender.createTLSSession();
		try(InputStream input = SendMailWithInlineImage.class.getResourceAsStream("/javamail.jpg")) {
			Message message = createMessageWithInlineImage(session, from, to, "Hello Java HTML邮件内嵌图片", 
					"<h1>Hello</h1><p><img src=\"cid:img01\"></p><p>这是一封内嵌图片的<u>javamail</u>邮件!</p>", "javamail.jpg", input);
			Transport.send(message);
		}
		
	}
	static Message createMessageWithInlineImage(Session session, String from, String to, String subject, String body,
			String fileName, InputStream input) throws AddressException, MessagingException, IOException {
		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress(from));
		message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
		message.setSubject(subject, "UTF-8");
		Multipart multipart = new MimeMultipart();
		
		//添加TEXT
		BodyPart textpart = new MimeBodyPart();
		textpart.setContent(body, "text/html;charset=utf-8");
		multipart.addBodyPart(textpart);
		//添加image
		BodyPart imagepart = new MimeBodyPart();
		imagepart.setFileName(fileName);
		imagepart.setDataHandler(new DataHandler(new ByteArrayDataSource(input, "image/jpeg")));
		//与HTML的<img src="cid:img01">关联;
		imagepart.setHeader("Content-ID", "<img01>");
		multipart.addBodyPart(imagepart);
		message.setContent(multipart);
		return message;
	}
}
