package com.yale.test.mybatis.test;

import java.io.IOException;
import java.util.List;

import com.yale.test.mybatis.dao.UserDao;
import com.yale.test.mybatis.vo.User;

public class Test {
	public static void main(String[] args) throws IOException {
//		SqlSession session = MyBatisUtil.getSession();
//		User user = session.selectOne("com.yale.test.mybatis.vo.UserMapper.selectUser", 1);
//		System.out.println("id=" + user.getId() + ",name=" + user.getName());
//		session.close();
		UserDao ud = new UserDao();
		User user = ud.getById(2);
		System.out.println("id=" + user.getId() + ",name=" + user.getName());
		
		user.setPwd("99990");
		int upd = ud.update(user);
		System.out.println("将查出来的对象名字更新一下:" +  upd);

		User userAdd = new User();
		userAdd.setId(4);
		userAdd.setName("mybatis");
		userAdd.setPwd("9898");
		int result = ud.add(userAdd);
		System.out.println("插入结果:" +  result);
		
		int del = ud.delete(1);
		System.out.println("删除是否成功:" + del);
		
		List<User> userList = ud.getAll();
		for (User userT : userList) {
			System.out.println(userT);
		}
	}
}
