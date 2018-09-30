package com.chinesedreamer.yunwork.api.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chinesedreamer.yunwork.api.model.ApiModel;
import com.chinesedreamer.yunwork.api.service.ApiMockService;
import com.chinesedreamer.yunwork.api.service.ApiModelService;

@RestController(value = "api")
public class ApiController {
	
	@Resource
	private ApiModelService apiModelService;
	@Resource
	private ApiMockService apiMockService;

	/**
	 * 获取模拟的api数据
	 * @param request
	 * @param modelName
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "mockData")
	public JSON getMockData(HttpServletRequest request, @RequestParam String modelName){
		//1. 校验model是否存在
		ApiModel model = this.apiModelService.isModelExist(modelName);
		if (null == model) {
			return this.getFailureJson("Model " + modelName + " is not exist. Please add it first.");
		}
		//2. 交给适配器模拟数据
		return this.apiMockService.mockData(model);
	}
	private JSON getFailureJson(String message) {
		JSONObject jo = new JSONObject();
		jo.put("message", message);
		return (JSON)jo;
	}
}
