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
		2、参数支持[类型|长度]描述。支持int（整数）、decimal（小数）、list（数组）、map（对象 可省略）、缺省省略为string
			[type|约束条件]
			age[float|5,2]=年龄
			name=姓名
			family[list].id=id
			family[list].value=fadaf
			position[map].comp=za
			position[model|modelName]
		
	-	java interface mock
	-	dubbo service mock
	-	spring cloud servcie mock
	-	schedule feedback mock
	-	mq mock
	-	file mock
	-	mybatis mock