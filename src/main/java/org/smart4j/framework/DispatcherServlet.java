package org.smart4j.framework;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.smart4j.framework.bean.Data;
import org.smart4j.framework.bean.Handler;
import org.smart4j.framework.bean.Param;
import org.smart4j.framework.bean.View;
import org.smart4j.framework.helper.BeanHelper;
import org.smart4j.framework.helper.ConfigHelper;
import org.smart4j.framework.helper.ControllerHelper;
import org.smart4j.framework.util.ArrayUtil;
import org.smart4j.framework.util.CodecUtil;
import org.smart4j.framework.util.JsonUtil;
import org.smart4j.framework.util.ReflectionUtil;
import org.smart4j.framework.util.StreamUtil;
import org.smart4j.framework.util.StringUtil;
/**
 * 请求转发器
 * @author Administrator
 *
 */
@WebServlet(urlPatterns="/*",loadOnStartup=0)
public class DispatcherServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;

	@Override
	public void init(ServletConfig servletConfig) throws ServletException {
		//初始化Helper 类
		HelperLoader.init();
		//获得ServletContext对象(用于注册Servlet)
		ServletContext servletContext = servletConfig.getServletContext();
		//注册处理Jsp的Servlet
		ServletRegistration jspServlet = servletContext.getServletRegistration("jsp");
		jspServlet.addMapping(ConfigHelper.getAppJspPath() + "*");
		//注册处理静态资源默认的Servlet
		ServletRegistration defaultServlet = servletContext.getServletRegistration("default");
		defaultServlet.addMapping(ConfigHelper.getAppAssetPath() + "*");
	}
	
	@Override
	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//获取请求方法
		String requestMethod = request.getMethod().toLowerCase();
		String requestPath = request.getPathInfo();
		//获取Action 处理器
		Handler handler = ControllerHelper.getHandler(requestMethod, requestPath);
		if (handler != null) {
			//获取Controller类及其Bean实例
			Class<?> controllerClass = handler.getControllerClass();
			Object controllerBean = BeanHelper.getBean(controllerClass);
			//创建请求参数
			Map<String, Object> paramMap = new HashMap<String, Object>();
			Enumeration<String> paramNames = request.getParameterNames();
			while (paramNames.hasMoreElements()){
				String paramName = paramNames.nextElement();
				String paramValue = request.getParameter(paramName);
				paramMap.put(paramName, paramValue);
			}
		
		String body = CodecUtil.decodeURL(StreamUtil.getString(request.getInputStream()));
		if (StringUtil.isNotEmpty(body)) {
			String[] params = StringUtil.splitString(body, "&");
			if (ArrayUtil.isNotEmpty(params)) {
				for (String param : params) {
					String[] array = StringUtil.splitString(param, "=");
					String paramName = array[0];
					String paramValue = array[1];
					paramMap.put(paramName, paramValue);
				}
			}
		}
		
		Param param = new Param(paramMap);
		//调用Action方法
		Object result;
		Method actionMethod = handler.getActionMethod();
		if (param.isEmpty()) {
			result = ReflectionUtil.invokeMethod(controllerBean, actionMethod);
		} else {
			result = ReflectionUtil.invokeMethod(controllerBean, actionMethod, param);
		}
		//处理Action方法返回值
		if (result instanceof View) {
			//返回jsp页面
			View view = (View) result;
			String path = view.getPath();
			if (StringUtil.isNotEmpty(path)) {
				if (path.startsWith("/")) {
					response.sendRedirect(request.getContextPath() + path);
				}else{
					Map<String, Object> model = view.getModel();
					for (Map.Entry<String, Object> entry : model.entrySet()){
						request.setAttribute(entry.getKey(), entry.getValue());
					}
					request.getRequestDispatcher(ConfigHelper.getAppJspPath() + path).forward(request, response);
				}
				
			}
		}else if (result instanceof Data){
			//返回Json数据
			Data data = (Data) result;
			Object model = data.getModel();
			if (model != null) {
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				PrintWriter writer = response.getWriter();
				String json = JsonUtil.toJson(model);
				writer.write(json);
				writer.flush();
				writer.close();
			}
		}
		
		
		}
		
	}
}
