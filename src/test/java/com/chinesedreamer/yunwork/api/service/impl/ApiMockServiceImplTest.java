package com.chinesedreamer.yunwork.api.service.impl;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;
import com.chinesedreamer.yunwork.api.model.ApiModel;
import com.chinesedreamer.yunwork.api.service.ApiMockService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApiMockServiceImplTest {

	@Resource
	private ApiMockService apiMockService;
	
	@Test
	public void testMockData() {
		System.err.println("********** start ***********");
		ApiModel model = new ApiModel();
		model.setModelName("test");
		model.setTemplatePath("/Users/paris/Desktop/data/yunwork/tmp/temp_test_1.tmp");
		JSON json = this.apiMockService.mockData(model);
		System.out.println("********** json ***********");
		System.out.println(json.toJSONString());
		System.err.println("********** end ***********");
	}

}
