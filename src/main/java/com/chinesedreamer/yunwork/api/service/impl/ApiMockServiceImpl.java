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
import java.util.regex.Pattern;

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

	private String map_model_prefix = "@model_";
	@Resource
	private ApiConfig apiConfig;
	@Override
	public Object mockData(ApiModel model) {
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
	private Object mockTemplateData(File templateFile) {
		Object object = null;
		try {
			List<String> properties = new ArrayList<String>();
			FileInputStream fis = new FileInputStream(templateFile);
			InputStreamReader isr = new InputStreamReader(fis, this.apiConfig.getApiModelTemplateEncode());
			BufferedReader br = new BufferedReader(isr);
			String tmpStr = null;
			
			Map<String, List<String>> mapProperties = new HashMap<String, List<String>>();
			boolean modelStart = false;
			String tmpModelName = null;
			while (null != (tmpStr = br.readLine())) {
				if (StringUtils.isNotEmpty(tmpStr)) {
					if (modelStart) {
						if (this.mapEnd(tmpStr)) {
							modelStart = false;
							tmpModelName = null;
						}else {
							mapProperties.get(tmpModelName).add(tmpStr.trim());
						}
					}else {
						if (this.mapStart(tmpStr)) {//开始读取到map配置
							modelStart = true;
							int beginIdx = tmpStr.indexOf("_");
							int endIdx = tmpStr.lastIndexOf("_");
							tmpModelName = map_model_prefix + tmpStr.substring(beginIdx + 1, endIdx);
							mapProperties.put(tmpModelName, new ArrayList<String>());
						}else {
							properties.add(tmpStr.trim());
						}
					}
				}
			}
			br.close();
			isr.close();
			fis.close();
			object = this.convertObject(properties, mapProperties);
		} catch (Exception e) {
			this.logger.error("{}", e);
			return null;
		}
		return object;
	}
	
	/**
	 * 根据属性配置生成对象
	 * @param propertyConfigurations
	 * @param mapProperties
	 * @return
	 */
	private Object convertObject(List<String> propertyConfigurations, Map<String, List<String>> mapProperties) throws Exception{
		Map<String, Class<?>> propertyMap = new HashMap<String, Class<?>>();
		Map<String, Object> valueMap = new HashMap<String, Object>();
		//封装map
		for (String property : propertyConfigurations) {
			String[] confs = this.splitConfs(property);
			ApiPropertyType propertyType = ApiPropertyType.get(confs[0]);
			String propertyName = property.substring(0, property.indexOf(ApplicationConstant.API_MOCK.PROP_CONF_START_DELIMETER));
			MockProperty mockProperty = null;
			Object propertyValue = null;
			Map<String, List<String>> tmpMapProperties = null;
			switch (propertyType) {
				case MAP:
					if (mapProperties.containsKey(map_model_prefix + propertyName)) {
						for (String tmpPropConf : mapProperties.get(map_model_prefix + propertyName)) {
							String tmpPropertyName = tmpPropConf.substring(0, tmpPropConf.indexOf(ApplicationConstant.API_MOCK.PROP_CONF_START_DELIMETER));
							if (mapProperties.containsKey(map_model_prefix + tmpPropertyName)) {
								if (null == tmpMapProperties) {
									tmpMapProperties = new HashMap<String, List<String>>();
								}
								tmpMapProperties.put(map_model_prefix + tmpPropertyName, mapProperties.get(map_model_prefix + tmpPropertyName));
							}
						}
					}
					propertyValue = this.convertObject(mapProperties.get(map_model_prefix + propertyName), tmpMapProperties);
					mockProperty = this.convertProperty(propertyName, propertyType, confs, propertyValue);
					valueMap.put(mockProperty.getPropertyName(), JSON.toJavaObject((JSON)JSON.toJSON(mockProperty.getValue()), Map.class));
					break;
				case MODEL:
					if (mapProperties.containsKey(map_model_prefix + propertyName)) {//如果当前配置文件命中model名称，优先
						for (String tmpPropConf : mapProperties.get(map_model_prefix + propertyName)) {
							String tmpPropertyName = tmpPropConf.substring(0, tmpPropConf.indexOf(ApplicationConstant.API_MOCK.PROP_CONF_START_DELIMETER));
							if (mapProperties.containsKey(map_model_prefix + tmpPropertyName)) {
								if (null == tmpMapProperties) {
									tmpMapProperties = new HashMap<String, List<String>>();
								}
								tmpMapProperties.put(map_model_prefix + tmpPropertyName, mapProperties.get(map_model_prefix + tmpPropertyName));
							}
						}
						propertyValue = this.convertObject(mapProperties.get(map_model_prefix + propertyName), tmpMapProperties);
					}else {
						propertyValue = this.mockData(new ApiModel(confs[1], this.apiConfig.getApiModelTmpFolder() + confs[1] + ".tmp"));
					}
					mockProperty = new MockProperty();
					mockProperty.setPropertyName(propertyName);
					mockProperty.setClazz(Map.class);
					mockProperty.setValue(propertyValue);
					valueMap.put(mockProperty.getPropertyName(), JSON.toJavaObject((JSON)JSON.toJSON(mockProperty.getValue()), Map.class));
					break;
				default:
					mockProperty = this.convertProperty(propertyName, propertyType, confs, null);
					valueMap.put(mockProperty.getPropertyName(), mockProperty.getValue());
					break;
			}
			propertyMap.put(mockProperty.getPropertyName(), mockProperty.getClazz());
		}
		return this.mockObject(propertyMap, valueMap);
	}
	
	private String[] splitConfs(String propertyConf) {
		int confStart = propertyConf.indexOf(ApplicationConstant.API_MOCK.PROP_CONF_START_DELIMETER) + 1;
		int confEnd = propertyConf.indexOf(ApplicationConstant.API_MOCK.PROP_CONF_END_DELIMETER);
		return propertyConf.substring(confStart, confEnd).split(ApplicationConstant.API_MOCK.PROP_CONF_SEPERATE_DELIMETER);
	}
	
	/**
	 * confStr [type|value|约束条件]
	 * property[int|value|min,max]
	 * property[string|value|length]，默认string，可以缺失
	 * property[decimal|value|length,precision]
	 * property[list|modelName|size]
	 * property[model|modelName]
	 * @param confStr
	 * @return
	 */
	private MockProperty convertProperty(String propertyName, ApiPropertyType propertyType, String[] confs, Object propertyValue) {
		MockProperty mockProperty = new MockProperty();
		try {
			mockProperty.setPropertyName(propertyName);;
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
				case MAP:
					mockProperty.setClazz(Map.class);
					mockProperty.setValue(propertyValue);
					break;
				case LIST:
					mockProperty.setClazz(List.class);
					//TODO
				break;
				case MODEL:
					mockProperty.setClazz(Class.forName(confs[2]));
					mockProperty.setValue(this.mockData(new ApiModel(confs[1], this.apiConfig.getApiModelRootFolder() + confs[1].replaceAll(".", File.separator) + ".tmp")));
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
			if (StringUtils.isNotEmpty(configurations[1])) {
				return (T)Integer.valueOf(configurations[1]);
			}else {
				int min = 10000;//默认 10000 - 100000
				int max = 100000;
				if (StringUtils.isNotEmpty(configurations[2]) && StringUtils.isNotEmpty(configurations[3])) {
					min = Integer.valueOf(configurations[2]);
					max = Integer.valueOf(configurations[3]);
				}else if (StringUtils.isNotEmpty(configurations[2]) && StringUtils.isEmpty(configurations[3])) {
					min = Integer.valueOf(configurations[2]);
					max = min + max;
				}else if (StringUtils.isEmpty(configurations[2]) && StringUtils.isNotEmpty(configurations[3])) {
					max = Integer.valueOf(configurations[3]);
					min = max - min > 0 ? max - min : 1;
				}
				Integer random = RandomUtils.nextInt(min, max);
				return (T)random;
			}
		}else if (clazz.equals(BigDecimal.class)) {//configurations = deciaml,length, precision, value
			if (StringUtils.isNotEmpty(configurations[1])) {
				return (T)(new BigDecimal(configurations[1]));
			}else {
				int length = 7;
				int precision = 2;
				if (StringUtils.isNotEmpty(configurations[2])) {
					length = Integer.valueOf(configurations[2]);
				}
				if (StringUtils.isNotEmpty(configurations[3])) {
					precision = Integer.valueOf(configurations[3]);
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
			if (StringUtils.isNotEmpty(configurations[1])) {
				return (T)(String)(configurations[1]);
			}else {
				return (T)this.randomString(StringUtils.isEmpty(configurations[2]) ? null : Integer.valueOf(configurations[2]));
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
				System.out.println(method.getName());
				String propertyeName = WordUtils.uncapitalize(method.getName().substring("set".length()));
				Object value = valueMap.get(propertyeName);
				method.invoke(object, value);
			}
		}
		return object;
	}
	
	/**
	 * 判断类型开始
	 * @param str
	 * @return
	 */
	private boolean mapStart(String str) {
		String patternStr = "^@model_[A-Za-z]{1,}_start$";
		Pattern pattern = Pattern.compile(patternStr);
		return pattern.matcher(str).matches();
	}
	private boolean mapEnd(String str) {
		String patternStr = "^@model_[A-Za-z]{1,}_end";
		Pattern pattern = Pattern.compile(patternStr);
		return pattern.matcher(str).matches();
	}
}
