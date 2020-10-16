package com.yale.test.jdbc;

/*
 * JDBC更新
 * 数据库操作总结起来就四个字：增删改查，行话叫CRUD：Create，Retrieve，Update和Delete。
 * 查就是查询，我们已经讲过了，就是使用PreparedStatement进行各种SELECT，然后处理结果集。现在我们来看看如何使用JDBC进行增删改。
 * 插入
 * 插入操作是INSERT，即插入一条新记录。通过JDBC进行插入，本质上也是用PreparedStatement执行一条SQL语句，不过最后执行的不是executeQuery()，而是executeUpdate()。示例代码如下：
 * try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
	    try (PreparedStatement ps = conn.prepareStatement(
	            "INSERT INTO students (id, grade, name, gender) VALUES (?,?,?,?)")) {
	        ps.setObject(1, 999); // 注意：索引从1开始
	        ps.setObject(2, 1); // grade
	        ps.setObject(3, "Bob"); // name
	        ps.setObject(4, "M"); // gender
	        int n = ps.executeUpdate(); // 1
	    }
	}
 * 设置参数与查询是一样的，有几个?占位符就必须设置对应的参数。虽然Statement也可以执行插入操作，但我们仍然要严格遵循绝不能手动拼SQL字符串的原则，以避免安全漏洞。
 * 当成功执行executeUpdate()后，返回值是int，表示插入的记录数量。此处总是1，因为只插入了一条记录。
 * 插入并获取主键
 * 如果数据库的表设置了自增主键，那么在执行INSERT语句时，并不需要指定主键，数据库会自动分配主键。对于使用自增主键的程序，有个额外的步骤，就是如何获取插入后的自增主键的值。
 * 要获取自增主键，不能先插入，再查询。因为两条SQL执行期间可能有别的程序也插入了同一个表。获取自增主键的正确写法是在创建PreparedStatement的时候，指定一个RETURN_GENERATED_KEYS标志位，表示JDBC驱动必须返回插入的自增主键。示例代码如下：
 * try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
	    try (PreparedStatement ps = conn.prepareStatement(
	            "INSERT INTO students (grade, name, gender) VALUES (?,?,?)",
	            Statement.RETURN_GENERATED_KEYS)) {
	        ps.setObject(1, 1); // grade
	        ps.setObject(2, "Bob"); // name
	        ps.setObject(3, "M"); // gender
	        int n = ps.executeUpdate(); // 1
	        try (ResultSet rs = ps.getGeneratedKeys()) {
	            if (rs.next()) {
	                long id = rs.getLong(1); // 注意：索引从1开始
	            }
	        }
	    }
	}
 * 观察上述代码，有两点注意事项：
 * 一是调用prepareStatement()时，第二个参数必须传入常量Statement.RETURN_GENERATED_KEYS，否则JDBC驱动不会返回自增主键；
 * 二是执行executeUpdate()方法后，必须调用getGeneratedKeys()获取一个ResultSet对象，这个对象包含了数据库自动生成的主键的值，读取该对象的每一行来获取自增主键的值。如果一次插入多条记录，那么这个ResultSet对象就会有多行返回值。如果插入时有多列自增，那么ResultSet对象的每一行都会对应多个自增值（自增列不一定必须是主键）。
 * 更新
 * 更新操作是UPDATE语句，它可以一次更新若干列的记录。更新操作和插入操作在JDBC代码的层面上实际上没有区别，除了SQL语句不同：
 * try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
	    try (PreparedStatement ps = conn.prepareStatement("UPDATE students SET name=? WHERE id=?")) {
	        ps.setObject(1, "Bob"); // 注意：索引从1开始
	        ps.setObject(2, 999);
	        int n = ps.executeUpdate(); // 返回更新的行数
	    }
	}
 * executeUpdate()返回数据库实际更新的行数。返回结果可能是正数，也可能是0（表示没有任何记录更新）。
 * 删除操作是DELETE语句，它可以一次删除若干列。和更新一样，除了SQL语句不同外，JDBC代码都是相同的：
 * try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
	    try (PreparedStatement ps = conn.prepareStatement("DELETE FROM students WHERE id=?")) {
	        ps.setObject(1, 999); // 注意：索引从1开始
	        int n = ps.executeUpdate(); // 删除的行数
	    }
	}
 * 小结
 * 使用JDBC执行INSERT、UPDATE和DELETE都可视为更新操作；
 * 更新操作使用PreparedStatement的executeUpdate()进行，返回受影响的行数。
 * ResultSet 也提供了 updateRow()，insertRow() 等方法。
 * 用这些方法还是用 executeUpdate() 配合 SQL 语句哪种更好呢
 */
public class JDBCUpdate {

}
