package com.yale.test.java.fanshe.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ObjectUtils {
	private ObjectUtils(){}
	/**
	 * wrap是包装的意思
	 * 
	 * @param wrapObject
	 * @param attribute
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
	 * @throws NoSuchMethodException 
	 * @throws InstantiationException 
	 */
	public static Object getObject(Object wrapObject, String attribute) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException, SecurityException, NoSuchMethodException, InstantiationException {
		String methodName = "get" + StringUtils.initcap(attribute);
		Field field = getObjectField(wrapObject, attribute);
		if (field == null){
			return null;
		}
		Method method = wrapObject.getClass().getMethod(methodName);
		Object obj = method.invoke(wrapObject);
		if (obj == null) {
			System.out.println("注意这里得到是Class对象" + field.getType());
			obj = field.getType().newInstance();//实例化类的对象
			setObjectValue(wrapObject, attribute, obj);
			
		}
		return obj;
	}
	
	/**
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
	 * @throws NoSuchMethodException 
	 */
	public static void setObjectValue(Object wrapObject, String attribute, Object value) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException, SecurityException, NoSuchMethodException {
		Field field = getObjectField(wrapObject, attribute);
		if (field == null){
			return;
		}
		String methodName = "set" + StringUtils.initcap(attribute);
		Method method = wrapObject.getClass().getMethod(methodName, field.getType());
		method.invoke(wrapObject, value);
	}
	
	public static Field getObjectField(Object wrapObject, String attribute) throws NoSuchFieldException, SecurityException {
		Field field = wrapObject.getClass().getDeclaredField(attribute);
		if (field == null) {
			field = wrapObject.getClass().getField(attribute);
		}
		return field;
	}
}
