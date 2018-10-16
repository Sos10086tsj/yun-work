package com.chinesedreamer.yunwork.api.service.impl;

import java.io.File;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.chinesedreamer.yunwork.api.config.ApiConfig;
import com.chinesedreamer.yunwork.api.model.ApiModel;
import com.chinesedreamer.yunwork.api.service.ApiModelService;

@Service
public class ApiModelServiceImpl implements ApiModelService{
	private Logger logger = LoggerFactory.getLogger(ApiModelServiceImpl.class);
	@Resource
	private ApiConfig apiConfig;
	@Override
	public ApiModel isModelExist(String modelName) {
		//1. 获取模板文件
		String[] modelPackage = modelName.split("\\.");
		StringBuilder builder = new StringBuilder();
		builder.append(this.apiConfig.getApiModelTmpFolder());
		for (int i = 0; i < modelPackage.length - 2; i++) {
			builder.append(modelPackage[i]).append(File.separator);
		}
		builder.append(modelPackage[modelPackage.length - 2]).append(".").append(modelPackage[modelPackage.length - 1]);
		
		String templatePath = builder.toString();
		File templateFile = new File(templatePath);
		if (!templateFile.exists()) {
			this.logger.info("model template {} not exist.", templatePath);
			return null;
		}
		//2. 返回model配置
		return new ApiModel(modelName, templatePath);
	}

}
