package com.yale.test.web.servlet.book;

public class Book {
	private String bid;
	private String bname;
	private double price;
	private int category;
	
	public Book(String bname, double price, int category) {
		this.bname = bname;
		this.price = price;
		this.category = category;
	}
	public String getBid() {
		return bid;
	}
	public void setBid(String bid) {
		this.bid = bid;
	}
	public String getBname() {
		return bname;
	}
	public void setBname(String bname) {
		this.bname = bname;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public int getCategory() {
		return category;
	}
	public void setCategory(int category) {
		this.category = category;
	}
	@Override
	public String toString() {
		return "Book [bid=" + bid + ", bname=" + bname + ", price=" + price + ", category=" + category + "]";
	}
}
