package org.smart4j.framework.helper;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.jsp.tagext.TryCatchFinally;

import org.apache.log4j.Logger;
import org.smart4j.framework.annotation.Aspect;
import org.smart4j.framework.annotation.Transaction;
import org.smart4j.framework.proxy.AspectProxy;
import org.smart4j.framework.proxy.Proxy;
import org.smart4j.framework.proxy.ProxyManager;
import org.smart4j.framework.proxy.TrancationProxy;

/**
 * Aop帮助类
 * @author Administrator
 *
 */
public final class AopHelper {
	
	private static Logger LOGGER = Logger.getLogger(AopHelper.class);
	
	static {
		try {
			//获取Map<切面类,目标类集合>
			Map<Class<?>, Set<Class<?>>> proxyMap = createProxyMap();
			//获取Map<目标类，代理对象集合>
			Map<Class<?>, List<Proxy>> targetMap = createTargetMap(proxyMap);
			for(Map.Entry<Class<?>, List<Proxy>> targetEntry : targetMap.entrySet()){
				Class<?> targetClass = targetEntry.getKey();
				List<Proxy> proxyList = targetEntry.getValue();
				//创建代理对象
				Object proxy = ProxyManager.createProxy(targetClass, proxyList);
				//判断是否是Service并把targetClass换成接口名完成注入
				if (targetClass.toString().endsWith("Impl")) {
					Set<Class<?>> beanClassSet = ClassHelper.getBeanClassSet();
					for(Class<?> cls : beanClassSet){
						if (cls.isAssignableFrom(targetClass) && !cls.equals(targetClass)) {
							targetClass = cls;
						}	
					}
				}
				//重新覆盖之前BeanHelper中类和实例之间的映射关系
				BeanHelper.setBean(targetClass, proxy);
			}
			
		} catch (Exception e) {
			LOGGER.error("aop failure", e);
		}
	}
	
	/**
	 * 查找所有aspect推展类(切面类) 所对应的 目标类
	 * @param aspect
	 * @return 目标类
	 * @throws Exception
	 */
	private static Set<Class<?>> createTargetClassSet(Aspect aspect) throws Exception{
		Set<Class<?>> targetClassSet = new HashSet<Class<?>>();
		Class<? extends Annotation> annotation = aspect.value();
		if (annotation != null && !annotation.equals(Aspect.class)) {
			targetClassSet.addAll(ClassHelper.getClassSetByAnnotation(annotation));
		}
		return targetClassSet;
	}
	
	/**
	 * 初始化映射关系
	 * @return Map<切面类,目标类>
	 * @throws Exception
	 */
	private static Map<Class<?>, Set<Class<?>>> createProxyMap() throws Exception{
		Map<Class<?>, Set<Class<?>>> proxyMap = new HashMap<Class<?>, Set<Class<?>>>();
		addAspectProxy(proxyMap);
		addTransactionProxy(proxyMap);
		return proxyMap;
	}

	private static Map<Class<?>, Set<Class<?>>> addAspectProxy(Map<Class<?>, Set<Class<?>>> proxyMap) throws Exception {
		//获取类或接口 对应的 子类或实现
		Set<Class<?>> proxyClassSet = ClassHelper.getClassSetBySuper(AspectProxy.class);
		for(Class<?> proxyClass : proxyClassSet){
			if (proxyClass.isAnnotationPresent(Aspect.class)) {
				//获取Aspect注解类中的注解属性
				Aspect aspect = proxyClass.getAnnotation(Aspect.class);
				//获取全部带有Aspect注解里注解类的目标类
				Set<Class<?>> targetClassSet = createTargetClassSet(aspect);
				proxyMap.put(proxyClass, targetClassSet);
			}
		}
		return proxyMap;
	} 
	
	private static void addTransactionProxy(Map<Class<?>, Set<Class<?>>> proxyMap) {
		Set<Class<?>> proxyClassSet = ClassHelper.getServiceClassSet();
		proxyMap.put(TrancationProxy.class, proxyClassSet);
	}
	
	/**
	 * 获取目标和代理对象的集合
	 * @param proxyMap
	 * @return 目标对象和代理对象
	 * @throws Exception
	 */
	private static Map<Class<?>, List<Proxy>> createTargetMap(Map<Class<?>, Set<Class<?>>> proxyMap) throws Exception{
		Map<Class<?>, List<Proxy>> targetMap = new HashMap<Class<?>, List<Proxy>>();
		for(Map.Entry<Class<?>, Set<Class<?>>> proxyEntry : proxyMap.entrySet()){
			Class<?> proxyClass = proxyEntry.getKey();
			Set<Class<?>> targetClassSet = proxyEntry.getValue();
			for(Class<?> targetClass : targetClassSet){
				Proxy proxy = (Proxy) proxyClass.newInstance();
				if (targetMap.containsKey(targetClass)) {
					targetMap.get(targetClass).add(proxy);
				} else {
					List<Proxy> proxyList = new ArrayList<Proxy>();
					proxyList.add(proxy);
					targetMap.put(targetClass, proxyList);
				}
			}
		}
		return targetMap;
	}
	
}
