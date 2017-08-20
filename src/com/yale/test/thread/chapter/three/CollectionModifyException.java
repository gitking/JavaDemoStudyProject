package com.yale.test.thread.chapter.three;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class CollectionModifyException {

	public static void main(String[] args) {
		try {
			Collection list = new ArrayList();
			list.add("1");
			list.add("2");
			list.add("3");
			Iterator it =list.iterator();
			while(it.hasNext()){
				String sd = (String)it.next();
				if("3".equals(sd)){
					list.remove(sd);//在迭代list的时候删除list,会发生异常
				} else {
					System.out.println(sd);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			Collection listCopy = new CopyOnWriteArrayList();//用这里List就不会发生异常
			listCopy.add("1");
			listCopy.add("2");
			listCopy.add("3");
			Iterator itSec =listCopy.iterator();
			while(itSec.hasNext()){
				String sd = (String)itSec.next();
				if("3".equals(sd)){
					listCopy.remove(sd);
				} else {
					System.out.println(sd);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
