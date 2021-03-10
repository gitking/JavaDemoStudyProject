package com.yale.test.java.meiju;

public class HfjEnum {
	enum Names {
		JERRY("lead guitar"){//必须有对应的带参构造方法
			public String sings() {//相当于覆盖了sings()方法
				return "我JERRY覆盖了sings方法";
			}
		},
		BOBBY("rhythm guitar"){
			public String sings() {
				return "我BOBBY覆盖了sings方法";
			}
		},
		PHIL("bass");
		
		private String instrument;
		
		Names(String instrument){
			this.instrument = instrument;
		}
		
		public String getInstrument() {
			return this.instrument;
		}
		
		public String sings() {
			return "occasionally";
		}
	}
	public static void main(String[] args) {
		for (Names n : Names.values()) {//每个enum都有内置的values(),通常会用在for循环中
			System.out.print(n);
			System.out.print(",instrument: " + n.getInstrument());
			//基本的sings()只会在enum值没有特定内容时才会被调用
			System.out.println(", sings: " + n.sings());
		}
	}
}
