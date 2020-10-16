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

public class SendMailWithAttachment {
	public static void main(String[] args) throws IOException, AddressException, MessagingException {
		final String smtp = "smtp.163.com";
		final String username = "yale268sh@163.com";
		final String password = "你自己邮箱的真实密码";
		final String from = "yale268sh@163.com";
		final String to = "937243987@qq.com";
		SendMail sender = new SendMail(smtp, username, password);
		Session session = sender.createTLSSession();
		try (InputStream input = SendMailWithAttachment.class.getResourceAsStream("/javamail.jpg")) {
			Message message = createMessageWithAttachment(session, from, to, "Hello Java邮件带附件", 
					"<h1>Hello</h1><p>这是一封带附件的<u>javamail</u>邮件!</p>", "javamail.jpg", input);
			Transport.send(message);
		}
	}
	static Message createMessageWithAttachment(Session session, String from, String to, String subject, String body, String fileName, 
			InputStream input) throws AddressException, MessagingException, IOException {
		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress(from));
		message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
		message.setSubject(subject, "UTF-8");
		
		Multipart multipart = new MimeMultipart();
		
		BodyPart textpart = new MimeBodyPart();//添加TEXT
		textpart.setContent(body, "text/html;charset=utf-8");
		multipart.addBodyPart(textpart);
		
		BodyPart imagePart = new MimeBodyPart();//添加image
		imagePart.setFileName(fileName);
		imagePart.setDataHandler(new DataHandler(new ByteArrayDataSource(input, "application/octet-stream")));
		
		multipart.addBodyPart(imagePart);
		message.setContent(multipart);
		return message;
	}
}
