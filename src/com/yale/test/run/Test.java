package com.yale.test.run;

public class Test {

	public static void main(String[] args) {
		RunntimeTest sdf = new RunntimeTest();
		try {
			sdf.finalize();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

}
