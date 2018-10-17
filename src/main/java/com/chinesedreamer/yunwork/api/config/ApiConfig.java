package com.chinesedreamer.yunwork.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value = {"classpath:config/api/api-config-${spring.profiles.active}.properties"}, ignoreResourceNotFound=true, encoding="utf-8")
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
	 * 字库
	 */
	@Value("${api.mock.word.library}")
	private String apiMockWordLibrary;
	
	@Value("${api.model.tmp.folder}")
	private String apiModelTmpFolder;

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

	public String getApiMockWordLibrary() {
		return apiMockWordLibrary;
	}

	public void setApiMockWordLibrary(String apiMockWordLibrary) {
		this.apiMockWordLibrary = apiMockWordLibrary;
	}

	public String getApiModelTmpFolder() {
		return apiModelTmpFolder;
	}

	public void setApiModelTmpFolder(String apiModelTmpFolder) {
		this.apiModelTmpFolder = apiModelTmpFolder;
	}

}
