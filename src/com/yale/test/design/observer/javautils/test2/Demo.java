package com.yale.test.design.observer.javautils.test2;

import java.util.Observable;
import java.util.Observer;
/**
 * 观察者模式
 * @author lenovo
 *
 */
class House extends Observable{
	private Double price;//房子价钱
	
	public House (Double price) {
		this.price = price;
	}
	
	public void setPrice (Double price) {
		if (this.price < price) {//如果房价上涨,则通知所有观察者
			super.setChanged();//设置状态
			super.notifyObservers(price);//通知所有观察者,并将发生变化的price通知给所有观察者
		}
		this.price = price;
	}
}
class Person implements Observer{

	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof House) {//人们只关注房子,别的东西变了,人们不关心
			if (arg instanceof Double) {
				System.out.println("我被通知房价上涨了,我得赶紧卖房子啊");
			}
		}
	}
}
public class Demo {
	public static void main(String[] args) {
		Person perA = new Person();
		Person perB = new Person();
		Person perC = new Person();
		House house = new House(8000d);
		house.addObserver(perA);//将所有观察者添加进来
		house.addObserver(perB);//将所有观察者添加进来
		house.addObserver(perC);//将所有观察者添加进来
		house.setPrice(900d);
		System.out.println("************房价便宜了没人管***************");
		house.setPrice(1000000d);
	}
}
