package com.chinesedreamer.yunwork.api.service;

import com.chinesedreamer.yunwork.api.model.ApiModel;

public interface ApiModelService {
	/**
	 * 检查期望模型是否存在
	 * @param modelName
	 * @return
	 */
	public ApiModel isModelExist(String modelName);
}
