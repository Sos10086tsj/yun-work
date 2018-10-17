package com.chinesedreamer.yunwork.api.vo;

import java.util.List;

public class ApiModelTreeNodelVo {
	private String folderName;
	private String folderPath;
	private String modelName;
	private String modelPath;
	private List<ApiModelTreeNodelVo> subNode;
	public String getFolderName() {
		return folderName;
	}
	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	public String getModelPath() {
		return modelPath;
	}
	public void setModelPath(String modelPath) {
		this.modelPath = modelPath;
	}
	public List<ApiModelTreeNodelVo> getSubNode() {
		return subNode;
	}
	public void setSubNode(List<ApiModelTreeNodelVo> subNode) {
		this.subNode = subNode;
	}
	public String getFolderPath() {
		return folderPath;
	}
	public void setFolderPath(String folderPath) {
		this.folderPath = folderPath;
	}
	
}
