package com.chinesedreamer.yunwork.api.model;

public class ApiModel {
	private String modelName;//类似package 路径，通过.分隔
	private String templatePath;//模板路径

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getTemplatePath() {
		return templatePath;
	}

	public void setTemplatePath(String templatePath) {
		this.templatePath = templatePath;
	}
	public ApiModel(){}
	public ApiModel(String modelName, String templatePath){
		this.modelName = modelName;
		this.templatePath = templatePath;
	}
}
