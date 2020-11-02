package com.yale.test.design.graphic.factory.homework;

import java.time.LocalDate;

public class Main {
	public static void main(String[] args) {
		LocalDate ld = LocalDateFactory.fromInt(20200202);
		System.out.println(ld);
		LocalDate ld2 = LocalDateFactory.fromInt(20200202);
		System.out.println(ld == ld2);
	}
}
