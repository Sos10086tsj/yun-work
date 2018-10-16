package com.chinesedreamer.yunwork.api.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
//		String[] modelPackage = modelName.split("\\.");
//		StringBuilder builder = new StringBuilder();
//		builder.append(this.apiConfig.getApiModelTmpFolder());
//		for (int i = 0; i < modelPackage.length - 2; i++) {
//			builder.append(modelPackage[i]).append(File.separator);
//		}
//		builder.append(modelPackage[modelPackage.length - 2]).append(".").append(modelPackage[modelPackage.length - 1]);
//		
		String templatePath = this.apiConfig.getApiModelTmpFolder() + modelName;
		File templateFile = new File(templatePath);
		if (!templateFile.exists()) {
			this.logger.info("model template {} not exist.", templatePath);
			return null;
		}
		//2. 返回model配置
		return new ApiModel(modelName, templatePath);
	}
	@Override
	public List<ApiModel> getTemplateList(String modelName) {
		List<ApiModel> apiModels = new ArrayList<ApiModel>();
		File folder = new File(this.apiConfig.getApiModelTmpFolder());
		List<String> filePaths = new ArrayList<String>();
		for (File file : folder.listFiles()) {
			this.addFile(filePaths, file);
		}
		for (String filePath : filePaths) {
			String templatePath = filePath.substring(folder.getPath().length() + 1);
			if (StringUtils.isNotEmpty(modelName)) {
				if (templatePath.contains(modelName)) {
					ApiModel apiModel = new ApiModel();
					apiModel.setModelName(templatePath);
					apiModels.add(apiModel);
				}
			}else {
				ApiModel apiModel = new ApiModel();
				apiModel.setModelName(templatePath);
				apiModels.add(apiModel);
			}
			
		}
		return apiModels;
	}

	private void addFile(List<String> filePaths, File file) {
		if (file.isDirectory()) {
			for (File subFile : file.listFiles()) {
				this.addFile(filePaths, subFile);
			}
		}else {
			if (file.getName().endsWith(".tmp")) {
				filePaths.add(file.getPath());
			}
		}
	}
}
