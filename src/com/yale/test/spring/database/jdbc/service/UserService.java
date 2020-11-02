package com.yale.test.spring.database.jdbc.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

/*
 * 现在，所有准备工作都已完毕。我们只需要在需要访问数据库的Bean中，注入JdbcTemplate即可：
 * JdbcTemplate用法
 * Spring提供的JdbcTemplate采用Template模式，提供了一系列以回调为特点的工具方法，目的是避免繁琐的try...catch语句。
 * 我们以具体的示例来说明JdbcTemplate的用法。
 * 首先我们看T execute(ConnectionCallback<T> action)方法，它提供了Jdbc的Connection供我们使用：
 * 也就是说，下述回调方法允许获取Connection，然后做任何基于Connection的操作。
 * 
 * 我们总结一下JdbcTemplate的用法，那就是：
 * 1.针对简单查询，优选query()和queryForObject()，因为只需提供SQL语句、参数和RowMapper；
 * 2.针对更新操作，优选update()，因为只需提供SQL语句和参数；
 * 3.任何复杂的操作，最终也可以通过execute(ConnectionCallback)实现，因为拿到Connection就可以做任何JDBC操作。
 * 实际上我们使用最多的仍然是各种查询。如果在设计表结构的时候，能够和JavaBean的属性一一对应，那么直接使用BeanPropertyRowMapper就很方便。如果表结构和JavaBean不一致怎么办？那就需要稍微改写一下查询，使结果集的结构和JavaBean保持一致。
 * 例如，表的列名是office_address，而JavaBean属性是workAddress，就需要指定别名，改写查询如下：
 * SELECT id, email, office_address AS workAddress, name FROM users WHERE email = ?
 * 小结
 * Spring提供了JdbcTemplate来简化JDBC操作；
 * 使用JdbcTemplate时，根据需要优先选择高级方法；
 * 任何JDBC操作都可以使用保底的execute(ConnectionCallback)方法。
 */
@Component
public class UserService {

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	/*
	 * 首先我们看T execute(ConnectionCallback<T> action)方法，它提供了Jdbc的Connection供我们使用：
	 */
	public User getUserById(long id) {
		return jdbcTemplate.execute((Connection conn) ->{// 注意传入的是ConnectionCallback:
			// 可以直接使用conn实例，不要释放它，回调结束后JdbcTemplate自动释放:
	        // 在内部手动创建的PreparedStatement、ResultSet必须用try(...)释放:
			//也就是说，上述回调方法允许获取Connection，然后做任何基于Connection的操作。
			try(PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE id = ?")){
				ps.setObject(1, id);
				try(ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						return new User(rs.getLong("id"), rs.getString("email"),
								rs.getString("password"),
								rs.getString("name"));
					}
					throw new RuntimeException("user not found by id.");
				}
			}
		});
	}
	
	/*
	 * 我们再看T execute(String sql, PreparedStatementCallback<T> action)的用法：
	 */
	public User getUserByName(String name) {
		//需要传入SQL语句，以及PreparedStatementCallback:
		return jdbcTemplate.execute("SELECT * FROM users WHERE name=?", 
		(PreparedStatement ps)->{//PreparedStatement实例已经由JdbcTemplate创建，并在回调后自动释放:
			ps.setObject(1, name);
			try(ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return new User(
							rs.getLong("id"),
							rs.getString("email"),
							rs.getString("password"),
							rs.getString("name"));
				}
				throw new RuntimeException("user not found by id.");
			}
		});
	}
	
	/*
	 * 最后，我们看T queryForObject(String sql, Object[] args, RowMapper<T> rowMapper)方法：
	 * 在queryForObject()方法中，传入SQL以及SQL参数后，JdbcTemplate会自动创建PreparedStatement，自动执行查询并返回ResultSet，
	 * 我们提供的RowMapper需要做的事情就是把ResultSet的当前行映射成一个JavaBean并返回。整个过程中，使用Connection、PreparedStatement和ResultSet都不需要我们手动管理。
	 * RowMapper不一定返回JavaBean，实际上它可以返回任何Java对象。例如，使用SELECT COUNT(*)查询时，可以返回Long：
	 */
	public User getUserByEmail(String email) {
	    // 传入SQL，参数和RowMapper实例:
		return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email = ?", new Object[]{email},
				(ResultSet rs, int rowNum)->{// 将ResultSet的当前行映射为一个JavaBean:
					return new User(
							rs.getLong("id"),
							rs.getString("email"),
							rs.getString("password"),
							rs.getString("name")
							);
				}
				);
	}
	
	/*
	 * RowMapper不一定返回JavaBean，实际上它可以返回任何Java对象。例如，使用SELECT COUNT(*)查询时，可以返回Long：
	 */
	public long getUsers() {
		return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM users", null, (ResultSet rs, int rowNum)->{
			//SELECT COUNT(*) 查询只有一列,取第一列数据:
			return rs.getLong(1);
		});
	}
	
	
	/*
	 * 如果我们期望返回多行记录，而不是一行，可以用query()方法：
	 * 下述query()方法传入的参数仍然是SQL、SQL参数以及RowMapper实例。这里我们直接使用Spring提供的BeanPropertyRowMapper。
	 * 如果数据库表的结构恰好和JavaBean的属性名称一致，那么BeanPropertyRowMapper就可以直接把一行记录按列名转换为JavaBean。
	 */
	public List<User> getUsers(int pageIndex) {
		int limit = 100;
		int offset = limit * (pageIndex -1);
		return jdbcTemplate.query("SELECT * FROM users LIMIT ? OFFSET ?", new Object[]{limit, offset}, new BeanPropertyRowMapper<>(User.class)); 
	}
	
	/*
	 * 如果我们执行的不是查询，而是插入、更新和删除操作，那么需要使用update()方法：
	 */
	public void updateUser(User user){
		//传入SQL,SQL参数,返回更新的行数:
		if (1 != jdbcTemplate.update("UPDATE user SET name = ? WHERE id=?", user.getName(), user.getId())){
			throw new RuntimeException("User not found by id");
		}
	}
	
	/*
	 * 只有一种INSERT操作比较特殊，那就是如果某一列是自增列（例如自增主键），通常，我们需要获取插入后的自增值。JdbcTemplate提供了一个KeyHolder来简化这一操作：
	 * JdbcTemplate还有许多重载方法，这里我们不一一介绍。需要强调的是，JdbcTemplate只是对JDBC操作的一个简单封装，它的目的是尽量减少手动编写try(resource) {...}的代码，
	 * 对于查询，主要通过RowMapper实现了JDBC结果集到Java对象的转换。
	 */
	public User register(String email, String password, String name) {
		//创建一个KeyHolder:
		KeyHolder holder = new GeneratedKeyHolder();
		if (1!= jdbcTemplate.update(
				//参数1:PreparedStatementCreator
				(conn) -> {
					//创建PreparedStatement时,必须指定RETURN_GENERATED_KEYS
					PreparedStatement ps = conn.prepareStatement("INSERT INTO users(email, password, name) VALUES(?,?,?)", Statement.RETURN_GENERATED_KEYS);
					ps.setObject(1, email);
					ps.setObject(2, password);
					ps.setObject(3, name);
					return ps;
				},holder//参数2:KeyHolder
		)) {
			throw new RuntimeException("Insert failed.");
		}//从KeyHolder中获取返回的自增值
		return new User(holder.getKey().longValue(), email, password, name);
	}
	
	public User login(String email, String password){
		User user = getUserByEmail(email);
		if (user.getPassword().equals(password)) {
			return user;
		}
		throw new RuntimeException("login failed.");
	}
}
