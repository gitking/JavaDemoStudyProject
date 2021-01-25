package com.yale.test.java.fanxing.headfristjava;

import java.util.ArrayList;
import java.util.List;

public class TestGenerics1 {
	public static void main(String[] args) {
		new TestGenerics1().go();
	}
	public void go() {
		Animal[] animals = new Animal[]{new Dog(), new Cat()};
		takeAnimals(animals);//传入Animal数组也可以
		
		Dog[] dogs = new Dog[]{new Dog(), new Dog(),new Dog()};
		takeAnimals(dogs);//传入Dog数组也可以,注意编译不会报错,但是运行报不报错,就看你的代码写的安不安全了
		
		ArrayList<Animal> animalList = new ArrayList<Animal>();
		animalList.add(new Dog());
		animalList.add(new Cat());
		animalList.add(new Dog());
		takeAnimalList(animalList);//这个可以正常编译通过,也可以正常运行
		
		ArrayList<Dog> dogList = new ArrayList<Dog>();
		dogList.add(new Dog());
		dogList.add(new Dog());
		dogList.add(new Dog());
		//takeAnimalList(dogList);//注意这个直接编译都不会通过,非常安全,可以保证你写出安全的代码
		
		takeAnimalList2(animalList);//添加ArrayList<Animal>类型的参数也可以
		takeAnimalList2(dogList);//添加ArrayList<Dog>类型的参数也可以
	}

	/*
	 * 如果方法的参数是Animal的数组,它也能够取用Animal次类型的数组。
	 * 也就是说,如果方法是这样声明的:void takeAnimals (Animal[] animals)  
	 * 若Dog有extends过Animal,你就可以用下列的俩种方式调用:
	 * takeAnimals(anAnimalArray)
	 * takeAnimals(aDogArray)
	 * 因为dog是一个Animal,多态在此发挥作用.
	 * 但是注意这样是不安全的,要怎么说泛型是一种安全的数据类型呢？
	 */
	public void takeAnimals (Animal[] animals) {
		for (Animal a : animals) {
			a.eat();
		}
		
		/*
		 * 当你传入的参数为Dog数组的时候,这行代码就会报错。java.lang.ArrayStoreException
		 * 注意这行代码在代码运行期间可能报错,如果你没有仔细测试的话,这行代码很可能就会在生产环境运行的时候报错
		 * 所以说数组是不安全的类型,泛型是安全的类型,泛型在编译期间就会报错,可以保证错误的代码不会跑到生产环境上面。
		 */
		animals[1] = new Cat();
	}
	
	/*
	 * 注意这个方法的参数是List<T>泛型,泛型是一种安全的数据类型
	 * void takeAnimalList(List<Animal> animalList)
	 * 如果传入ArrayList<Dog>可以用吗?答案是不可以,因为不安全
	 * 怎么不安全,如果真让你传ArrayList<Dog>进来,你在方法里面
	 * 往ArrayList<Dog>里面加入new Cat()对象怎么办？要知道Dog跟Cat可没有任何继承关系
	 */
	public void takeAnimalList(List<Animal> animalList) {
		for (Animal al : animalList) {
			al.eat();
		}
	}
	
	/**
	 * 使用? extends Animal 这种泛型就可以传入List<Animal>和List<Dog>这俩种参数了
	 * 注意使用带有<?>的声明时,编译器不会让你加入任何东西到集合中,你可以调用animals中任何元素的方法,但不能往集合里面加入元素
	 * 记住<? extends Animal>此处的extends同时代表继承和实现
	 * 注意:
	 * public <T extends Animal> void takeAnimalList2(ArrayList<T> list);
	 * 跟这一行是一样的
	 * public void takeAnimalList2(List<? extends Animal> animals)
	 * 问:如果都一样,为什么要用有问号的那个?
	 * 答:这要看你是否会使用到T来决定。举例来说,如果方法有俩个参数--都是继承Animal的集合会怎么样?
	 * 此时,只声明一次会比较有效率。
	 * public <T extends Animal> void takeAnimalList2(ArrayList<T> one, ArrayList<T> two);
	 * 而不必这样
	 * public void takeAnimalList2(ArrayList<? extends Animal> one, ArrayList<? extends Animal> two);
	 * 《Head First Java》
	 * @param animals
	 */
	public void takeAnimalList2(List<? extends Animal> animals) {
		for (Animal al : animals) {
			al.eat();
			if(al instanceof Dog) {
				System.out.println("安全泛型多态参数");
				((Dog)al).bark();
			}
		}
		animals.add(null);//这个倒是可以添加
		//animals.add(new Dog());//编译不通过,?不会让你往集合里面添加元素,添加null元素可以
	}
	
	
	abstract class Animal {
		void eat() {
			System.out.println("animal eating");
		}
	}
	
	class Dog extends Animal {
		void bark() {
			System.out.println("狗有一个别的动物都不会的本领：就是看门");
		}
	}
	
	class Cat extends Animal {
		void meow() {
			System.out.println("猫有一个别的动物都不会的本领：撒娇");
		}
	}
}
