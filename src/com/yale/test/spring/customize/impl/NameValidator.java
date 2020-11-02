package com.yale.test.spring.customize.impl;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.yale.test.spring.customize.Validator;

@Component
@Order(3)
public class NameValidator implements Validator{
	@Override
	public void validate(String email, String password, String name) {
		if (name == null || name.isEmpty() || name.length() > 20) {
			throw new IllegalArgumentException("invalid name: " + name);
		}
	}
}
