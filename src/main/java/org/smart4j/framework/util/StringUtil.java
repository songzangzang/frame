package org.smart4j.framework.util;

import org.apache.commons.lang3.StringUtils;

/**
 * 字符串工具类
 * @author Administrator
 *
 */
public final class StringUtil {
	
	/**
	 *判断字符串是否为空 
	 */
	public static boolean isEmpty(String str){
		if (str != null) {
			str = str.trim();
		}
		return StringUtils.isEmpty(str);
	}
	
	/**
	 * 非空
	 */
	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}
	
	/**
	 * 按照指定格式分割
	 */
	public static String[] splitString(String value, String splitSign) {
		return value.split(splitSign);
	}
	
}
