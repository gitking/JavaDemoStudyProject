package com.yale.test.spring.customize.impl;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import com.yale.test.spring.customize.Validator;

@Component
@Order(2)
public class PasswordValidator implements Validator{
	@Override
	public void validate(String email, String password, String name) {
		if (!password.matches("^.{6,20}$")) {
			throw new IllegalArgumentException("invalid password");
		}
	}
}
