package org.smart4j.framework.helper;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

/**
 * Servlet 助手类
 * @author Administrator
 *
 */
public class ServletHelper {

	private static final Logger LOGGER = Logger.getLogger(ServletHelper.class);
	
	/**
	 * 使每一个线程独自拥有一份ServletHelper实例
	 */
	private static final ThreadLocal<ServletHelper> SERVLET_HELPER_HOLDER = new ThreadLocal<ServletHelper>();
	
	private HttpServletRequest request;
	
	private HttpServletResponse response;
	
	private ServletHelper (HttpServletRequest request, HttpServletResponse response){
		this.request = request;
		this.response = response;
	}
	
	/**
	 * 初始化
	 */
	public static void init(HttpServletRequest request, HttpServletResponse response){
		SERVLET_HELPER_HOLDER.set(new ServletHelper(request, response));
	}
	
	/**
	 * 销毁
	 */
	public static void destory(){
		SERVLET_HELPER_HOLDER.remove();
	}
	
	/**
	 * 获取request对象
	 */
	public static HttpServletRequest getRequest(){
		return SERVLET_HELPER_HOLDER.get().request;
	}
	
	/**
	 * 获取response对象
	 */
	public static HttpServletResponse getResponse(){
		return SERVLET_HELPER_HOLDER.get().response;
	}
	
	/**
	 * 获取Session对象
	 */
	private static HttpSession getSession(){
		return getRequest().getSession();
	}
	
	/**
	 * 获取ServletContext对象
	 */
	@SuppressWarnings("unused")
	private static ServletContext getServletContext(){
		return getRequest().getServletContext();
	}
	
	/**
	 * 将属性放入到Request中
	 */
	public static void setRequestAttribute(String key, Object value){
		getRequest().setAttribute(key, value);
	}

	/**
	 * 从Request中获取属性
	 * @param <T>
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getRequestAttribute(String key){
		return (T) getRequest().getAttribute(key);
	}
	
	/**
	 * 从Request中移除属性
	 * @return 
	 */
	public static void removeRequestAttribute(String key){
		getRequest().removeAttribute(key);
	} 
	
	/**
	 * 发送重定向响应
	 */
	public static void sendRedirect(String location){
		try {
			getResponse().sendRedirect(location);
		} catch (IOException e) {
			LOGGER.error("redirect failure", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * 发送内部转
	 */
	public static void sendRequestDispatcher(String location){
		try {
			getRequest().getRequestDispatcher(location).forward(getRequest(), getResponse());
		} catch (ServletException | IOException e) {
			LOGGER.error("request dispatcher failure", e);
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 将属性放入Session中
	 */
	public static void setSessionAttribute(String key, String value){
		getSession().setAttribute(key, value);
	}
	
	/**
	 * 从Session中移除属性
	 */
	public static void removeSessionAttribute(String key){
		getSession().removeAttribute(key);
	}
	
	/**
	 * 使Session失效
	 */
	public static void invalidateSession(){
		getSession().invalidate();
	}
	
}
