package org.smart4j.framework.proxy;

import java.lang.reflect.Method;
import java.util.List;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;


/**
 * 代理管理器
 * @author Administrator
 *
 */
public class ProxyManager {

	/**
	 * 用cglib类进行代理的创建
	 * 
	 * @param targetClass
	 * @param proxyList
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T createProxy(final Class<?> targetClass, final List<Proxy> proxyList){
		return (T) Enhancer.create(targetClass, new MethodInterceptor(){
					@Override
					public Object intercept(Object targetObject, Method targetMethod, Object[] methodParams, MethodProxy methodProxy)
							throws Throwable {
						//返回代理类
						return new ProxyChain(targetClass, targetObject, targetMethod, methodProxy, methodParams, proxyList).doProxyChain();
					}}
				
				);
	}
}
