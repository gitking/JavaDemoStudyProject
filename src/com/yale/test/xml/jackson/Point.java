package com.yale.test.xml.jackson;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/*
 * 使用Jackson将Point对象变成这样的xml：<point x="100" y="100" z="100"/>
 */
public class Point {
	private int x;
	private int y;
	private int z;
	
	public Point(int x, int y, int z) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
	}
	@JacksonXmlProperty(isAttribute=true)
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	@JacksonXmlProperty(isAttribute=true)
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	@JacksonXmlProperty(isAttribute=true)
	public int getZ() {
		return z;
	}
	public void setZ(int z) {
		this.z = z;
	}
	
	public static void main(String[] args) throws JsonGenerationException, JsonMappingException, IOException {
		File file = new File("PointText.xml");
		System.out.println(file.getAbsolutePath());
		XmlMapper xmlMapper = new XmlMapper();
		xmlMapper.writeValue(file, new Point(100, 200, 300));
	}
}
