package com.yale.test.java.meiju;

/**
 * 枚举类就是类多例模式的实现
 */
public enum Sex {
	MAN("男"),WOMAN("女");//这行代码必须写在第一行,其实就是枚举类Sex的对象
	
	private String value;
	private Sex(String value){
		this.value = value;
	}
	
	public void print(){
		System.out.println("枚举类就是类多例模式的实现,所以枚举类不能用有公开构造方法.枚举类也可以有自己方法.");
	}
	@Override
	public String toString() {
		return this.value;
	}
	
	public static void main(String[] args) {
		Sex.MAN.print();
		System.out.println(Sex.MAN);
		System.out.println(Sex.WOMAN);
		System.out.println(Sex.MAN.name() + "," + Sex.MAN.ordinal());
		for (Sex temp : Sex.values()) {
			System.out.println(temp.name() + "," + temp.ordinal());
		}
	}
}
