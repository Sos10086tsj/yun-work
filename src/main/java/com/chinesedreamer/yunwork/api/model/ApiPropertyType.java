package com.chinesedreamer.yunwork.api.model;

/**
 * 属性类型
 * @author paris
 *
 */
public enum ApiPropertyType {
	STRING,
	DECIMAL,
	INT,
	BOOLEAN,
	LIST,
	MAP,
	MODEL;
	
	private ApiPropertyType(){
		
	}
	
	public static ApiPropertyType get(String str) {
		for (ApiPropertyType type : ApiPropertyType.values()) {
			if (type.name().equalsIgnoreCase(str)) {
				return type;
			}
		}
		return STRING;
	}
}
