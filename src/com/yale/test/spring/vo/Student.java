package com.yale.test.spring.vo;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class Student {
	private String name;
	private int age;
	private String[] books;//Spring数组注入
	private List<String> hobbies;//爱好
	private Map<String, String> cards;//SpringMap注入
	private Set<String> games;
	private String wifi;//Spring还可以注入一个null值进来
	private Properties info;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String[] getBooks() {
		return books;
	}
	public void setBooks(String[] books) {
		this.books = books;
	}
	
	public List<String> getHobbies() {
		return hobbies;
	}
	public void setHobbies(List<String> hobbies) {
		this.hobbies = hobbies;
	}
	
	public Map<String, String> getCards() {
		return cards;
	}
	public void setCards(Map<String, String> cards) {
		this.cards = cards;
	}
	
	public String getWifi() {
		return wifi;
	}
	public void setWifi(String wifi) {
		this.wifi = wifi;
	}
	public Set<String> getGames() {
		return games;
	}
	public void setGames(Set<String> games) {
		this.games = games;
	}
	
	
	public Properties getInfo() {
		return info;
	}
	public void setInfo(Properties info) {
		this.info = info;
	}
	public void show() {
		System.out.println("姓名:" + this.name + ",年龄:" + this.age + ",读过的书:" + Arrays.toString(this.books) + ",爱好:" + this.hobbies.toString()
		+ ",银行卡:" + this.cards + "游戏:" + this.games + "无线网" + this.wifi + "\n" 
		+ ",学生属性:" + this.info);
	}
}
