package com.yale.test.mybatis.dao;

import java.io.IOException;

import org.apache.ibatis.session.SqlSession;

import com.yale.test.mybatis.util.MyBatisUtil;
import com.yale.test.mybatis.vo.Teacher2;

public class TeacherDao {
	public Teacher2 getTeacher(String id) throws IOException {
		SqlSession session = MyBatisUtil.getSession();
		Teacher2 teacher = session.selectOne("com.yale.test.mybatis.vo.teacher.mapper.getTeacherOneToMany", id);
		session.close();
		return teacher;
	}
	
	public Teacher2 getTeacherSec(String id) throws IOException {
		SqlSession session = MyBatisUtil.getSession();
		Teacher2 teacher = session.selectOne("com.yale.test.mybatis.vo.teacher.mapper.getTeacherOneToManySec", id);
		session.close();
		return teacher;
	}
}
