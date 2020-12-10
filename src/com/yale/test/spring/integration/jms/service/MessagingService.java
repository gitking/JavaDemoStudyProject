package com.yale.test.spring.integration.jms.service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yale.test.spring.integration.jms.mesage.MailMessage;

@Component
public class MessagingService {
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Autowired
	JmsTemplate jmsTemplate;
	
	/*
	 * JMS的消息类型支持以下几种：
	 * 1.TextMessage：文本消息；
	 * 2.BytesMessage：二进制消息；
	 * 3.MapMessage：包含多个Key-Value对的消息；
	 * 4.ObjectMessage：直接序列化Java对象的消息；
	 * 5.StreamMessage：一个包含基本类型序列的消息。
	 * 最常用的是发送基于JSON的文本消息，上述代码通过JmsTemplate创建一个TextMessage并发送到名称为jms/queue/mail的Queue。
	 * 注意：Artemis消息服务器默认配置下会自动创建Queue，因此不必手动创建一个名为jms/queue/mail的Queue，但不是所有的消息服务器都会自动创建Queue，生产环境的消息服务器通常会关闭自动创建功能，需要手动创建Queue。
	 * 再注意到MailMessage是我们自己定义的一个JavaBean，真正的JMS消息是创建的TextMessage，它的内容是JSON。
	 * 当用户注册成功后，我们就调用MessagingService.sendMailMessage()发送一条JMS消息，此代码十分简单，这里不再贴出。
	 */
	public void sendMailMessage(MailMessage msg) throws JsonProcessingException {
		String text = objectMapper.writeValueAsString(msg);
		jmsTemplate.send("jms/queue/mail", new MessageCreator(){
			@Override
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage(text);
			}
		});
	}
}
