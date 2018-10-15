package com.chinesedreamer.yunwork.api.model;

import java.util.Map;

import net.sf.cglib.beans.BeanGenerator;
import net.sf.cglib.beans.BeanMap;

public class DynamicBean {
	private Object object;
	private BeanMap beanMap;
	
	public DynamicBean(){}
	
	public DynamicBean(Map<String, Class<?>> propertyMap){
		this.object = this.generateBean(propertyMap);
		this.beanMap = BeanMap.create(this.object);
	}
	
	private Object generateBean(Map<String, Class<?>> propertyMap){
		BeanGenerator generator = new BeanGenerator();
		for (String key : propertyMap.keySet()) {
			generator.addProperty(key, propertyMap.get(key));
		}
		return generator.create();
	}
	
	public void setValue(Object property, Object value){
		beanMap.put(property, value);
	}
	public Object getValue(String property){
		return beanMap.get(property);
	}
	public Object getObject() {
		return this.object;
	}
}
