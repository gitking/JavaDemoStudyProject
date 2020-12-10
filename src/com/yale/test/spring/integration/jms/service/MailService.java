package com.yale.test.spring.integration.jms.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.yale.test.spring.integration.jms.mesage.MailMessage;

@Component
public class MailService {
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	public void sendRegistrationMail(MailMessage mm){
		logger.info("[send mail] sending registration mail to {}...", mm.email);
		//TODO: simulate a long-time task;
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		logger.info("[send mail] registration mail was sent to {}.", mm.email);
	}
}
