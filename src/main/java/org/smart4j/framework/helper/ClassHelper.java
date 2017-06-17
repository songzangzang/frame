package org.smart4j.framework.helper;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import org.smart4j.framework.annotation.Controller;
import org.smart4j.framework.annotation.Inject;
import org.smart4j.framework.annotation.Service;
import org.smart4j.framework.util.ClassUtil;

/**
 * 类操作助手类
 * @author Administrator
 *
 */
public class ClassHelper {

	/**
	 * 定义类集合(用于存放所有加载的类)
	 */
	private static final Set<Class<?>> CLASS_SET;
	
	static{
		String basePackage = ConfigHelper.getAppBasePackage();
		CLASS_SET = ClassUtil.getClassSet(basePackage);
	}
	
	/**
	 * 获取应用包名下的所有类
	 */
	public static Set<Class<?>> getBeanClassSet(){
		return CLASS_SET;
	}
	
	/**
	 * 获取应用包下所有Controller
	 */
	public static Set<Class<?>> getControllerClassSet(){
		Set<Class<?>> classSet = new HashSet<Class<?>>();
		for(Class<?> cls : CLASS_SET){
			if (cls.isAnnotationPresent(Controller.class)) {
				classSet.add(cls);
			}
		}
		return classSet;
	}
	
	/**
	 * 获取应用包下的所有Service
	 */
	public static Set<Class<?>> getServiceClassSet(){
		Set<Class<?>> classSet = new HashSet<Class<?>>();
		for(Class<?> cls : CLASS_SET){
			if (cls.isAnnotationPresent(Service.class)) {
				classSet.add(cls);
			}
		}
		return classSet;
	}
	
	/**
	 * 获取应用包名下某父类(或接口)的所有子类(或实现类)
	 */
	public static Set<Class<?>> getClassSetBySuper(Class<?> superClass){
		Set<Class<?>> classSet = new HashSet<Class<?>>();
		for(Class<?> cls : classSet){
			if (superClass.isAssignableFrom(cls)) {
				classSet.add(cls);
			}
		}
		return classSet;
	}
	
	/**
	 * 获取应用包名下带有某注解的所有类
	 */
	public static Set<Class<?>> getClassSetByAnnotation(Class<? extends Annotation> annotation){
		Set<Class<?>> classSet = new HashSet<Class<?>>();
		for(Class<?> cls : CLASS_SET){
			if (cls.isAnnotationPresent(annotation)) {
				classSet.add(cls);
			}
		}
		return classSet;
	}
}
