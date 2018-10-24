package com.chinesedreamer.yunwork.tools.java.impl;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.chinesedreamer.yunwork.tools.java.dynamic.JavaMockTmplateService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JavaMockTmplateServiceImplTest {

	@Resource
	private JavaMockTmplateService javaMockTmplateService;
	@Test
	public void testTransfer2Template() {
		System.out.println("********** start");
		this.javaMockTmplateService.transfer2Template("/Applications/dev/workspace/eclipse/log4spring/src/main/java/com/chinesedreamer/log4spring/testbean/");
		System.out.println("********** end");
	}

}
