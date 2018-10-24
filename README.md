# yun-work
YunWork 主要致力于mock服务，通过简单的mock方式，加快项目中的测试和集成。
	-	api interface mock
		|_	数据格式配置
		|_	数据模拟
		|_	支持调用者修正参数
		|_	目录配置（文件目录）
		|_	自定义封装器（支持第三方自定义处理数据）
		|_	支持模型
		
		api接口模拟服务，需要能够简便的增加模板配置，并根据模板生成随机数据。
		在调用方有需求时，还要支持调用方修改参数，以便测试不同场景
		字段需要支持类型 + 长度配置，避免随机生成的数据格式不正确。
		
		1、参数或者说明中，不可出现“=”
		2、参数支持
			 * confStr [type|value|约束条件]
	 * property[int|value|min|max]
	 * property[string|value|length]
	 * property[decimal|value|length,precision]
	 * property[list|modelName|size]
	 * property[model|modelName]
	 
	 示例1：
	 	col_1[string]
		col_2[string]
		col_3[int]
		col_4[model|row]
	示例2：
		name[string]
		age[int||20|60]
		address[string|杉杉大厦]
		company[string]
		family[map|family]
		fm[model|fm]
		@model_family_start
		id[int]
		name[string]
		address[string]
		level[map|level]
		@model_family_end
		@model_level_start
		id[int]
		name[string]
		@model_level_end
		list[list|fm|2]
		
	-	java interface mock
	-	dubbo service mock
	-	spring cloud servcie mock
	-	schedule feedback mock
	-	mq mock
	-	file mock
	-	mybatis mock
	
API数据模拟模板说明
	每行配置一个属性，格式为	propertyName[type|value|约束条件]，默认为string
		property[int|value|min|max]
		property[string|value|length]
		property[decimal|value|length,precision]
		property[list|modelName|size]
		property[list|modelName|size]
		
	示例1：简单的配置对象student
		name
		age[int]
		class[string]
		
	示例2：内嵌对象student
		name
		age[int]
		clas[model|clas]
		@model_clas_start
		grade
		clas[int||1|10]
		@model_clas_end
		ls[list|clas|3]
		ext_std[model|student]	//外部model
		
		
		
		