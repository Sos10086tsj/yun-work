package com.chinesedreamer.yunwork.api.service.impl;

import java.io.File;
import java.io.IOException;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.chinesedreamer.yunwork.api.service.ApiModelService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApiModelServiceImplTest {

	@Resource
	private ApiModelService apiModelService;
	@Test
	public void testSaveTemplate() {
		System.err.println("********** start ***********");
		String templateName = "aaa/bbb/ccc/test_1539745389512.tmp";
		File file = new File("/Users/paris/Desktop/test.txt");
		try {
			this.apiModelService.saveTemplate(templateName, FileUtils.readFileToString(file));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.err.println("********** end ***********");
	}

}
