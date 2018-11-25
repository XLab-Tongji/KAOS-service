package com.test;

import com.test.filter.SessionFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public FilterRegistrationBean registrationBean(){
		FilterRegistrationBean bean = new FilterRegistrationBean();
		bean.addUrlPatterns("/*");
		bean.setFilter(new SessionFilter());
		return bean;
	}
}