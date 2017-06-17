package org.smart4j.framework.helper;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.smart4j.framework.util.ReflectionUtil;

/**
 * Bean助手类
 * @author Administrator
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public final class BeanHelper {

	/**
	 * 定义Bean映射(用于存放Bean类与Bean实例的映射关系)
	 */
	private static final Map<Class<?>, Object> BEAN_MAP = new HashMap<Class<?>, Object>();
	
	static {
		Set<Class<?>> beanClassSet = ClassHelper.getBeanClassSet();
		Set<Class<?>> serviceClassSet = ClassHelper.getServiceClassSet();
		//遍历全部类文件
		for (Class cls : beanClassSet){
		Object obj = null;
		Class serviceDI = null;
			Iterator<Class<?>> serviceIterator = serviceClassSet.iterator();
			//遍历带Service注解的类文件
			while (serviceIterator.hasNext()) {
				Class<?> serviceClass = serviceIterator.next();
				if (cls.isAssignableFrom(serviceClass) && !serviceClass.equals(cls)) {
					serviceDI = serviceClass;
				}
			}
			
			if (serviceDI != null) {
				obj = ReflectionUtil.newInstance(serviceDI);
			}else{
				obj = ReflectionUtil.newInstance(cls);
			}
			
			BEAN_MAP.put(cls, obj);
		} 
	}
	
	/**
	 * 获取Bean映射
	 */
	public static Map<Class<?>, Object> getBeanMap(){
		return BEAN_MAP;
	}
	
	/**
	 * 获取Bean实例
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(Class<T> cls){
		if (!BEAN_MAP.containsKey(cls)) {
			throw new RuntimeException();
		}
		return (T) BEAN_MAP.get(cls);
	}
	
	/**
	 * 设置Bean实例
	 * 用于将Bean实例放入到BeanMap中
	 */
	public static void setBean(Class<?> cls, Object obj){
		BEAN_MAP.put(cls, obj);
	}
}
