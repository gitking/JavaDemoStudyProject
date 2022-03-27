package com.yale.test.json;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * book.json
 * {
    "id": 1,
    "name": "Java核心技术",
    "author": "Cay S. Horstmann",
    "isbn": "978-7-111-54742-6",
    "tags": ["Java", "Network"],
    "pubDate": "2016-09-01",
    "price": 119.5
 * }
 * @author issuser
 */
public class BookLArr {
	public long id;
	public String name;
	public String author;
	
	@JsonDeserialize(using= IsbnDeserializer.class)
	public BigInteger isbn;
	
	//public List<Object> tags;
	//public List<Integer> tags;
	public String[] tags;
	public LocalDate pubDate;
	public BigDecimal price;
}
