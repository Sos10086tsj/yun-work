package com.chinesedreamer.yunwork.api.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chinesedreamer.yunwork.api.config.ApplicationConstant;
import com.chinesedreamer.yunwork.api.model.ApiModel;
import com.chinesedreamer.yunwork.api.service.ApiMockService;
import com.chinesedreamer.yunwork.api.service.ApiModelService;
import com.chinesedreamer.yunwork.api.vo.ApiModelTreeNodelVo;

@RestController
@RequestMapping(value = "api")
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
	@RequestMapping(value = "mockData")
	public Object getMockData(HttpServletRequest request, @RequestParam String modelName){
		//1. 校验model是否存在
		ApiModel model = this.apiModelService.isModelExist(modelName);
		if (null == model) {
			return this.getFailureJson("Model " + modelName + " is not exist. Please add it first.");
		}
		//2. 交给适配器模拟数据
		Map<String, Object> paramMap = new HashMap<String, Object>();
		Enumeration<String> parameterNames = request.getParameterNames();
		while (parameterNames.hasMoreElements()) {
			String key = (String) parameterNames.nextElement();
			paramMap.put(key, request.getParameter(key));
		}
		return this.apiMockService.mockData(model, paramMap);
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
					if (apiModel.getModelName().endsWith(ApplicationConstant.API_MOCK.TEMPLATE_FILE_PATTERN)) {
						ApiModelTreeNodelVo node = new ApiModelTreeNodelVo();
						node.setModelName(dept[i]);
						node.setModelPath(apiModel.getTemplatePath());
						if (null == tmpVo.getSubNode()) {
							tmpVo.setSubNode(new ArrayList<ApiModelTreeNodelVo>());
						}
						tmpVo.getSubNode().add(node);
					}else {
						ApiModelTreeNodelVo node = new ApiModelTreeNodelVo();
						node.setFolderPath((StringUtils.isEmpty(folderName) ? "" : folderName ) + dept[i] + File.separator);
						node.setFolderName(dept[i]);
						if (null == vo.getSubNode()) {
							vo.setSubNode(new ArrayList<ApiModelTreeNodelVo>());
						}
						vo.getSubNode().add(node);
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
	@RequestMapping(value = "tmp/model/save")
	public void saveModelTemplate(@RequestParam("modelName")String modelName, @RequestParam("templateContent")String templateContent){
		this.apiModelService.saveTemplate(modelName, templateContent);
	}
	
	/**
	 * 保存模板路径
	 * @param templateName
	 */
	@RequestMapping(value = "tmp/save")
	public void saveTemplate(@RequestParam("templateName")String templateName){
		this.apiModelService.saveTemplateFolder(templateName);
	}
	
	@RequestMapping(value = "tmp/model/get")
	public String getModel(@RequestParam("modelName") String modelName){
		return this.apiModelService.getModel(modelName);
	}
	
	@RequestMapping(value = "tmp/convert")
	public void convert(@RequestParam("codeSource")String codeSource, @RequestParam("folderPath")String folderPath){
		this.apiModelService.transfer2Template(codeSource, folderPath);
	}
}
