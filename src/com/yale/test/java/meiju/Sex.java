package com.yale.test.java.meiju;

interface IColor {
	public String getColor();
}
/**
 * 枚举类就是类多例模式的实现
 * enum就是一种特殊的类,enum会隐含继承java.lang.Enum
 * 从java1.5开始有了枚举
 */
public enum Sex implements IColor{
	MAN("男","王"), WOMAN("女","张");//这行代码必须写在第一行,其实就是枚举类Sex的对象,这里实际上调用的是Sex的构造方法
	
	private final String name;//注意种类虽然跟父类的Enum.class里面的属性一样,但是属性是不会覆盖父类的
	private final String value;//注意：枚举类的字段也可以是非final类型，即可以在运行期修改，但是不推荐这样做！
	//enum的构造方法要声明为private，字段强烈建议声明为final；
	//https://www.liaoxuefeng.com/wiki/1252599548343744/1260473188087424
	private Sex(String value,String name){//枚举就是多例,构造方法绝对不能使用public
		this.value = value;
		this.name = name;
	}
	
//	public final String name() {这个方法不能编译通过,因为这个name()方法在Enum.class类里面是final方法,但是可以用name属性
//		return "";
//	}
	
//	public int ordinal() {这个方法也不能编译通过,因为这个ordinal()方法在Enum.class类里面是final方法
//		return 0;
//	}
	
	public void print(){
		System.out.println("枚举类就是类多例模式的实现,所以枚举类不能用有公开构造方法.枚举类也可以有自己方法.");
	}
	
	@Override
	public String getColor() {
		System.out.println("枚举类还可以实现接口");
		return null;
	}
	@Override
	public String toString() {
		return this.value;
	}
	
	public static void main(String[] args) {
		Sex.MAN.print();
		System.out.println(Sex.MAN);
		System.out.println(Sex.WOMAN);
		//默认情况下，对枚举常量调用toString()会返回和name()一样的字符串。但是，toString()可以被覆写，
		//而name()则不行,因为name()方法是父类Enum.class的final方法,子类是覆写不了的。判断枚举常量的名字，要始终使用name()方法，绝不能调用toString()！
		//枚举类可以应用在switch语句中。因为枚举类天生具有类型信息和有限个枚举常量，所以比int、String类型更适合用在switch语句中
		System.out.println(Sex.MAN.name() + "," + Sex.MAN.ordinal());
		for (Sex temp : Sex.values()) {
			System.out.println(temp.name() + "," + temp.ordinal());
		}
		
		IColor ic = Sex.MAN;
		ic.getColor();
		
		Sex sex = Sex.MAN;
		switch(sex) {
			case MAN:
				System.out.println("男人");
				break;
			case WOMAN:
				System.out.println("女人");
				break;
			default://加上default语句，可以在漏写某个枚举常量时自动报错，从而及时发现错误。
				throw new RuntimeException("cannot process " + sex);
		}
	}
}
