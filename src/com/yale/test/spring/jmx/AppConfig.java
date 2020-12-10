package com.yale.test.spring.jmx;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@ComponentScan
@EnableWebMvc
@EnableMBeanExport//首先在AppConfig中加上@EnableMBeanExport注解，告诉Spring自动注册MBean：
@EnableTransactionManagement
@PropertySource({"classpath:/jdbc.properties"})
public class AppConfig {

}
