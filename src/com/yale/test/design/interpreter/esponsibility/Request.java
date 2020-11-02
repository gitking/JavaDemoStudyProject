package com.yale.test.design.interpreter.esponsibility;

import java.math.BigDecimal;

public class Request {
	private String name;
	private BigDecimal amount;
	
	public Request(String name, BigDecimal amount) {
		this.name = name;
		this.amount = amount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	@Override
	public String toString() {
		return String.format("{Request: name=%s, amount=%s}", name, amount);
	}
}
