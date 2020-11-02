package com.yale.test.spring.mybatis.lxf.service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yale.test.spring.mybatis.lxf.entity.User;
import com.yale.test.spring.mybatis.lxf.mapper.UserMapper;

/*
 * 有了@MapperScan，就可以让MyBatis自动扫描指定包的所有Mapper并创建实现类。在真正的业务逻辑中，我们可以直接注入：
 */
@Component
@Transactional
public class UserService {
	@Autowired // 注入UserMapper:
	UserMapper userMapper;
	
	public User getUserById(long id) {
		User user = userMapper.getById(id);// 调用Mapper方法:
		if (user == null){
			throw new RuntimeException("User not found by id.");
		}
		return user;
	}
	
	public User fetchUserByEmail(String email) {
		return userMapper.getByEmail(email);
	}
	
	public User getUserByEmail(String email) {
		User user = fetchUserByEmail(email);
		if (user == null) {
			throw new RuntimeException("User not found by email.");
		}
		return user;
	}
	
	public List<User> getUsers(int pageIndex) {
		int pageSize = 1000;
		return userMapper.getAll((pageIndex-1)*pageSize, pageSize);
	}
	
	public User login(String email, String password) {
		User user = userMapper.getByEmail(email);
		if (user != null && password.equals(user.getPassword())) {
			return user;
		}
		throw new RuntimeException("login failed.");
	}
	
	public User register(String email, String password, String name) {
		User user = new User();
		user.setEmail(email);
		user.setPassword(password);
		user.setName(name);
		user.setCreatedAt(LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli());
		userMapper.insert(user);
		return user;
	}
	
	public void updateUser(Long id, String name) {
		User user = getUserById(id);
		user.setName(name);
		user.setCreatedAt(System.currentTimeMillis());
		userMapper.update(user);
	}
	
	public void deleteUser(Long id) {
		userMapper.deleteById(id);
	}
}
