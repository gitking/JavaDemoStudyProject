package com.yale.test.java.meiju.javaguide;

import com.yale.test.java.meiju.Sex;

/*
 * 3.使用 == 比较枚举类型
 * 由于枚举类型确保JVM中仅存在一个常量实例，因此我们可以安全地使用 == 运算符比较两个变量，如上例所示；此外，== 运算符可提供编译时和运行时的安全性。
 * 首先，让我们看一下以下代码段中的运行时安全性，其中 == 运算符用于比较状态，并且如果两个值均为null 都不会引发 NullPointerException。
 * 相反，如果使用equals方法，将抛出 NullPointerException：
 */
public class Pizza {
	private PizzaStatus status;
	
	public enum PizzaStatus {
		ORDERED,
		READY,
		DELIVERED;
	}

	public PizzaStatus getStatus() {
		return status;
	}

	public void setStatus(PizzaStatus status) {
		this.status = status;
	}
	
	public boolean isDeliverable() {
		return getStatus() == PizzaStatus.READY;
	}
	
	//4.在 switch 语句中使用枚举类型
	public int getDeliveryTimeInDays() {
		switch(status) {
		case ORDERED:
			return 5;
		case READY:
			return 2;
		case DELIVERED:
			return 0;
		}
		return 0;
	}
	
	public static void main(String[] args) {
		Pizza testPz = new Pizza();
		/*
		 * 由于枚举类型确保JVM中仅存在一个常量实例，因此我们可以安全地使用 == 运算符比较两个变量，如上例所示；此外，== 运算符可提供编译时和运行时的安全性。
		 * 首先，让我们看一下以下代码段中的运行时安全性，其中 == 运算符用于比较状态，并且如果两个值均为null 都不会引发 NullPointerException。相反，如果使用equals方法，将抛出 NullPointerException：
		 */
		if(testPz.getStatus().equals(Pizza.PizzaStatus.DELIVERED)) {
			
		}
		if(testPz.getStatus() == Pizza.PizzaStatus.DELIVERED) {
			
		}
		
	
		/*
		 * 对于编译时安全性，我们看另一个示例，两个不同枚举类型进行比较，使用equal方法比较结果确定为true，因为getStatus方法的枚举值与另一个类型枚举值一致，但逻辑上应该为false。
		 * 这个问题可以使用==操作符避免。因为编译器会表示类型不兼容错误：
		 */
		if (testPz.getStatus().equals(Sex.MAN)) {
			System.out.println("这里编译不会报错");
		}
//		if (testPz.getStatus() == Sex.MAN) {//编译报错
//			
//		}
		//在 switch 语句中使用枚举类型
	}
}
