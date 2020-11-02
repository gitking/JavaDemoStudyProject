package com.yale.test.design.graphic.builder;

public class HeadingBuilder {
	public String buildHeading(String line) {
		int n=0;
		while(line.charAt(0) == '#'){
			n++;
			line = line.substring(1);
		}
		//return String.format("<h%d>%s</h%d>", n, line.strip(), n);
		return String.format("<h%d>%s</h%d>", n, "", n);
	}
}
