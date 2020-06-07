package com.yale.test.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class TestJDBCDemoo {
	public static final String DBDRIVER = "oracle.jdbc.driver.OracleDriver";
	public static final String DBURL = "jdbc:oracle:thin:@localhost:1521:mldn";
	public static final String DBUSER = "scott";
	public static final String DBPASSWORD = "tiger";
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		Class.forName(DBDRIVER);//数据库驱动加载
		/**
		 * Class.forName(DBDRIVER);这行代码其实就是为了加载OracleDriver这个类,
		 * 那能不能换成这种写法呢,Class cls = OracleDriver.class;
		 */
		Connection conn = DriverManager.getConnection(DBURL, DBUSER, DBPASSWORD);
		/**
		 * oralce的主要安装目录:product\11.2.0\dbhome_1\NETWORK\ADMIN;这个目录有俩个文件
		 * listener.ora,tnsnames.ora
		 */
		System.out.println(conn);
		
		Statement statement = conn.createStatement();//创建数据库操作对象
		String sql = " INSERT INTO member(mid, name, age, birthday, note) VALUES "
				+ "(myseq.nextval, '张三', 10, SYSDATE, '是个人')";
		int len = statement.executeUpdate(sql);//执行SQL语句
		System.out.println("数据库插入行数:" + len);
		
		String updateSQL = "update member set name = '李四', age =20,where mid in (1,2,3,4,5)";
		int updateLen = statement.executeUpdate(updateSQL);
		System.out.println("数据库更新行数:" + updateLen);
		
		/**
		 * SQLSyntaxErrorException出现这个错误就是SQL语法异常,查SQL语句就行了
		 */
		String delSQL = " delete from member where mid betwe1en 10 and 20 ";
		int delLen = statement.executeUpdate(delSQL);
		System.out.println("数据库删除行数:" + delLen);
		
		statement.close();
		conn.close();
	}
}
