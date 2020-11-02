package com.yale.test.java.meiju.javaguide;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

/*
 * 6.EnumSet and EnumMap
 * EnumSet 是一种专门为枚举类型所设计的 Set 类型。
 * 与HashSet相比，由于使用了内部位向量表示，因此它是特定 Enum 常量集的非常有效且紧凑的表示形式。
 * 它提供了类型安全的替代方法，以替代传统的基于int的“位标志”，使我们能够编写更易读和易于维护的简洁代码。
 * EnumSet 是抽象类，其有两个实现：RegularEnumSet 、JumboEnumSet，选择哪一个取决于实例化时枚举中常量的数量。
 * 在很多场景中的枚举常量集合操作（如：取子集、增加、删除、containsAll和removeAll批操作）使用EnumSet非常合适；如果需要迭代所有可能的常量则使用Enum.values()。
 */
public class EnumSetDemo {
	
	private static EnumSet<PizzaStatus> undeliveredPizzaStatus = EnumSet.of(PizzaStatus.ORDERED, PizzaStatus.READY);
	private PizzaStatus status;
	public enum PizzaStatus {
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
		System.out.println("Time to delivery is " + this.getStatus().getTimeToDelivery() + " days ");
	}
	
	public static List<EnumSetDemo> getAllUndeliveredPizzas(List<EnumSetDemo> input) {
		return input.stream().filter((s)->undeliveredPizzaStatus.contains(s.getStatus())).collect(Collectors.toList());
	}
	
	public void deliver () {
		if (isDeliverable()) {
			
		}
	}

	public PizzaStatus getStatus() {
		return status;
	}

	public void setStatus(PizzaStatus status) {
		this.status = status;
	}
	
	//下面的测试演示了展示了 EnumSet 在某些场景下的强大功能：
	public static void main(String[] args) {
		List<EnumSetDemo> pzList = new ArrayList<>();
		EnumSetDemo esd = new EnumSetDemo();
		esd.setStatus(EnumSetDemo.PizzaStatus.ORDERED);
		
		EnumSetDemo esd1 = new EnumSetDemo();
		esd1.setStatus(EnumSetDemo.PizzaStatus.ORDERED);
		
		EnumSetDemo esd2 = new EnumSetDemo();
		esd2.setStatus(EnumSetDemo.PizzaStatus.DELIVERED);
		
		EnumSetDemo esd3 = new EnumSetDemo();
		esd3.setStatus(EnumSetDemo.PizzaStatus.READY);
		
		pzList.add(esd);
		pzList.add(esd1);
		pzList.add(esd2);
		pzList.add(esd3);
		
		List<EnumSetDemo> undeliveredPzs = EnumSetDemo.getAllUndeliveredPizzas(pzList);
		System.out.println(undeliveredPzs.size() == 3);
	}
}
