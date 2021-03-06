package com.yale.test.jdbc;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Arrays;
import java.util.Date;

public class TestJDBCDemo {
	public static final String DBDRIVER = "oracle.jdbc.driver.OracleDriver";
	public static final String DBURL = "jdbc:oracle:thin:@localhost:1521:mldn";
	public static final String DBUSER = "scott";
	public static final String DBPASSWORD = "tiger";
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		//这行代码现在其实不写也可以,因为有SPI,好像是JDK1.6之前才需要这么写
		//https://www.jianshu.com/p/3a3edbcd8f24
		//https://zhuanlan.zhihu.com/p/28909673
		Class.forName(DBDRIVER);//数据库驱动加载
		/**
		 * Class.forName(DBDRIVER);这行代码其实就是为了加载OracleDriver这个类,
		 * 那能不能换成这种写法呢,Class cls = OracleDriver.class;
		 * public static Connection getConnection(String url, Properties info) throws SQLException
		 * 这里对参数Properties的要求有固定格式,
		 */
		System.out.println("在JDBC中的java.sql.DriverManager类中有一个方法getConnection的参数可以接收Properties");
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
		
		String queryStr = "select mid, name, age, birthday, note from member order by mid";
		ResultSet rs = statement.executeQuery(queryStr);
		while (rs.next()) {
			int mid = rs.getInt("mid");
			String name = rs.getString(2);//建议使用这种形式,比较简单,编号从1开始
			int age = rs.getInt("age");
			Date birthday = rs.getDate("mid");
			String note = rs.getString("note");
			System.out.println(mid + "," + name + "," + age + "," + birthday + "," + note);
		}
		String name = "找刘";
		String enName = "Mr'liu";//外国名字
		int age = 10;
		String birthday = "1989-101-10";
		String note = "不是个人";
		/**
		 * 这种拼接的SQL非常低级,坚决不要使用,而且当字符串enName里面带有单引号'时,statement.executeUpdate这个方法就会报错
		 * 使用PreparedStatement这个类代替statement
		 */
		String sqlUpdate = " INSERT INTO member(mid, name, age, birthday, note) VALUES "
				+ "(myseq.nextval, '"+name+"', "+age+", to_Date('" + birthday+"','yyyy-mm-dd'), "+note+")";
		try {
			int lenn = statement.executeUpdate(sqlUpdate);//执行SQL语句
			System.out.println("数据库插入行数:" + lenn);
		} catch (SQLSyntaxErrorException e) {
			e.printStackTrace();
		}
		statement.close();
		
		Date date = new Date();//这里是java.util.Date
		String psSql = " INSERT INTO member(mid, name, age, birthday, note) VALUES "
				+ "(myseq.nextval, ?, ?, ?, ?)";
		PreparedStatement ps = conn.prepareStatement(psSql);
		ps.setString(1, enName);//编号从1开始
		ps.setInt(2, age);
		ps.setDate(3, new java.sql.Date(date.getTime()));//这里是java.sql.Date()
		ps.setString(4, note);
		int psLen = ps.executeUpdate();
		System.out.println("数据库插入行数:" + psLen);
		ps.close();
		
		String psQuerySql = " SELECT mid, name, age, birthday, note FROM member ";
		PreparedStatement psQuery = conn.prepareStatement(psQuerySql);
		ResultSet psRs= psQuery.executeQuery();
		while (psRs.next()) {
			int mid = psRs.getInt(1);
			String namePs = psRs.getString(2);//建议使用这种形式,比较简单,编号从1开始
			int agePs = psRs.getInt(3);
			Date birthdayUtil = psRs.getDate(4);
			String notePS = psRs.getString(5);
			System.out.println(mid + "," + namePs + "," + agePs + "," + birthdayUtil + "," + notePS);
		}
		psQuery.close();
		String psQueryW = " SELECT mid, name, age, birthday, note FROM member where mid = ? ";
		PreparedStatement psW = conn.prepareStatement(psQueryW);
		ResultSet pwRs = psW.executeQuery();
		psW.setInt(1, 5);//设置查询条件
		while (pwRs.next()) {
			int mid = pwRs.getInt(1);
			String namePs = pwRs.getString(2);//建议使用这种形式,比较简单,编号从1开始
			int agePs = pwRs.getInt(3);
			Date birthdayUtil = pwRs.getDate(4);
			String notePS = pwRs.getString(5);
			System.out.println(mid + "," + namePs + "," + agePs + "," + birthdayUtil + "," + notePS);
		}
		pwRs.close();
		
