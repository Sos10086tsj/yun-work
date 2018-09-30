package com.chinesedreamer.yunwork.api.service;

import com.alibaba.fastjson.JSON;
import com.chinesedreamer.yunwork.api.model.ApiModel;

public interface ApiMockService {
	/**
	 * 模拟数据
	 * @param model
	 * @return
	 */
	public JSON mockData(ApiModel model);
}
