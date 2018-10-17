package com.chinesedreamer.yunwork.api.service;

import java.util.List;

import com.chinesedreamer.yunwork.api.model.ApiModel;

public interface ApiModelService {
	/**
	 * 检查期望模型是否存在
	 * @param modelName
	 * @return
	 */
	public ApiModel isModelExist(String modelName);
	
	public void saveTemplateFolder(String templateName);
	
	/**
	 * 获取配置文件列表
	 * @param modelName
	 * @return
	 */
	public List<ApiModel> getTemplateList(String folderName);
	
	/**
	 * 保存模板
	 * @param modelName
	 * @param templateContent
	 */
	public void saveTemplate(String modelName, String templateContent);
}
