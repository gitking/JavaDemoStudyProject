package com.yale.test.java.meiju.javaguide;

/*
 * 5.枚举类型的属性,方法和构造函数
 * 你可以通过在枚举类型中定义属性,方法和构造函数让它变得更加强大。
 * 下面，让我们扩展上面的示例，实现从比萨的一个阶段到另一个阶段的过渡，并了解如何摆脱之前使用的if语句和switch语句：
 */
public class Pizza1 {
	
	private PizzaStatus status;
	public enum PizzaStatus{
		ORDERED(5){
			@Override
			public boolean isOrdered() {
				return true;
			}
		},
		READY(2) {
			@Override
			public boolean isReady() {
				System.out.println("我什么时候被调用,我重写的是谁的方法");
				return true;
			}
			
			@Override
			public boolean isOrdered() {
				System.out.println("还能重写多个方法?这里的父类super是谁");
				System.out.println(super.getClass());
				return super.isOrdered();
			}
		},
		DELIVERED(0) {
			@Override
			public boolean isDelivered() {
				return true;
			}
		};
		
		public boolean isDelivered() {
			return false;
		}
		
		private int timeToDelivery;
		
		PizzaStatus(int timeToDelivery) {
			this.timeToDelivery = timeToDelivery;
		}
		
		public boolean isOrdered() {
			return false;
		}
		
		public boolean isReady(){
			return false;
		}
		
		public int getTimeToDelivery() {
			return timeToDelivery;
		}
	}
	
	public boolean isDeliverable() {
		return this.status.isReady();
	}

	public void printTimeToDeliver() {
		System.out.println("Time to delivery is " + this.getStatus().getTimeToDelivery());
	}

	public PizzaStatus getStatus() {
		return status;
	}

	public void setStatus(PizzaStatus status) {
		this.status = status;
	}
	
	public static void main(String[] args) {
		Pizza1 p1 = new Pizza1();
		p1.setStatus(Pizza1.PizzaStatus.READY);
		System.out.println(p1.isDeliverable());
		System.out.println(Pizza1.PizzaStatus.READY.isReady());
		System.out.println(Pizza1.PizzaStatus.READY.isOrdered());
	}
}
