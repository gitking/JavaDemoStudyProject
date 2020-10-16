package com.yale.test.xml.jackson.demo1;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Score {
	@JacksonXmlProperty(localName="acc01")
	private Double langue;
	@JacksonXmlProperty(localName="acc02")
	private Double math;
	@JacksonXmlProperty(localName="acc03")
	private Double english;
	
	public Double getLangue() {
		return langue;
	}
	public void setLangue(Double langue) {
		this.langue = langue;
	}
	public Double getMath() {
		return math;
	}
	public void setMath(Double math) {
		this.math = math;
	}
	public Double getEnglish() {
		return english;
	}
	public void setEnglish(Double english) {
		this.english = english;
	}
	
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}
