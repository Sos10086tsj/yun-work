package com.chinesedreamer.yunwork.tools.java.dynamic.tmp;

import java.io.File;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.chinesedreamer.yunwork.tools.java.dynamic.JavaStringCompiler;

public class DynamicLoader {
	public static Class<?> parse2Class(File file, String className) throws Exception{
		int index = className.lastIndexOf(".");
		String className_1 = className.substring(0, index);
		String className_2 = className.substring(index + 1) + ".java";
		
		JavaStringCompiler compiler = new JavaStringCompiler();
		String content = FileUtils.readFileToString(file);
		content = content.replace("package " + className_1, "package " + "com.chinesedreamer.yunwork.tools.java.dynamic.tmp");
		Map<String, byte[]> results = compiler.compile(className_2, content);
		Class<?> clazz = compiler.loadClass("com.chinesedreamer.yunwork.tools.java.dynamic.tmp." + className.substring(index + 1), results);
		return clazz;
	}
}
