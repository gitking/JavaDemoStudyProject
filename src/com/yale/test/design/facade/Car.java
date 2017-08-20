package com.yale.design.facade;

/**
 * 最少知识原则:就任何对象而言,在该对象的方法内,我们只应该调用属于以下范围的方法:
 * 1,该对象本身
 * 2,被当做方法的参数而传递进来的对象
 * 3,此方法所创建或实例化的任何对象
 * 4,对象的任何组件
 * @author lenovo
 *
 */
public class Car {
	Engine engine;//这是这个类的组件,我们能够调用的方法
	public Car(){
		
	}
	
	public void start(Key key){//被当作参数传进来的对象,它的方法也可以调用
		Doors doors = new Doors();//在这里我们创建了一个新的对象,它的方法可以被调用
		boolean authorized = key.turns();
		if(authorized){
			engine.start();//类的组件的方法也可以调用
			updateDashboardDisplay();//可以调用同一个对象内的本地方法,可以调用你所创建或实例化的对象的方法
			doors.lock();
		}
	}
	
	public void updateDashboardDisplay(){
		
	}
}
