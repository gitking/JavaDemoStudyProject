package com.yale.test.java.meiju;

/**
 * 这个叫多例模式,多例模式跟枚举其实是类似的
 * @author dell
 */
public class Color {
	private static final Color RED = new Color("RED");
	private static final Color BLUE = new Color("BLUE");
	private static final Color GREEN = new Color("GREEN");
	private String title;
	public Color(String title) {
		this.title = title;
	}
	public static Color getInstance(int ch) {
		switch(ch) {
			case 0: return RED;
			case 1: return GREEN;
			case 2: return BLUE;
			default : return null;
		}
	}
	
	@Override
	public String toString() {
		return this.title;
	}
	public static void main(String[] args) {
		System.out.println("多例设计模式:" + Color.getInstance(0));
	}
}
