package com.yale.test.spring.mybatis.lxf.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.yale.test.spring.mybatis.lxf.entity.User;

public interface UserMapper {
	
	/*
	 * 和Hibernate不同的是，MyBatis使用Mapper来实现映射，而且Mapper必须是接口。我们以User类为例，在User类和users表之间映射的UserMapper编写如下：
	 * 注意：这里的Mapper不是JdbcTemplate的RowMapper的概念，它是定义访问users表的接口方法。比如我们定义了一个User getById(long)的主键查询方法，不仅要定义接口方法本身，
	 * 还要明确写出查询的SQL，这里用注解@Select标记。
	 * SQL语句的任何参数，都与方法参数按名称对应。例如，方法参数id的名字通过注解@Param()标记为id，则SQL语句里将来替换的占位符就是#{id}。
	 */
	@Select("SELECT * FROM users WHERE id = #{id}")
	User getById(@Param("id") long id);
	
	@Select("SELECT * FROM users WHERE email = #{email}")
	User getByEmail(@Param("email")String email);
	
	/*
	 * 如果有多个参数，那么每个参数命名后直接在SQL中写出对应的占位符即可：
	 * 注意：MyBatis执行查询后，将根据方法的返回类型自动把ResultSet的每一行转换为User实例，转换规则当然是按列名和属性名对应。如果列名和属性名不同，最简单的方式是编写SELECT语句的别名：
	 * -- 列名是created_time，属性名是createdAt:
	 * SELECT id, name, email, created_time AS createdAt FROM users
	 */
	@Select("SELECT * FROM users LIMIT #{offset}, #{maxResults}")
	List<User> getAll(@Param("offset")int offset, @Param("maxResults")int maxResults);
	
	/*
	 * 执行INSERT语句就稍微麻烦点，因为我们希望传入User实例，因此，定义的方法接口与@Insert注解如下：
	 * 下述方法传入的参数名称是user，参数类型是User类，在SQL中引用的时候，以#{obj.property}的方式写占位符。和Hibernate这样的全自动化ORM相比，MyBatis必须写出完整的INSERT语句。
	 * 如果users表的id是自增主键，那么，我们在SQL中不传入id，但希望获取插入后的主键，需要再加一个@Options注解：
	 * keyProperty和keyColumn分别指出JavaBean的属性和数据库的主键列名。
	 */
	@Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
	@Insert("INSERT INTO users (email, password, name, createdAt) VALUES (#{user.email},#{user.password},"
			+ "#{user.name},#{user.createdAt})")
	void insert(@Param("user")User user);
	
	/*
	 * 执行UPDATE和DELETE语句相对比较简单，我们定义方法如下：
	 * 有了UserMapper接口，还需要对应的实现类才能真正执行这些数据库操作的方法。虽然可以自己写实现类，但我们除了编写UserMapper接口外，还有BookMapper、BonusMapper……一个一个写太麻烦，
	 * 因此，MyBatis提供了一个MapperFactoryBean来自动创建所有Mapper的实现类。可以用一个简单的注解来启用它：
	 * @MapperScan("com.itranswarp.learnjava.mapper")
	 */
	@Update("UPDATE users SET name = #{user.name}, createdAt=#{user.createdAt} WHERE id = #{user.id}")
	void update(@Param("user")User user);
	
	@Delete("DELETE FROM users WHERE id =#{id}")
	void deleteById(@Param("id")long id);
}
