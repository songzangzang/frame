package org.smart4j.framework.util;
/**
 * 数字转换工具类
 * @author Administrator
 *
 */
public final class CastUtil {
	
	/**
	 * 转换成int
	 */
	public static int castInt(Object obj){
		return castInt(obj, 0);
	}
	
	public static int castInt(Object obj, int defaultValue) {
		int  value = defaultValue;
		if (obj != null) {
			String castString = castString(obj);
			if (StringUtil.isNotEmpty(castString)) {
				value = Integer.parseInt(castString);
			}
		}
		return value;
	}
	
	/**
	 * 转成boolean
	 * @param property
	 * @return
	 */
	public static boolean castBoolean(Object obj){
		return castBoolean(obj, false);
	}
	
	public static boolean castBoolean(Object obj, boolean defaultValue) {
		boolean value = defaultValue;
		if (obj != null) {
			String castString = castString(obj);
			if (StringUtil.isNotEmpty(castString)) {
				value = Boolean.parseBoolean(castString);
			}
		}
		return value;
	}
	
	/**
	 * 转为String型
	 */
	public static String castString(Object obj){
		return CastUtil.castString(obj, "");
	}
	
	public static String castString(Object obj, String defaultValue){
		return obj != null ? String.valueOf(obj) : defaultValue;
	}
	 
	/**
	 * 转为double
	 */
	public static double castDouble(Object obj){
		return castDouble(obj, 0);
	}
	
	public static double castDouble(Object obj, double defaultValue){
		double value = defaultValue;
		if (obj != null) {
			String castString = castString(obj);
			if (StringUtil.isNotEmpty(castString)) {
				value = Double.parseDouble(castString);
			}
		}
		return value;
	}
	
	/**
	 * 转为long
	 */
	public static long castLong(Object obj){
		return castLong(obj, 0);
	}
	
	public static long castLong(Object obj, long defaultValue){
		long value = defaultValue;
		if (obj != null) {
			String castString = castString(obj);
			if (StringUtil.isNotEmpty(castString)) {
				value = Long.parseLong(castString);
			}	
		}
		return value;
	}
}
