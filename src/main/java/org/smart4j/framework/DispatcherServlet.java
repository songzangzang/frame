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
import org.smart4j.framework.helper.RequestHelper;
import org.smart4j.framework.helper.UploadHelper;
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
		
		UploadHelper.init(servletContext);
	}
	
	@Override
	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//获取请求方法
		String requestMethod = request.getMethod().toLowerCase();
		String requestPath = request.getPathInfo();
		
		if (requestPath.equals("/favicon.ico")) {
			return;	
		}
		
		//获取Action 处理器
		Handler handler = ControllerHelper.getHandler(requestMethod, requestPath);
		if (handler != null) {
			//获取Controller类及其Bean实例
			Class<?> controllerClass = handler.getControllerClass();
			Object controllerBean = BeanHelper.getBean(controllerClass);
			//创建请求参数
			
			Param param = null;
			if (UploadHelper.isMultipart(request)) {
				param = UploadHelper.createParam(request);
			} else {
				param = RequestHelper.createParam(request);
			}
			
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
			handleViewResult((View) result, request, response);
		} else if (result instanceof Data){
			//返回json数据
			handleDataResult((Data)result, response);
		  }
		}
	}
	
	/**
	 * @throws IOException 
	 * @throws ServletException 
	 * 
	 */
	private void handleViewResult(View view, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		String path = view.getPath();
		if (StringUtil.isNotEmpty(path)) {
			if (path.startsWith("/")) {
				try {
					response.sendRedirect(request.getContextPath() + path);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				Map<String, Object> model = view.getModel();
				for(Map.Entry<String, Object> entry : model.entrySet()){
					request.setAttribute(entry.getKey(), entry.getValue());
				}
				request.getRequestDispatcher(ConfigHelper.getAppJspPath() + path).forward(request, response);;
			}
		}
	}
	
	private void handleDataResult(Data data, HttpServletResponse response) throws IOException{
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
