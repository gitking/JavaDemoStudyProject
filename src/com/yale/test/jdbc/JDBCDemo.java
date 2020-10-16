package com.yale.test.jdbc;

/**
 * JDBC编程
 * 程序运行的时候，往往需要存取数据。现代应用程序最基本，也是使用最广泛的数据存储就是关系数据库。
 * Java为关系数据库定义了一套标准的访问接口：JDBC（Java Database Connectivity），本章我们介绍如何在Java程序中使用JDBC。
 * JDBC简介
 * 在介绍JDBC之前，我们先简单介绍一下关系数据库。
 * 程序运行的时候，数据都是在内存中的。当程序终止的时候，通常都需要将数据保存到磁盘上，无论是保存到本地磁盘，还是通过网络保存到服务器上，最终都会将数据写入磁盘文件。
 * 而如何定义数据的存储格式就是一个大问题。如果我们自己来定义存储格式，比如保存一个班级所有学生的成绩单：
 * 	名字		成绩
	Michael	99
	Bob	85
	Bart	59
	Lisa	87
 * 你可以用一个文本文件保存，一行保存一个学生，用,隔开：
 *  Michael,99
	Bob,85
	Bart,59
	Lisa,87
 * 你还可以用JSON格式保存，也是文本文件：
 * [
	    {"name":"Michael","score":99},
	    {"name":"Bob","score":85},
	    {"name":"Bart","score":59},
	    {"name":"Lisa","score":87}
	]
 * 你还可以定义各种保存格式，但是问题来了：
 * 存储和读取需要自己实现，JSON还是标准，自己定义的格式就各式各样了；
 * 不能做快速查询，只有把数据全部读到内存中才能自己遍历，但有时候数据的大小远远超过了内存（比如蓝光电影，40GB的数据），根本无法全部读入内存。
 * 为了便于程序保存和读取数据，而且，能直接通过条件快速查询到指定的数据，就出现了数据库（Database）这种专门用于集中存储和查询的软件。
 * 数据库软件诞生的历史非常久远，早在1950年数据库就诞生了。经历了网状数据库，层次数据库，我们现在广泛使用的关系数据库是20世纪70年代基于关系模型的基础上诞生的。
 * 关系模型有一套复杂的数学理论，但是从概念上是十分容易理解的。举个学校的例子：
 * 假设某个XX省YY市ZZ县第一实验小学有3个年级，要表示出这3个年级，可以在Excel中用一个表格画出来：
 * 每个年级又有若干个班级，要把所有班级表示出来，可以在Excel中再画一个表格：
 * 这两个表格有个映射关系，就是根据Grade_ID可以在班级表中查找到对应的所有班级：
 * 也就是Grade表的每一行对应Class表的多行，在关系数据库中，这种基于表（Table）的一对多的关系就是关系数据库的基础。
 * 根据某个年级的ID就可以查找所有班级的行，这种查询语句在关系数据库中称为SQL语句，可以写成：
 * SELECT * FROM classes WHERE grade_id = '1';
 * 结果也是一个表：
 * 类似的，Class表的一行记录又可以关联到Student表的多行记录：
 * 由于本教程不涉及到关系数据库的详细内容，如果你想从零学习关系数据库和基本的SQL语句，请参考SQL课程(https://www.liaoxuefeng.com/wiki/1177760294764384)。
 * NoSQL
 * 你也许还听说过NoSQL数据库，很多NoSQL宣传其速度和规模远远超过关系数据库，所以很多同学觉得有了NoSQL是否就不需要SQL了呢？千万不要被他们忽悠了，连SQL都不明白怎么可能搞明白NoSQL呢？
 * 数据库类别
 * 既然我们要使用关系数据库，就必须选择一个关系数据库。目前广泛使用的关系数据库也就这么几种：
 * 付费的商用数据库：
 *  Oracle，典型的高富帅；
    SQL Server，微软自家产品，Windows定制专款；
    DB2，IBM的产品，听起来挺高端；
    Sybase，曾经跟微软是好基友，后来关系破裂，现在家境惨淡。
 * 这些数据库都是不开源而且付费的，最大的好处是花了钱出了问题可以找厂家解决，不过在Web的世界里，常常需要部署成千上万的数据库服务器，当然不能把大把大把的银子扔给厂家，所以，无论是Google、Facebook，还是国内的BAT，无一例外都选择了免费的开源数据库：
 * MySQL，大家都在用，一般错不了；PostgreSQL，学术气息有点重，其实挺不错，但知名度没有MySQL高；sqlite，嵌入式数据库，适合桌面和移动应用。
 * 作为一个Java工程师，选择哪个免费数据库呢？当然是MySQL。因为MySQL普及率最高，出了错，可以很容易找到解决方法。而且，围绕MySQL有一大堆监控和运维的工具，安装和使用很方便。
 * 安装MySQL
 * 为了能继续后面的学习，你需要从MySQL官方网站下载并安装MySQL Community Server 5.6(https://dev.mysql.com/downloads/mysql/)，这个版本是免费的，其他高级版本是要收钱的（请放心，收钱的功能我们用不上）。MySQL是跨平台的，选择对应的平台下载安装文件，安装即可
 * 安装时，MySQL会提示输入root用户的口令，请务必记清楚。如果怕记不住，就把口令设置为password。
 * 在Windows上，安装时请选择UTF-8编码，以便正确地处理中文。
 * 在Mac或Linux上，需要编辑MySQL的配置文件，把数据库默认的编码全部改为UTF-8。MySQL的配置文件默认存放在/etc/my.cnf或者/etc/mysql/my.cnf：
 * [client]
	default-character-set = utf8
	[mysqld]
	default-storage-engine = INNODB
	character-set-server = utf8
	collation-server = utf8_general_ci
 * 大家记得把collation-server里的utf8_general_ci也改成utf8mb4_general_ci，不然会启动失败
 * [client]
	default-character-set = utf8mb4
	[mysqld]
	default-storage-engine = INNODB
	character-set-server = utf8mb4
	collation-server = utf8mb4_general_ci
 * 重启MySQL后，可以通过MySQL的客户端命令行检查编码：
 * $ mysql -u root -p
 * mysql> show variables like '%char%';
 * 看到utf8字样就表示编码设置正确。
 * 注：如果MySQL的版本≥5.5.3，可以把编码设置为utf8mb4，utf8mb4和utf8完全兼容，但它支持最新的Unicode标准，可以显示emoji字符。
 * JDBC
 * 什么是JDBC？JDBC是Java DataBase Connectivity的缩写，它是Java程序访问数据库的标准接口。
 * 使用Java程序访问数据库时，Java代码并不是直接通过TCP连接去访问数据库，而是通过JDBC接口来访问，而JDBC接口则通过JDBC驱动来实现真正对数据库的访问。
 * 例如，我们在Java代码中如果要访问MySQL，那么必须编写代码操作JDBC接口。注意到JDBC接口是Java标准库自带的，所以可以直接编译。
 * 而具体的JDBC驱动是由数据库厂商提供的，例如，MySQL的JDBC驱动由Oracle提供。因此，访问某个具体的数据库，我们只需要引入该厂商提供的JDBC驱动，
 * 就可以通过JDBC接口来访问，这样保证了Java程序编写的是一套数据库访问代码，却可以访问各种不同的数据库，因为他们都提供了标准的JDBC驱动：
 * 	┌ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ┐

	│  ┌───────────────┐  │
	   │   Java App    │
	│  └───────────────┘  │
	           │
	│          ▼          │
	   ┌───────────────┐
	│  │JDBC Interface │<─┼─── JDK
	   └───────────────┘
	│          │          │
	           ▼
	│  ┌───────────────┐  │
	   │  JDBC Driver  │<───── Vendor
	│  └───────────────┘  │
	           │
	└ ─ ─ ─ ─ ─│─ ─ ─ ─ ─ ┘
	           ▼
	   ┌───────────────┐
	   │   Database    │
	   └───────────────┘
 * 从代码来看，Java标准库自带的JDBC接口其实就是定义了一组接口，而某个具体的JDBC驱动其实就是实现了这些接口的类：
 * 	┌ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ┐

	│  ┌───────────────┐  │
	   │   Java App    │
	│  └───────────────┘  │
	           │
	│          ▼          │
	   ┌───────────────┐
	│  │JDBC Interface │<─┼─── JDK
	   └───────────────┘
	│          │          │
	           ▼
	│  ┌───────────────┐  │
	   │ MySQL Driver  │<───── Oracle
	│  └───────────────┘  │
	           │
	└ ─ ─ ─ ─ ─│─ ─ ─ ─ ─ ┘
	           ▼
	   ┌───────────────┐
	   │     MySQL     │
	   └───────────────┘
 * 实际上，一个MySQL的JDBC的驱动就是一个jar包，它本身也是纯Java编写的。我们自己编写的代码只需要引用Java标准库提供的java.sql包下面的相关接口，由此再间接地通过MySQL驱动的jar包通过网络访问MySQL服务器，所有复杂的网络通讯都被封装到JDBC驱动中，因此，Java程序本身只需要引入一个MySQL驱动的jar包就可以正常访问MySQL服务器：
 *  ┌ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ┐
	   ┌───────────────┐
	│  │   App.class   │  │
	   └───────────────┘
	│          │          │
	           ▼
	│  ┌───────────────┐  │
	   │  java.sql.*   │
	│  └───────────────┘  │
	           │
	│          ▼          │
	   ┌───────────────┐     TCP    ┌───────────────┐
	│  │ mysql-xxx.jar │──┼────────>│     MySQL     │
	   └───────────────┘            └───────────────┘
	└ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ┘
	          JVM
 * 小结
 * 使用JDBC的好处是：
 * 各数据库厂商使用相同的接口，Java代码不需要针对不同数据库分别开发；
 * Java程序编译期仅依赖java.sql包，不依赖具体数据库的jar包；
 * 可随时替换底层数据库，访问数据库的Java代码基本不变。
 * 
 * JDBC查询
 * 前面我们讲了Java程序要通过JDBC接口来查询数据库。JDBC是一套接口规范，它在哪呢？就在Java的标准库java.sql里放着，不过这里面大部分都是接口。接口并不能直接实例化，而是必须实例化对应的实现类，然后通过接口引用这个实例。那么问题来了：JDBC接口的实现类在哪？
 * 因为JDBC接口并不知道我们要使用哪个数据库，所以，用哪个数据库，我们就去使用哪个数据库的“实现类”，我们把某个数据库实现了JDBC接口的jar包称为JDBC驱动。
 * 因为我们选择了MySQL 5.x作为数据库，所以我们首先得找一个MySQL的JDBC驱动。所谓JDBC驱动，其实就是一个第三方jar包，我们直接添加一个Maven依赖就可以了：
 * <dependency>
	    <groupId>mysql</groupId>
	    <artifactId>mysql-connector-java</artifactId>
	    <version>5.1.47</version>
	    <scope>runtime</scope>
	</dependency>
 * 注意到这里添加依赖的scope是runtime，因为编译Java程序并不需要MySQL的这个jar包，只有在运行期才需要使用。如果把runtime改成compile，虽然也能正常编译，但是在IDE里写程序的时候，会多出来一大堆类似com.mysql.jdbc.Connection这样的类，非常容易与Java标准库的JDBC接口混淆，所以坚决不要设置为compile。
 * 有了驱动，我们还要确保MySQL在本机正常运行，并且还需要准备一点数据。这里我们用一个脚本创建数据库和表，然后插入一些数据：
 * -- 创建数据库learjdbc:
DROP DATABASE IF EXISTS learnjdbc;
CREATE DATABASE learnjdbc;

-- 创建登录用户learn/口令learnpassword
CREATE USER IF NOT EXISTS learn@'%' IDENTIFIED BY 'learnpassword';
GRANT ALL PRIVILEGES ON learnjdbc.* TO learn@'%' WITH GRANT OPTION;
FLUSH PRIVILEGES;

-- 创建表students:
USE learnjdbc;
CREATE TABLE students (
  id BIGINT AUTO_INCREMENT NOT NULL,
  name VARCHAR(50) NOT NULL,
  gender TINYINT(1) NOT NULL,
  grade INT NOT NULL,
  score INT NOT NULL,
  PRIMARY KEY(id)
) Engine=INNODB DEFAULT CHARSET=UTF8;

-- 插入初始数据:
INSERT INTO students (name, gender, grade, score) VALUES ('小明', 1, 1, 88);
INSERT INTO students (name, gender, grade, score) VALUES ('小红', 1, 1, 95);
INSERT INTO students (name, gender, grade, score) VALUES ('小军', 0, 1, 93);
INSERT INTO students (name, gender, grade, score) VALUES ('小白', 0, 1, 100);
INSERT INTO students (name, gender, grade, score) VALUES ('小牛', 1, 2, 96);
INSERT INTO students (name, gender, grade, score) VALUES ('小兵', 1, 2, 99);
INSERT INTO students (name, gender, grade, score) VALUES ('小强', 0, 2, 86);
INSERT INTO students (name, gender, grade, score) VALUES ('小乔', 0, 2, 79);
INSERT INTO students (name, gender, grade, score) VALUES ('小青', 1, 3, 85);
INSERT INTO students (name, gender, grade, score) VALUES ('小王', 1, 3, 90);
INSERT INTO students (name, gender, grade, score) VALUES ('小林', 0, 3, 91);
INSERT INTO students (name, gender, grade, score) VALUES ('小贝', 0, 3, 77);
 * 在控制台输入mysql -u root -p，输入root口令后以root身份，把上述SQL贴到控制台执行一遍就行。如果你运行的是最新版MySQL 8.x，需要调整一下CREATE USER语句。
 * JDBC连接
 * 使用JDBC时，我们先了解什么是Connection。Connection代表一个JDBC连接，它相当于Java程序到数据库的连接（通常是TCP连接）。
 * 打开一个Connection时，需要准备URL、用户名和口令，才能成功连接到数据库。URL是由数据库厂商指定的格式，例如，MySQL的URL是：jdbc:mysql://<hostname>:<port>/<db>?key1=value1&key2=value2
 * 假设数据库运行在本机localhost，端口使用标准的3306，数据库名称是learnjdbc，那么URL如下：jdbc:mysql://localhost:3306/learnjdbc?useSSL=false&characterEncoding=utf8
 * 后面的两个参数表示不使用SSL加密，使用UTF-8作为字符编码（注意MySQL的UTF-8是utf8）。
 * 要获取数据库连接，使用如下代码：
 * // JDBC连接的URL, 不同数据库有不同的格式:
String JDBC_URL = "jdbc:mysql://localhost:3306/test";
String JDBC_USER = "root";
String JDBC_PASSWORD = "password";
// 获取连接:
Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
// TODO: 访问数据库...
// 关闭连接:
conn.close();
 * 核心代码是DriverManager提供的静态方法getConnection()。DriverManager会自动扫描classpath，找到所有的JDBC驱动，然后根据我们传入的URL自动挑选一个合适的驱动。
 * 因为JDBC连接是一种昂贵的资源，所以使用后要及时释放。使用try (resource)来自动释放JDBC连接是一个好方法：
 * try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
    	...
	}	
 * JDBC查询
 * 获取到JDBC连接后，下一步我们就可以查询数据库了。查询数据库分以下几步：
 * 第一步，通过Connection提供的createStatement()方法创建一个Statement对象，用于执行一个查询；
 * 第二步，执行Statement对象提供的executeQuery("SELECT * FROM students")并传入SQL语句，执行查询并获得返回的结果集，使用ResultSet来引用这个结果集；
 * 第三步，反复调用ResultSet的next()方法并读取每一行结果。
 * 完整查询代码如下：
 * try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
	    try (Statement stmt = conn.createStatement()) {
	        try (ResultSet rs = stmt.executeQuery("SELECT id, grade, name, gender FROM students WHERE gender=1")) {
	            while (rs.next()) {
	                long id = rs.getLong(1); // 注意：索引从1开始
	                long grade = rs.getLong(2);
	                String name = rs.getString(3);
	                int gender = rs.getInt(4);
	            }
	        }
	    }
	}
 * 注意要点：
 * Statment和ResultSet都是需要关闭的资源，因此嵌套使用try (resource)确保及时关闭；
 * rs.next()用于判断是否有下一行记录，如果有，将自动把当前行移动到下一行（一开始获得ResultSet时当前行不是第一行）；
 * ResultSet获取列时，索引从1开始而不是0；
 * 必须根据SELECT的列的对应位置来调用getLong(1)，getString(2)这些方法，否则对应位置的数据类型不对，将报错。
 * SQL注入
 * 使用Statement拼字符串非常容易引发SQL注入的问题，这是因为SQL参数往往是从方法参数传入的。
 * 我们来看一个例子：假设用户登录的验证方法如下：
 * User login(String name, String pass) {
	    ...
	    stmt.executeQuery("SELECT * FROM user WHERE login='" + name + "' AND pass='" + pass + "'");
	    ...
	}
 * 其中，参数name和pass通常都是Web页面输入后由程序接收到的。
 * 如果用户的输入是程序期待的值，就可以拼出正确的SQL。例如：name = "bob"，pass = "1234"：
 * SELECT * FROM user WHERE login='bob' AND pass='1234'
 * 但是，如果用户的输入是一个精心构造的字符串，就可以拼出意想不到的SQL，这个SQL也是正确的，但它查询的条件不是程序设计的意图。例如：name = "bob' OR pass=", pass = " OR pass='"：
 * SELECT * FROM user WHERE login='bob' OR pass=' AND pass=' OR pass=''
 * 这个SQL语句执行的时候，根本不用判断口令是否正确，这样一来，登录就形同虚设。
 * 要避免SQL注入攻击，一个办法是针对所有字符串参数进行转义，但是转义很麻烦，而且需要在任何使用SQL的地方增加转义代码。
 * 还有一个办法就是使用PreparedStatement。使用PreparedStatement可以完全避免SQL注入的问题，因为PreparedStatement始终使用?作为占位符，并且把数据连同SQL本身传给数据库，这样可以保证每次传给数据库的SQL语句是相同的，只是占位符的数据不同，还能高效利用数据库本身对查询的缓存。上述登录SQL如果用PreparedStatement可以改写如下：
 * User login(String name, String pass) {
	    ...
	    String sql = "SELECT * FROM user WHERE login=? AND pass=?";
	    PreparedStatement ps = conn.prepareStatement(sql);
	    ps.setObject(1, name);
	    ps.setObject(2, pass);
	    ...
	}
 * 所以，PreparedStatement比Statement更安全，而且更快。
 * 注意:使用Java对数据库进行操作时，必须使用PreparedStatement，严禁任何通过参数拼字符串的代码！
 * 我们把上面使用Statement的代码改为使用PreparedStatement：
 * try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
	    try (PreparedStatement ps = conn.prepareStatement("SELECT id, grade, name, gender FROM students WHERE gender=? AND grade=?")) {
	        ps.setObject(1, "M"); // 注意：索引从1开始
	        ps.setObject(2, 3);
	        try (ResultSet rs = ps.executeQuery()) {
	            while (rs.next()) {
	                long id = rs.getLong("id");
	                long grade = rs.getLong("grade");
	                String name = rs.getString("name");
	                String gender = rs.getString("gender");
	            }
	        }
	    }
	} 
 * 使用PreparedStatement和Statement稍有不同，必须首先调用setObject()设置每个占位符?的值，最后获取的仍然是ResultSet对象。
 * 另外注意到从结果集读取列时，使用String类型的列名比索引要易读，而且不易出错。
 * 注意到JDBC查询的返回值总是ResultSet，即使我们写这样的聚合查询SELECT SUM(score) FROM ...，也需要按结果集读取：
 * ResultSet rs = ...
	if (rs.next()) {
	    double sum = rs.getDouble(1);
	}
 * 数据类型
 * 有的童鞋可能注意到了，使用JDBC的时候，我们需要在Java数据类型和SQL数据类型之间进行转换。JDBC在java.sql.Types定义了一组常量来表示如何映射SQL数据类型，但是平时我们使用的类型通常也就以下几种：
 * SQL数据类型		Java数据类型
	BIT,BOOL	boolean
	INTEGER		int
	BIGINT		long
	REAL		float
	FLOAT,DOUBLE	double
	CHAR,VARCHAR	String
	DECIMAL		BigDecimal
	DATE		java.sql.Date, LocalDate
	TIME		java.sql.Time, LocalTime
 * 注意：只有最新的JDBC驱动才支持LocalDate和LocalTime。
 * 小结
 * JDBC接口的Connection代表一个JDBC连接；
 * 使用JDBC查询时，总是使用PreparedStatement进行查询而不是Statement；
 * 查询结果总是ResultSet，即使使用聚合查询也不例外。
 * 
 * public key is not allowed
 * 解决办法：static final String jdbcUrl = "jdbc:mysql://localhost/learnjdbc?allowPublicKeyRetrieval=true&useSSL=false&characterEncoding=utf8";
 * @author dell
 */
public class JDBCDemo {

}
