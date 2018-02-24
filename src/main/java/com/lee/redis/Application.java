package com.lee.redis;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ComponentScan
public class Application {

	public static void main(String[] args) {
		
		SpringApplication app = new SpringApplication(Application.class);
	    app.setBannerMode(Banner.Mode.OFF);			// 是否打印banner
	    // app.setApplicationContextClass();		// 指定spring应用上下文启动类
	    app.setWebEnvironment(false);
	    app.run(args);
	}
}
