package com.yale.test.spring.orm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public class DbTemplate {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	final JdbcTemplate jdbcTemplate;
	private Map<Class<?>, Mapper<?>> classMapping;
	
	public DbTemplate(JdbcTemplate jdbcTemplate, String basePackage){
		this.jdbcTemplate = jdbcTemplate;
		List<Class<?>> classes = scanEntities(basePackage);
		Map<Class<?>, Mapper<?>> classMapping = new HashMap<>();
		try {
			for(Class<?> clazz: classes) {
				logger.info("Found class: " + clazz.getName());
				Mapper<?> mapper = new Mapper<>(clazz);
				classMapping.put(clazz, mapper);
			}
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
		this.classMapping = classMapping;
	}
	
	
	public <T> T get(Class<T> clazz, Object id) {
		T t = fetch(clazz, id);
		if (t == null) {
			throw new EntityNotFoundException(clazz.getSimpleName());
		}
		return t;
	}
	
	public <T> T fetch(Class<T> clazz, Object id) {
		Mapper<T> mapper = getMapper(clazz);
		logger.info("SQL:" + mapper.selectSQL);
		List<T> list = (List<T>)jdbcTemplate.query(mapper.selectSQL, new Object[]{id}, mapper.rowMapper);
		if (list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}
	
	public <T> void delete(T bean) {
		try {
			Mapper<?> mapper = getMapper(bean.getClass());
			delete(bean.getClass(), mapper.getIdValue(bean));
		} catch(ReflectiveOperationException e){
			throw new PersistenceException(e);
		}
	}
	
	public <T> void delete(Class<T> clazz, Object id) {
		Mapper<?> mapper = getMapper(clazz);
		logger.info("SQL: " + mapper.deleteSQL);
		jdbcTemplate.update(mapper.deleteSQL, id);
	}
	
	public Select select(String... selectFields) {
		return new Select(new Criteria(this), selectFields);
	}
	
	public <T> From<T> from(Class<T> entityClass) {
		Mapper<T> mapper = getMapper(entityClass);
		return new From<>(new Criteria<>(this), mapper);
	}
	
	public <T> void update(T bean) {
		try {
			Mapper<?> mapper = getMapper(bean.getClass());
			Object[] args = new Object[mapper.updatableProperties.size() +1];
			int n=0;
			for (AccessibleProperty prop: mapper.updatableProperties) {
				args[n] = prop.getter.invoke(bean);
				n++;
			}
			args[n] = mapper.id.getter.invoke(bean);
			logger.info("SQL: " + mapper.updateSQL);
			jdbcTemplate.update(mapper.updateSQL, args);
		} catch (ReflectiveOperationException e) {
			throw new PersistenceException(e);
		}
	}
	
	public <T> void insert(T bean) {
		try {
			int rows;
			final Mapper<?> mapper = getMapper(bean.getClass());
			Object[] args = new Object[mapper.insertableProperties.size()];
			int n=0;
			for(AccessibleProperty prop: mapper.insertableProperties) {
				args[n] = prop.getter.invoke(bean);
				n++;
			}
			logger.info("SQL: " + mapper.insertSQL);
			if (mapper.id.isIdentityId()) {
				KeyHolder keyHolder = new GeneratedKeyHolder();
				rows = jdbcTemplate.update(new PreparedStatementCreator(){
					public PreparedStatement createPreparedStatement(Connection connection) throws SQLException{
						PreparedStatement ps = connection.prepareStatement(mapper.insertSQL, Statement.RETURN_GENERATED_KEYS);
						for(int i=0; i<args.length; i++) {
							ps.setObject(i+1, args[i]);
						}
						return ps;
					}
				}, keyHolder);
				if (rows == 1) {
					mapper.id.setter.invoke(bean, keyHolder.getKey());
				}
			} else {
				rows = jdbcTemplate.update(mapper.insertSQL, args);
			}
		} catch(ReflectiveOperationException e){
			throw new PersistenceException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	<T> Mapper<T> getMapper(Class<T> clazz) {
		Mapper<T> mapper = (Mapper<T>) this.classMapping.get(clazz);
		if (mapper == null) {
			throw new RuntimeException("Target class is not a registered entity: " + clazz.getName());
		}
		return mapper;
	}
	
	private static List<Class<?>> scanEntities(String basePackage) {
		ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
		provider.addIncludeFilter(new AnnotationTypeFilter(Entity.class));
		List<Class<?>> classes = new ArrayList<>();
		Set<BeanDefinition> beans = provider.findCandidateComponents(basePackage);
		for (BeanDefinition bean: beans) {
			try {
				classes.add(Class.forName(bean.getBeanClassName()));
			} catch(ClassNotFoundException e) {
				throw new RuntimeException(e);
			}
		}
		return classes;
	}
}
