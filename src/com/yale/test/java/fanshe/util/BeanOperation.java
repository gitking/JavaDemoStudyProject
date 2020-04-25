package com.yale.test.java.fanshe.util;

import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat.Field;

public class BeanOperation {
	private BeanOperation(){}
	public static void setBeanValue(Object actionObject, String msg) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException, SecurityException, NoSuchMethodException, InstantiationException {
		String[] result = msg.split("\\|");
		for (int i=0; i<result.length; i++) {
			String[] temp = result[i].split(":", 2);
			String attribute = temp[0];
			String[] fields = attribute.split("\\.");
			if(fields.length > 2) {//多级配置
				Object currentObj = actionObject;
				for (int y=0;y<fields.length-1; y++) {
					currentObj = ObjectUtils.getObject(currentObj, fields[y]);
				}
				Object value = ObjectValueUtils.getValue(currentObj, fields[fields.length - 1], temp[1]);
				ObjectUtils.setObjectValue(currentObj, fields[fields.length-1], value);
			} else {
				Object currentObject = ObjectUtils.getObject(actionObject, fields[0]);
				Object value = ObjectValueUtils.getValue(currentObject, fields[1], temp[1]);
				ObjectUtils.setObjectValue(currentObject, fields[1], value);
			}
		}
	}

}
