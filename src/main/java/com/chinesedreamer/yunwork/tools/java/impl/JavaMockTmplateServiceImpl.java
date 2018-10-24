package com.chinesedreamer.yunwork.tools.java.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.math.BigDecimal;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chinesedreamer.yunwork.api.config.ApiConfig;
import com.chinesedreamer.yunwork.api.config.ApplicationConstant;
import com.chinesedreamer.yunwork.tools.java.dynamic.JavaMockTmplateService;
import com.chinesedreamer.yunwork.tools.java.dynamic.tmp.DynamicLoader;

@Service
public class JavaMockTmplateServiceImpl implements JavaMockTmplateService{
	private Logger logger = LoggerFactory.getLogger(JavaMockTmplateServiceImpl.class);
	
	@Autowired
	private ApiConfig apiConfig;
	@Override
	public void transfer2Template(String folder) {
		File f = new File(folder);
		if (!f.exists() || f.list().length == 0) {
			return;
		}
		try {
			this.loopFolder(f);
		} catch (Exception e) {
			this.logger.error("{}", e);
		}
	}

	private void loopFolder(File folder)  throws Exception{
		for (File file : folder.listFiles()) {
			if (file.isDirectory()) {
				this.loopFolder(file);
			}else {
				this.parse2Template(file);
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	private void parse2Template(File file) throws Exception{
		String className = this.getClassName(file);
		Class clazz = DynamicLoader.parse2Class(file, className);
		Field[] fields = clazz.getDeclaredFields();
		if (null == fields) {
			return;
		}
		StringBuilder builder = new StringBuilder();
		for (Field field : fields) {
			builder.append(field.getName())
			.append(ApplicationConstant.API_MOCK.PROP_CONF_START_DELIMETER);
			if (field.getType().equals(String.class)) {
				builder.append("string");
			}else if (field.getType().equals(Integer.class)) {
				builder.append("int");
			}else if (field.getType().equals(Double.class) || field.getType().equals(Float.class) || field.getType().equals(BigDecimal.class)) {
				builder.append("decimal");
			}else if (field.getType().equals(Boolean.class)) {
				builder.append("boolean");
			}else if (field.getType().getName().equals("int")) {
				builder.append("int");
			}else if (field.getType().getName().equals("float") || field.getType().getName().equals("double")) {
				builder.append("decimal");
			}else if (field.getType().getName().equals("boolean")) {
				builder.append("boolean");
			}else {
				//其他类型
				builder.append("model|");
				String extJavaName = field.getType().getName();
				int lastIndex = extJavaName.lastIndexOf(".");
				String[] names = extJavaName.substring(0, lastIndex).split(".");
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < names.length - 1; i++) {
					sb.append(names[i]).append(File.separator);
				}
				sb.append(names[names.length - 1]).append(".").append(ApplicationConstant.API_MOCK.TEMPLATE_FILE_PATTERN);
				builder.append(sb.toString());
			}
			builder.append(ApplicationConstant.API_MOCK.PROP_CONF_END_DELIMETER)
			.append("\n");
		}
		
		String[] paths = className.split("\\.");
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < paths.length - 1; i++) {
			sb.append(paths[i]).append(File.separator);
		}
		File folder = new File(this.apiConfig.getApiModelTmpFolder() + sb.toString());
		if (!folder.exists()) {
			folder.mkdirs();
		}
		FileUtils.writeStringToFile(
				new File(this.apiConfig.getApiModelTmpFolder() + sb.toString() + paths[paths.length - 1] + "." + ApplicationConstant.API_MOCK.TEMPLATE_FILE_PATTERN), 
				builder.toString(), "utf-8", false);
	}
	
	/**
	 * 根据package 读取
	 * @param file
	 * @return
	 */
	private String javaPackageLine = "package ";
	private String getClassName(File file) throws Exception{
		InputStream is = new FileInputStream(file);
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		String tmp = null;
		while ((tmp = br.readLine()) != null) {
			if (StringUtils.isNotEmpty(tmp) && tmp.startsWith(javaPackageLine)) {
				break;
			}
		}
		br.close();
		isr.close();
		is.close();
		int endIndex = tmp.indexOf(";");
		int suffixINdex = file.getName().lastIndexOf(".");
		return tmp.substring(javaPackageLine.length(), endIndex) + "." + file.getName().substring(0, suffixINdex);
	}
}
