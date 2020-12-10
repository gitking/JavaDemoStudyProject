package com.yale.test.spring.integration.jms.service;

import javax.jms.Message;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yale.test.spring.integration.jms.mesage.MailMessage;

@Component
public class MailMessageListener {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Autowired
	MailService mailService;
	
	/*
	 * 下面我们要详细讨论的是如何处理消息，即编写Consumer。从理论上讲，可以创建另一个Java进程来处理消息，但对于我们这个简单的Web程序来说没有必要，直接在同一个Web应用中接收并处理消息即可。
	 * 处理消息的核心代码是编写一个Bean，并在处理方法上标注@JmsListener：
	 * 注意到@JmsListener指定了Queue的名称，因此，凡是发到此Queue的消息都会被这个onMailMessageReceived()方法处理，方法参数是JMS的Message接口，
	 * 我们通过强制转型为TextMessage并提取JSON，反序列化后获得自定义的JavaBean，也就获得了发送邮件所需的所有信息。
	 * 下面问题来了：Spring处理JMS消息的流程是什么？
	 */
	@JmsListener(destination = "jms/queue/mail", concurrency = "10")
	public void onMailMessageReceived(Message message) throws Exception{
		logger.info("received message: " + message);
		if (message instanceof TextMessage) {
			String text = ((TextMessage)message).getText();
			MailMessage mm = objectMapper.readValue(text, MailMessage.class);
			mailService.sendRegistrationMail(mm);
		} else {
			logger.error("unable to process non-text message!");
		}
	}
}
