package com.chinesedreamer.yunwork.api.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.chinesedreamer.yunwork.api.config.ApiConfig;
import com.chinesedreamer.yunwork.api.config.ApplicationConstant;
import com.chinesedreamer.yunwork.api.model.ApiModel;
import com.chinesedreamer.yunwork.api.model.ApiPropertyType;
import com.chinesedreamer.yunwork.api.model.DynamicBean;
import com.chinesedreamer.yunwork.api.model.MockProperty;
import com.chinesedreamer.yunwork.api.service.ApiMockService;

@Service
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
		JSON json = null;
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
			
			json = this.convertObject(properties);
		} catch (Exception e) {
			this.logger.error("{}", e);
			return null;
		}
		return json;
	}
	
	/**
	 * 根据属性配置生成对象
	 * @param propertyConfigurations
	 * @return
	 */
	private JSON convertObject(List<String> propertyConfigurations) throws Exception{
		Map<String, Class<?>> propertyMap = new HashMap<String, Class<?>>();
		Map<String, Object> valueMap = new HashMap<String, Object>();
		//封装map
		for (String property : propertyConfigurations) {
			MockProperty mockProperty = this.convertProperty(property);
			propertyMap.put(mockProperty.getPropertyName(), mockProperty.getClazz());
			valueMap.put(mockProperty.getPropertyName(), mockProperty.getValue());
		}
		Object object = this.mockObject(propertyMap, valueMap);
		return (JSON)JSON.toJSON(object);
	}
	
	/**
	 * confStr [type|约束条件|value]
	 * property[int|min,max|value]
	 * property[string|length|value]
	 * property[decimal|length,precision|value]
	 * property[list|modelName|size]
	 * property[model|modelName]
	 * @param confStr
	 * @return
	 */
	private MockProperty convertProperty(String confStr) {
		MockProperty mockProperty = new MockProperty();
		try {
			int confStart = confStr.indexOf(ApplicationConstant.API_MOCK.PROP_CONF_START_DELIMETER) + 1;
			int confEnd = confStr.indexOf(ApplicationConstant.API_MOCK.PROP_CONF_END_DELIMETER);
			String[] confs = confStr.substring(confStart, confEnd).split(ApplicationConstant.API_MOCK.PROP_CONF_SEPERATE_DELIMETER);
			String propertyName = confStr.substring(0, confStart - 1);
			mockProperty.setPropertyName(propertyName);;
			
			ApiPropertyType propertyType = ApiPropertyType.get(confs[0]);
			switch (propertyType) {
				case STRING:
					mockProperty.setClazz(String.class);
					mockProperty.setValue(this.mockPropertyValue(String.class, convertFinalConfs(confs,4)));
				break;
				case INT:
					mockProperty.setClazz(Integer.class);
					mockProperty.setValue(this.mockPropertyValue(Integer.class, convertFinalConfs(confs,4)));
				break;
				case DECIMAL:
					mockProperty.setClazz(BigDecimal.class);
					mockProperty.setValue(this.mockPropertyValue(BigDecimal.class, convertFinalConfs(confs,5)));
				break;
				case LIST:
					mockProperty.setClazz(List.class);
					//TODO
				break;
				case MODEL:
					mockProperty.setClazz(Class.forName(confs[2]));
					//TODO
				break;
				default:
				break;
			}
		} catch (Exception e) {
			this.logger.error("{}", e);
		}
		return mockProperty;
	}
	
	private String[] convertFinalConfs(String[] confs, int size) {
		if (confs.length == size) {
			return confs;
		}else {
			String[] fConfs = new String[size];
			for (int i = 0; i < confs.length; i++) {
				fConfs[i] = confs[i];
			}
			return fConfs;
		}
	}
	
	/**
	 * 模拟字段值
	 * @param clazz
	 * @param configurations
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private <T> T mockPropertyValue(Class<T> clazz, String... configurations) {
		if (clazz.equals(Integer.class)) {//configurations = int,min, max, value
			if (StringUtils.isNotEmpty(configurations[3])) {
				return (T)Integer.valueOf(configurations[3]);
			}else {
				int min = 10000;//默认 10000 - 100000
				int max = 100000;
				if (StringUtils.isNotEmpty(configurations[1]) && StringUtils.isNotEmpty(configurations[2])) {
					min = Integer.valueOf(configurations[1]);
					max = Integer.valueOf(configurations[2]);
				}else if (StringUtils.isNotEmpty(configurations[1]) && StringUtils.isEmpty(configurations[2])) {
					min = Integer.valueOf(configurations[1]);
					max = min + max;
				}else if (StringUtils.isEmpty(configurations[1]) && StringUtils.isNotEmpty(configurations[2])) {
					max = Integer.valueOf(configurations[2]);
					min = max - min > 0 ? max - min : 1;
				}
				Integer random = RandomUtils.nextInt(min, max);
				return (T)random;
			}
		}else if (clazz.equals(BigDecimal.class)) {//configurations = deciaml,length, precision, value
			if (StringUtils.isNotEmpty(configurations[3])) {
				return (T)(new BigDecimal(configurations[3]));
			}else {
				int length = 7;
				int precision = 2;
				if (StringUtils.isNotEmpty(configurations[1])) {
					length = Integer.valueOf(configurations[1]);
				}
				if (StringUtils.isNotEmpty(configurations[2])) {
					precision = Integer.valueOf(configurations[2]);
				}
				StringBuilder builder = new StringBuilder();
				int tmpInt_1 = 0;
				for (int i = 0; i < length; i++) {
					int tmpInt = RandomUtils.nextInt(1, 9);
					tmpInt_1 = tmpInt_1 * 10 + tmpInt;
				}
				int tmpInt_2 = 0;
				for (int i = 0; i < precision; i++) {
					int tmpInt = RandomUtils.nextInt(1, 9);
					tmpInt_2 = tmpInt_2 * 10 + tmpInt;
				}
				builder.append(tmpInt_1).append(".").append(tmpInt_2);
				return (T)(new BigDecimal(builder.toString()));
			}
		}else if (clazz.equals(String.class)) {//configurations = string,length, value
			if (StringUtils.isNotEmpty(configurations[2])) {
				return (T)(String)(configurations[2]);
			}else {
				return (T)this.randomString(StringUtils.isEmpty(configurations[1]) ? null : Integer.valueOf(configurations[1]));
			}
			
		}
		return null;
	}
	
	/**
	 * 随机生成字符串
	 * @param length
	 * @return
	 */
	private String randomString(Integer length) {
		File folder = new File(this.apiConfig.getApiMockWordLibrary());
		int fileNum = folder.list().length;
		int randomIdx = RandomUtils.nextInt(0, fileNum);
		File file = folder.listFiles()[randomIdx];
		List<String> lines = new ArrayList<String>();
		
		try {
			InputStream is = new FileInputStream(file);
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
			String line = null;
			while ((line = br.readLine()) != null) {
				lines.add(line);
			}
			br.close();
			is.close();
		} catch (Exception e) {
			this.logger.error("{}", e);
		}
		return lines.get(RandomUtils.nextInt(0, lines.size()));
	}
	
	/**
	 * 模拟生成对象
	 * @param propertyMap
	 * @param valueMap
	 * @return
	 * @throws Exception
	 */
	private Object mockObject(Map<String, Class<?>> propertyMap, Map<String, Object> valueMap) throws Exception{
		DynamicBean bean = new DynamicBean(propertyMap);
		Object object = bean.getObject();
		Method[] methods = object.getClass().getDeclaredMethods();
		for (Method method : methods) {
			if (method.getName().startsWith("set")) {//setter方法
				String propertyeName = WordUtils.uncapitalize(method.getName().substring("set".length()));
				Object value = valueMap.get(propertyeName);
				method.invoke(object, value);
			}
		}
		return object;
	}
}
