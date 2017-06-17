package org.smart4j.framework.proxy;
/**
 * 代理接口
 * @author Administrator
 *
 */
public interface Proxy {
	
	/**
	 * 执行链式代理
	 * @throws Throwable 
	 */
	Object doProxy(ProxyChain proxyChain) throws Exception, Throwable;
}	
