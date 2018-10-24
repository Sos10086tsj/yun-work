package com.chinesedreamer.yunwork.tools.java.dynamic;

public interface JavaMockTmplateService {
	/**
	 * java 转换成api mock中的template
	 * @param codeSource
	 * @param folder
	 * @param prefix	类名前缀。前缀为空时，表示文件夹是跟目录
	 */
	public void transfer2Template(String folder);
}
