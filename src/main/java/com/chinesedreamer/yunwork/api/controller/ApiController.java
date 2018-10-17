package com.chinesedreamer.yunwork.api.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chinesedreamer.yunwork.api.model.ApiModel;
import com.chinesedreamer.yunwork.api.service.ApiMockService;
import com.chinesedreamer.yunwork.api.service.ApiModelService;
import com.chinesedreamer.yunwork.api.vo.ApiModelTreeNodelVo;

@RestController(value = "api")
public class ApiController {
	
	@Resource
	private ApiModelService apiModelService;
	@Resource
	private ApiMockService apiMockService;

	/**
	 * 获取模拟的api数据
	 * @param request
	 * @param modelName
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "mockData")
	public Object getMockData(HttpServletRequest request, @RequestParam String modelName){
		//1. 校验model是否存在
		ApiModel model = this.apiModelService.isModelExist(modelName);
		if (null == model) {
			return this.getFailureJson("Model " + modelName + " is not exist. Please add it first.");
		}
		//2. 交给适配器模拟数据
		return this.apiMockService.mockData(model);
	}
	private JSON getFailureJson(String message) {
		JSONObject jo = new JSONObject();
		jo.put("message", message);
		return (JSON)jo;
	}
	
	/**
	 * 获取模板列表
	 * @param modelName
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "tmp/list")
	public ApiModelTreeNodelVo getTemplateList(String folderName){
		ApiModelTreeNodelVo vo = new ApiModelTreeNodelVo();
		vo.setFolderName("root");
		vo.setFolderPath(folderName);
		List<ApiModel> apiModels = this.apiModelService.getTemplateList(folderName);
		if (null != apiModels) {
			apiModels.forEach(apiModel -> {
				int index = StringUtils.isEmpty(folderName) ? 0 : folderName.length();
				String[] dept = apiModel.getTemplatePath().substring(index).split(File.separator);
				ApiModelTreeNodelVo tmpVo = vo;
				for (int i = 0; i < dept.length ; i++) {
					if (i == dept.length - 1) {
						ApiModelTreeNodelVo node = new ApiModelTreeNodelVo();
						node.setModelName(dept[i]);
						node.setModelPath(apiModel.getTemplatePath());
						if (null == tmpVo.getSubNode()) {
							tmpVo.setSubNode(new ArrayList<ApiModelTreeNodelVo>());
						}
						tmpVo.getSubNode().add(node);
					}else {
						ApiModelTreeNodelVo node = new ApiModelTreeNodelVo();
						node.setFolderPath(StringUtils.isEmpty(folderName) ? "" : folderName + dept[i] + File.separator);
						node.setFolderName(dept[i]);
						if (null == tmpVo.getSubNode()) {
							tmpVo.setSubNode(new ArrayList<ApiModelTreeNodelVo>());
							tmpVo.getSubNode().add(node);
							tmpVo = tmpVo.getSubNode().get(0);
						}else {
							boolean exist = false;
							for (int j = 0; j < tmpVo.getSubNode().size(); j++) {
								ApiModelTreeNodelVo nodelVo = tmpVo.getSubNode().get(j);
								if (nodelVo.getFolderName().equalsIgnoreCase(dept[i])) {
									tmpVo = nodelVo;
									exist = true;
									break;
								}
							}
							if (!exist) {
								tmpVo.getSubNode().add(node);
								tmpVo = tmpVo.getSubNode().get(tmpVo.getSubNode().size() - 1);
							}
						}
					}
				}
			});
		}
		return vo;
	}
	
	/**
	 * 保存、更新模板
	 * @param modelName
	 * @param templateContent
	 */
	@ResponseBody
	@RequestMapping(value = "tmp/model/save")
	public void saveModelTemplate(@PathParam("modelName")String modelName, @PathParam("templateContent")String templateContent){
		ApiModel model = this.apiModelService.isModelExist(modelName);
		if (null == model) {
			this.apiModelService.saveTemplate(modelName, templateContent);
		}else {
			this.apiModelService.updateTemplate(modelName, templateContent);
		}
	}
	
	/**
	 * 保存模板路径
	 * @param templateName
	 */
	@ResponseBody
	@RequestMapping(value = "tmp/save")
	public void saveTemplate(@PathParam("templateName")String templateName){
		this.apiModelService.saveTemplateFolde(templateName);
	}
}
