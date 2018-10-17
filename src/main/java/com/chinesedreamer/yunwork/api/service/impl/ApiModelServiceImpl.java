package com.chinesedreamer.yunwork.api.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
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
		File folder = new File(this.apiConfig.getApiModelTmpFolder() + (StringUtils.isEmpty(folderName) ? "" : folderName));
		
		for (File file : folder.listFiles()) {
			if (file.isDirectory() || file.getName().endsWith(ApplicationConstant.API_MOCK.TEMPLATE_FILE_PATTERN)) {
				ApiModel apiModel = new ApiModel();
				apiModel.setModelName(file.getName());
				String path = file.getPath().substring(this.apiConfig.getApiModelTmpFolder().length());
				apiModel.setTemplatePath(path);
				apiModels.add(apiModel);
			}
		}
		return apiModels;
	}
	
	@Override
	public void saveTemplate(String modelName, String templateContent) {
		try {
			FileUtils.writeStringToFile(new File(this.apiConfig.getApiModelTmpFolder() + modelName), templateContent, ApplicationConstant.SYS.DEFAULT_ENCODING, false);
		} catch (IOException e) {
			this.logger.error("{}", e);
		}
	}
	
	@Override
	public void saveTemplateFolder(String templateName) {
		if (!templateName.endsWith(File.separator)) {
			templateName = templateName + File.separator;
		}
		String templatePath = this.apiConfig.getApiModelTmpFolder() + templateName;
		File templateFile = new File(templatePath);
		if (!templateFile.exists()) {
			templateFile.mkdirs();
		}
	}
	@Override
	public String getModel(String modelName) {
		String templatePath = this.apiConfig.getApiModelTmpFolder() + modelName;
		try {
			return FileUtils.readFileToString(new File(templatePath));
		} catch (IOException e) {
			this.logger.error("{}", e);
			return null;
		}
	}
}
