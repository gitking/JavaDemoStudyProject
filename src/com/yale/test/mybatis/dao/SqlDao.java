package com.yale.test.mybatis.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import com.yale.test.mybatis.vo.User;

/**
 * mybatis使用注解开发,不需要在配置mapper.xml文件了
 * 甚至SqlDao的实现类都不需要了,太屌了,少写了很多代码
 * @author dell
 */
public interface SqlDao {
	//这里也可以使用别名解决数据库列名跟类属性名不一致的问题
	@Select("SELECT id,name,pwd password FROM USERINFO")
	public List<User> getAll();
	
	//注意这里的#{password}要跟类的属性名一样
	@Insert("insert into userinfo(id,name,pwd) values (#{id}, #{name}, #{password})")
	public int addUser(User user);
	
	//mybatis注解怎么分页查询
	@Select("SELECT id,name,pwd password FROM USERINFO")
	public List<User> getAllByPage();
}
