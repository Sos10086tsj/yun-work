package com.chinesedreamer.yunwork.api.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chinesedreamer.yunwork.api.config.ApiConfig;
import com.chinesedreamer.yunwork.api.model.ApiModel;
import com.chinesedreamer.yunwork.api.service.ApiMockService;

public class ApiMockServiceImpl implements ApiMockService{
	
	private Logger logger = LoggerFactory.getLogger(ApiMockServiceImpl.class);

	@Resource
	private ApiConfig apiConfig;
	@Override
	public JSON mockData(ApiModel model) {
		//	1.	读取文件
		File templateFile = new File(model.getTemplatePath());
		//	2.	解析不同节点，并根据生成器生产内容
		return this.mockTemplateData(templateFile);
	}

	/**
	 * 模拟
	 * @param templateFile
	 * @return
	 */
	private JSON mockTemplateData(File templateFile) {
		JSONObject jo = new JSONObject();
		try {
			List<String> properties = new ArrayList<String>();
			FileInputStream fis = new FileInputStream(templateFile);
			InputStreamReader isr = new InputStreamReader(fis, this.apiConfig.getApiModelTemplateEncode());
			BufferedReader br = new BufferedReader(isr);
			String tmpStr = null;
			while (null != (tmpStr = br.readLine())) {
				if (StringUtils.isNotEmpty(tmpStr)) {
					properties.add(tmpStr.trim());
				}
			}
			br.close();
			isr.close();
			fis.close();
			
			
			List<String> listProperties = new ArrayList<String>();//list 类型的属性
			List<String> mapProperties = new ArrayList<String>();//map 类型的属性
			List<String> modelProperties = new ArrayList<String>();//外联model 类型的属性
			for (String property : properties) {
				if (this.match(property, this.apiConfig.getApiModelPropertyPatternList())) {
					listProperties.add(property);
				}else if (this.match(property, this.apiConfig.getApiModelPropertyPatternMap())) {
					mapProperties.add(property);
				}else if (this.match(property, this.apiConfig.getApiModelPropertyPatternModel())) {
					modelProperties.add(property);
				}else {
					this.mockProperty(jo, property);
				}
			}
			this.mockListProperties(jo, listProperties);
			this.mockMapProperties(jo, mapProperties);
			this.mockModelProperties(jo, modelProperties);
		} catch (Exception e) {
			this.logger.error("{}", e);
		}
		return jo;
	}
	
	/**
	 * 模拟某个字段的值
	 * @param jo
	 * @param property
	 * @throws Exception 
	 */
	private void mockProperty(JSONObject jo, String property){
		String libraries = this.apiConfig.getApiMockWordLibrary();
		String[] libs = libraries.split(",");
		int idx = RandomUtils.nextInt(0, libs.length);
		String lib = libs[idx];
		int lineIdx = RandomUtils.nextInt(0, 100);
		String tmpStr = null;
		try {
			FileInputStream fis = new FileInputStream(lib);
			InputStreamReader isr = new InputStreamReader(fis, this.apiConfig.getApiModelTemplateEncode());
			BufferedReader br = new BufferedReader(isr);
			int i = 0;
			while (null != (tmpStr = br.readLine())) {
				i++;
				if (i == lineIdx) {
					break;
				}
			}
			br.close();
			isr.close();
			fis.close();
		} catch (Exception e) {
			this.logger.error("{}", e);
		}
		jo.put(property, tmpStr);
	}
	
	private boolean match(String str, String pattern) {
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(str);
		return m.matches();
	}
	
	/**
	 * 模拟list类型的数据
	 * @param jo
	 * @param listProperties
	 */
	private void mockListProperties(JSONObject jo, List<String> listProperties){
		
	}
	
	/**
	 * 模拟 map类型的数据
	 * @param jo
	 * @param mapProperties
	 */
	private void mockMapProperties(JSONObject jo, List<String> mapProperties) {
		
	}
	
	/**
	 * 模拟model类型的数据
	 * @param jo
	 * @param modelProperties
	 */
	private void mockModelProperties(JSONObject jo, List<String> modelProperties) {
		
	}
}
