package com.yale.test.spring.customize.impl;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.yale.test.spring.customize.Validator;

@Component
@Order(1)
public class EmailValidator implements Validator{
	@Override
	public void validate(String email, String password, String name) {
		if (!email.matches("^[a-z0-9]+\\@[a-z0-9]+\\.[a-z]{2,10}$")) {
			throw new IllegalArgumentException("invalid email: " + email);
		}
	}
}
