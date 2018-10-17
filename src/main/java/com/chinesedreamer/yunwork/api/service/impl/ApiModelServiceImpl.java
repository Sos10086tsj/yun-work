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
import com.chinesedreamer.yunwork.api.config.ApplicationConstant;
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
	public List<ApiModel> getTemplateList(String folderName) {
		List<ApiModel> apiModels = new ArrayList<ApiModel>();
		File folder = new File(this.apiConfig.getApiModelTmpFolder());
		List<String> filePaths = new ArrayList<String>();
		for (File file : folder.listFiles()) {
			this.addFile(filePaths, file);
		}
		for (String filePath : filePaths) {
			String templatePath = filePath.substring(folder.getPath().length() + 1);
			if (StringUtils.isNotEmpty(folderName)) {
				if (templatePath.startsWith(folderName)) {
					ApiModel apiModel = new ApiModel();
					apiModel.setModelName(templatePath.substring(templatePath.lastIndexOf(File.separator) + 1));
					apiModel.setTemplatePath(templatePath);
					apiModels.add(apiModel);
				}
			}else {
				ApiModel apiModel = new ApiModel();
				apiModel.setModelName(templatePath.substring(templatePath.lastIndexOf(File.separator) + 1));
				apiModel.setTemplatePath(templatePath);
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
			if (file.getName().endsWith(ApplicationConstant.API_MOCK.TEMPLATE_FILE_PATTERN)) {
				filePaths.add(file.getPath());
			}
		}
	}
	@Override
	public void saveTemplate(String modelName, String template) {
		// TODO Auto-generated method stub
		System.out.println("modelName:" + modelName);
		System.out.println("template:" + template);
	}
	@Override
	public void updateTemplate(String modelName, String template) {
		// TODO Auto-generated method stub
		System.out.println("modelName:" + modelName);
		System.out.println("template:" + template);
	}
	
	@Override
	public void saveTemplateFolde(String templateName) {
		if (!templateName.endsWith(File.separator)) {
			templateName = templateName + File.separator;
		}
		String templatePath = this.apiConfig.getApiModelTmpFolder() + templateName;
		File templateFile = new File(templatePath);
		if (!templateFile.exists()) {
			templateFile.mkdirs();
		}
	}
}