		/*
		 * 今天在实现一个数据库批量更新的代码时，发现
		 * String sql = "UPDATE t_demo SET columns='Well' WHERE column_id IN (?)";
		 * 这条语句中的参数在使用PrepareStatement来预编译之后，是不可以传入一个拼接字符串的，比如
		 * String criteria="'a','b','c'";
		 * prepareStatement.setString(1,criteria);
		 * 想要达到的效果是和执行下边一条语句一样：
		 * UPDATE t_demo SET columns='Well' WHERE column_id IN ('a','b','c')
		 * 但是最后发现这样并不起作用，最后思考后恍然大悟。 
		 * 因为PrepareStatement是预编译的，所以在编译完sql语句后发现这个sql只有一个参数，所以在设置参数的时候会默认（’a’,’b’,’c’）为一个参数，就会去查找column_id为“’a’,’b’,’c’”的数据，结果当然是不符合期望的。
		 * 可以通过PrepareStatement的setArray()方法
		 * 这个也是在网上发现的方法，这个是不支持mysql数据库的，在mysql环境下使用会报SQLFeatureNotSupportedException异常 
		 * 可能支持Oracle（没有测试）。 代码如下。
		 * String arr[]={"a","b","c"};
		 * Array v=conn.createArrayOf("VARCHAR", arr);
		 * prepareStatement.setArray(1, v);
		 * prepareStatement.executeUpdate(); 注意这个不经常用
		 * 上边几个方法都在我的mysql环境中阵亡了之后，就只好使用笨方法了。那就是在in的条件中多加几个“？”。
		 * 注意你更新或者查询用到IN的话,本质上就是批处理,就用JDBC的批处理一条数据一条数据更新就行。
		 * https://blog.csdn.net/lismooth/article/details/76934980
		 */
		String psQueryLike = " SELECT mid, name, age, birthday, note FROM member where name like ? ";
		PreparedStatement psLike = conn.prepareStatement(psQueryLike);
		ResultSet rsLike = psLike.executeQuery();
		psLike.setString(1, "%张%");//模糊查询
		while (rsLike.next()) {
			int mid = rsLike.getInt(1);
			String namePs = rsLike.getString(2);//建议使用这种形式,比较简单,编号从1开始
			int agePs = rsLike.getInt(3);
			Date birthdayUtil = rsLike.getDate(4);
			String notePS = rsLike.getString(5);
			System.out.println(mid + "," + namePs + "," + agePs + "," + birthdayUtil + "," + notePS);
		}
		rsLike.close();
		//分页查询
		StringBuilder pageQuery = new StringBuilder(" SELECT * FROM (SELECT mid, name, age, birthday, note, ROWNUM rn FROM member where name like ? ");
		pageQuery.append(" and rownum<=? ) temp where temp.rn >?");
		PreparedStatement psPage = conn.prepareStatement(pageQuery.toString());
		ResultSet rsPage = psPage.executeQuery();
		psPage.setString(1, "%张%");//模糊查询
		int currentPage = 1;//当前页
		int pageSize = 3;//每页显示3条数据
		psPage.setInt(2, currentPage * pageSize);//模糊查询
		psPage.setInt(3, (currentPage -1) * pageSize);//模糊查询
		while (rsPage.next()) {
			int mid = rsPage.getInt(1);
			String namePs = rsPage.getString(2);//建议使用这种形式,比较简单,编号从1开始
			int agePs = rsPage.getInt(3);
			Date birthdayUtil = rsPage.getDate(4);
			String notePS = rsPage.getString(5);
			System.out.println(mid + "," + namePs + "," + agePs + "," + birthdayUtil + "," + notePS);
		}
		rsPage.close();
		
