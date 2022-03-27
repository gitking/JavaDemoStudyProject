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
    "tags": ["Java", 1, 1.99, true, false],
    "tags": [1, 2, 3, 4, 5],
    "pubDate": "2016-09-01",
    "price": 119.5
 * }
 * @author issuser
 */
public class Book {
	public long id;
	public String name;
	public String author;
	
	@JsonDeserialize(using= IsbnDeserializer.class)
	public BigInteger isbn;
	
	//public List<Object> tags;
	//public List<Integer> tags;
	public List<String> tags;
	public LocalDate pubDate;
	public BigDecimal price;
}
