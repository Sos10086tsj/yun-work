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
	
	/**
	 * 获取配置文件列表
	 * @param modelName
	 * @return
	 */
	public List<ApiModel> getTemplateList(String modelName);
	
	/**
	 * 保存模板
	 * @param modelName
	 * @param template
	 */
	public void saveTemplate(String modelName, String template);
	/**
	 * 更新模板
	 * @param modelName
	 * @param template
	 */
	public void updateTemplate(String modelName, String template);
}
