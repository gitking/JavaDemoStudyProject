package com.yale.test.java.fanxing.ref;

import java.lang.reflect.Array;
import java.util.Arrays;

import com.yale.test.java.fanxing.Pair;

/**
 * https://www.liaoxuefeng.com/wiki/1252599548343744/1265105940850016
 * @author dell
 * @param <T>
 */
public class ArrayHelper2<T> {
	public static void main(String[] args) {
		ArrayHelper2 ah = new ArrayHelper2();
		Pair<String>[] parr = (Pair<String>[])ah.createArray(Pair.class);
		System.out.println(parr.getClass() == Pair[].class);
	}
	
	//编译错误
//	T[] createArray() {
//        return new T[5];
//    }
	
	//必须借助Class<T>来创建泛型数组：
	//可以通过Array.newInstance(Class<T>, int)创建T[]数组，需要强制转型；
	T[] createArray(Class<T> cls) {
		return (T[])Array.newInstance(cls, 5);
	}
}
