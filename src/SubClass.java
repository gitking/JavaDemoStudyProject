
public class SubClass extends Father {
	String name;
	public void study() {
		this.name = "学习";
		System.out.println(this.name);
	}
	
	public void money() {
		this.name = "没钱";
		System.out.println(this.name);
	}
	public static void main(String[] args) {
		SubClass sub = new SubClass();
		sub.money();
		sub.study();
		
		Father fa = new Father();
		fa.money();
		
		Father faSec = (Father)sub;
		faSec.name = "父类";
		System.out.println(">>>" + faSec.name);
		faSec.money();
		
		//SubClass subSec = (SubClass)fa;
		//subSec.money();
		//subSec.study();
		
		Father faSecT = new SubClass();
		faSecT.money();
		
		//SubClass.test(subSec);
	}
	
	public static void test(Father sub){
		sub.money();
	}
}
