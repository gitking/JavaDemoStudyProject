package com.yale.test.mybatis.dao;

import java.io.IOException;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.yale.test.mybatis.util.MyBatisUtil;
import com.yale.test.mybatis.vo.Student;
import com.yale.test.mybatis.vo.Teacher;

public class StudentDao {
	public List<Student> getStudents() throws IOException {
		SqlSession session = MyBatisUtil.getSession();
		List<Student> userList = session.selectList("com.yale.test.mybatis.vo.student.mapper.getStudents");
		session.close();
		return userList;
	}
	
	
	public List<Student> getStudentList() throws IOException {
		SqlSession session = MyBatisUtil.getSession();
		List<Student> userList = session.selectList("com.yale.test.mybatis.vo.student.mapper.getStudentList");
		session.close();
		return userList;
	}
	
	public Teacher getTeacherId(String id) throws IOException {
		SqlSession session = MyBatisUtil.getSession();
		Teacher userList = session.selectOne("com.yale.test.mybatis.vo.teacher.mapper.getTeacher", id);
		session.close();
		return userList;
	}
}
