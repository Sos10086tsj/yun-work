package com.chinesedreamer.yunwork.api.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chinesedreamer.yunwork.api.config.ApiConfig;
import com.chinesedreamer.yunwork.api.model.ApiModel;
import com.chinesedreamer.yunwork.api.service.ApiMockService;

public class ApiMockServiceImpl implements ApiMockService{

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
			
			JSONObject jo = new JSONObject();
			List<String> listProperties = new ArrayList<String>();//list 类型的属性
			List<String> mapProperties = new ArrayList<String>();//map 类型的属性
			List<String> modelProperties = new ArrayList<String>();//外联model 类型的属性
			for (String property : properties) {
				if (match(property, this.apiConfig.getApiModelPropertyPatternList())) {
					listProperties.add(property);
				}else if (match(property, this.apiConfig.getApiModelPropertyPatternMap())) {
					mapProperties.add(property);
				}else if (match(property, this.apiConfig.getApiModelPropertyPatternModel())) {
					modelProperties.add(property);
				}else {
					this.mockProperty(jo, property);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	/**
	 * 模拟某个字段的值
	 * @param jo
	 * @param property
	 */
	private void mockProperty(JSONObject jo, String property) {
		if (property.contains("[")) {//不包含 [] 
			
		}
	}
	
	private boolean match(String str, String pattern) {
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(str);
		return m.matches();
	}
}
