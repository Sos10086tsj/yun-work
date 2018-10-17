package com.chinesedreamer.yunwork;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.chinesedreamer.yunwork.api.config.ApiConfig;

public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent>{

	private Logger logger = LoggerFactory.getLogger(ApplicationStartup.class);
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		this.logger.info("********** execute initialization steps after startup **********");
		
		ApplicationContext ac = event.getApplicationContext();
		this.initApiConfig(ac.getBean(ApiConfig.class));
		
		this.logger.info("********** initialized **********");
	}

	private void initApiConfig(ApiConfig apiConfig) {
		this.logger.info("********** check api config **********");
		File rootFolder = new File(apiConfig.getApiModelRootFolder());
		if (!rootFolder.exists()) {
			rootFolder.mkdirs();
		}
		File tplFolder = new File(apiConfig.getApiModelTmpFolder());
		if (!tplFolder.exists()) {
			tplFolder.mkdirs();
		}
		File libFodler = new File(apiConfig.getApiMockWordLibrary());
		if (!libFodler.exists()) {
			libFodler.mkdirs();
		}
	}
}
