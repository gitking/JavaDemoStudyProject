package com.yale.test.java.fanshe.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

interface IFruit {
	public void eat() ;
}

class Apple implements IFruit {
	@Override
	public void eat() {
		System.out.println("吃苹果");
	}
}
@Retention(RetentionPolicy.RUNTIME)
@interface FactoryAnno {
	public Class<?> target();
}
@FactoryAnno(target = Apple.class)
class Factory {
	public static <T> T getInstance() throws InstantiationException, IllegalAccessException {
		FactoryAnno fa = Factory.class.getDeclaredAnnotation(FactoryAnno.class);
		return (T)fa.target().newInstance();
	}
}
public class FactoryAnnotation {
	public static void main(String[] args) throws InstantiationException, IllegalAccessException {
		IFruit fruit = Factory.getInstance();
		fruit.eat();
	}
}
