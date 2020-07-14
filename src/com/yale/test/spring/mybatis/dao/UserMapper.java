package com.yale.test.spring.mybatis.dao;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.yale.test.spring.mybatis.vo.User;

/*
 * Spring整合MyBatis的第三种方式,使用注解,不要user.mapper.xml这个配置文件了
 */
public interface UserMapper {
	@Select("select * from userinfo ")
	List<User> selectUser();
}
