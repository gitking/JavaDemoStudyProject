package com.yale.test.json;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

//https://www.liaoxuefeng.com/wiki/1252599548343744/1320418650619938
public class BookFirstNameMap {
	public long id;
	public String name;
	public Map<String, String> author;
	
	public BigInteger isbn;
	
	public List<String> tags;
}
