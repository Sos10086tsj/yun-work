package com.chinesedreamer.yunwork.api.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;
import com.chinesedreamer.yunwork.api.vo.ApiModelTreeNodelVo;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApiControllerTest {

	@Resource
	private ApiController apiController;
	@Test
	public void testGetMockData() {
		System.out.println("********** start **********");
		HttpServletRequest request = new MockHttpServletRequest();
		Object object = this.apiController.getMockData(request, "temp_test_1.tmp");
		System.out.println("object");
		System.out.println(JSON.toJSONString(object));
		System.out.println("********** end **********");
	}

	@Test
	public void testTemplateList() {
		System.out.println("********** start **********");
		ApiModelTreeNodelVo treeNodelVo = this.apiController.getTemplateList("aaa/bbb/");
		System.out.println(JSON.toJSONString(treeNodelVo));
		System.out.println("********** end **********");
	}
	
	@Test
	public void testSaveTemplate() {
		System.out.println("********** start **********");
		this.apiController.saveTemplate("aaa/bbb/ccc");
		System.out.println("********** end **********");
	}
}
