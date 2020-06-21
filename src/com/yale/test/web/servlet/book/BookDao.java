package com.yale.test.web.servlet.book;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

public class BookDao {
	private QueryRunner qr = null;//new TxQueryRunner();这个类是itcast的jar包里面的类
	
	public List<Book> findAll() throws SQLException{
		String sql = " select * from t_book ";
		//return qr.query(sql, new BeanListHandler<Book>(Book.class));
		List<Book> bookList = new ArrayList<Book>();
		Book book = new Book("JAVASE1", 10.0, 1);
		Book book1 = new Book("JAVASE2", 15.0, 1);
		Book book2 = new Book("JAVASE3", 20.0, 1);
		Book book3 = new Book("JAVASE4", 25.0, 1);
		Book book4 = new Book("JAVAEE1", 30.0, 2);
		Book book5 = new Book("JAVAEE2", 35.0, 2);
		Book book6 = new Book("JAVAEE3", 40.0, 2);
		Book book7 = new Book("Java_Framework_1", 45.0, 3);
		Book book8 = new Book("Java_Framework_2", 50.0, 3);
		Collections.addAll(bookList, book, book1, book2, book3, book4, book5, book6, book7, book8);
		return bookList;
	}
	
	public List<Book> findByCategory(int category) throws SQLException {
		String sql = "select * from t_book where category = ? ";
		//return qr.query(sql, new BeanListHandler<Book>(Book.class), category);
		
		List<Book> bookList = new ArrayList<Book>();
		if (category == 1) {
			Book book = new Book("JAVASE1", 10.0, 1);
			Book book1 = new Book("JAVASE2", 15.0, 1);
			Book book2 = new Book("JAVASE3", 20.0, 1);
			Book book3 = new Book("JAVASE4", 25.0, 1);
			Collections.addAll(bookList, book, book1, book2, book3);
		} else if (category == 2) {
			Book book4 = new Book("JAVAEE1", 30.0, 2);
			Book book5 = new Book("JAVAEE2", 35.0, 2);
			Book book6 = new Book("JAVAEE3", 40.0, 2);
			Collections.addAll(bookList, book4, book5, book6);
		} else if (category == 3) {
			Book book7 = new Book("Java_Framework_1", 45.0, 3);
			Book book8 = new Book("Java_Framework_2", 50.0, 3);
			Collections.addAll(bookList, book7, book8);
		}
		return bookList;
	}
}
