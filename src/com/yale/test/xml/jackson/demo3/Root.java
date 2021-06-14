package com.yale.test.xml.jackson.demo3;

public class Root {
	private AttrMap<String, Object> extendInfos;

	public AttrMap<String, Object> getExtendInfos() {
		return extendInfos;
	}

	public void setExtendInfos(AttrMap<String, Object> extendInfos) {
		this.extendInfos = extendInfos;
	}
	
	@Override
	public String toString() {
		return extendInfos.toString();
	}
}
