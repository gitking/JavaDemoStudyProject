package com.yale.test.mybatis.test;

import java.io.IOException;
import java.util.List;

import com.yale.test.mybatis.dao.StudentDao;
import com.yale.test.mybatis.dao.TeacherDao;
import com.yale.test.mybatis.vo.Student;
import com.yale.test.mybatis.vo.Student2;
import com.yale.test.mybatis.vo.Teacher;
import com.yale.test.mybatis.vo.Teacher2;

public class ManyToOneTest {

	public static void main(String[] args) throws IOException {
		StudentDao stu = new StudentDao();
		List<Student> stuList = stu.getStudents();
		for (Student stuTe: stuList) {//多对一测试
			System.out.println("学生名称:" + stuTe.getName() + ",学生老师名字:" + stuTe.getTeacher().getName());
		}
		Teacher te = stu.getTeacherId("t001");
		
		System.out.println("老师名字:" + te.getName());
		System.out.println("多对一测试,按查询结果嵌套------------");
		List<Student> stuListSec = stu.getStudentList();
		if (!stuListSec.isEmpty()) {
			for (Student stuTe1: stuListSec) {//多对一测试,按查询结果嵌套
				System.out.println("学生名称:" + stuTe1.getName() + ",学生老师名字:" + stuTe1.getTeacher().getName());
			}
		} else {
			System.out.println("空数据");
		}
		
		System.out.println("下面是一对多的演示------------------");
		TeacherDao td = new TeacherDao();
		Teacher2 teacher = td.getTeacher("t001");
		System.out.println("老师名字:" + teacher.getName() + ",有以下几个学生:");
		List<Student2> stuTeaList = teacher.getStuList();
		for (Student2 stuTea : stuTeaList) {
			System.out.println("学生名字:" + stuTea.getName() + ",学生编号:" + stuTea.getId());
		}
		
		System.out.println("一对多的第二种写法--------------");
		Teacher2 teacherSec = td.getTeacherSec("t003");
		System.out.println("老师名字:" + teacherSec.getName() + ",有以下几个学生:");
		List<Student2> stuTeaSecList = teacherSec.getStuList();
		for (Student2 stuTea : stuTeaSecList) {
			System.out.println("学生名字:" + stuTea.getName() + ",学生编号:" + stuTea.getId());
		}
	}
}
