package com.yale.test.spring.integration.mail.service;

import java.time.LocalDateTime;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.yale.test.spring.integration.mail.entity.User;

@Component
public class MailService {
	
	@Value("${smtp.from}")
	String from;
	
	@Autowired
	JavaMailSender mailSender;
	
	/*
	 * 观察下述代码，MimeMessage是JavaMail的邮件对象，而MimeMessageHelper是Spring提供的用于简化设置MimeMessage的类，
	 * 比如我们设置HTML邮件就可以直接调用setText(String text, boolean html)方法，而不必再调用比较繁琐的JavaMail接口方法。
	 * 最后一步是调用JavaMailSender.send()方法把邮件发送出去。
	 * 在MVC的某个Controller方法中，当用户注册成功后，我们就启动一个新线程来异步发送邮件：
	 * User user = userService.register(email, password, name);
	logger.info("user registered: {}", user.getEmail());
	// send registration mail:
	new Thread(() -> {
	    mailService.sendRegistrationMail(user);
	}).start();
	 * 因为发送邮件是一种耗时的任务，从几秒到几分钟不等，因此，异步发送是保证页面能快速显示的必要措施。这里我们直接启动了一个新的线程，但实际上还有更优化的方法，我们在下一节讨论。
	 * 小结
	 * Spring可以集成JavaMail，通过简单的封装，能简化邮件发送代码。其核心是定义一个JavaMailSender的Bean，然后调用其send()方法。
	 */
	public void sendRegistrationMail(User user) {
		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
			helper.setFrom(from);
			helper.setTo(user.getEmail());
			helper.setSubject("Welcome to Java course!");
			String html = String.format("<p>Hi, %s,</p><p>Welcome to Java course!</p><p>Sent at %s </p>", user.getName(), LocalDateTime.now());
			helper.setText(html, true);
			mailSender.send(mimeMessage);
		} catch (MailException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (MessagingException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
