package com.yale.test.thread.heima.zhangxiaoxiang;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class CollectionModifyException {
	public static void main(String[] args) {
		Collection users = new ArrayList();
		//Collection users = new CopyOnWriteArrayList<User>();CopyOnWriteArrayList是juc包下面的并发List,线程安全的
		users.add(new User("张三", 28));
		users.add(new User("李四", 25));
		users.add(new User("王五", 31));
		Iterator itrUsers = users.iterator();//Iterator是一个接口,users.iterator()返回的是是ArrayList的一个内部类Itr,
		while (itrUsers.hasNext()) {
			System.out.println("这里会输出好几次");
			User user = (User)itrUsers.next();
			if ("王五".equals(user.getName())) {//这里换成李四就不会报错,张三就会报错
				users.remove(user);//这里会出现错误
				//itrUsers.remove();
			} else {
				System.out.println(user);
			}
		}
	}
}
