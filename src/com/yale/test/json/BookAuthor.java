package com.yale.test.json;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

//https://www.liaoxuefeng.com/wiki/1252599548343744/1320418650619938
//更好的方法就是使用Map代替BookAuthor,这样就不用再创建一个类了
public class BookAuthor {
	public String firstName;
	public String lastName;
}
	
