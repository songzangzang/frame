package org.smart4j.framework.proxy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import net.sf.cglib.proxy.MethodProxy;

/**
 * 代理链
 * @author Administrator
 *
 */
public class ProxyChain {

	private final Class<?> targetClass;
	private final Object targetObject;
	private final Method targetMethod;
	private final MethodProxy methodProxy;
	private final Object[] methodParams;
	
	private List<Proxy> proxyList = new ArrayList<Proxy>();

	private int proxyIndex = 0;
	
	public ProxyChain (Class<?> targetClass, Object targetObject, Method targetMethod
			, MethodProxy methodProxy, Object[] methodParams, List<Proxy> proxyList){
		this.targetClass = targetClass;
		this.targetObject = targetObject;
		this.targetMethod = targetMethod;
		this.methodProxy = methodProxy;
		this.methodParams = methodParams;
		this.proxyList = proxyList;
	}

	public Class<?> getTargetClass() {
		return targetClass;
	}

	public Method getTargetMethod() {
		return targetMethod;
	}

	public Object[] getMethodParams() {
		return methodParams;
	}
	
	/**
	 * 执行链式代理
	 * @return
	 * @throws Throwable
	 */
	public Object doProxyChain() throws Throwable{
		Object methodResult;
		/*
		 * 用proxyIndex进行标识进行比对
		 * 如果prorxyIndex比proxyList小代表已经没有代理对象需要执行,这时会执行目标类的业务方法
		 * 如果proxyIndex 比 proxylist大代表目标类还有别的代理类需要执行，这时会执行抽象类中doProxy方法，————
		 * ——并调用其代理对象具体实现的方法来进行切面逻辑代码，这时会重复doProxyChain这个过程重复询问
		 * 
		 * 
		 * 疑问1 这样执行 如果一个需要代理的类有两个Aspect注解实现是不是就不会执行before的方法
		 * 疑问2 如果能执行 那是应用什么逻辑来执行的
		 */
		if (proxyIndex < proxyList.size()) {
			methodResult = proxyList.get(proxyIndex++).doProxy(this);
		}else{
			methodResult = methodProxy.invokeSuper(targetObject, methodParams);
		}
		return methodResult;
	}
}
