package com.chinesedreamer.yunwork.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value = {"classpath:config/api/api-config.properties"}, ignoreResourceNotFound=true, encoding="utf-8")
public class ApiConfig {
	/**
	 * model template 根目录
	 */
	@Value("${api.model.root.folder}")
	private String apiModelRootFolder;
	/**
	 *  model template 文件编码格式，解决不同系统环境下因编码格式引起的问题
	 */
	@Value("${api.model.template.encode}")
	private String apiModelTemplateEncode;
	
	/**
	 * 判断字段类型是否为list的正则表达式；
	 * 示例：[list]
	 */
	@Value("${api.model.property.pattern.list}")
	private String apiModelPropertyPatternList;
	/**
	 * 判断字段类型是否为list的正则表达式；
	 * 示例：[map]
	 */
	@Value("${api.model.property.pattern.map}")
	private String apiModelPropertyPatternMap;
	/**
	 * 判断字段类型是否为list的正则表达式；
	 * 示例：[model|modelName]
	 */
	@Value("${api.model.property.pattern.model}")
	private String apiModelPropertyPatternModel;
	
	/**
	 * 字库，用逗号分隔
	 */
	@Value("${api.mock.word.library}")
	private String apiMockWordLibrary;

	public String getApiModelRootFolder() {
		return apiModelRootFolder;
	}

	public void setApiModelRootFolder(String apiModelRootFolder) {
		this.apiModelRootFolder = apiModelRootFolder;
	}

	public String getApiModelTemplateEncode() {
		if (null == this.apiModelTemplateEncode || "".equals(this.apiModelTemplateEncode)) {
			this.apiModelTemplateEncode = "utf-8";
		}
		return apiModelTemplateEncode;
	}

	public void setApiModelTemplateEncode(String apiModelTemplateEncode) {
		this.apiModelTemplateEncode = apiModelTemplateEncode;
	}

	public String getApiModelPropertyPatternList() {
		return apiModelPropertyPatternList;
	}

	public void setApiModelPropertyPatternList(String apiModelPropertyPatternList) {
		this.apiModelPropertyPatternList = apiModelPropertyPatternList;
	}

	public String getApiModelPropertyPatternMap() {
		return apiModelPropertyPatternMap;
	}

	public void setApiModelPropertyPatternMap(String apiModelPropertyPatternMap) {
		this.apiModelPropertyPatternMap = apiModelPropertyPatternMap;
	}

	public String getApiModelPropertyPatternModel() {
		return apiModelPropertyPatternModel;
	}

	public void setApiModelPropertyPatternModel(String apiModelPropertyPatternModel) {
		this.apiModelPropertyPatternModel = apiModelPropertyPatternModel;
	}

	public String getApiMockWordLibrary() {
		return apiMockWordLibrary;
	}

	public void setApiMockWordLibrary(String apiMockWordLibrary) {
		this.apiMockWordLibrary = apiMockWordLibrary;
	}

}
