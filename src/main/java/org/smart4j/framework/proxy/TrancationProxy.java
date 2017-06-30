package org.smart4j.framework.proxy;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.smart4j.framework.annotation.Transaction;
import org.smart4j.framework.helper.DataBaseHelper;

/**
 * 事务代理
 * @author Administrator
 *
 */
public class TrancationProxy implements Proxy {

	private static final Logger LOGGER = Logger.getLogger(TrancationProxy.class);
	
	private static final ThreadLocal<Boolean> FLAG_HOLDER = new ThreadLocal<Boolean>(){
		protected Boolean initialValue() {
			return false;
		};
	};
	
	/*
	 * 方法说明： 定义的FLAG_HOLDER是一个本地线程变量，它是一个标志，可以保证同一线程中事务控制相关逻辑只会执行一次，通过proxyChain对象
	 * 可获取目标方法，进而判断该方法是否带有Transaction注解。首先调用DatabaseHelper.beginTransaction方法开启事务，然后调用ProxyChain
	 * 对象的doProxyChain执行目标方法，接着调用DatabaseHelper.commitTransaction提交事务，或者在异常中调用DatabaseHelper.rollbackTransaction
	 * 方法回滚事务，最后一处FLAG_HOLDER标志
	 *
	 */
	@Override
	public Object doProxy(ProxyChain proxyChain) throws Exception, Throwable {
		Object result;
		boolean flag = FLAG_HOLDER.get();
		Method method = proxyChain.getTargetMethod();
		if (!flag && method.isAnnotationPresent(Transaction.class)) {
			FLAG_HOLDER.set(true);
			
			try {
				DataBaseHelper.beginTransaction();
				LOGGER.debug("begin transaction");
				result = proxyChain.doProxyChain();
				DataBaseHelper.commitTranaction();
				LOGGER.debug("commit transaction");
			} catch (Exception e) {
				DataBaseHelper.rollbackTrancation();
				LOGGER.debug("rollback transaction");
				throw e;
			} finally {
				FLAG_HOLDER.remove();
			}
		} else {
			result = proxyChain.doProxyChain();
		}
		
		return result;
		
	}

}