		//统计查询
		StringBuilder countSql = new StringBuilder(" SELECT count(*) FROM member where name like ? ");
		PreparedStatement countPage = conn.prepareStatement(countSql.toString());
		ResultSet countRs = countPage.executeQuery();
		countPage.setString(1, "%张%");//模糊查询
		while (countRs.next()) {
			long count = countRs.getLong(1);//注意如果你的数据太多,要用Long接收
			System.out.println(count);
		}
		countPage.close();
		batchDeal(conn);
		batchDealTran(conn);
		conn.close();
	}
	
	/**
	 * 批处理
	 * Statement接口定义的批处理方法:
	 *  增加待执行SQL: public void addBatch(String sql)
	 *  执行批处理操作: public int[] executeBatch()
	 * PreparedStatement接口定义的批处理方法:
	 * 	增加待执行SQL: public void addBatch()
	 *  执行批处理操作: public int[] executeBatch()
	 *  批处理的操作特点就是一次性将所有的SQL向数据库发出,速度快,性能高
	 * @throws SQLException 
	 */
	public static void batchDeal(Connection conn ) throws SQLException {
		StringBuilder sb = new StringBuilder(" INSERT INTO member(mid, name) values (myseq.nextval, ?) ");
		PreparedStatement preparedState = conn.prepareStatement(sb.toString());
		for (int x=0; x<10; x++) {
			preparedState.setString(1, "mldn-" + x);
			preparedState.addBatch();//将操作追加到批处理之中
		}
		/**
		 * 这里的批处理没有启用事务,如果批处理中间出现错误,那么批处理出现错误之前的sql都会提交到数据库中,
		 * 如果这些数据是一个整体,那么这个批处理就不行,这是因为JDBC默认的事务是自动提交的
		 */
		int[] result = preparedState.executeBatch();//执行批处理操作
		preparedState.close();
		System.out.println("PreparedStatement批处理操作:" + Arrays.toString(result));
		
		//使用Statement批处理
		StringBuilder sqlStr = new StringBuilder(" INSERT INTO member(mid, name) values (myseq.nextval, '#name#') ");
		Statement state = conn.createStatement();
		for (int x=0; x<10; x++) {
			state.addBatch(sqlStr.toString().replaceAll("#name#", "mldn - " + x));//将操作追加到批处理之中
		}
		int[] resultState = state.executeBatch();//执行批处理操作
		state.close();
		System.out.println(Arrays.toString(resultState));
		
		/**
		 * 调用存储过程
		 */
		CallableStatement callableStatement = conn.prepareCall("{call ANT_MIGRATE_PKG.ANT_TO_V7(?,?)}");    
		callableStatement.setString(1, ""); 
		callableStatement.registerOutParameter(2,Types.VARCHAR);
		callableStatement.execute();
	    String errolog = callableStatement.getString(2);
	    if(!"OK".equals(errolog)){
	    	System.out.println("存储过程返回错误信息为:" + errolog);
	    }
	    callableStatement.close();
	}
	
	/**
	 * 事务的控制都在Connection接口里面:
	 * @param conn
	 * @throws SQLException
	 */
	public static void batchDealTran(Connection conn ) throws SQLException {
		conn.setAutoCommit(false);//取消自动提交事务
		try {
			StringBuilder sb = new StringBuilder(" INSERT INTO member(mid, name) values (myseq.nextval, ?) ");
			PreparedStatement preparedState = conn.prepareStatement(sb.toString());
			for (int x=0; x<10; x++) {
				preparedState.setString(1, "mldn-" + x);
				preparedState.addBatch();//将操作追加到批处理之中
			}
			/**
			 * 这里的批处理没有启用事务,如果批处理中间出现错误,那么批处理出现错误之前的sql都会提交到数据库中,
			 * 如果这些数据是一个整体,那么这个批处理就不行,这是因为JDBC默认的事务是自动提交的
			 */
			int[] result = preparedState.executeBatch();//执行批处理操作
			preparedState.close();
			System.out.println("PreparedStatement批处理操作:" + Arrays.toString(result));
			conn.commit();
		} catch(Exception e) {
			e.printStackTrace();
			conn.rollback();
		}
		
		try {
			conn.setAutoCommit(false);//取消自动提交事务
			//使用Statement批处理
			StringBuilder sqlStr = new StringBuilder(" INSERT INTO member(mid, name) values (myseq.nextval, '#name#') ");
			Statement state = conn.createStatement();
			for (int x=0; x<10; x++) {
				state.addBatch(sqlStr.toString().replaceAll("#name#", "mldn - " + x));//将操作追加到批处理之中
			}
			int[] resultState = state.executeBatch();//执行批处理操作
			state.close();
			System.out.println(Arrays.toString(resultState));
			conn.commit();//提交事务
		} catch(Exception e) {
			e.printStackTrace();
			conn.rollback();//回滚事务
		}
	}
}
