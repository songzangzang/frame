package org.smart4j.framework.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.apache.log4j.Logger;

/**
 * 反射工具类
 * @author Administrator
 *
 */
public class ReflectionUtil {

	private static final Logger LOGGER = Logger.getLogger(ReflectionUtil.class);
	
	/**
	 * 创建实例
	 */
	public static Object newInstance(Class<?> cls){
		Object instance;
		try {
			instance = cls.newInstance();
		
		} catch (Exception e) {
			LOGGER.error("new instance failure", e);
			throw new RuntimeException();
		} 
		return instance;
	}
	
	/**
	 * 调用方法
	 */
	public static Object invokeMethod(Object obj, Method method, Object... args){
		Object result;
		method.setAccessible(true);
		try {
			result = method.invoke(obj, args);
		} catch (Exception e) {
			LOGGER.error("invoke method failure", e);
			throw new RuntimeException();
		}
		return result;
	}
	
	/**
	 * 设置成员变量
	 */
	public static void setField(Object obj, Field field, Object value){
		field.setAccessible(true);
		try {
			field.set(obj, value);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	
}
