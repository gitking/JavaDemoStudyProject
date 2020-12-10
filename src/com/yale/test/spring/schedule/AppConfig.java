package com.yale.test.spring.schedule;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@ComponentScan
@EnableWebMvc
@EnableScheduling//开启了定时任务的支持：
@EnableTransactionManagement
@PropertySource({"classpath:/jdbc.properties", "classpath:/task.properties"})
public class AppConfig {

}
