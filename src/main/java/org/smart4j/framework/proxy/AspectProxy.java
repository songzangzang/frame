package org.smart4j.framework.proxy;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;

/**
 * 切面代理
 * 有选择的进行代理实现
 * begin在方法开始的时候运行
 * before在方法之前运行
 * after在方法之后运行
 * end在方法最后进行运行
 * @author Administrator
 *
 */
public abstract class AspectProxy implements Proxy{

	private static final Logger LOGGER = Logger.getLogger(AspectProxy.class);
	
	@Override
	public Object doProxy(ProxyChain proxyChain) throws Throwable {
		Object result = null;
		
		Class<?> cls = proxyChain.getTargetClass();
		Method method = proxyChain.getTargetMethod();
		Object[] params = proxyChain.getMethodParams();
		
		begin();
		
		try {
			if (intercept(cls, method, params)) {
				before(cls, method, params);
				result = proxyChain.doProxyChain();
				after(cls, method, params, result);
			}else{
				result = proxyChain.doProxyChain();
			}
		} catch (Exception e) {
			LOGGER.error("proxy failure", e);
			error(cls, method, params, e);
			throw e;
		}finally{
			end();
		}
		
		return result;
	}

	void begin() {
		
	}
	
	public boolean intercept(Class<?> cls, Method method, Object[] params) {
		return true;
	}
	
	public void before(Class<?> cls, Method method, Object[] params) {
		
	}
	
	public void after(Class<?> cls, Method method, Object[] params, Object result) {
		
	}
	
	public void error(Class<?> cls, Method method, Object[] params, Exception e) {
		
	}
	
	public void end() {
		
	}
	
}
