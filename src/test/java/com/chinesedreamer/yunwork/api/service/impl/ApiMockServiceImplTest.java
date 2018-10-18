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
		model.setTemplatePath("/Users/paris/Desktop/data/yunwork/tmp/aaa/bbb/ccc/test_1539745389512.tmp");
		Object json = this.apiMockService.mockData(model, null);
		System.out.println("********** json ***********");
		System.out.println(JSON.toJSONString(json));
		System.err.println("********** end ***********");
	}
}
