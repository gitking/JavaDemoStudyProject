package com.yale.test.mybatis.dao;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;

import com.yale.test.mybatis.util.MyBatisUtil;
import com.yale.test.mybatis.vo.User;

public class UserDao {
	
	public List<User> getAll() throws IOException {
		SqlSession session = MyBatisUtil.getSession();
		List<User> userList = session.selectList("com.yale.test.mybatis.vo.UserMapper.selectAll");
		session.close();
		return userList;
	}
	
	//分页查询
	public List<User> getAllByPage(int currentPage, int pageSize) throws IOException {
		SqlSession session = MyBatisUtil.getSession();
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("startIndex", currentPage  * pageSize);
		map.put("pageSize", (currentPage-1) * pageSize);//每页多少条
		List<User> userList = session.selectList("com.yale.test.mybatis.vo.UserMapper.selectAllByPage", map);
		session.close();
		return userList;
	}
	
	//分页查询,通过rowbounds实现分页查询
	public List<User> getAllByPageRowBounds(int currentPage, int pageSize) throws IOException {
		SqlSession session = MyBatisUtil.getSession();
		//RowBounds第一个参数是当前页数,第二个是每页的数量
		RowBounds rowBounds = new RowBounds((currentPage-1) * pageSize, pageSize);
		List<User> userList = session.selectList("com.yale.test.mybatis.vo.UserMapper.getAllByPageRowBounds", null ,rowBounds);
		session.close();
		return userList;
	}
	
	public User getById(int id) throws IOException {
		SqlSession session = MyBatisUtil.getSession();
		User user = session.selectOne("com.yale.test.mybatis.vo.UserMapper.selectUser", id);
		session.close();
		return user;
	}
	
	public User getByUserMap(int id) throws IOException {
		SqlSession session = MyBatisUtil.getSession();
		User user = session.selectOne("com.yale.test.mybatis.vo.UserMapper.selectUserMap", id);
		session.close();
		return user;
	}
	
	public int add(User user) throws IOException {
		SqlSession session = MyBatisUtil.getSession();
		int result = session.insert("com.yale.test.mybatis.vo.UserMapper.addUser", user);
		session.commit();//不提交,插入不进去,mybatis必须手动提交事务
		session.close();
		return result;
	}
	
	public int update(User user) throws IOException {
		SqlSession session = MyBatisUtil.getSession();
		/*
		 * 注意这里用的MyBatis的insert方法来更新的,也可以使用update方法
		 * 原理是这样的：在org.apache.ibatis.session.defaults.DefaultSqlSession
		 * 这个类里面insert和delete方法,最终调用的都是update方法
		 */
		int result = session.insert("com.yale.test.mybatis.vo.UserMapper.updateUser", user);
		session.commit();//不提交,插入不进去,mybatis必须手动提交事务
		session.close();
		return result;
	}
	
	public int delete(int id) throws IOException {
		SqlSession session = MyBatisUtil.getSession();
		/*
		 * 注意这里用的MyBatis的insert方法来更新的,也可以使用update方法
		 * 原理是这样的：在org.apache.ibatis.session.defaults.DefaultSqlSession
		 * 这个类里面insert和delete方法,最终调用的都是update方法
		 */
		int result = session.delete("com.yale.test.mybatis.vo.UserMapper.deleteUser", id);
		session.commit();//不提交,插入不进去,mybatis必须手动提交事务
		session.close();
		return result;
	}
	
	/*
	 * MyBatis的动态SQL查询
	 */
	public List<User> getUserByDynamicSql(Map<String, Object> map) throws IOException{
		SqlSession session = MyBatisUtil.getSession();
		List<User> userList = session.selectList("com.yale.test.mybatis.vo.UserMapper.getUserByCondition", map);
		return userList;
	}
	
	/*
	 * MyBatis的动态SQL查询
	 */
	public List<User> getUserByDynamicSqlWhen(Map<String, Object> map) throws IOException{
		SqlSession session = MyBatisUtil.getSession();
		List<User> userList = session.selectList("com.yale.test.mybatis.vo.UserMapper.getUserByConditionSec", map);
		return userList;
	}
	
	/*
	 * MyBatis的动态SQL查询
	 */
	public int updteUserByDynamicSqlWhen(Map<String, Object> map) throws IOException{
		SqlSession session = MyBatisUtil.getSession();
		int result = session.update("com.yale.test.mybatis.vo.UserMapper.updateAuthorIfNecessary", map);
		session.commit();//不提交,插入不进去,mybatis必须手动提交事务
		return result;
	}
}
