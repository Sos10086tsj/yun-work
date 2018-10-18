package com.chinesedreamer.yunwork.api.service;

import java.util.Map;

import com.chinesedreamer.yunwork.api.model.ApiModel;

public interface ApiMockService {
	/**
	 * 模拟数据
	 * @param model
	 * @param parameters
	 * @return
	 */
	public Object mockData(ApiModel model, Map<String, Object> parameters);
}
