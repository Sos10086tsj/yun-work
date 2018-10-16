package com.chinesedreamer.yunwork.api.service;

import com.chinesedreamer.yunwork.api.model.ApiModel;

public interface ApiMockService {
	/**
	 * 模拟数据
	 * @param model
	 * @return
	 */
	public Object mockData(ApiModel model);
}
