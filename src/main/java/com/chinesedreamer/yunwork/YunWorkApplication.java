package com.chinesedreamer.yunwork;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class YunWorkApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication springApplication = new SpringApplication(YunWorkApplication.class);
		springApplication.addListeners(new ApplicationStartup());
		springApplication.run(args);
	}
	
	@Override//为了打包springboot项目
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(this.getClass());
    }
}
