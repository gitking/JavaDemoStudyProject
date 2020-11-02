package com.yale.test.java.meiju.javaguide;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

/*
 * 9. Enum 类型的 JSON 表现形式
 * 使用Jackson库，可以将枚举类型的JSON表示为POJO。下面的代码段显示了可以用于同一目的的Jackson批注：
 * https://github.com/Snailclimb/JavaGuide/blob/master/docs/java/basis/%E7%94%A8%E5%A5%BDJava%E4%B8%AD%E7%9A%84%E6%9E%9A%E4%B8%BE%E7%9C%9F%E7%9A%84%E6%B2%A1%E6%9C%89%E9%82%A3%E4%B9%88%E7%AE%80%E5%8D%95.md
 * 有关枚举类型的JSON序列化/反序列化（包括自定义）的更多信息，请参阅Jackson-将枚举序列化为JSON对象(https://www.baeldung.com/jackson-serialize-enums)。
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum EnumJson {
	ORDERED(5) {
		@Override
		public boolean isOrdered() {
			return true;
		}
	},
	READY(2) {
		@Override
		public boolean isReady() {
			return true;
		}
	},
	DELIVERED(0) {
		@Override
		public boolean isDelivered() {
			return true;
		}
	};
	
	public boolean isOrdered() {
		return false;
	}
	
	public boolean isReady() {
		return false;
	}
	
	public boolean isDelivered(){
		return false;
	}
	
	private int timeToDelivery;
	
	@JsonProperty("timeToDelivery")
	public int getTimeToDelivery() {
		return timeToDelivery;
	}
	private EnumJson(int timeToDelivery){
		this.timeToDelivery = timeToDelivery;
	}
	
	public static void main(String[] args) {
		
	}
}
