package com.chinesedreamer.yunwork.api.service.impl;

import java.io.File;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
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
		String[] modelPackage = modelName.split(".");
		StringBuilder builder = new StringBuilder();
		builder.append(this.apiConfig.getApiModelRootFolder());
		for (String packageName : modelPackage) {
			if (StringUtils.isNotEmpty(packageName)) {
				builder.append(packageName.trim());
			}
		}
		
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
