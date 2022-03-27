package com.yale.test.json;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class BookFirstName {
	public long id;
	public String name;
	//更好的方法就是使用Map代替BookAuthor,这样就不用再创建一个类了
	public BookAuthor author;
	
	public BigInteger isbn;
	
	public List<String> tags;
}
