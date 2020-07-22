package com.yale.test.mybatis.test;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yale.test.mybatis.dao.UserDao;
import com.yale.test.mybatis.vo.User;

public class Test {
	public static void main(String[] args) throws IOException {
//		SqlSession session = MyBatisUtil.getSession();
//		User user = session.selectOne("com.yale.test.mybatis.vo.UserMapper.selectUser", 1);
//		System.out.println("id=" + user.getId() + ",name=" + user.getName());
//		session.close();
		UserDao ud = new UserDao();
		User user = ud.getById(66);
		System.out.println("id=" + user.getId() + ",name=" + user.getName() + ",password=" + user.getPassword());
		
		User userMap = ud.getByUserMap(66);
		System.out.println("使用别名方式进行查询:id=" + userMap.getId() + ",name=" + userMap.getName() + ",password=" + userMap.getPassword());
		
		List<User> userListPage = ud.getAllByPage(1, 5);
		for (User userP : userListPage){
			System.out.println("使用分页方式进行查询:id=" + userP.getId() + ",name=" + userP.getName() + ",password=" + userP.getPassword());
		}
		
		System.out.println("RowBounds实现分页查询------------");
		List<User> userListPage2 = ud.getAllByPageRowBounds(2, 5);
		for (User userP : userListPage2){
			System.out.println("RowBounds实现分页查询:id=" + userP.getId() + ",name=" + userP.getName() + ",password=" + userP.getPassword());
		}
		
		
		user.setPassword("9999110");
		int upd = ud.update(user);
		System.out.println("将查出来的对象名字更新一下:" +  upd);

		User userAdd = new User();
		userAdd.setId(4);
		userAdd.setName("mybatis");
		userAdd.setPassword("9898");
		int result = ud.add(userAdd);
		System.out.println("插入结果:" +  result);
		
		int del = ud.delete(1);
		System.out.println("删除是否成功:" + del);
		
		List<User> userList = ud.getAll();
		for (User userT : userList) {
			System.out.println(userT);
		}
		
		System.out.println("****************************MyBatis的动态查询SQL*****************************");
		List<User> userListDy = ud.getUserByDynamicSql(null);
		System.out.println("当参数传null时查询所有数据没有where条件:" + userListDy.size());
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("name", "大侠2%");
		userListDy = ud.getUserByDynamicSql(paramMap);
		System.out.println("当参数传只时查询数据使用where条件:" + userListDy.size());
		
		Map<String, Object> paramMapSec = new HashMap<String, Object>();
		paramMapSec.put("name", null);
		paramMapSec.put("id", null);//这俩个都传null,就会使用otherwise条件
		paramMapSec.put("pwd", "2222");//otherwise也可以传参数进去

		userListDy = ud.getUserByDynamicSqlWhen(paramMapSec);
		System.out.println("when条件使用:" + userListDy.size());
		if (userListDy != null && userListDy.size() > 0) {
			System.out.println("when条件使用:name=" + userListDy.get(0).getName() + ",id:" + userListDy.get(0).getId());
		}
		
		Map<String, Object> paramMapUp = new HashMap<String, Object>();
		paramMapUp.put("name", "陈总");
		paramMapUp.put("id", "88");
		int resultUp = ud.updteUserByDynamicSqlWhen(paramMapUp);
		System.out.println("动态更新结果:" + resultUp);
		/*
		 * MyBatis怎么调用存储过程啊
		 */
	}
}
